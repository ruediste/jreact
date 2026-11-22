package com.github.ruediste.jreact.primitives;

import static com.github.ruediste.jreact.CoreElements.div;
import static com.github.ruediste.jreact.CoreElements.text;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.event.KeyEvent;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import org.junit.jupiter.api.Test;

import com.antepod.taffy.style.Display;
import com.github.ruediste.jreact.Component;
import com.github.ruediste.jreact.Hooks;
import com.github.ruediste.jreact.JReactRoot;
import com.github.ruediste.jreact.Node;
import com.github.ruediste.jreact.OptionalN;
import com.github.ruediste.jreact.State;
import com.github.ruediste.jreact.elements.ElementNode;
import com.github.ruediste.jreact.style.StyleProperties;

class AccordionPrimitiveTest {

    /**
     * Renders an accordion with observable item state. The trigger and content get
     * stable class names so tests can find their rendered nodes.
     */
    static class TestAccordion<T> extends Component {
        private final OptionalN<T> value;
        private final Consumer<OptionalN<T>> onValueChange;
        private final boolean collapsible;
        private final List<Accordion.ItemData<T>> items;
        private final Consumer<Accordion.ItemContext> observed;

        TestAccordion(OptionalN<T> value, Consumer<OptionalN<T>> onValueChange, boolean collapsible,
                List<Accordion.ItemData<T>> items, Consumer<Accordion.ItemContext> observed) {
            this.value = value;
            this.onValueChange = onValueChange;
            this.collapsible = collapsible;
            this.items = items;
            this.observed = observed;
        }

        @Override
        public void render() {
            var root = new Accordion.RootSingle<T>(ra -> {
                div().width(300).apply(ra.config()).children(() -> {
                    int index = 0;
                    for (var item : ra.items()) {
                        int itemIndex = index++;
                        new Accordion.Item<>(item, ia -> div().apply(ia.config()).children(() -> {
                            Accordion.Header(ha -> div().apply(ha.config()).children(() -> {
                                Accordion.Trigger(ta -> {
                                    if (itemIndex == 0)
                                        observed.accept(ta.ctx());
                                    div().className("trigger-" + itemIndex).width(300).height(40)
                                            .apply(ta.config())
                                            .disabled(ta.ctx().isDisabled())
                                            .focusable(!ta.ctx().isDisabled())
                                            .accessibleName(ta.ctx().accessibleName())
                                            .children(item.header);
                                });
                            }));
                            Accordion.Content(ca -> div().className("content-" + itemIndex)
                                    .display(ca.ctx().isOpen() ? Display.FLEX : Display.NONE)
                                    .apply(ca.config()).children(item.content));
                        }));
                    }
                });
            });
            root.collapsible(collapsible);
            if (onValueChange != null)
                root.onValueChange(onValueChange);
            if (value != null)
                root.value(value);
            for (var item : items) {
                root.item(item.value, data -> {
                    data.disabled = item.disabled;
                    data.accessibleName = item.accessibleName;
                }, item.header, item.content);
            }
        }
    }

    /**
     * A controlled accordion whose value lives in component state.
     */
    static class ControlledAccordion<T> extends Component {
        private final List<Accordion.ItemData<T>> items;
        private final Consumer<Accordion.ItemContext> observed;

        ControlledAccordion(List<Accordion.ItemData<T>> items, Consumer<Accordion.ItemContext> observed) {
            this.items = items;
            this.observed = observed;
        }

        @Override
        public void render() {
            State<OptionalN<T>> state = Hooks.useState(OptionalN.empty());
            new TestAccordion<>(state.get(), state::set, true, items, observed);
        }
    }

    private static List<Accordion.ItemData<String>> twoItems() {
        return List.of(
                new Accordion.ItemData<>("foo", () -> text("foo trigger"), () -> text("foo content")),
                new Accordion.ItemData<>("bar", () -> text("bar trigger"), () -> text("bar content")));
    }

    private static ElementNode find(Node node, String className) {
        if (node instanceof ElementNode element && element.classNames.contains(className))
            return element;
        for (Node child : node.children()) {
            ElementNode result = find(child, className);
            if (result != null)
                return result;
        }
        return null;
    }

    private static ElementNode trigger(JReactRoot root, int index) {
        ElementNode node = find(root.rootNode(), "trigger-" + index);
        assertNotNull(node);
        return node;
    }

    private static void click(JReactRoot root, ElementNode node) {
        double x = node.bounds.getCenterX();
        double y = node.bounds.getCenterY();
        root.pointerPressed(x, y);
        root.pointerReleased(x, y);
        root.refresh();
    }

