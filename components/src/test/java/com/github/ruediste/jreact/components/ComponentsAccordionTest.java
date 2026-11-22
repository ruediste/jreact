package com.github.ruediste.jreact.components;

import static com.github.ruediste.jreact.CoreElements.text;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.antepod.taffy.style.AlignContent;
import com.antepod.taffy.style.AlignItems;
import com.antepod.taffy.style.Dimension;
import com.antepod.taffy.style.Display;
import com.antepod.taffy.style.FlexDirection;
import com.antepod.taffy.style.LengthPercentage;
import com.antepod.taffy.style.LengthPercentageAuto;
import com.antepod.taffy.style.Overflow;
import com.antepod.taffy.style.TextAlign;
import com.github.ruediste.jreact.Component;
import com.github.ruediste.jreact.Hooks;
import com.github.ruediste.jreact.JReactRoot;
import com.github.ruediste.jreact.Node;
import com.github.ruediste.jreact.OptionalN;
import com.github.ruediste.jreact.State;
import com.github.ruediste.jreact.elements.ElementNode;
import com.github.ruediste.jreact.style.StyleProperties;
import com.github.ruediste.jreact.style.StyleProperties.BorderStyle;
import com.github.ruediste.jreact.theme.ThemeElements;
import com.github.ruediste.jreact.utility.UtilityElements;

class ComponentsAccordionTest {

    private static final ThemeElements THEME = new ThemeElements(new UtilityElements.UtilityTokenValues(),
            new ThemeElements.ThemeTokenValues());
    private static final Components COMPONENTS = new Components(THEME);

    static class AccordionDemo extends Component {
        private final boolean disableSecond;

        AccordionDemo(boolean disableSecond) {
            this.disableSecond = disableSecond;
        }

        @Override
        public void render() {
            State<OptionalN<String>> value = Hooks.useState(OptionalN.of("one"));
            var root = COMPONENTS.accordion(value.get()).onValueChange(value::set);
            root.item("one", () -> text("One"), () -> text("Content one"));
            if (disableSecond)
                root.item("two", item -> item.disabled(true), () -> text("Two"), () -> text("Content two"));
            else
                root.item("two", () -> text("Two"), () -> text("Content two"));
        }
    }

    private static List<ElementNode> findAll(Node node, String className) {
        List<ElementNode> result = new ArrayList<>();
        if (node instanceof ElementNode element && element.classNames.contains(className))
            result.add(element);
        for (Node child : node.children())
            result.addAll(findAll(child, className));
        return result;
    }

    private static ElementNode find(Node node, String className) {
        List<ElementNode> result = findAll(node, className);
        assertFalse(result.isEmpty(), "Missing node with class " + className);
        return result.get(0);
    }

    private static void click(JReactRoot root, ElementNode node) {
        double x = node.bounds.getCenterX();
        double y = node.bounds.getCenterY();
        root.pointerPressed(x, y);
        root.pointerReleased(x, y);
        root.refresh();
    }

    /**
     * Verifies the static theme and utility styles applied to the accordion
     * structure.
     */
    @Test
    void rootItemsAndTriggerUseExpectedStyles() {
        JReactRoot root = new JReactRoot(() -> new AccordionDemo(false));
        root.refresh(500, 400);

        ElementNode accordion = find(root.rootNode(), "accordion");
        assertEquals(Display.FLEX, StyleProperties.DISPLAY.get(accordion));
        assertEquals(Dimension.percent(1f), StyleProperties.WIDTH.get(accordion));
        assertEquals(FlexDirection.COLUMN, StyleProperties.FLEX_DIRECTION.get(accordion));

        List<ElementNode> items = findAll(root.rootNode(), "accordion-item");
        assertEquals(2, items.size());
        assertEquals(FlexDirection.COLUMN, StyleProperties.FLEX_DIRECTION.get(items.get(0)));
        assertEquals(1.0, StyleProperties.BORDER_BOTTOM_WIDTH.get(items.get(0)));
        assertEquals(BorderStyle.SOLID, StyleProperties.BORDER_BOTTOM_STYLE.get(items.get(0)));
        assertEquals(THEME.themeValues.colorThemeValues.border, StyleProperties.BORDER_BOTTOM_COLOR.get(items.get(0)));
        assertEquals(BorderStyle.NONE, StyleProperties.BORDER_BOTTOM_STYLE.get(items.get(1)));

        ElementNode trigger = find(root.rootNode(), "accordion-trigger");
        assertEquals(Display.FLEX, StyleProperties.DISPLAY.get(trigger));
        assertEquals(Dimension.percent(0f), StyleProperties.FLEX_BASIS.get(trigger));
        assertEquals(AlignItems.START, StyleProperties.ALIGN_ITEMS.get(trigger));
        assertEquals(AlignContent.SPACE_BETWEEN, StyleProperties.JUSTIFY_CONTENT.get(trigger));
        assertEquals(LengthPercentage.length(8f), StyleProperties.BORDER_TOP_LEFT_RADIUS.get(trigger).horizontal());
        assertEquals(LengthPercentage.length(8f),
                StyleProperties.BORDER_BOTTOM_RIGHT_RADIUS.get(trigger).horizontal());
        assertEquals(1.0, StyleProperties.BORDER_TOP_WIDTH.get(trigger));
        assertEquals(BorderStyle.SOLID, StyleProperties.BORDER_TOP_STYLE.get(trigger));
        assertEquals(0, StyleProperties.BORDER_TOP_COLOR.get(trigger).toColor().getAlpha());
        assertEquals(TextAlign.LEGACY_LEFT, StyleProperties.TEXT_ALIGN.get(trigger));
        assertEquals(14, StyleProperties.FONT_SIZE.get(trigger));
        assertTrue(StyleProperties.BOLD.get(trigger));
        assertTrue(trigger.focusable);
        assertFalse(trigger.disabled);
        assertEquals("one", trigger.accessibleName);

        ElementNode content = find(root.rootNode(), "accordion-content");
        assertEquals(Display.FLEX, StyleProperties.DISPLAY.get(content));
        assertEquals(Overflow.HIDDEN, StyleProperties.OVERFLOW_Y.get(content));
        assertEquals(LengthPercentage.length(10), StyleProperties.PADDING_BOTTOM.get(content));
        assertEquals(14, StyleProperties.FONT_SIZE.get(content));

        ElementNode icon = find(root.rootNode(), "accordion-trigger-icon");
        assertEquals(Dimension.length(16), StyleProperties.WIDTH.get(icon));
        assertEquals(Dimension.length(16), StyleProperties.HEIGHT.get(icon));
        assertEquals(0.0, StyleProperties.FLEX_SHRINK.get(icon));
        assertEquals(LengthPercentageAuto.AUTO, StyleProperties.MARGIN_LEFT.get(icon));
        assertEquals(THEME.themeValues.colorThemeValues.mutedForeground, StyleProperties.COLOR.get(icon));
    }

