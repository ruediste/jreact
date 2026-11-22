package com.github.ruediste.jreact;

import static com.github.ruediste.jreact.CoreElements.div;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.font.LineMetrics;
import java.awt.image.BufferedImage;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.Test;

import com.github.ruediste.jreact.color.JColor;
import com.github.ruediste.jreact.color.OkLch;
import com.github.ruediste.jreact.color.Rgb;
import com.github.ruediste.jreact.elements.DivElement;
import com.github.ruediste.jreact.elements.TextElement;
import com.github.ruediste.jreact.style.StyleProperties;

class StyleFeatureTest {

    /**
     * Verifies that an underlined text node paints a line below its baseline.
     */
    @Test
    void underlineIsPaintedBelowTheBaseline() {
        TextElement text = new TextElement("hello");
        text.bounds.setRect(10, 10, 100, 40);
        text.lines.add(new Node.Line("hello", 0, 25));
        StyleProperties.COLOR.set(text.style, JColor.BLACK);
        StyleProperties.TEXT_DECORATION.set(text.style, StyleProperties.TextDecoration.UNDERLINE);

        BufferedImage image = new BufferedImage(140, 70, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, 140, 70);
        text.paint(graphics, new Rectangle(0, 0, 140, 70));
        LineMetrics metrics = graphics.getFont().getLineMetrics("hello", graphics.getFontRenderContext());
        graphics.dispose();

        int underlineY = Math.round(10 + 25 + metrics.getUnderlineOffset());
        assertEquals(Color.BLACK.getRGB(), image.getRGB(12, underlineY));
    }

    /**
     * Verifies that {@code pointer-events: none} prevents a node from being hit.
     */
    @Test
    void pointerEventsNonePreventsHitTesting() {
        AtomicBoolean clicked = new AtomicBoolean();
        JReactRoot root = new JReactRoot(() -> div().width(100).height(100)
                .pointerEventsNone().onClick(event -> clicked.set(true)));

        root.refresh(200, 200);
        root.pointerPressed(50, 50);
        root.pointerReleased(50, 50);

        assertFalse(clicked.get());
    }

    /**
     * Verifies that a child can re-enable hit testing below a node that disables
     * pointer events.
     */
    @Test
    void pointerEventsAutoOverridesInheritedNone() {
        AtomicBoolean clicked = new AtomicBoolean();
        JReactRoot root = new JReactRoot(() -> div().width(200).height(200)
                .pointerEventsNone().children(() -> div().width(100).height(100)
                        .pointerEvents(StyleProperties.PointerEvents.AUTO)
                        .onClick(event -> clicked.set(true))));

        root.refresh(200, 200);
        root.pointerPressed(50, 50);
        root.pointerReleased(50, 50);

        assertTrue(clicked.get());
    }

    /**
     * Verifies that a focus ring is painted outside the border box and that the
     * element interior remains untouched.
     */
    @Test
    void ringPaintsOutsideTheBorderBox() {
        DivElement node = new DivElement();
        node.bounds.setRect(20, 20, 60, 40);
        StyleProperties.RING_WIDTH.set(node.style, 3.0);
        StyleProperties.RING_COLOR.set(node.style, JColor.rgb(255, 0, 0));

        BufferedImage image = new BufferedImage(120, 100, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, 120, 100);
        node.paint(graphics, new Rectangle(0, 0, 120, 100));
        graphics.dispose();

        assertEquals(Color.RED.getRGB(), image.getRGB(18, 40));
        assertEquals(Color.WHITE.getRGB(), image.getRGB(15, 40));
        assertEquals(Color.WHITE.getRGB(), image.getRGB(25, 40));
    }

    /**
     * Verifies that a ring offset leaves a transparent gap between the element and
     * the ring.
     */
    @Test
    void ringOffsetLeavesGap() {
        DivElement node = new DivElement();
        node.bounds.setRect(20, 20, 60, 40);
        StyleProperties.RING_WIDTH.set(node.style, 1.0);
        StyleProperties.RING_OFFSET.set(node.style, 2.0);
        StyleProperties.RING_COLOR.set(node.style, JColor.rgb(255, 0, 0));

        BufferedImage image = new BufferedImage(120, 100, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, 120, 100);
        node.paint(graphics, new Rectangle(0, 0, 120, 100));
        graphics.dispose();

        assertEquals(Color.RED.getRGB(), image.getRGB(17, 40));
        assertEquals(Color.WHITE.getRGB(), image.getRGB(19, 40));
    }

    /**
     * Verifies that RGB colors keep their channels when an alpha value is applied.
     */
    @Test
    void rgbColorKeepsChannelsWhenApplyingAlpha() {
        JColor result = JColor.rgb(10, 20, 30).withAlpha(0.5);

        assertEquals(new Rgb(10, 20, 30, 128), result);
        assertEquals(128, result.toColor().getAlpha());
    }

    /**
     * Verifies that OKLCH colors retain their components when changing alpha.
     */
    @Test
    void oklchColorKeepsComponentsWhenApplyingAlpha() {
        OkLch result = new OkLch(0.5, 0.1, 120, 0.25).withAlpha(0.75);

        assertEquals(0.5, result.l);
        assertEquals(0.1, result.c);
        assertEquals(120, result.h);
        assertEquals(0.75, result.alpha);
    }
}
