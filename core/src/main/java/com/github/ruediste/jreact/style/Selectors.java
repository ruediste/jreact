package com.github.ruediste.jreact.style;

import java.util.Objects;
import java.util.function.Function;

import com.github.ruediste.jreact.Node;

public class Selectors {
    public interface SelectorElementApiHelper<Api, TSelectors> {
        TSelectors selectorChained(Function<Selector, Selector> factory);

        Api selectorTerminal(Selector selector);
    }

    public interface SelectorElementApi<Self, TSelectors, Helper extends SelectorElementApiHelper<Self, TSelectors>>
            extends ApiHelperProvider<Helper> {

        default Self hasId(String id) {
            Objects.requireNonNull(id);
            return _helper().selectorTerminal(node -> new Selector.Result(id.equals(node.id)));
        }

        default Self hasClass(String name) {
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

        default TSelectors parent() {
            return _helper().selectorChained(selector -> node -> new Selector.Result(
                    node.parentElement().map(parent -> selector.match(parent).matches()).orElse(false)));
        }

        default TSelectors ancestor() {
            return _helper().selectorChained(selector -> node -> new Selector.Result(
                    node.ancestorElements().anyMatch(ancestor -> selector.match(ancestor).matches())));
        }
    }
}
