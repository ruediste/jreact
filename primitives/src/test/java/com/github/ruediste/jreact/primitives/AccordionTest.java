package com.github.ruediste.jreact.primitives;

import static com.github.ruediste.jreact.CoreElements.div;
import static com.github.ruediste.jreact.CoreElements.text;

import com.github.ruediste.jreact.OptionalN;

public class AccordionTest {

    private static class MyComponents {
        public <T> Accordion.RootSingle<T> rootSingle(OptionalN<T> value) {
            return new Accordion.RootSingle<T>(ra -> div().apply(ra.config()).children(() -> {
                ra.items().forEach(item -> {
                    new Accordion.Item<>(item, ia -> div().apply(ia.config()).children(() -> {
                        Accordion.Header(ha -> {
                            div().apply(ha.config()).children(() -> {
                                Accordion.Trigger(ta -> div().apply(ta.config()).children(item.header));
                            });
                        });
                        Accordion.Content(ca -> {
                            div().apply(ca.config()).children(item.content);
                        });
                    }));
                });
            })).value(value);
        }

    }

    public void testRootSingle() {
        MyComponents c = new MyComponents();
        c.rootSingle(OptionalN.of("foo"))
                .item("foo",
                        () -> text("foo trigger"),
                        () -> text("foo content"))
                .item("bar",
                        () -> text("bar trigger"),
                        () -> text("bar contents"));
    }
}
