package com.github.ruediste.jreact.nodes;

import java.util.Objects;

import com.github.ruediste.jreact.Fiber;

public abstract class ComponentNode extends Node {

    public final Object identityType;
    public Fiber fiber;

    public ComponentNode(Object identityType) {
        this.identityType = Objects.requireNonNull(identityType);
    }

    @Override
    public Object identityType() {
        return identityType;
    }

    @Override
    public void created() {
        fiber = new Fiber();
        fiber.component = this;
    }

    @Override
    public void reconcile(Node oldNode) {
        fiber = ((ComponentNode) oldNode).fiber;
        fiber.component = this;
    }

    public abstract void render();

    public abstract boolean sameProperties(ComponentNode other);
}
