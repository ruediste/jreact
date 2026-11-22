package com.github.ruediste.jreact;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import com.github.ruediste.jreact.elements.ElementNode;

public class Node {

    private List<Node> children = new ArrayList<>();

    public Node parent;
    public int depth;
    public Object key;

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

    public List<ElementNode> elementChildren() {
        List<ElementNode> result = new ArrayList<>();
        for (Node child : children) {
            if (child instanceof ElementNode element)
                result.add(element);
            else
                result.addAll(child.elementChildren());
        }
        return result;
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
