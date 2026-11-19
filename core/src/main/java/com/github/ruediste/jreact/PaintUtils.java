package com.github.ruediste.jreact;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.Path2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.util.function.Consumer;

import com.github.ruediste.jreact.nodes.ElementNode;
import com.github.ruediste.jreact.style.StyleProperties;
import com.github.ruediste.jreact.style.StyleProperties.Border;

public final class PaintUtils {
    private PaintUtils() {
    }

    public static Shape shape(ElementNode node) {
        Border.Radius radius = StyleProperties.RADIUS.get(node.style);
        return roundedRectangle(node.bounds.x, node.bounds.y, node.bounds.width, node.bounds.height, radius);
    }

    private static Shape roundedRectangle(double x, double y, double width, double height, Border.Radius radius) {
        double max = Math.max(0, Math.min(width, height) / 2);
        double topLeft = Math.min(radius.topLeft(), max);
        double topRight = Math.min(radius.topRight(), max);
        double bottomRight = Math.min(radius.bottomRight(), max);
        double bottomLeft = Math.min(radius.bottomLeft(), max);
        if (topLeft == topRight && topRight == bottomRight && bottomRight == bottomLeft) {
            return new RoundRectangle2D.Double(x, y, width, height, topLeft * 2, topLeft * 2);
        }
        Path2D path = new Path2D.Double();
        path.moveTo(x + topLeft, y);
        path.lineTo(x + width - topRight, y);
        appendArc(path, x + width - topRight, y + topRight, topRight, 90);
        path.lineTo(x + width, y + height - bottomRight);
        appendArc(path, x + width - bottomRight, y + height - bottomRight, bottomRight, 0);
        path.lineTo(x + bottomLeft, y + height);
        appendArc(path, x + bottomLeft, y + height - bottomLeft, bottomLeft, 270);
        path.lineTo(x, y + topLeft);
        appendArc(path, x + topLeft, y + topLeft, topLeft, 180);
        path.closePath();
        return path;
    }

    private static void appendArc(Path2D path, double centerX, double centerY, double radius, double startDegrees) {
        if (radius == 0)
            return;
        path.append(new Arc2D.Double(centerX - radius, centerY - radius, radius * 2, radius * 2,
                startDegrees, -90, Arc2D.OPEN), true);
    }

    public static void paintBorder(ElementNode node, Graphics2D graphics, Shape outer) {
        Border border = StyleProperties.BORDER.get(node.style);
        if (border == null)
            return;
        java.awt.geom.Rectangle2D.Double bounds = node.bounds;
        double left = bounds.x, top = bounds.y, right = left + bounds.width, bottom = top + bounds.height;
        double innerLeft = Math.min(right, left + border.left().width());
        double innerRight = Math.max(innerLeft, right - border.right().width());
        double innerTop = Math.min(bottom, top + border.top().width());
        double innerBottom = Math.max(innerTop, bottom - border.bottom().width());
        Border.Radius radius = StyleProperties.RADIUS.get(node.style);
        Border.Radius innerRadius = new Border.Radius(
                Math.max(0, radius.topLeft() - Math.max(border.top().width(), border.left().width())),
                Math.max(0, radius.topRight() - Math.max(border.top().width(), border.right().width())),
                Math.max(0, radius.bottomRight() - Math.max(border.bottom().width(), border.right().width())),
                Math.max(0, radius.bottomLeft() - Math.max(border.bottom().width(), border.left().width())));
        Area ring = new Area(outer);
        ring.subtract(new Area(roundedRectangle(innerLeft, innerTop, innerRight - innerLeft,
                innerBottom - innerTop, innerRadius)));
        Graphics2D copy = (Graphics2D) graphics.create();
        try {
            copy.clip(ring);
            double centerX = (left + right) / 2, centerY = (top + bottom) / 2;
            polygon(copy, border.top().color(), left, top, right, top, centerX, centerY);
            polygon(copy, border.right().color(), right, top, right, bottom, centerX, centerY);
            polygon(copy, border.bottom().color(), right, bottom, left, bottom, centerX, centerY);
            polygon(copy, border.left().color(), left, bottom, left, top, centerX, centerY);
        } finally {
            copy.dispose();
        }
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
