package com.github.ruediste.jreact;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.util.function.Consumer;

import com.antepod.taffy.style.CompactLength;
import com.antepod.taffy.style.LengthPercentage;
import com.github.ruediste.jreact.color.JColor;
import com.github.ruediste.jreact.elements.ElementNode;
import com.github.ruediste.jreact.style.Style;
import com.github.ruediste.jreact.style.StyleProperties;
import com.github.ruediste.jreact.style.StyleProperties.BorderStyle;

public final class PaintUtils {
    private PaintUtils() {
    }

    private static final int TOP_LEFT = 0;
    private static final int TOP_RIGHT = 1;
    private static final int BOTTOM_RIGHT = 2;
    private static final int BOTTOM_LEFT = 3;

    /**
     * Resolved corner radii, indexed by {@link #TOP_LEFT}, {@link #TOP_RIGHT},
     * {@link #BOTTOM_RIGHT}, and {@link #BOTTOM_LEFT}. The horizontal radii are
     * relative to the width, the vertical radii to the height.
     */
    private record Corners(double[] horizontal, double[] vertical) {
        Corners {
            if (horizontal.length != 4 || vertical.length != 4)
                throw new IllegalArgumentException("Expected four corner radii");
        }
    }

    public static Shape shape(ElementNode node) {
        Rectangle2D.Double bounds = node.bounds;
        return roundedRectangle(bounds.x, bounds.y, bounds.width, bounds.height,
                cornerRadii(node.style, bounds.width, bounds.height));
    }

    private static Corners cornerRadii(Style style, double width, double height) {
        double horizontalBasis = Math.max(0, width);
        double verticalBasis = Math.max(0, height);
        return new Corners(
                new double[] {
                        resolve(StyleProperties.BORDER_TOP_LEFT_RADIUS.get(style).horizontal(), horizontalBasis),
                        resolve(StyleProperties.BORDER_TOP_RIGHT_RADIUS.get(style).horizontal(), horizontalBasis),
                        resolve(StyleProperties.BORDER_BOTTOM_RIGHT_RADIUS.get(style).horizontal(), horizontalBasis),
                        resolve(StyleProperties.BORDER_BOTTOM_LEFT_RADIUS.get(style).horizontal(), horizontalBasis) },
                new double[] {
                        resolve(StyleProperties.BORDER_TOP_LEFT_RADIUS.get(style).vertical(), verticalBasis),
                        resolve(StyleProperties.BORDER_TOP_RIGHT_RADIUS.get(style).vertical(), verticalBasis),
                        resolve(StyleProperties.BORDER_BOTTOM_RIGHT_RADIUS.get(style).vertical(), verticalBasis),
                        resolve(StyleProperties.BORDER_BOTTOM_LEFT_RADIUS.get(style).vertical(), verticalBasis) });
    }

    /**
     * Resolves the corner radii and reduces them if they overlap along an edge.
     * Following CSS, a corner with a zero component is square, and overlapping
     * radii are scaled by one common factor instead of being clamped
     * independently.
     */
    private static Corners fit(Corners corners, double width, double height) {
        double[] horizontal = corners.horizontal().clone();
        double[] vertical = corners.vertical().clone();
        for (int index = 0; index < 4; index++) {
            if (!(horizontal[index] > 0) || !(vertical[index] > 0)) {
                horizontal[index] = 0;
                vertical[index] = 0;
            }
        }
        double factor = Math.min(
                Math.min(scale(width, horizontal[TOP_LEFT] + horizontal[TOP_RIGHT]),
                        scale(width, horizontal[BOTTOM_LEFT] + horizontal[BOTTOM_RIGHT])),
                Math.min(scale(height, vertical[TOP_LEFT] + vertical[BOTTOM_LEFT]),
                        scale(height, vertical[TOP_RIGHT] + vertical[BOTTOM_RIGHT])));
        for (int index = 0; index < 4; index++) {
            horizontal[index] *= factor;
            vertical[index] *= factor;
        }
        return new Corners(horizontal, vertical);
    }

