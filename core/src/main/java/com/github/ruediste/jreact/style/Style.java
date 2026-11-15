package com.github.ruediste.jreact.style;

import java.util.HashMap;

import com.github.ruediste.jreact.style.StyleProperties.Property;

public final class Style {
    public HashMap<Property<?>, Object> defaultValues;

    final HashMap<Property<?>, Object> values = new HashMap<>();

    public void putAll(Style other) {
        values.putAll(other.values);
    }

    public <T> void put(Property<T> property, T value) {
        values.put(property, value);
    }
}
