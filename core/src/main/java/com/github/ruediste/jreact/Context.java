package com.github.ruediste.jreact;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A value shared with descendant components without passing it through
 * properties.
 *
 * <p>
 * A context is created once with a default value and provided to a subtree with
 * {@link #provide(Object, Runnable)}. Components read the value of the nearest
 * matching provider with {@link Hooks#useContext(Context)}. Without a provider,
 * the default value is returned.
 *
 * <p>
 * When a provider is rendered again with a value that differs according to
 * {@link Objects#equals(Object, Object)}, the fibers of all subscribed
 * components are marked dirty. They are rerendered in the same refresh, even
 * when they are hidden behind component boundaries whose properties did not
 * change.
 *
 * <p>
 * Providers may be nested. The nearest provider in the node tree wins; it does
 * not matter whether it was emitted by the same component or by an ancestor.
 * The provider node itself is not visible: it takes no part in layout or
 * painting.
 *
 * @param <T> the provided value type
 */
public final class Context<T> {

    private final T defaultValue;

    private Context(T defaultValue) {
        this.defaultValue = defaultValue;
    }

    /**
     * Creates a context with the supplied default value.
     *
     * @param defaultValue value returned by {@link Hooks#useContext(Context)}
     *                     when no provider is present
     * @param <T>          the provided value type
     * @return the new context
     */
    public static <T> Context<T> create(T defaultValue) {
        return new Context<>(defaultValue);
    }

    /**
     * Returns the value used when no provider of this context is present.
     */
    public T defaultValue() {
        return defaultValue;
    }

    /**
     * Provides {@code value} to all descendants created by {@code children}.
     *
     * <p>
     * The provider is part of the rendered node tree, so it is reconciled like
     * any other node. Changing the value therefore invalidates subscribing
     * components.
     *
     * @param value    the value visible to consumers below this provider
     * @param children callback rendering the provider's descendants
     */
    public void provide(T value, Runnable children) {
        Objects.requireNonNull(children);
        ProviderNode provider = new ProviderNode(this, value);
        JReactContext.addNode(provider);
        JReactContext.withContainer(provider, children);
    }

    @SuppressWarnings("unchecked")
    public T use() {
        Fiber fiber = Hooks.fiber();
        ContextHook hook = Hooks.use(ContextHook.class, ContextHook::new);
        ProviderNode provider = findProvider(fiber.component);
        T value = provider == null ? defaultValue : (T) provider.value;
        hook.update(fiber, provider);
        return value;
    }

    private ProviderNode findProvider(Node component) {
        for (Node ancestor = component.parent; ancestor != null; ancestor = ancestor.parent)
            if (ancestor instanceof ProviderNode provider && provider.context == this)
                return provider;
        return null;
    }

    private static final class ProviderNode extends Node {
        private final Context<?> context;
        private final Object value;
        private final Set<ContextHook> subscribers = new LinkedHashSet<>();

        ProviderNode(Context<?> context, Object value) {
            this.context = context;
            this.value = value;
        }

        @Override
        public Object identityType() {
            return context;
        }

        @Override
        public void reconcile(Node oldNode) {
            ProviderNode old = (ProviderNode) oldNode;
            subscribers.addAll(old.subscribers);
            old.subscribers.clear();
            for (ContextHook hook : subscribers)
                hook.providerReconciled(old, this);

            if (Objects.equals(old.value, value))
                return;
            JReactRoot root = JReactContext.root();
            for (ContextHook hook : subscribers)
                if (hook.fiber != null)
                    root.markDirty(hook.fiber);
        }

        @Override
        public void dispose() {
            subscribers.clear();
        }
    }

    private static final class ContextHook implements Hooks.Hook {
        private Fiber fiber;
        private ProviderNode provider;

        void update(Fiber fiber, ProviderNode provider) {
            if (this.provider != provider) {
                if (this.provider != null)
                    this.provider.subscribers.remove(this);
                this.provider = provider;
                if (provider != null)
                    provider.subscribers.add(this);
            }
            this.fiber = fiber;
        }

        void providerReconciled(ProviderNode oldProvider, ProviderNode newProvider) {
            if (provider == oldProvider)
                provider = newProvider;
        }

        @Override
        public void close() {
            if (provider != null)
                provider.subscribers.remove(this);
            provider = null;
            fiber = null;
        }
    }
}
