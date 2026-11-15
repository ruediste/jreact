package com.github.ruediste.jreact.style;

import java.util.Objects;
import java.util.function.Function;

import com.github.ruediste.jreact.nodes.Node;

public class Selectors {
    public interface SelectorBuilder<Self> {
        Self selectorChained(Function<Selector, Selector> factory);

        Self selectorTerminal(Selector selector);
    }

    public interface DefaultSelectors<Self, Builder extends SelectorBuilder<Self>> extends ApiHelperProvider<Builder> {

        default Self id(String id) {
            Objects.requireNonNull(id);
            return _helper().selectorTerminal(node -> new Selector.Result(id.equals(node.id)));
        }

        default Self className(String name) {
            if (name == null || name.isBlank() || name.chars().anyMatch(Character::isWhitespace))
                throw new IllegalArgumentException("A selector class must be a single non-empty name");
            return _helper().selectorTerminal(node -> new Selector.Result(node.classNames.contains(name)));
        }

        default Self type(Class<? extends Node> type) {
            Objects.requireNonNull(type);
            return _helper().selectorTerminal(node -> new Selector.Result(type.isAssignableFrom(node.getClass())));
        }

        default Self hover() {
            return _helper().selectorTerminal(node -> new Selector.Result(node.hover));
        }

        default Self active() {
            return _helper().selectorTerminal(node -> new Selector.Result(node.active));
        }

        default Self focus() {
            return _helper().selectorTerminal(node -> new Selector.Result(node.focus));
        }

        default Self disabled() {
            return _helper().selectorTerminal(node -> new Selector.Result(node.disabled));
        }

        default Self parent() {
            return _helper().selectorChained(
                    selector -> node -> node.parent == null ? new Selector.Result(false) : selector.match(node.parent));
        }

        default Self ancestor() {
            return _helper().selectorChained(selector -> node -> new Selector.Result(
                    node.ancestors().anyMatch(ancestor -> selector.match(ancestor).matches())));
        }
    }
}
