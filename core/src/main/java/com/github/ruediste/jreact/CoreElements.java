package com.github.ruediste.jreact;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import javax.swing.Icon;

import com.github.ruediste.jreact.events.ClickEvent;
import com.github.ruediste.jreact.nodes.DivNode;
import com.github.ruediste.jreact.nodes.ElementNode;
import com.github.ruediste.jreact.nodes.IconNode;
import com.github.ruediste.jreact.nodes.InputNode;
import com.github.ruediste.jreact.nodes.SelectNode;
import com.github.ruediste.jreact.nodes.SpanNode;
import com.github.ruediste.jreact.nodes.TextNode;
import com.github.ruediste.jreact.style.ApiHelperProvider;
import com.github.ruediste.jreact.style.CoreStyleApis.LayoutStyleApi;
import com.github.ruediste.jreact.style.CoreStyleApis.StyleApiHelper;
import com.github.ruediste.jreact.style.StyleProperties.Property;

public final class CoreElements {

    private CoreElements() {
    }

    public static class NodeApiHelper<Api> implements StyleApiHelper<Api>, ChildrenApiHelper<Api>, EventApiHelper<Api>,
            NodeAccessApiHelper<Api> {

        public Api api;
        public ElementNode node;

        public NodeApiHelper(ElementNode node) {
            this.node = node;

        }

        @Override
        public Api apply(Consumer<ElementNode> updater) {
            updater.accept(node);
            return api;
        }

        @Override
        public <T> Api putStyle(Property<T> property, T value) {
            property.set(node.inline, value);
            return api;
        }

        @Override
        public Api withChildren(Runnable children) {
            JReactContext.withContainer(node, children);
            return api;
        }

        @Override
        public <T extends JReactEvent> Api on(Class<T> eventClass, EventListener<T> listener) {
            node.on(eventClass, listener);
            return api;
        }

    }

    public interface NodeAccessApiHelper<Api> {
        Api apply(Consumer<ElementNode> updater);
    }

    public interface ElementNodeCoreApi<Self, Helper extends NodeAccessApiHelper<Self>>
            extends ApiHelperProvider<Helper> {
        default Self key(Object key) {
            return _helper().apply(n -> n.key = key);
        }

        default Self id(String id) {
            return _helper().apply(n -> n.id = id);
        }

        default Self classNames(String... classNames) {
            Objects.requireNonNull(classNames);
            return _helper().apply(n -> {
                n.classNames.clear();
                for (String className : classNames)
                    n.classNames.add(Objects.requireNonNull(className));
            });
        }

        default Self className(String className) {
            Objects.requireNonNull(className);
            return _helper().apply(n -> n.classNames.add(className));
        }

        default Self accessibleName(String accessibleName) {
            return _helper().apply(n -> n.accessibleName = accessibleName);
        }

        @SuppressWarnings("unchecked")
        default Self apply(Consumer<? super Self> consumer) {
            return _helper().apply(n -> consumer.accept((Self) this));
        }
    }

    public interface EventApiHelper<Api> {
        <T extends JReactEvent> Api on(Class<T> eventClass, EventListener<T> listener);
    }

    public interface EventApi<Self, Helper extends EventApiHelper<Self>> extends ApiHelperProvider<Helper> {
        default Self onClick(EventListener<ClickEvent> onClick) {
            return _helper().on(ClickEvent.class, onClick);
        }
    }

    public static interface VisualStyleApiBase<Self> extends ElementNodeCoreApi<Self, NodeApiHelper<Self>>,
            LayoutStyleApi<Self, NodeApiHelper<Self>>,
            com.github.ruediste.jreact.style.CoreStyleApis.VisualStyleApi<Self, NodeApiHelper<Self>>,
            EventApi<Self, NodeApiHelper<Self>> {
    }

    public static interface VisualStyleApi extends VisualStyleApiBase<VisualStyleApi> {
    }

    public static interface VisualStyleWithChildrenApi extends VisualStyleApiBase<VisualStyleWithChildrenApi>,
            ChildrenApi<VisualStyleWithChildrenApi, NodeApiHelper<VisualStyleWithChildrenApi>> {
    }

    static class NodeApi<Api> implements VisualStyleApiBase<Api>, ChildrenApi<Api, NodeApiHelper<Api>> {
        public NodeApiHelper<Api> helper;

        @Override
        public NodeApiHelper<Api> _helper() {
            return helper;
        }

    }

    interface NodeConfigurationApiHelper<Api, TNode> {
        TNode node();

        Api api();
    }

    interface ChildrenApiHelper<Api> {
        Api withChildren(Runnable children);
    }

    interface ChildrenApi<Self, Helper extends ChildrenApiHelper<Self>> extends ApiHelperProvider<Helper> {
        default Self children(Runnable children) {
            return _helper().withChildren(children);
        }
    }

    static <T> T api(ElementNode node) {
        var api = new NodeApi<T>();
        return api(node, api);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    static <T> T api(ElementNode node, NodeApi<? extends T> api) {
        var helper = new NodeApiHelper<T>(node);
        api.helper = (NodeApiHelper) helper;
        helper.api = (T) api;
        return (T) api;
    }

    private static <T> T addNode(ElementNode node) {
        JReactContext.addNode(node);
        return api(node);
    }

    private static <T> T addNode(ElementNode node, NodeApi<? extends T> api) {
        JReactContext.addNode(node);
        return api(node, api);
    }

    public static VisualStyleWithChildrenApi div() {
        return addNode(new DivNode(), new DivApi());
    }

    public static VisualStyleWithChildrenApi div(Runnable children) {
        return div().children(children);
    }

    public static VisualStyleWithChildrenApi span() {
        return addNode(new SpanNode(), new SpanApi());
    }

    public static VisualStyleWithChildrenApi span(Runnable children) {
        var api = span();
        return api.children(children);
    }

    public static VisualStyleApi text(String text) {
        return addNode(new TextNode(Objects.requireNonNull(text)), new TextApi());
    }

    public static VisualStyleApi icon(Icon icon) {
        return addNode(new IconNode(Objects.requireNonNull(icon)));
    }

    public interface InputApi extends VisualStyleApiBase<InputApi> {
        default InputApi disabled(boolean disabled) {
            var helper = _helper();
            helper.node.disabled = disabled;
            return helper.api;
        }
    }

    public static InputApi input(String value, Consumer<String> onChange) {
        InputNode node = new InputNode(Objects.requireNonNull(value), Objects.requireNonNull(onChange));
        node.focusable = true;
        class ApiImpl extends NodeApi<InputApi> implements InputApi {
        }
        return addNode(node, new ApiImpl());
    }

    public static VisualStyleApi select(String value, List<String> options, Consumer<String> onChange) {
        SelectNode node = new SelectNode(value, options, onChange);
        node.focusable = true;
        class ApiImpl extends NodeApi<VisualStyleApi> implements VisualStyleApi {
        }
        return addNode(node, new ApiImpl());
    }

    public static float viewportWidth() {
        return JReactContext.viewportWidth();
    }

    public static float viewportHeight() {
        return JReactContext.viewportHeight();
    }

    private static class DivApi extends NodeApi<VisualStyleWithChildrenApi> implements VisualStyleWithChildrenApi {
    }

    private static class SpanApi extends NodeApi<VisualStyleWithChildrenApi> implements VisualStyleWithChildrenApi {
    }

    private static class TextApi extends NodeApi<VisualStyleApi> implements VisualStyleApi {
    }
}
