package com.github.ruediste.jreact;

import java.awt.Component;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import com.antepod.taffy.style.Display;
import com.github.ruediste.jreact.events.ClickEvent;
import com.github.ruediste.jreact.events.KeyboardEvent;
import com.github.ruediste.jreact.nodes.ComponentNode;
import com.github.ruediste.jreact.nodes.ElementNode;
import com.github.ruediste.jreact.nodes.Node;
import com.github.ruediste.jreact.style.Style;
import com.github.ruediste.jreact.style.StyleProperties;

public final class JReactRoot {

    private final JReactContext.Context context = new JReactContext.Context(this);
    private final Thread ownerThread = Thread.currentThread();
    private final Fiber rootFiber;
    private final List<Hooks.Effect> layoutEffects = new ArrayList<>();
    private final List<Hooks.Effect> effects = new ArrayList<>();
    private final List<Set<Fiber>> dirtyFibers = new ArrayList<>();
    private final Set<NodeReference> nodeReferences = new HashSet<>();

    private float viewportWidth;
    private float viewportHeight;
    private boolean rendering;
    public Runnable invalidationCallback = () -> {
    };
    public Consumer<Component> onComponentAdded = c -> {
    };
    public Consumer<Component> onComponentRemoved = c -> {
    };

    private final NodeReference hoverRef = createReference();
    private final NodeReference activeRef = createReference();
    private final NodeReference focusRef = createReference();
    private final NodeReference pressRef = createReference();
    private double pointerX;
    private double pointerY;
    private boolean pointerInside;

    public JReactRoot(Runnable rootRender) {
        var rootComponent = new ComponentNode(JReactRoot.class) {

            {
                depth = 0;
            }

            @Override
            public void render() {
                rootRender.run();
            }

            @Override
            public boolean sameProperties(ComponentNode other) {
                return true;
            }

        };
        rootComponent.created();
        this.rootFiber = rootComponent.fiber;
        this.markDirty(rootFiber);
    }

    private NodeReference createReference() {
        NodeReference reference = new NodeReference();
        nodeReferences.add(reference);
        return reference;
    }

    void replaceNode(Node oldNode, Node newNode) {
        for (NodeReference reference : nodeReferences)
            reference.replace(oldNode, newNode);
    }

    void invalidateNode(Node node) {
        for (NodeReference reference : nodeReferences)
            reference.invalidateAt(node);
    }

    JReactContext.Context context() {
        return context;
    }

    public Node rootNode() {
        return rootFiber.component;
    }

    public boolean needsRender() {
        return !dirtyFibers.isEmpty();
    }

    public void render() {
        refresh();
    }

    public RefreshResult refresh() {
        validateThread();
        if (rendering)
            throw new IllegalStateException("A render is already in progress");

        JReactContext.Context previousContext = JReactContext.installContext(context);
        try {
            beginRender();
            try {
                renderComponents();
            } finally {
                endRender();
            }

            applyInteractionState();
            resolveStyles(rootNode());
            com.github.ruediste.jreact.layout.LayoutEngine.layout(rootNode(), viewportWidth, viewportHeight);

            if (pointerInside) {
                List<Node> hit = hitPath(pointerX, pointerY);
                if (!hoverRef.equalsPath(hit)) {
                    hoverRef.setPath(hit);
                    applyInteractionState();
                    resolveStyles(rootNode());
                    com.github.ruediste.jreact.layout.LayoutEngine.layout(rootNode(), viewportWidth,
                            viewportHeight);
                }
            }

            afterLayoutTree(rootNode());

            runEffects(layoutEffects);

            Node focused = focusRef.target();
            return new RefreshResult(rootFiber.component,
                    focused == null ? null : focused.key,
                    new Rectangle2D.Double(0, 0, viewportWidth, viewportHeight));
        } finally {
            JReactContext.restoreContext(previousContext);
        }
    }

