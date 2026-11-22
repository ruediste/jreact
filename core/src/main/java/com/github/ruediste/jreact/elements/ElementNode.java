package com.github.ruediste.jreact.elements;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import com.antepod.taffy.geometry.Size;
import com.antepod.taffy.style.AvailableSpace;
import com.antepod.taffy.style.Display;
import com.antepod.taffy.tree.Layout;
import com.github.ruediste.jreact.EventListener;
import com.github.ruediste.jreact.JReactEvent;
import com.github.ruediste.jreact.Node;
import com.github.ruediste.jreact.PaintUtils;
import com.github.ruediste.jreact.color.JColor;
import com.github.ruediste.jreact.style.Style;
import com.github.ruediste.jreact.style.StyleProperties;
import com.github.ruediste.jreact.style.StyleRule;

public class ElementNode extends Node {

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
    public Layout layout = Layout.zero();

    public <T extends JReactEvent> ElementNode on(Class<T> eventType, EventListener<T> listener) {
        bubbleListeners.computeIfAbsent(Objects.requireNonNull(eventType), ignored -> new ArrayList<>())
                .add(Objects.requireNonNull(listener));
        return this;
    }

    public <T extends JReactEvent> ElementNode capture(Class<T> eventType, EventListener<T> listener) {
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

    /**
     * Called after the node has been laid out, allowing for any logic which
     * depends on the final bounds to be executed. Invoked on each render.
     */
    public void afterLayout() {

    }

    public void applyLayoutDefaults(Style style) {
    }

    public ElementNode() {
    }

    public ElementNode(Object key) {
        super(key);
    }

    public Optional<ElementNode> parentElement() {
        for (Node ancestor = parent; ancestor != null; ancestor = ancestor.parent)
            if (ancestor instanceof ElementNode element)
                return Optional.of(element);
        return Optional.empty();
    }

    public Stream<ElementNode> ancestorElements() {
        return ancestors()
                .filter(ElementNode.class::isInstance)
                .map(ElementNode.class::cast);
    }

    /**
     * Computes the intrinsic size of this node for the layout engine. Called
     * during layout for nodes without children.
     *
     * @param knownDimensions dimensions already fixed by the layout, if any
     * @param availableSpace  space available to the node
     * @return the intrinsic size of the node's content
     */
    public Size<Float> measure(Size<Optional<Float>> knownDimensions, Size<AvailableSpace> availableSpace) {
        return Size.zeroFloat();
    }

    public Rectangle2D.Double paintBounds() {
        return new Rectangle2D.Double(bounds.x, bounds.y, bounds.width, bounds.height);
    }

    public void paint(Graphics2D graphics, Rectangle clip) {
        if (StyleProperties.DISPLAY.get(style) == Display.NONE)
            return;
        if (clip != null && !bounds.intersects(clip))
            return;

        Composite previousComposite = graphics.getComposite();
        double opacity = StyleProperties.OPACITY.get(style);
        if (opacity < 1) {
            float alpha = (float) Math.max(0, Math.min(1, opacity));
            graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        }
        try {
            Shape shape = PaintUtils.shape(this);
            StyleProperties.Shadow shadow = StyleProperties.BOX_SHADOW.get(style);
            if (shadow != null)
                PaintUtils.shadow(this, graphics, shadow, layer -> layer.fill(shape));

            JColor background = StyleProperties.BACKGROUND_COLOR.get(style);
            if (background != null) {
                graphics.setColor(background.toColor());
                graphics.fill(shape);
            }

            PaintUtils.paintBorder(this, graphics, shape);
            paintContent(graphics);
        } finally {
            graphics.setComposite(previousComposite);
        }
    }

    protected void paintContent(Graphics2D graphics) {
    }
}