    private static Shape roundedRectangle(double x, double y, double width, double height, Corners corners) {
        double w = Math.max(0, width);
        double h = Math.max(0, height);
        Corners fitted = fit(corners, w, h);
        double[] horizontal = fitted.horizontal();
        double[] vertical = fitted.vertical();

        boolean uniformHorizontal = horizontal[TOP_LEFT] == horizontal[TOP_RIGHT]
                && horizontal[TOP_RIGHT] == horizontal[BOTTOM_RIGHT]
                && horizontal[BOTTOM_RIGHT] == horizontal[BOTTOM_LEFT];
        boolean uniformVertical = vertical[TOP_LEFT] == vertical[TOP_RIGHT]
                && vertical[TOP_RIGHT] == vertical[BOTTOM_RIGHT]
                && vertical[BOTTOM_RIGHT] == vertical[BOTTOM_LEFT];
        if (uniformHorizontal && uniformVertical) {
            return new RoundRectangle2D.Double(x, y, w, h, horizontal[TOP_LEFT] * 2, vertical[TOP_LEFT] * 2);
        }

        Path2D path = new Path2D.Double();
        path.moveTo(x + horizontal[TOP_LEFT], y);
        path.lineTo(x + w - horizontal[TOP_RIGHT], y);
        appendArc(path, x + w - horizontal[TOP_RIGHT], y + vertical[TOP_RIGHT], horizontal[TOP_RIGHT],
                vertical[TOP_RIGHT], 90);
        path.lineTo(x + w, y + h - vertical[BOTTOM_RIGHT]);
        appendArc(path, x + w - horizontal[BOTTOM_RIGHT], y + h - vertical[BOTTOM_RIGHT],
                horizontal[BOTTOM_RIGHT], vertical[BOTTOM_RIGHT], 0);
        path.lineTo(x + horizontal[BOTTOM_LEFT], y + h);
        appendArc(path, x + horizontal[BOTTOM_LEFT], y + h - vertical[BOTTOM_LEFT], horizontal[BOTTOM_LEFT],
                vertical[BOTTOM_LEFT], 270);
        path.lineTo(x, y + vertical[TOP_LEFT]);
        appendArc(path, x + horizontal[TOP_LEFT], y + vertical[TOP_LEFT], horizontal[TOP_LEFT],
                vertical[TOP_LEFT], 180);
        path.closePath();
        return path;
    }

    private static double scale(double edge, double radii) {
        return radii > edge ? Math.max(0, edge) / radii : 1;
    }

    private static double resolve(LengthPercentage value, double basis) {
        CompactLength length = value.intoRaw();
        return switch (length.kind()) {
            case LENGTH -> length.value();
            case PERCENT -> length.value() * basis;
            default -> 0;
        };
    }

    private static void appendArc(Path2D path, double centerX, double centerY, double horizontal, double vertical,
            double startDegrees) {
        if (horizontal == 0 || vertical == 0)
            return;
        path.append(new Arc2D.Double(centerX - horizontal, centerY - vertical, horizontal * 2, vertical * 2,
                startDegrees, -90, Arc2D.OPEN), true);
    }

