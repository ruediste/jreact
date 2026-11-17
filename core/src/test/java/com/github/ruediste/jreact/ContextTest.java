package com.github.ruediste.jreact;

import static com.github.ruediste.jreact.CoreElements.text;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.Test;

class ContextTest {

    /**
     * Verifies that consumers without a provider receive the default value and
     * that nested providers shadow outer ones according to the nearest ancestor
     * provider in the node tree.
     */
    @Test
    void defaultValueAndNearestProvider() {
        Context<String> context = Context.create("default");
        List<String> seen = new ArrayList<>();

        class Probe extends Component {
            @Override
            public void render() {
                seen.add(Hooks.useContext(context));
            }
        }

        JReactRoot root = new JReactRoot(() -> {
            new Probe();
            context.provide("outer", () -> {
                new Probe();
                context.provide("inner", () -> new Probe());
            });
        });

        root.refresh();

        assertEquals(List.of("default", "outer", "inner"), seen);
    }

    /**
     * Verifies that changing a provider value invalidates a consumer behind a
     * clean component boundary. The middle component keeps its fiber, while the
     * leaf is rerendered in the same refresh and observes the new value.
     */
    @Test
    void providerValueChangeRerendersConsumersThroughCleanComponents() {
        Context<String> context = Context.create("default");
        AtomicReference<State<String>> value = new AtomicReference<>();
        int[] middleRenders = { 0 };
        int[] leafRenders = { 0 };
        List<String> seen = new ArrayList<>();

        class Leaf extends Component {
            @Override
            public void render() {
                leafRenders[0]++;
                String current = Hooks.useContext(context);
                seen.add(current);
                text(current);
            }
        }

        class Middle extends Component {
            @Override
            public void render() {
                middleRenders[0]++;
                new Leaf();
            }
        }

        JReactRoot root = new JReactRoot(() -> {
            value.set(Hooks.useState("a"));
            context.provide(value.get().get(), () -> new Middle());
        });

        root.refresh(200, 100);
        assertEquals(1, middleRenders[0]);
        assertEquals(1, leafRenders[0]);
        assertEquals(List.of("a"), seen);

        value.get().set("b");
        root.refresh(200, 100);

        assertEquals(1, middleRenders[0]);
        assertEquals(2, leafRenders[0]);
        assertEquals(List.of("a", "b"), seen);
    }

    /**
     * Verifies that a provider rerender with an equal value does not invalidate
     * its consumers, even though the provider component itself rerendered.
     */
    @Test
    void equalProviderValueKeepsConsumersClean() {
        Context<String> context = Context.create("default");
        AtomicReference<State<Integer>> trigger = new AtomicReference<>();
        int[] leafRenders = { 0 };

        class Leaf extends Component {
            @Override
            public void render() {
                leafRenders[0]++;
                text(Hooks.useContext(context));
            }
        }

        JReactRoot root = new JReactRoot(() -> {
            trigger.set(Hooks.useState(0));
            trigger.get().get();
            context.provide("same", () -> new Leaf());
        });

        root.refresh(200, 100);
        assertEquals(1, leafRenders[0]);

        trigger.get().set(1);
        root.refresh(200, 100);

        assertEquals(1, leafRenders[0]);
    }
}
