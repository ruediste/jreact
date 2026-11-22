package com.github.ruediste.jreact;

import static com.github.ruediste.jreact.CoreElements.div;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.github.ruediste.jreact.color.JColor;
import com.github.ruediste.jreact.elements.ElementNode;
import com.github.ruediste.jreact.style.StyleProperties;

class SelectorStyleTest {

    static class Item extends Component {
        private final JColor color;

        Item(JColor color) {
            this.color = color;
        }

        @Override
        public void render() {
            div().width(20).height(20).notLast().backgroundColor(color);
        }
    }

    /**
     * Verifies that a selector terminal registers a rule on the rendered node and
     * that the rule only affects the resolved style while the selector matches.
     */
    @Test
    void hoverRuleIsRegisteredAndAppliedOnlyWhenHovered() {
        JColor red = JColor.rgb(255, 0, 0);
        JReactRoot root = new JReactRoot(() -> div().width(100).height(100).hover().backgroundColor(red));

        root.refresh(200, 200);
        ElementNode node = (ElementNode) root.rootNode().children().get(0);
        assertEquals(1, node.styleRules.size());
        assertEquals(red, node.styleRules.get(0).style().values.get(StyleProperties.BACKGROUND_COLOR));
        assertNull(StyleProperties.BACKGROUND_COLOR.get(node, null));

        root.pointerMoved(50, 50);
        root.refresh();
        assertEquals(red, StyleProperties.BACKGROUND_COLOR.get(node, null));
    }

    /**
     * Verifies that inline declarations take precedence over selector rules and
     * that {@code inline()} ends the current rule scope.
     */
    @Test
    void inlineStyleOverridesSelectorRule() {
        JColor ruleColor = JColor.rgb(255, 0, 0);
        JColor inlineColor = JColor.rgb(0, 0, 255);
        JReactRoot root = new JReactRoot(() -> div().width(100).height(100)
                .hover().backgroundColor(ruleColor)
                .inline().backgroundColor(inlineColor));

        root.refresh(200, 200);
        ElementNode node = (ElementNode) root.rootNode().children().get(0);
        assertEquals(ruleColor, node.styleRules.get(0).style().values.get(StyleProperties.BACKGROUND_COLOR));
        assertEquals(inlineColor, node.inline.values.get(StyleProperties.BACKGROUND_COLOR));

        root.pointerMoved(50, 50);
        root.refresh();
        assertEquals(inlineColor, StyleProperties.BACKGROUND_COLOR.get(node, null));
    }

    /**
     * Verifies that chained {@code ancestor()} and {@code parent()} selectors match
     * the node tree and apply the rule to the expected element.
     */
    @Test
    void chainedAncestorAndParentSelectorsMatch() {
        JColor ancestorColor = JColor.rgb(10, 20, 30);
        JColor parentColor = JColor.rgb(40, 50, 60);
        JReactRoot root = new JReactRoot(() -> div().className("outer").width(200).height(200)
                .children(() -> {
                    div().className("ancestor-child").width(50).height(50)
                            .ancestor().hasClass("outer").backgroundColor(ancestorColor);
                    div().className("parent").width(50).height(50)
                            .children(() -> div().className("parent-child").width(20).height(20)
                                    .parent().hasClass("parent").backgroundColor(parentColor));
                }));

        root.refresh(200, 200);
        ElementNode outer = (ElementNode) root.rootNode().children().get(0);
        ElementNode ancestorChild = (ElementNode) outer.children().get(0);
        ElementNode parent = (ElementNode) outer.children().get(1);
        ElementNode parentChild = (ElementNode) parent.children().get(0);

        assertEquals(ancestorColor, StyleProperties.BACKGROUND_COLOR.get(ancestorChild, null));
        assertNull(StyleProperties.BACKGROUND_COLOR.get(parent, null));
        assertEquals(parentColor, StyleProperties.BACKGROUND_COLOR.get(parentChild, null));
    }

    /**
     * Verifies that {@code notLast()} matches all but the last element child and
     * that component wrappers are transparent when determining element siblings.
     */
    @Test
    void notLastMatchesAllButLastElementChildAcrossComponents() {
        JColor red = JColor.rgb(255, 0, 0);
        JReactRoot root = new JReactRoot(() -> div().width(100).height(100).children(() -> {
            new Item(red);
            new Item(red);
        }));

        root.refresh(200, 200);
        ElementNode outer = (ElementNode) root.rootNode().children().get(0);
        List<ElementNode> items = outer.elementChildren();
        assertEquals(2, items.size());
        assertEquals(red, StyleProperties.BACKGROUND_COLOR.get(items.get(0), null));
        assertNull(StyleProperties.BACKGROUND_COLOR.get(items.get(1), null));
    }

    /**
     * Verifies that {@code focusVisible()} only matches after keyboard interaction
     * and is cleared again by a pointer press.
     */
    @Test
    void focusVisibleTracksKeyboardFocus() {
        JColor red = JColor.rgb(255, 0, 0);
        JReactRoot root = new JReactRoot(
                () -> div().width(100).height(100).focusVisible().backgroundColor(red));

        root.refresh(200, 200);
        ElementNode node = (ElementNode) root.rootNode().children().get(0);

        root.pointerPressed(50, 50);
        root.refresh();
        assertTrue(node.focus);
        assertFalse(node.focusVisible);
        assertNull(StyleProperties.BACKGROUND_COLOR.get(node, null));

        root.keyPressed(65);
        root.refresh();
        assertTrue(node.focusVisible);
        assertEquals(red, StyleProperties.BACKGROUND_COLOR.get(node, null));

        root.pointerPressed(50, 50);
        root.refresh();
        assertFalse(node.focusVisible);
        assertNull(StyleProperties.BACKGROUND_COLOR.get(node, null));
    }

    /**
     * Verifies that the {@code disabled(boolean)} state and the {@code disabled()}
     * selector work together.
     */
    @Test
    void disabledStateIsSelectable() {
        JColor red = JColor.rgb(255, 0, 0);
        JReactRoot root = new JReactRoot(() -> {
            div().width(50).height(50).disabled(true).disabled().backgroundColor(red);
            div().width(50).height(50).disabled(false).disabled().backgroundColor(red);
        });

        root.refresh(200, 200);
        ElementNode disabledNode = (ElementNode) root.rootNode().children().get(0);
        ElementNode enabledNode = (ElementNode) root.rootNode().children().get(1);
        assertEquals(red, StyleProperties.BACKGROUND_COLOR.get(disabledNode, null));
        assertNull(StyleProperties.BACKGROUND_COLOR.get(enabledNode, null));
    }
}
