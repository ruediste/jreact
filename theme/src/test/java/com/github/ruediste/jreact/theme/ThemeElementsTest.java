package com.github.ruediste.jreact.theme;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.awt.image.BufferedImage;
import java.util.List;

import javax.swing.ImageIcon;

import org.junit.jupiter.api.Test;

import com.github.ruediste.jreact.JReactRoot;
import com.github.ruediste.jreact.color.JColor;
import com.github.ruediste.jreact.elements.ElementNode;
import com.github.ruediste.jreact.elements.IconElement;
import com.github.ruediste.jreact.elements.InputElement;
import com.github.ruediste.jreact.elements.SelectElement;
import com.github.ruediste.jreact.elements.SpanElement;
import com.github.ruediste.jreact.elements.TextElement;
import com.github.ruediste.jreact.style.StyleProperties;
import com.github.ruediste.jreact.utility.UtilityElements;

class ThemeElementsTest {

    private final ThemeElements theme = new ThemeElements(new UtilityElements.UtilityTokenValues(),
            new ThemeElements.ThemeTokenValues());

    /**
     * Verifies that theme element factories create the expected node types and that
     * semantic ring color tokens support alpha.
     */
    @Test
    void themeFactoriesAndRingAlphaToken() {
        JReactRoot root = new JReactRoot(() -> theme.div().ringColor().ring(0.5).children(() -> {
            theme.span().children(() -> theme.text("hello"));
            theme.icon(new ImageIcon(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB)));
            theme.input("value", value -> {
            });
            theme.select("a", List.of("a", "b"), value -> {
            });
        }));
        root.refresh(200, 200);

        ElementNode outer = (ElementNode) root.rootNode().children().get(0);
        JColor ringColor = StyleProperties.RING_COLOR.get(outer);
        assertNotNull(ringColor);
        assertEquals(0.5, ringColor.toOkLch().alpha, 1e-9);

        var elements = outer.elementChildren();
        assertInstanceOf(SpanElement.class, elements.get(0));
        assertInstanceOf(IconElement.class, elements.get(1));
        assertInstanceOf(InputElement.class, elements.get(2));
        assertInstanceOf(SelectElement.class, elements.get(3));
        assertInstanceOf(TextElement.class, elements.get(0).elementChildren().get(0));
    }

    /**
     * Verifies the border, chart, and sidebar token accessors, including alpha
     * variants on border colors.
     */
    @Test
    void semanticBorderChartAndSidebarTokens() {
        ColorTokens.Values values = theme.themeValues.colorThemeValues;
        JReactRoot root = new JReactRoot(() -> theme.div()
                .borderColor().border(0.5)
                .backgroundColor().sidebar()
                .color().sidebarForeground()
                .children(() -> theme.span().backgroundColor().chart1()));
        root.refresh(200, 200);

        ElementNode outer = (ElementNode) root.rootNode().children().get(0);
        ElementNode child = outer.elementChildren().get(0);
        JColor border = StyleProperties.BORDER_TOP_COLOR.get(outer);
        assertEquals(values.border.toOkLch().l, border.toOkLch().l, 1e-9);
        assertEquals(0.5, border.toOkLch().alpha, 1e-9);
        assertEquals(0.5, StyleProperties.BORDER_BOTTOM_COLOR.get(outer).toOkLch().alpha, 1e-9);
        assertEquals(values.sidebar, StyleProperties.BACKGROUND_COLOR.get(outer));
        assertEquals(values.sidebarForeground, StyleProperties.COLOR.get(outer));
        assertEquals(values.chart1, StyleProperties.BACKGROUND_COLOR.get(child));

        for (JColor chart : List.of(values.chart1, values.chart2, values.chart3, values.chart4, values.chart5))
            assertNotNull(chart);
        for (JColor sidebar : List.of(values.sidebar, values.sidebarForeground, values.sidebarPrimary,
                values.sidebarPrimaryForeground, values.sidebarAccent, values.sidebarAccentForeground,
                values.sidebarBorder, values.sidebarRing))
            assertNotNull(sidebar);
    }
}