    /**
     * Verifies uncontrolled open/close behavior and the state exposed by
     * {@link Accordion.ItemContext}.
     */
    @Test
    void clickOpensAndClosesUncontrolledAccordion() {
        AtomicReference<Accordion.ItemContext> observed = new AtomicReference<>();
        JReactRoot root = new JReactRoot(
                () -> new TestAccordion<>(null, null, true, twoItems(), observed::set));
        root.refresh(400, 300);

        assertFalse(observed.get().isOpen());
        assertEquals("foo", observed.get().accessibleName());
        assertEquals(Accordion.Orientation.VERTICAL, observed.get().orientation());
        assertNotNull(observed.get().toggle());
        assertNotNull(observed.get().setOpen());

        click(root, trigger(root, 0));
        assertTrue(observed.get().isOpen());

        click(root, trigger(root, 0));
        assertFalse(observed.get().isOpen());
    }

    /**
     * Verifies that a non-collapsible single accordion keeps the open item open.
     */
    @Test
    void nonCollapsibleAccordionKeepsItemOpen() {
        AtomicReference<Accordion.ItemContext> observed = new AtomicReference<>();
        JReactRoot root = new JReactRoot(
                () -> new TestAccordion<>(null, null, false, twoItems(), observed::set));
        root.refresh(400, 300);

        click(root, trigger(root, 0));
        assertTrue(observed.get().isOpen());
        click(root, trigger(root, 0));
        assertTrue(observed.get().isOpen());
    }

    /**
     * Verifies that disabled items ignore pointer activation.
     */
    @Test
    void disabledItemDoesNotToggle() {
        AtomicReference<Accordion.ItemContext> observed = new AtomicReference<>();
        List<Accordion.ItemData<String>> items = List.of(
                new Accordion.ItemData<>("foo", () -> text("foo trigger"), () -> text("foo content"))
                        .disabled(true),
                new Accordion.ItemData<>("bar", () -> text("bar trigger"), () -> text("bar content")));
        JReactRoot root = new JReactRoot(
                () -> new TestAccordion<>(null, null, true, items, observed::set));
        root.refresh(400, 300);

        assertTrue(trigger(root, 0).disabled);
        click(root, trigger(root, 0));
        assertFalse(observed.get().isOpen());
    }

    /**
     * Verifies that Enter and Space toggle the focused trigger.
     */
    @Test
    void keyboardTogglesFocusedTrigger() {
        AtomicReference<Accordion.ItemContext> observed = new AtomicReference<>();
        JReactRoot root = new JReactRoot(
                () -> new TestAccordion<>(null, null, true, twoItems(), observed::set));
        root.refresh(400, 300);
        ElementNode target = trigger(root, 0);
        root.pointerPressed(target.bounds.getCenterX(), target.bounds.getCenterY());

        root.keyPressed(KeyEvent.VK_ENTER);
        root.refresh();
        assertTrue(observed.get().isOpen());

        root.keyPressed(KeyEvent.VK_SPACE);
        root.refresh();
        assertFalse(observed.get().isOpen());
    }

    /**
     * Verifies that controlled mode reports changes through {@code onValueChange}
     * and follows the value supplied by the parent.
     */
    @Test
    void controlledAccordionFollowsParentValue() {
        AtomicReference<Accordion.ItemContext> observed = new AtomicReference<>();
        JReactRoot root = new JReactRoot(
                () -> new ControlledAccordion<>(twoItems(), observed::set));
        root.refresh(400, 300);

        click(root, trigger(root, 0));
        assertTrue(observed.get().isOpen());
        assertEquals("foo", observed.get().accessibleName());
    }

    /**
     * Verifies that content stays mounted while closed and is hidden with
     * {@code display: none}.
     */
    @Test
    void contentStaysMountedWhenClosed() {
        AtomicReference<Accordion.ItemContext> observed = new AtomicReference<>();
        JReactRoot root = new JReactRoot(
                () -> new TestAccordion<>(null, null, true, twoItems(), observed::set));
        root.refresh(400, 300);

        ElementNode content = find(root.rootNode(), "content-0");
        assertNotNull(content);
        assertEquals(Display.NONE, StyleProperties.DISPLAY.get(content));

        click(root, trigger(root, 0));
        content = find(root.rootNode(), "content-0");
        assertNotNull(content);
        assertEquals(Display.FLEX, StyleProperties.DISPLAY.get(content));
    }
}
