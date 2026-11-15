package com.github.ruediste.jreact.nodes;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import com.antepod.taffy.tree.Layout;
import com.github.ruediste.jreact.EventListener;
import com.github.ruediste.jreact.JReactEvent;
import com.github.ruediste.jreact.style.Style;
import com.github.ruediste.jreact.style.StyleRule;

public class Node {

    public Node parent;
    public int depth;
    public Object key;

    public String id;
    public List<String> classNames = new ArrayList<>();

    public final Style inline = new Style();
    public final List<StyleRule> styleRules = new ArrayList<>();
    public Style style = new Style();

    public boolean hover;
    public boolean active;
    public boolean focus;
    public boolean disabled;
    public boolean focusable;
    public String accessibleName;

    public final Rectangle2D.Double bounds = new Rectangle2D.Double();

    private final Map<Class<? extends JReactEvent>, List<EventListener<?>>> captureListeners = new HashMap<>();
    private final Map<Class<? extends JReactEvent>, List<EventListener<?>>> bubbleListeners = new HashMap<>();
    private Layout layout = Layout.zero();

    public final List<Line> lines = new ArrayList<>();

    private List<Node> children = new ArrayList<>();

    public Node() {
    }

    public Node(Object key) {
        this.key = key;
    }

    public Node setKey(Object key) {
        this.key = key;
        return this;
    }

    public Identity identity() {
        return new Identity(identityType(), key);
    }

    public Object identityType() {
        return getClass();
    }

    public boolean sameIdentity(Node other) {
        return other != null && identity().equals(other.identity());
    }

    public List<Node> children() {
        return Collections.unmodifiableList(children);
    }

    public Node add(Node child) {
        Objects.requireNonNull(child);
        if (child == this)
            throw new IllegalArgumentException("A node cannot be its own child");
        child.parent = this;
        children.add(child);
        child.updateDepth(depth + 1);
        return this;
    }

    /**
     * Called after the node has been rendered, allowing for any post-render logic
     * to be executed. Invoked on each render
     */
    public void afterRender() {

    }

    /**
     * Called after the node has been laid out, allowing for any logic which
     * depends on the final bounds to be executed. Invoked on each render.
     */
    public void afterLayout() {

    }

    /**
     * Called after the node has been created, allowing for any initialization logic
     * to be executed. Invoked once after the node is added to the render tree.
     * Unlike afterRender, this is not
     * called if another node is reconciled into this node.
     */
    public void created() {
    }

    /**
     * Called when the node is reconciled with a new node, allowing for any update
     * logic
     * to be executed. Invoked when the node is replaced by another node in the
     * render tree.
     */
    public void reconcile(Node oldNode) {

    }

    /**
     * Called when the node is no longer part of the render tree. Note that a node
     * is reconciled into another node, this
     * method will not be called for the old node. It only called when there is no
     * replacement for this node in the render tree.
     */
    public void dispose() {

    }

    private void updateDepth(int newDepth) {
        if (depth == newDepth)
            return;
        depth = newDepth;
        for (Node child : children)
            child.updateDepth(newDepth + 1);
    }

    public Node replaceChildren(List<? extends Node> replacement) {
        Objects.requireNonNull(replacement);
        children = new ArrayList<>();
        for (Node child : replacement)
            add(child);
        return this;
    }

    public Layout getLayout() {
        return layout;
    }

    public void setLayout(Layout layout) {
        this.layout = Objects.requireNonNull(layout);
    }

    public void applyLayoutDefaults() {
    }

    public <T extends JReactEvent> Node on(Class<T> eventType, EventListener<T> listener) {
        bubbleListeners.computeIfAbsent(Objects.requireNonNull(eventType), ignored -> new ArrayList<>())
                .add(Objects.requireNonNull(listener));
        return this;
    }

    public <T extends JReactEvent> Node capture(Class<T> eventType, EventListener<T> listener) {
        captureListeners.computeIfAbsent(Objects.requireNonNull(eventType), ignored -> new ArrayList<>())
                .add(Objects.requireNonNull(listener));
        return this;
    }

    @SuppressWarnings("unchecked")
    public void dispatchCapture(JReactEvent event) {
        for (EventListener<?> listener : captureListeners.getOrDefault(event.getClass(), List.of()))
            ((EventListener<JReactEvent>) listener).handle(event);
    }

    @SuppressWarnings("unchecked")
    public void dispatchBubble(JReactEvent event) {
        for (EventListener<?> listener : bubbleListeners.getOrDefault(event.getClass(), List.of()))
            ((EventListener<JReactEvent>) listener).handle(event);
    }

    public record Identity(Object type, Object key) {
        public Identity {
            Objects.requireNonNull(type);
        }
    }

    public record Line(String text, float x, float baseline) {
        public Line {
            Objects.requireNonNull(text);
        }
    }

    public Stream<Node> ancestors() {
        Stream.Builder<Node> builder = Stream.builder();
        for (Node ancestor = parent; ancestor != null; ancestor = ancestor.parent)
            builder.add(ancestor);
        return builder.build();
    }
}
