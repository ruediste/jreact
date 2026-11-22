package com.github.ruediste.jreact.components;

import com.github.ruediste.jreact.OptionalN;
import com.github.ruediste.jreact.primitives.Accordion;
import com.github.ruediste.jreact.theme.ThemeElements;

public class Components {
    public ThemeElements e;

    public Components(ThemeElements elements) {
        this.e = elements;
    }

    public <T> Accordion.RootSingle<T> accordion(OptionalN<T> value) {
        return new Accordion.RootSingle<T>(ra -> e.div().apply(ra.config()).children(() -> {
            ra.items().forEach(item -> {
                new Accordion.Item<>(item, ia -> e.div().apply(ia.config()).children(() -> {
                    Accordion.Header(ha -> {
                        e.div().apply(ha.config()).children(() -> {
                            Accordion.Trigger(ta -> e.div().apply(ta.config()).disabled(ta.ctx().isDisabled())
                                    .children(item.header));
                        });
                    });
                    Accordion.Content(ca -> {
                        e.div().apply(ca.config()).children(item.content);
                    });
                }));
            });
        })).value(value);
    }

}