    public static void paintBorder(ElementNode node, Graphics2D graphics, Shape outer) {
        double topWidth = borderWidth(node.style, StyleProperties.BORDER_TOP_STYLE, StyleProperties.BORDER_TOP_WIDTH);
        double rightWidth = borderWidth(node.style, StyleProperties.BORDER_RIGHT_STYLE,
                StyleProperties.BORDER_RIGHT_WIDTH);
        double bottomWidth = borderWidth(node.style, StyleProperties.BORDER_BOTTOM_STYLE,
                StyleProperties.BORDER_BOTTOM_WIDTH);
        double leftWidth = borderWidth(node.style, StyleProperties.BORDER_LEFT_STYLE,
                StyleProperties.BORDER_LEFT_WIDTH);
        if (topWidth == 0 && rightWidth == 0 && bottomWidth == 0 && leftWidth == 0)
            return;

        Rectangle2D.Double bounds = node.bounds;
        double left = bounds.x, top = bounds.y, right = left + bounds.width, bottom = top + bounds.height;
        double innerLeft = Math.min(right, left + leftWidth);
        double innerRight = Math.max(innerLeft, right - rightWidth);
        double innerTop = Math.min(bottom, top + topWidth);
        double innerBottom = Math.max(innerTop, bottom - bottomWidth);

        Corners outerCorners = fit(cornerRadii(node.style, bounds.width, bounds.height), bounds.width,
                bounds.height);
        double[] horizontal = new double[] {
                Math.max(0, outerCorners.horizontal()[TOP_LEFT] - Math.max(topWidth, leftWidth)),
                Math.max(0, outerCorners.horizontal()[TOP_RIGHT] - Math.max(topWidth, rightWidth)),
                Math.max(0, outerCorners.horizontal()[BOTTOM_RIGHT] - Math.max(bottomWidth, rightWidth)),
                Math.max(0, outerCorners.horizontal()[BOTTOM_LEFT] - Math.max(bottomWidth, leftWidth)) };
        double[] vertical = new double[] {
                Math.max(0, outerCorners.vertical()[TOP_LEFT] - Math.max(topWidth, leftWidth)),
                Math.max(0, outerCorners.vertical()[TOP_RIGHT] - Math.max(topWidth, rightWidth)),
                Math.max(0, outerCorners.vertical()[BOTTOM_RIGHT] - Math.max(bottomWidth, rightWidth)),
                Math.max(0, outerCorners.vertical()[BOTTOM_LEFT] - Math.max(bottomWidth, leftWidth)) };
        Corners innerCorners = new Corners(horizontal, vertical);

        Area ring = new Area(outer);
        ring.subtract(new Area(roundedRectangle(innerLeft, innerTop, innerRight - innerLeft,
                innerBottom - innerTop, innerCorners)));
        Graphics2D copy = (Graphics2D) graphics.create();
        try {
            copy.clip(ring);
            double centerX = (left + right) / 2, centerY = (top + bottom) / 2;
            polygon(copy, borderColor(node.style, StyleProperties.BORDER_TOP_COLOR), left, top, right, top, centerX,
                    centerY);
            polygon(copy, borderColor(node.style, StyleProperties.BORDER_RIGHT_COLOR), right, top, right, bottom,
                    centerX, centerY);
            polygon(copy, borderColor(node.style, StyleProperties.BORDER_BOTTOM_COLOR), right, bottom, left, bottom,
                    centerX, centerY);
            polygon(copy, borderColor(node.style, StyleProperties.BORDER_LEFT_COLOR), left, bottom, left, top, centerX,
                    centerY);
        } finally {
            copy.dispose();
        }
    }

    private static double borderWidth(Style style, StyleProperties.Property<BorderStyle> borderStyle,
            StyleProperties.Property<Double> width) {
        BorderStyle resolvedStyle = borderStyle.get(style);
        if (resolvedStyle == BorderStyle.NONE || resolvedStyle == BorderStyle.HIDDEN)
            return 0;
        return Math.max(0, width.get(style));
    }

    private static Color borderColor(Style style, StyleProperties.Property<JColor> color) {
        return color.get(style).toColor();
    }

    private static void polygon(Graphics2D graphics, Color color, double... points) {
        Path2D path = new Path2D.Double();
        path.moveTo(points[0], points[1]);
        for (int index = 2; index < points.length; index += 2)
            path.lineTo(points[index], points[index + 1]);
        path.closePath();
        graphics.setColor(color);
        graphics.fill(path);
    }

    public static void shadow(ElementNode node, Graphics2D graphics, StyleProperties.Shadow shadow,
            Consumer<Graphics2D> draw) {
        int radius = (int) Math.ceil(shadow.blur());
        int padding = radius * 2 + 2;
        int width = Math.max(1, (int) Math.ceil(node.bounds.width) + padding * 2);
        int height = Math.max(1, (int) Math.ceil(node.bounds.height) + padding * 2);
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D layer = image.createGraphics();
        try {
            layer.setRenderingHints(graphics.getRenderingHints());
            layer.translate(padding - node.bounds.x, padding - node.bounds.y);
            layer.setColor(shadow.color());
            draw.accept(layer);
        } finally {
            layer.dispose();
        }
        if (radius > 0) {
            float[] kernel = new float[radius * 2 + 1];
            double sigma = Math.max(0.5, radius / 2.0), sum = 0;
            for (int index = 0; index < kernel.length; index++) {
                double distance = index - radius;
                kernel[index] = (float) Math.exp(-distance * distance / (2 * sigma * sigma));
                sum += kernel[index];
            }
            for (int index = 0; index < kernel.length; index++)
                kernel[index] /= (float) sum;
            image = new ConvolveOp(new Kernel(kernel.length, 1, kernel), ConvolveOp.EDGE_ZERO_FILL, null).filter(image,
                    null);
            image = new ConvolveOp(new Kernel(1, kernel.length, kernel), ConvolveOp.EDGE_ZERO_FILL, null).filter(image,
                    null);
        }
        graphics.drawImage(image, (int) Math.floor(node.bounds.x - padding + shadow.offsetX()),
                (int) Math.floor(node.bounds.y - padding + shadow.offsetY()), null);
    }
}
