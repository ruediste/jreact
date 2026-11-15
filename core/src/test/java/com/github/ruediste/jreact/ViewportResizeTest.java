package com.github.ruediste.jreact;

import static com.github.ruediste.jreact.CoreElements.div;
import static com.github.ruediste.jreact.CoreElements.viewportHeight;
import static com.github.ruediste.jreact.CoreElements.viewportWidth;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.github.ruediste.jreact.nodes.ElementNode;

class ViewportResizeTest {

    @Test
    void viewportChangesRerenderAndRelayout() {
        JReactRoot root = new JReactRoot(() -> div().width(viewportWidth()).height(viewportHeight()));
        root.refresh(100, 80);
        assertEquals(100, ((ElementNode) root.rootNode().children().get(0)).bounds.width, 0.01);
        assertEquals(80, ((ElementNode) root.rootNode().children().get(0)).bounds.height, 0.01);

        root.refresh(250, 120);
        assertEquals(250, ((ElementNode) root.rootNode().children().get(0)).bounds.width, 0.01);
        assertEquals(120, ((ElementNode) root.rootNode().children().get(0)).bounds.height, 0.01);
    }
}
