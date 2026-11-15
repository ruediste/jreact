package com.github.ruediste.jreact.style;

import com.github.ruediste.jreact.nodes.Node;

@FunctionalInterface
public interface Selector {
    public Result match(Node node);

    // in the future, a parameter `Consumer<StyleRule> register` wil be added to
    // trigger targeted style rule evaluation when a relevant state changes on the
    // node
    public record Result(boolean matches) {
    }
}