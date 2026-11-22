package com.github.ruediste.jreact;

import java.util.Objects;
import java.util.function.Consumer;

public final class JReactContext {

    private static final ThreadLocal<Context> CURRENT = new ThreadLocal<>();

    private JReactContext() {
    }

    public static Context context() {
        Context context = CURRENT.get();
        if (context == null)
            throw new IllegalStateException("No active JReact render context");
        return context;
    }

    public static Context installContext(Context context) {
        Objects.requireNonNull(context);
        Context previous = CURRENT.get();
        CURRENT.set(context);
        return previous;
    }

    public static void restoreContext(Context previous) {
        if (previous == null)
            CURRENT.remove();
        else
            CURRENT.set(previous);
    }

    public static JReactRoot root() {
        return context().root;
    }

    public static void key(Object key) {
        context().pendingKey = OptionalN.of(key);
    }

    public static float viewportWidth() {
        return context().root.viewportWidth();
    }

    public static float viewportHeight() {
        return context().root.viewportHeight();
    }

    public static void withContainer(Node node, Runnable children) {
        withContainer(node::add, children);
    }

    public static void withContainer(Consumer<Node> onNodeCreated, Runnable children) {
        if (children == null)
            return;
        Context context = context();
        Consumer<Node> previousCallback = context.onNodeCreated;
        int previousKeyCounter = context.keyCounter;
        context.onNodeCreated = onNodeCreated;
        context.keyCounter = 0;
        try {
            children.run();
        } finally {
            context.onNodeCreated = previousCallback;
            context.keyCounter = previousKeyCounter;
        }
    }

    public static void addNode(Node node) {
        Context context = context();
        if (context.pendingKey.isPresent()) {
            node.key = context.pendingKey.get();
            context.pendingKey = OptionalN.empty();
        } else if (node.key == null) {
            node.key = new AutoKey(context.keyCounter++);
        }
        if (context.onNodeCreated == null)
            throw new IllegalStateException("No node creation callback is active");
        context.onNodeCreated.accept(node);
    }

    public static final class Context {
        public final JReactRoot root;
        public Consumer<Node> onNodeCreated;
        public Fiber fiber;

        public int keyCounter;
        public OptionalN<Object> pendingKey = OptionalN.empty();

        public Context(JReactRoot root) {
            this.root = Objects.requireNonNull(root);
        }

        public void resetKeyCounters() {
            keyCounter = 0;
            pendingKey = OptionalN.empty();
        }
    }

    private record AutoKey(int index) {
    }
}
