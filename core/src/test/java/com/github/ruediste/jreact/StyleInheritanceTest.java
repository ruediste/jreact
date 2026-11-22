package com.github.ruediste.jreact;

import static com.github.ruediste.jreact.CoreElements.div;
import static com.github.ruediste.jreact.CoreElements.text;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.antepod.taffy.style.Direction;
import com.github.ruediste.jreact.color.JColor;
import com.github.ruediste.jreact.elements.ElementNode;
import com.github.ruediste.jreact.style.StyleProperties;

class StyleInheritanceTest {

    static class Label extends Component {
        @Override
        public void render() {
            text("hello");
        }
    }

    /**
     * Verifies that inherited text properties resolve from the nearest ancestor
     * when they are not declared on the node itself.
     */
    @Test
    void inheritedTextPropertiesComeFromNearestAncestor() {
        JColor red = JColor.rgb(255, 0, 0);
        JReactRoot root = new JReactRoot(
                () -> div().color(red).fontSize(20).bold(true).children(() -> text("hello")));

        root.refresh(200, 100);
        ElementNode parent = (ElementNode) root.rootNode().children().get(0);
        ElementNode child = (ElementNode) parent.children().get(0);

        assertEquals(red, StyleProperties.COLOR.get(child));
        assertEquals(20, StyleProperties.FONT_SIZE.get(child));
        assertTrue(StyleProperties.BOLD.get(child));
    }

    /**
     * Verifies that a value declared on the node wins over an inherited value.
     */
    @Test
    void declaredTextPropertyOverridesInheritedValue() {
        JColor red = JColor.rgb(255, 0, 0);
        JColor blue = JColor.rgb(0, 0, 255);
        JReactRoot root = new JReactRoot(
                () -> div().color(red).children(() -> text("hello").color(blue)));

        root.refresh(200, 100);
        ElementNode parent = (ElementNode) root.rootNode().children().get(0);
        ElementNode child = (ElementNode) parent.children().get(0);

        assertEquals(blue, StyleProperties.COLOR.get(child));
    }

    /**
     * Verifies that non-inherited properties stay local to the element where they
     * are declared.
     */
    @Test
    void nonInheritedPropertyDoesNotInherit() {
        JColor red = JColor.rgb(255, 0, 0);
        JReactRoot root = new JReactRoot(
                () -> div().backgroundColor(red).children(() -> div().width(10).height(10)));

        root.refresh(200, 100);
        ElementNode parent = (ElementNode) root.rootNode().children().get(0);
        ElementNode child = (ElementNode) parent.children().get(0);

        assertEquals(red, StyleProperties.BACKGROUND_COLOR.get(parent, null));
        assertNull(StyleProperties.BACKGROUND_COLOR.get(child, null));
    }

    /**
     * Verifies that inheritance crosses transparent component boundaries.
     */
    @Test
    void inheritanceCrossesComponentBoundaries() {
        JColor red = JColor.rgb(255, 0, 0);
        JReactRoot root = new JReactRoot(() -> div().color(red).children(() -> new Label()));

        root.refresh(200, 100);
        ElementNode parent = (ElementNode) root.rootNode().children().get(0);
        ElementNode rendered = parent.elementChildren().get(0);

        assertEquals(red, StyleProperties.COLOR.get(rendered));
    }

    /**
     * Verifies that inherited layout properties are included in the Taffy style
     * used for layout.
     */
    @Test
    void inheritedDirectionIsAppliedToLayoutStyle() {
        JReactRoot root = new JReactRoot(
                () -> div().direction(Direction.RTL).children(() -> div().width(10).height(10)));

        root.refresh(200, 100);
        ElementNode parent = (ElementNode) root.rootNode().children().get(0);
        ElementNode child = (ElementNode) parent.children().get(0);

        assertEquals(Direction.RTL, StyleProperties.DIRECTION.get(child));
        assertEquals(Direction.RTL, StyleProperties.buildTaffyStyle(child).direction());
    }
}
