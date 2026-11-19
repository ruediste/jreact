package com.github.ruediste.jreact.style;

import java.util.EnumSet;
import java.util.HashMap;

import com.github.ruediste.jreact.style.StyleProperties.Property;

public final class Style {
    public HashMap<Property<?>, Object> defaultValues;

    final public HashMap<Property<?>, Object> values = new HashMap<>();
    final public EnumSet<StyleProperties.MultiPropertyTaffyUpdater> taffyUpdaters = EnumSet
            .noneOf(StyleProperties.MultiPropertyTaffyUpdater.class);

    public void putAll(Style other) {
        values.putAll(other.values);
        taffyUpdaters.addAll(other.taffyUpdaters);
    }

    public <T> void put(Property<T> property, T value) {
        values.put(property, value);
        property.registerTaffyUpdater(this);
    }
}
