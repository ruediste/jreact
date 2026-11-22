package com.github.ruediste.jreact.utility;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import java.awt.image.BufferedImage;
import java.util.List;

import javax.swing.ImageIcon;

import org.junit.jupiter.api.Test;

import com.antepod.taffy.style.Dimension;
import com.antepod.taffy.style.LengthPercentage;
import com.antepod.taffy.style.LengthPercentageAuto;
import com.github.ruediste.jreact.JReactRoot;
import com.github.ruediste.jreact.elements.ElementNode;
import com.github.ruediste.jreact.elements.IconElement;
import com.github.ruediste.jreact.elements.InputElement;
import com.github.ruediste.jreact.elements.SelectElement;
import com.github.ruediste.jreact.elements.SpanElement;
import com.github.ruediste.jreact.elements.TextElement;
import com.github.ruediste.jreact.style.StyleProperties;
import com.github.ruediste.jreact.style.StyleProperties.BorderStyle;

class UtilityTokenTest {

    private final UtilityElements utility = new UtilityElements(new UtilityElements.UtilityTokenValues());

    /**
     * Verifies that gap helpers apply the spacing scale to the row and column gaps.
     */
    @Test
    void gapTokensUseSpacingScale() {
        JReactRoot root = new JReactRoot(() -> utility.div().gap(2).gapX(1).gapY(3));
        root.refresh(200, 200);
        ElementNode node = (ElementNode) root.rootNode().children().get(0);

        assertEquals(LengthPercentage.length(24), StyleProperties.ROW_GAP.get(node));
        assertEquals(LengthPercentage.length(8), StyleProperties.COLUMN_GAP.get(node));
    }

    /**
     * Verifies that size and full-size helpers set width and height correctly.
     */
    @Test
    void sizeTokensAndFullSize() {
        JReactRoot root = new JReactRoot(() -> {
            utility.div().size(4);
            utility.div().wFull().hFull();
        });
        root.refresh(200, 200);

        ElementNode size = (ElementNode) root.rootNode().children().get(0);
        ElementNode full = (ElementNode) root.rootNode().children().get(1);
        assertEquals(Dimension.length(32), StyleProperties.WIDTH.get(size));
        assertEquals(Dimension.length(32), StyleProperties.HEIGHT.get(size));
        assertEquals(Dimension.percent(1f), StyleProperties.WIDTH.get(full));
        assertEquals(Dimension.percent(1f), StyleProperties.HEIGHT.get(full));
    }

    /**
     * Verifies that auto-margin helpers set the corresponding margins to auto.
     */
    @Test
    void autoMarginTokens() {
        JReactRoot root = new JReactRoot(() -> utility.div().mAuto());
        root.refresh(200, 200);
        ElementNode node = (ElementNode) root.rootNode().children().get(0);

        assertEquals(LengthPercentageAuto.AUTO, StyleProperties.MARGIN_TOP.get(node));
        assertEquals(LengthPercentageAuto.AUTO, StyleProperties.MARGIN_RIGHT.get(node));
        assertEquals(LengthPercentageAuto.AUTO, StyleProperties.MARGIN_BOTTOM.get(node));
        assertEquals(LengthPercentageAuto.AUTO, StyleProperties.MARGIN_LEFT.get(node));

        JReactRoot second = new JReactRoot(() -> utility.div().mlAuto());
        second.refresh(200, 200);
        ElementNode secondNode = (ElementNode) second.rootNode().children().get(0);
        assertEquals(LengthPercentageAuto.AUTO, StyleProperties.MARGIN_LEFT.get(secondNode));
        assertEquals(LengthPercentageAuto.length(0), StyleProperties.MARGIN_RIGHT.get(secondNode));
    }

    /**
     * Verifies that flex helpers set grow, shrink, and basis like Tailwind
     * {@code flex-1}, {@code grow-0}, and {@code shrink-0}.
     */
    @Test
    void flexTokens() {
        JReactRoot root = new JReactRoot(() -> {
            utility.div().flex1();
            utility.div().grow0().shrink0();
        });
        root.refresh(200, 200);

        ElementNode flex1 = (ElementNode) root.rootNode().children().get(0);
        ElementNode zero = (ElementNode) root.rootNode().children().get(1);
        assertEquals(1.0, StyleProperties.FLEX_GROW.get(flex1));
        assertEquals(1.0, StyleProperties.FLEX_SHRINK.get(flex1));
        assertEquals(Dimension.percent(0f), StyleProperties.FLEX_BASIS.get(flex1));
        assertEquals(0.0, StyleProperties.FLEX_GROW.get(zero));
        assertEquals(0.0, StyleProperties.FLEX_SHRINK.get(zero));
    }

    /**
     * Verifies that border helpers set a solid style together with the requested
     * width.
     */
    @Test
    void borderTokensSetSolidStyle() {
        JReactRoot root = new JReactRoot(() -> {
            utility.div().border();
            utility.div().border2();
        });
        root.refresh(200, 200);

        ElementNode border = (ElementNode) root.rootNode().children().get(0);
        ElementNode border2 = (ElementNode) root.rootNode().children().get(1);
        assertEquals(1.0, StyleProperties.BORDER_TOP_WIDTH.get(border));
        assertEquals(BorderStyle.SOLID, StyleProperties.BORDER_TOP_STYLE.get(border));
        assertEquals(2.0, StyleProperties.BORDER_BOTTOM_WIDTH.get(border2));
        assertEquals(BorderStyle.SOLID, StyleProperties.BORDER_BOTTOM_STYLE.get(border2));
    }

    /**
     * Verifies that ring helpers set the default width and an explicit offset.
     */
    @Test
    void ringTokens() {
        JReactRoot root = new JReactRoot(() -> utility.div().ring().ringOffset(2));
        root.refresh(200, 200);
        ElementNode node = (ElementNode) root.rootNode().children().get(0);

        assertEquals(3.0, StyleProperties.RING_WIDTH.get(node));
        assertEquals(2.0, StyleProperties.RING_OFFSET.get(node));
    }

    /**
     * Verifies that the utility layer exposes all core element factories.
     */
    @Test
    void elementFactories() {
        JReactRoot root = new JReactRoot(() -> utility.div().children(() -> {
            utility.span().children(() -> utility.text("hello"));
            utility.icon(new ImageIcon(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB)));
            utility.input("value", value -> {
            });
            utility.select("a", List.of("a", "b"), value -> {
            });
        }));
        root.refresh(200, 200);

        ElementNode outer = (ElementNode) root.rootNode().children().get(0);
        var elements = outer.elementChildren();
        assertInstanceOf(SpanElement.class, elements.get(0));
        assertInstanceOf(IconElement.class, elements.get(1));
        assertInstanceOf(InputElement.class, elements.get(2));
        assertInstanceOf(SelectElement.class, elements.get(3));
        assertInstanceOf(TextElement.class, elements.get(0).elementChildren().get(0));
    }
}
