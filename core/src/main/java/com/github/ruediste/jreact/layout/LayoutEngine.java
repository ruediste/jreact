package com.github.ruediste.jreact.layout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import com.antepod.taffy.geometry.Size;
import com.antepod.taffy.style.AvailableSpace;
import com.antepod.taffy.style.FlexDirection;
import com.antepod.taffy.tree.Layout;
import com.antepod.taffy.tree.NodeId;
import com.antepod.taffy.tree.TaffyTree;
import com.github.ruediste.jreact.Node;
import com.github.ruediste.jreact.TextLayout;
import com.github.ruediste.jreact.elements.ElementNode;
import com.github.ruediste.jreact.elements.TextElement;

public final class LayoutEngine {

    private LayoutEngine() {
    }

    public static void layout(Node root, float viewportWidth, float viewportHeight) {
        if (root == null)
            return;

        Map<Node, NodeId> nodeIds = new HashMap<>();
        Map<NodeId, ElementNode> nodesById = new HashMap<>();

        try (TaffyTree<Node> tree = new TaffyTree<>()) {
            List<NodeId> topLevel = new ArrayList<>();
            for (ElementNode child : root.elementChildren())
                topLevel.add(build(tree, child, nodeIds, nodesById));

            com.antepod.taffy.style.Style rootStyle = com.antepod.taffy.style.Style.builder()
                    .display(com.antepod.taffy.style.Display.FLEX)
                    .flexDirection(FlexDirection.COLUMN)
                    .size(viewportWidth, viewportHeight)
                    .build();
            NodeId rootId = tree.newWithChildren(rootStyle, topLevel);

            tree.computeLayoutWithMeasure(rootId,
                    new Size<>(AvailableSpace.definite(viewportWidth), AvailableSpace.definite(viewportHeight)),
                    LayoutEngine::measure);

            assignLayouts(tree, rootId, 0, 0, nodesById);
        }

        commitText(root);
    }

    private static NodeId build(TaffyTree<Node> tree, ElementNode node, Map<Node, NodeId> nodeIds,
            Map<NodeId, ElementNode> nodesById) {
        List<ElementNode> children = node.elementChildren();
        NodeId id;
        if (children.isEmpty()) {
            id = tree.newLeafWithContext(com.github.ruediste.jreact.style.StyleProperties.buildTaffyStyle(node.style),
                    node);
        } else {
            List<NodeId> childIds = new ArrayList<>();
            for (ElementNode child : children)
                childIds.add(build(tree, child, nodeIds, nodesById));
            id = tree.newWithChildren(com.github.ruediste.jreact.style.StyleProperties.buildTaffyStyle(node.style),
                    childIds);
        }
        nodeIds.put(node, id);
        nodesById.put(id, node);
        return id;
    }

    private static void assignLayouts(TaffyTree<Node> tree, NodeId id, float offsetX, float offsetY,
            Map<NodeId, ElementNode> nodesById) {
        ElementNode node = nodesById.get(id);
        Layout layout = tree.layout(id);
        float nodeX = offsetX;
        float nodeY = offsetY;
        if (node != null) {
            node.layout = Objects.requireNonNull(layout);
            node.bounds.setRect(offsetX + layout.location.x, offsetY + layout.location.y,
                    layout.size.width, layout.size.height);
            nodeX = (float) node.bounds.x;
            nodeY = (float) node.bounds.y;
        }
        for (NodeId child : tree.children(id))
            assignLayouts(tree, child, nodeX, nodeY, nodesById);
    }

    private static Size<java.lang.Float> measure(Size<Optional<java.lang.Float>> knownDimensions,
            Size<AvailableSpace> availableSpace, NodeId id, Optional<Node> context,
            com.antepod.taffy.style.Style style) {
        Node node = context.orElse(null);
        if (node instanceof ElementNode element)
            return element.measure(knownDimensions, availableSpace);
        return Size.zeroFloat();
    }

    private static void commitText(Node node) {
        if (node instanceof TextElement text)
            TextLayout.commit(text);
        for (Node child : node.children())
            commitText(child);
    }
}