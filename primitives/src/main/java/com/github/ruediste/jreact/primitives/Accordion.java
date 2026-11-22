package com.github.ruediste.jreact.primitives;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.github.ruediste.jreact.Component;
import com.github.ruediste.jreact.Context;
import com.github.ruediste.jreact.CoreElementApis.EventApi;
import com.github.ruediste.jreact.Hooks;
import com.github.ruediste.jreact.OptionalN;
import com.github.ruediste.jreact.State;
import com.github.ruediste.jreact.events.KeyboardEvent;

public class Accordion {

    public enum Orientation {
        HORIZONTAL,
        VERTICAL
    }

    private static record RootContext(OptionalN<?> value, Orientation orientation, Consumer<Object> toggleItem,
            BiConsumer<Object, Boolean> setItemOpen, boolean collapsible) {
    }

    private static Context<RootContext> rootContext = Context.create(null);

    public static class RootSingle<T> extends Component {
        protected OptionalN<T> value = OptionalN.empty();
        protected Consumer<OptionalN<T>> onValueChange;
        public Orientation orientation = Orientation.VERTICAL;
        protected boolean collapsible = true;

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

        /**
         * Controls whether the open item can be closed again. Defaults to
         * {@code true}.
         */
        public RootSingle<T> collapsible(boolean collapsible) {
            this.collapsible = collapsible;
            return this;
        }

        @Override
        public void render() {
            OptionalN<T> initialValue = value == null ? OptionalN.empty() : value;
            State<OptionalN<T>> state = Hooks.useState(initialValue);
            boolean controlled = onValueChange != null;
            OptionalN<T> selected = controlled ? initialValue : state.get();
            if (selected == null)
                selected = OptionalN.empty();
            OptionalN<T> current = selected;
            Orientation resolvedOrientation = orientation == null ? Orientation.VERTICAL : orientation;

            Consumer<OptionalN<T>> change = next -> {
                if (!controlled)
                    state.set(next);
                if (onValueChange != null)
                    onValueChange.accept(next);
            };

            Consumer<Object> toggleItem = itemValue -> {
                boolean isSelected = current.isPresent() && Objects.equals(current.get(), itemValue);
                if (isSelected && !collapsible)
                    return;
                if (isSelected)
                    change.accept(OptionalN.empty());
                else
                    change.accept(OptionalN.of(castItem(itemValue)));
            };

            BiConsumer<Object, Boolean> setItemOpen = (itemValue, open) -> {
                boolean isSelected = current.isPresent() && Objects.equals(current.get(), itemValue);
                if (open == isSelected)
                    return;
                if (open)
                    change.accept(OptionalN.of(castItem(itemValue)));
                else if (collapsible)
                    change.accept(OptionalN.empty());
            };

            rootContext.provide(new RootContext(current, resolvedOrientation, toggleItem, setItemOpen, collapsible),
                    () -> {
                        render.accept(new Args<>(this, eventApi -> {
                        }, items));
                    });
        }

        @SuppressWarnings("unchecked")
        private T castItem(Object itemValue) {
            return (T) itemValue;
        }
    }

    public static class RootMultiple {
        // todo
    }

    public static record ItemContext(boolean isOpen, boolean isDisabled, Orientation orientation,
            Runnable toggle, Consumer<Boolean> setOpen, String accessibleName) {
    }

    private static Context<ItemContext> itemContext = Context.create(null);

    public static class ItemData<T> {
        public final T value;
        public boolean disabled;
        public String accessibleName;
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

        public ItemData<T> accessibleName(String accessibleName) {
            this.accessibleName = accessibleName;
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
            boolean isOpen = rootContextValue.value().map(x -> Objects.equals(x, data.value)).orElse(false);
            Runnable toggle = () -> rootContextValue.toggleItem().accept(data.value);
            Consumer<Boolean> setOpen = open -> rootContextValue.setItemOpen().accept(data.value, open);
            String accessibleName = data.accessibleName != null ? data.accessibleName
                    : String.valueOf(data.value);
            var itemContextValue = new ItemContext(isOpen, data.disabled,
                    rootContextValue.orientation(), toggle, setOpen, accessibleName);
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
        ItemContext ctx = itemContext.use();
        render.accept(new TriggerArgs(ctx, eventApi -> {
            eventApi.onClick(event -> {
                if (!ctx.isDisabled())
                    ctx.toggle().run();
            });
            eventApi.on(KeyboardEvent.class, event -> {
                if (ctx.isDisabled())
                    return;
                if (event.keyCode == KeyEvent.VK_ENTER || event.keyCode == KeyEvent.VK_SPACE) {
                    ctx.toggle().run();
                    event.stopPropagation();
                }
            });
        }));
    }

    public record ContentArgs(ItemContext ctx, Consumer<EventApi<?, ?>> config) {
    }

    public static void Content(Consumer<ContentArgs> render) {
        render.accept(new ContentArgs(itemContext.use(), eventApi -> {
        }));
    }
}
