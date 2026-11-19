package com.github.ruediste.jreact;

import static com.github.ruediste.jreact.CoreElements.div;
import static com.github.ruediste.jreact.CoreElements.text;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.antepod.taffy.style.FlexDirection;
import com.github.ruediste.jreact.color.JColor;
import com.github.ruediste.jreact.nodes.ElementNode;
import com.github.ruediste.jreact.style.StyleProperties;

class StylesLayoutTest {

    @Test
    void scopedRulesAndLayoutProduceBounds() {
        JReactRoot root = new JReactRoot(() -> {
            div().width(200).height(100).padding(10).flexDirection(FlexDirection.ROW).children(() -> {
                div().width(50).height(20).backgroundColor(JColor.rgb(255, 0, 0));
                text("hello");
            });
        });

        root.refresh(200, 100);
        ElementNode container = (ElementNode) root.rootNode().children().get(0);
        ElementNode box = (ElementNode) container.children().get(0);
        assertEquals(JColor.rgb(255, 0, 0), StyleProperties.BACKGROUND_COLOR.get(box.style, null));
        assertEquals(10, box.bounds.x, 0.01);
        assertEquals(10, box.bounds.y, 0.01);
        assertEquals(50, box.bounds.width, 0.01);
        assertEquals(20, box.bounds.height, 0.01);
        assertTrue(((ElementNode) container.children().get(1)).bounds.width > 0);
    }
}
