package com.github.ruediste.jreact.primitives;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import com.github.ruediste.jreact.Component;
import com.github.ruediste.jreact.Context;
import com.github.ruediste.jreact.CoreElementApis.EventApi;
import com.github.ruediste.jreact.OptionalN;

public class Accordion {

    public enum Orientation {
        HORIZONTAL,
        VERTICAL
    }

    private static record RootContext(OptionalN<?> value, Orientation orientation) {
    }

    private static Context<RootContext> rootContext = Context.create(null);

    public static class RootSingle<T> extends Component {
        protected OptionalN<T> value;
        protected Consumer<OptionalN<T>> onValueChange;
        public Orientation orientation;

        protected List<ItemData<T>> items = new ArrayList<>();

        public record Args<T>(RootSingle<T> root, Consumer<EventApi<?, ?>> config, List<ItemData<T>> items) {
        }

        protected Consumer<Args<T>> render;

        public RootSingle(Consumer<Args<T>> render) {
            this.render = render;
        }

        public RootSingle<T> value(OptionalN<T> value) {
            this.value = value;
            return this;
        }

        /**
         * Sets the callback to be invoked when the value changes. If no callback is
         * set, the component is uncontrolled.
         */
        public RootSingle<T> onValueChange(Consumer<OptionalN<T>> cb) {
            this.onValueChange = cb;
            return this;
        }

        public RootSingle<T> item(T value, Runnable header, Runnable content) {
            return item(value, null, header, content);
        }

        public RootSingle<T> item(T value, Consumer<ItemData<T>> configure, Runnable header, Runnable content) {
            ItemData<T> item = new ItemData<>(value, header, content);
            if (configure != null)
                configure.accept(item);
            items.add(item);
            return this;
        }

        public RootSingle<T> orientation(Orientation orientation) {
            this.orientation = orientation;
            return this;
        }

        @Override
        public void render() {
            rootContext.provide(new RootContext(value, orientation), () -> {
                render.accept(new Args<>(this, eventApi -> {
                }, items));
            });
        }
    }

    public static class RootMultiple {
        // todo
    }

    public static record ItemContext(boolean isOpen, boolean isDisabled, Orientation orientation) {
    }

    private static Context<ItemContext> itemContext = Context.create(null);

    public static class ItemData<T> {
        public final T value;
        public boolean disabled;
        public Runnable header;
        public Runnable content;

        public ItemData(T value, Runnable header, Runnable content) {
            this.value = value;
            this.header = header;
            this.content = content;
        }

        public ItemData<T> disabled(boolean disabled) {
            this.disabled = disabled;
            return this;
        }

    }

    public static class Item<T> extends Component {
        private Consumer<Args> render;

        public final ItemData<?> data;

        public Item(ItemData<?> itemData, Consumer<Args> render) {
            this.key = itemData.value;
            this.data = itemData;
            this.render = render;
        }

        public record Args(ItemData<?> data, Consumer<EventApi<?, ?>> config) {
        }

        public void render() {
            var rootContextValue = rootContext.use();
            var itemContextValue = new ItemContext(
                    rootContextValue.value().map(x -> Objects.equals(x, data.value)).orElse(false),
                    data.disabled,
                    rootContextValue.orientation());
            itemContext
                    .provide(itemContextValue, () -> {
                        render.accept(new Args(data, eventApi -> {
                        }));
                    });
        }
    }

    public record HeaderArgs(ItemContext ctx, Consumer<EventApi<?, ?>> config) {
    }

    public static void Header(Consumer<HeaderArgs> render) {
        render.accept(new HeaderArgs(itemContext.use(), eventApi -> {
        }));
    }

    public record TriggerArgs(ItemContext ctx, Consumer<EventApi<?, ?>> config) {
    }

    public static void Trigger(Consumer<TriggerArgs> render) {
        render.accept(new TriggerArgs(itemContext.use(), eventApi -> {
        }));
    }

    public record ContentArgs(ItemContext ctx, Consumer<EventApi<?, ?>> config) {
    }

    public static void Content(Consumer<ContentArgs> render) {
        render.accept(new ContentArgs(itemContext.use(), eventApi -> {
        }));
    }
}
