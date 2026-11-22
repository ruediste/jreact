package com.github.ruediste.jreact;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

import javax.swing.Icon;

import com.github.ruediste.jreact.CoreElementApis.ChildrenApi;
import com.github.ruediste.jreact.CoreElementApis.ElementApiHelper;
import com.github.ruediste.jreact.CoreElementApis.ElementStyleApiBase;
import com.github.ruediste.jreact.CoreElementApis.XSelectors;
import com.github.ruediste.jreact.elements.DivElement;
import com.github.ruediste.jreact.elements.ElementNode;
import com.github.ruediste.jreact.elements.IconElement;
import com.github.ruediste.jreact.elements.InputElement;
import com.github.ruediste.jreact.elements.SelectElement;
import com.github.ruediste.jreact.elements.SpanElement;
import com.github.ruediste.jreact.elements.TextElement;
import com.github.ruediste.jreact.style.Selector;
import com.github.ruediste.jreact.style.Selectors.SelectorElementApiHelper;
import com.github.ruediste.jreact.style.Style;
import com.github.ruediste.jreact.style.StyleProperties.Property;
import com.github.ruediste.jreact.style.StyleRule;

public final class CoreElements {

    private CoreElements() {
    }

    public static interface ElementStyleApi
            extends ElementStyleApiBase<ElementStyleApi, ElementApiHelper<ElementStyleApi>> {
    }

    public static interface ElementWithChildrenApi
            extends ElementStyleApiBase<ElementWithChildrenApi, ElementApiHelper<ElementWithChildrenApi>>,
            ChildrenApi<ElementWithChildrenApi, ElementApiHelper<ElementWithChildrenApi>> {
    }

    static class ElementApiImplBase<Api>
            implements ElementStyleApiBase<Api, ElementApiHelper<Api>>,
            ChildrenApi<Api, ElementApiHelper<Api>> {
        public ElementApiHelper<Api> helper;

        @Override
        public ElementApiHelper<Api> _helper() {
            return helper;
        }

    }

    public static <T> T api(ElementNode node) {
        var api = new ElementApiImplBase<T>();
        return api(node, api);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static <T> T api(ElementNode node, ElementApiImplBase<? extends T> api) {
        var helper = new ElementApiHelperImpl<T>(node);
        api.helper = (ElementApiHelperImpl) helper;
        helper.api = (T) api;
        return (T) api;
    }

    public static <T> T addElement(ElementNode element) {
        JReactContext.addNode(element);
        return api(element);
    }

    public static <T> T addElement(ElementNode element, ElementApiImplBase<? extends T> api) {
        JReactContext.addNode(element);
        return api(element, api);
    }

    public static ElementWithChildrenApi div() {
        return addElement(new DivElement(), new ElementWithChildrenApiImpl());
    }

    public static ElementWithChildrenApi div(Runnable children) {
        return div().children(children);
    }

    public static ElementWithChildrenApi span() {
        return addElement(new SpanElement(), new ElementWithChildrenApiImpl());
    }

    public static ElementWithChildrenApi span(Runnable children) {
        return span().children(children);
    }

    public static ElementStyleApi text(String text) {
        return addElement(new TextElement(Objects.requireNonNull(text)), new ElementApiImpl());
    }

    public static ElementStyleApi icon(Icon icon) {
        return addElement(new IconElement(Objects.requireNonNull(icon)));
    }

    public interface InputApi extends ElementStyleApiBase<InputApi, ElementApiHelper<InputApi>> {
        default InputApi disabled(boolean disabled) {
            return _helper().apply(node -> node.disabled = disabled);
        }
    }

    public static InputApi input(String value, Consumer<String> onChange) {
        InputElement node = new InputElement(Objects.requireNonNull(value), Objects.requireNonNull(onChange));
        node.focusable = true;
        class ApiImpl extends ElementApiImplBase<InputApi> implements InputApi {
        }
        return addElement(node, new ApiImpl());
    }

    public static ElementStyleApi select(String value, List<String> options, Consumer<String> onChange) {
        SelectElement node = new SelectElement(value, options, onChange);
        node.focusable = true;
        class ApiImpl extends ElementApiImplBase<ElementStyleApi> implements ElementStyleApi {
        }
        return addElement(node, new ApiImpl());
    }

    public static float viewportWidth() {
        return JReactContext.viewportWidth();
    }

    public static float viewportHeight() {
        return JReactContext.viewportHeight();
    }

    public static class ElementApiHelperImpl<Api>
            implements ElementApiHelper<Api> {

        public Api api;
        public ElementNode node;

        private Optional<StyleRule> currentStyleRule = Optional.empty();

        public ElementApiHelperImpl(ElementNode node) {
            this.node = node;

        }

        @Override
        public Api apply(Consumer<ElementNode> updater) {
            updater.accept(node);
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

        @Override
        public <T> Api putStyle(Property<T> property, T value) {
            property.set(currentStyleRule.map(x -> x.style()).orElse(node.inline), value);
            return api;
        }

        @Override
        public XSelectors<Api, ?> selectorChained(Function<Selector, Selector> factory) {
            class Helper implements SelectorElementApiHelper<Api, XSelectors<Api, ?>> {
                public XSelectors<Api, ?> api;
                private List<Function<Selector, Selector>> chainedSelectors = new ArrayList<>();

                @Override
                public XSelectors<Api, ?> selectorChained(Function<Selector, Selector> factory) {
                    chainedSelectors.add(factory);
                    return api;
                }

                @Override
                public Api selectorTerminal(Selector selector) {
                    // Apply all chained selectors to the current selector
                    for (Function<Selector, Selector> f : chainedSelectors) {
                        selector = f.apply(selector);
                    }
                    currentStyleRule = Optional.of(new StyleRule(selector, new Style()));
                    return ElementApiHelperImpl.this.api;
                }

            }
            var helper = new Helper();
            var api = new XSelectors<Api, Helper>() {

                @Override
                public Helper _helper() {
                    return helper;
                }
            };
            helper.api = api;
            return api;
        }

        @Override
        public Api selectorTerminal(Selector selector) {
            currentStyleRule = Optional.of(new StyleRule(selector, new Style()));
            return api;
        }

    }

    public static class ElementWithChildrenApiImpl extends ElementApiImplBase<ElementWithChildrenApi>
            implements ElementWithChildrenApi {
    }

    public static class ElementApiImpl extends ElementApiImplBase<ElementStyleApi> implements ElementStyleApi {
    }
}