    /**
     * Verifies hover, focus-visible, and click interaction styling.
     */
    @Test
    void hoverAndFocusVisibleSelectorsApply() {
        JReactRoot root = new JReactRoot(() -> new AccordionDemo(false));
        root.refresh(500, 400);

        ElementNode trigger = find(root.rootNode(), "accordion-trigger");
        double x = trigger.bounds.getCenterX();
        double y = trigger.bounds.getCenterY();

        root.pointerMoved(x, y);
        root.refresh();
        trigger = find(root.rootNode(), "accordion-trigger");
        assertEquals(THEME.themeValues.colorThemeValues.accent, StyleProperties.BACKGROUND_COLOR.get(trigger));
        assertEquals(StyleProperties.TextDecoration.UNDERLINE, StyleProperties.TEXT_DECORATION.get(trigger));

        root.pointerPressed(x, y);
        root.keyPressed(KeyEvent.VK_ENTER);
        root.refresh();
        trigger = find(root.rootNode(), "accordion-trigger");
        assertTrue(trigger.focusVisible);
        assertEquals(3.0, StyleProperties.RING_WIDTH.get(trigger));
        assertEquals(0.5, StyleProperties.RING_COLOR.get(trigger).toOkLch().alpha, 1e-9);
    }

    /**
     * Verifies that clicking a trigger opens the content, and that disabled items
     * receive the disabled styles and cannot be activated.
     */
    @Test
    void contentOpensAndDisabledItemIsInert() {
        JReactRoot root = new JReactRoot(() -> new AccordionDemo(true));
        root.refresh(500, 400);

        List<ElementNode> triggers = findAll(root.rootNode(), "accordion-trigger");
        List<ElementNode> contents = findAll(root.rootNode(), "accordion-content");
        assertEquals(Display.FLEX, StyleProperties.DISPLAY.get(contents.get(0)));
        assertEquals(Display.NONE, StyleProperties.DISPLAY.get(contents.get(1)));

        ElementNode disabledTrigger = triggers.get(1);
        assertTrue(disabledTrigger.disabled);
        assertFalse(disabledTrigger.focusable);
        assertEquals(0.5, StyleProperties.OPACITY.get(disabledTrigger));
        assertEquals(StyleProperties.PointerEvents.NONE, StyleProperties.POINTER_EVENTS.get(disabledTrigger));

        // Clicking an enabled trigger closes and reopens its content.
        click(root, findAll(root.rootNode(), "accordion-trigger").get(0));
        contents = findAll(root.rootNode(), "accordion-content");
        assertEquals(Display.NONE, StyleProperties.DISPLAY.get(contents.get(0)));
        click(root, findAll(root.rootNode(), "accordion-trigger").get(0));
        contents = findAll(root.rootNode(), "accordion-content");
        assertEquals(Display.FLEX, StyleProperties.DISPLAY.get(contents.get(0)));

        // A disabled trigger is inert.
        click(root, findAll(root.rootNode(), "accordion-trigger").get(1));
        contents = findAll(root.rootNode(), "accordion-content");
        assertEquals(Display.NONE, StyleProperties.DISPLAY.get(contents.get(1)));
    }
}
