package com.github.ruediste;

import static com.github.ruediste.jreact.style.StyleProperties.DISPLAY;

import java.util.function.Consumer;

import com.antepod.taffy.style.Display;
import com.github.ruediste.jreact.nodes.Node;
import com.github.ruediste.jreact.style.Style;
import com.github.ruediste.jreact.style.StyleProperties.Property;

public class NextApi {

    @FunctionalInterface
    public interface Selector {
        public Result match(Node node);

        public record Result(boolean matches, Consumer<Rule> register) {
        }
    }

    public record Rule(Selector selector, Style style) {

    }

    public interface LayoutStyles<Self> {

        <T> Self _putStyle(Property<T> property, T value);

        default Self display(Display value) {
            return _putStyle(DISPLAY, value);
        }
    }

    public interface DefaultSelectors<Self> {

    }
}