    public static void resolveStyles(Node node) {
        if (node instanceof ElementNode element) {
            Style style = new Style();
            element.applyLayoutDefaults(style);
            element.styleRules.stream()
                    .filter(rule -> rule.selector().match(element).matches())
                    .forEach(rule -> style.putAll(rule.style()));
            style.putAll(element.inline);
            element.style = style;
        }
        node.children().forEach(JReactRoot::resolveStyles);
    }

    public RefreshResult refresh(float viewportWidth, float viewportHeight) {
        setViewport(viewportWidth, viewportHeight);
        return refresh();
    }

    public void paintCompleted() {
        validateThread();
        JReactContext.Context previousContext = JReactContext.installContext(context);
        try {
            runEffects(effects);
        } finally {
            JReactContext.restoreContext(previousContext);
        }
    }

    public void close() {
        validateThread();
        JReactContext.Context previousContext = JReactContext.installContext(context);
        try {
            Reconciler.disposeTree(this, rootNode());
        } finally {
            JReactContext.restoreContext(previousContext);
        }

        layoutEffects.clear();
        effects.clear();
        dirtyFibers.clear();
        for (NodeReference reference : nodeReferences)
            reference.clear();
    }

    private void renderComponents() {
        Reconciler.Context context = new Reconciler.Context(this);
        while (!dirtyFibers.isEmpty()) {
            List<Set<Fiber>> pending = new ArrayList<>(dirtyFibers);
            dirtyFibers.clear();
            for (Set<Fiber> level : pending) {
                for (Fiber fiber : level) {
                    if (fiber.dirty && fiber.component != null)
                        Reconciler.renderAndReconcileDirtyComponent(context, fiber);
                }
            }
        }
    }

    void markDirty(Fiber fiber) {
        if (fiber.component == null || fiber.dirty) {
            return;
        }
        fiber.dirty = true;
        int depth = Math.max(0, fiber.component.depth);
        while (dirtyFibers.size() <= depth)
            dirtyFibers.add(new HashSet<>());
        dirtyFibers.get(depth).add(fiber);
    }

    private static void afterLayoutTree(Node node) {
        if (node instanceof ElementNode element)
            element.afterLayout();
        for (Node child : node.children())
            afterLayoutTree(child);
    }

    private static void runEffects(List<Hooks.Effect> queue) {
        if (queue.isEmpty())
            return;
        List<Hooks.Effect> effects = new ArrayList<>(queue);
        queue.clear();

        RuntimeException failure = null;
        for (Hooks.Effect effect : effects) {
            try {
                effect.run();
            } catch (RuntimeException exception) {
                if (failure == null)
                    failure = exception;
                else
                    failure.addSuppressed(exception);
            }
        }
        if (failure != null)
            throw failure;
    }

    public void pointerMoved(double x, double y) {
        validateThread();
        pointerX = x;
        pointerY = y;
        pointerInside = true;
        updateHover();
    }

    public void pointerExited() {
        validateThread();
        pointerInside = false;
        if (!hoverRef.isEmpty() || hoverRef.isTruncated()) {
            hoverRef.clear();
            requestRefresh();
        }
    }

    public void pointerPressed(double x, double y) {
        validateThread();
        pointerX = x;
        pointerY = y;
        pointerInside = true;
        List<Node> hit = hitPath(x, y);
        hoverRef.setPath(hit);
        pressRef.setPath(hit);
        activeRef.setPath(hit);
        focusRef.setPath(hit);
        requestRefresh();
    }

    public void pointerReleased(double x, double y) {
        validateThread();
        pointerX = x;
        pointerY = y;
        pointerInside = true;
        List<Node> hit = hitPath(x, y);
        hoverRef.setPath(hit);
        if (pressRef.equalsPath(hit)) {
            Node target = pressRef.target();
            if (target != null)
                dispatch(target, new ClickEvent());
        }
        pressRef.clear();
        activeRef.clear();
        requestRefresh();
    }

