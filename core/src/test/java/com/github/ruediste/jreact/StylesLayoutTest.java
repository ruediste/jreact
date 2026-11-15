package com.github.ruediste.jreact;

import static com.github.ruediste.jreact.CoreComponents.div;
import static com.github.ruediste.jreact.CoreComponents.text;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Color;

import org.junit.jupiter.api.Test;

import com.antepod.taffy.style.FlexDirection;
import com.github.ruediste.jreact.nodes.Node;
import com.github.ruediste.jreact.style.StyleProperties;

class StylesLayoutTest {

    @Test
    void scopedRulesAndLayoutProduceBounds() {
        JReactRoot root = new JReactRoot(() -> {
            div().width(200).height(100).padding(10).flexDirection(FlexDirection.ROW).children(() -> {
                div().width(50).height(20).backgroundColor(Color.RED);
                text("hello");
            });
        });

        root.refresh(200, 100);
        Node container = root.rootNode().children().get(0);
        Node box = container.children().get(0);
        assertEquals(Color.RED, StyleProperties.BACKGROUND.get(box.style, null));
        assertEquals(10, box.bounds.x, 0.01);
        assertEquals(10, box.bounds.y, 0.01);
        assertEquals(50, box.bounds.width, 0.01);
        assertEquals(20, box.bounds.height, 0.01);
        assertTrue(container.children().get(1).bounds.width > 0);
    }
}
