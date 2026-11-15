package com.github.ruediste.jreact;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import javax.swing.Icon;

import com.github.ruediste.jreact.nodes.DivNode;
import com.github.ruediste.jreact.nodes.IconNode;
import com.github.ruediste.jreact.nodes.InputNode;
import com.github.ruediste.jreact.nodes.Node;
import com.github.ruediste.jreact.nodes.SelectNode;
import com.github.ruediste.jreact.nodes.SpanNode;
import com.github.ruediste.jreact.nodes.TextNode;
import com.github.ruediste.jreact.style.ApiHelperProvider;
import com.github.ruediste.jreact.style.StyleProperties.Property;
import com.github.ruediste.jreact.style.Styles.LayoutStyleApi;
import com.github.ruediste.jreact.style.Styles.StyleApiHelper;

public final class CoreComponents {

    private CoreComponents() {
    }

    static class NodeApiHelper<Api> implements StyleApiHelper<Api>, ChildrenApiHelper<Api> {

        public Api api;
        public Node node;

        public NodeApiHelper(Node node) {
            this.node = node;

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

    }

    public static interface VisualStyleApiBase<Self> extends LayoutStyleApi<Self, NodeApiHelper<Self>>,
            com.github.ruediste.jreact.style.Styles.VisualStyleApi<Self, NodeApiHelper<Self>> {
    }

    public static interface VisualStyleApi extends VisualStyleApiBase<VisualStyleApi> {
    }

    public static interface VisualStyleWithChildrenApi extends VisualStyleApiBase<VisualStyleWithChildrenApi>,
            ChildrenApi<VisualStyleWithChildrenApi, NodeApiHelper<VisualStyleWithChildrenApi>> {
    }

    static class NodeApi<Api> implements VisualStyleApiBase<Api>, ChildrenApi<Api, NodeApiHelper<Api>> {
        public NodeApiHelper<Api> builder;

        @Override
        public NodeApiHelper<Api> _helper() {
            return builder;
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

    static <T> T api(Node node) {
        var api = new NodeApi<T>();
        return api(node, api);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    static <T> T api(Node node, NodeApi<? extends T> api) {
        var builder = new NodeApiHelper<T>(node);
        api.builder = (NodeApiHelper) builder;
        builder.api = (T) api;
        return (T) api;
    }

    private static <T> T addNode(Node node) {
        JReactContext.addNode(node);
        return api(node);
    }

    private static <T> T addNode(Node node, NodeApi<? extends T> api) {
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
        default InputApi accessibleName(String accessibleName) {
            var helper = _helper();
            helper.node.accessibleName = accessibleName;
            return helper.api;
        }

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