    public void keyPressed(int keyCode) {
        validateThread();
        Node target = focusRef.target();
        if (target != null)
            dispatch(target, new KeyboardEvent(keyCode));
    }

    private void updateHover() {
        List<Node> hit = hitPath(pointerX, pointerY);
        if (!hoverRef.equalsPath(hit)) {
            hoverRef.setPath(hit);
            requestRefresh();
        }
    }

    private List<Node> hitPath(double x, double y) {
        Node hit = hitTest(rootNode(), x, y);
        if (hit == null)
            return List.of();
        List<Node> path = new ArrayList<>();
        for (Node node = hit; node != null; node = node.parent)
            path.add(node);
        java.util.Collections.reverse(path);
        return path;
    }

    private static Node hitTest(Node node, double x, double y) {
        if (node == null)
            return null;
        if (node instanceof ElementNode visual && StyleProperties.DISPLAY.get(visual.style) == Display.NONE)
            return null;
        Node hit = null;
        for (Node child : node.children()) {
            Node childHit = hitTest(child, x, y);
            if (childHit != null)
                hit = childHit;
        }
        if (hit != null)
            return hit;
        if (node instanceof ElementNode element && element.bounds.contains(x, y))
            return element;
        return null;
    }

    private void applyInteractionState() {
        if (rootNode() != null)
            applyInteractionState(rootNode(), new ArrayList<>());
    }

    private void applyInteractionState(Node node, List<Node> path) {
        path.add(node);
        if (node instanceof ElementNode element) {
            element.hover = hoverRef.contains(node);
            element.active = activeRef.contains(node);
            element.focus = focusRef.equalsPath(path);
        }
        for (Node child : node.children())
            applyInteractionState(child, path);
        path.remove(path.size() - 1);
    }

    private static void dispatch(Node target, JReactEvent event) {
        event.target = target;
        List<Node> path = new ArrayList<>();
        for (Node node = target; node != null; node = node.parent)
            path.add(node);
        java.util.Collections.reverse(path);
        for (int index = 0; index < path.size() - 1; index++) {
            if (path.get(index) instanceof ElementNode element)
                element.dispatchCapture(event);
            if (event.isStopPropagation())
                return;
        }
        Node targetNode = path.getLast();
        if (targetNode instanceof ElementNode element)
            element.dispatchCapture(event);
        if (event.isStopPropagation())
            return;
        if (targetNode instanceof ElementNode element)
            element.dispatchBubble(event);
        if (event.isStopPropagation())
            return;
        for (int index = path.size() - 2; index >= 0; index--) {
            if (path.get(index) instanceof ElementNode element)
                element.dispatchBubble(event);
            if (event.isStopPropagation())
                return;
        }
    }

    void enqueueLayoutEffect(Hooks.Effect effect) {
        layoutEffects.add(effect);
    }

    void enqueueEffect(Hooks.Effect effect) {
        effects.add(effect);
    }

    void requestRefresh() {
        invalidationCallback.run();
    }

    public float viewportWidth() {
        return viewportWidth;
    }

    public float viewportHeight() {
        return viewportHeight;
    }

    public void setViewport(float width, float height) {
        validateThread();
        if (!Float.isFinite(width) || width < 0)
            throw new IllegalArgumentException("Viewport width must be finite and non-negative");
        if (!Float.isFinite(height) || height < 0)
            throw new IllegalArgumentException("Viewport height must be finite and non-negative");
        if (Float.compare(viewportWidth, width) == 0 && Float.compare(viewportHeight, height) == 0)
            return;
        viewportWidth = width;
        viewportHeight = height;
        markDirty(rootFiber);
    }

    void validateStateWrite() {
        validateThread();
        if (rendering)
            throw new IllegalStateException("State must not change during component rendering");
    }

    void beginRender() {
        rendering = true;
    }

    void endRender() {
        rendering = false;
    }

    private void validateThread() {
        if (Thread.currentThread() != ownerThread)
            throw new IllegalStateException("JReactRoot must be accessed from its owning thread");
    }
}
