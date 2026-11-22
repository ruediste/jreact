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

    public static class ElementApiImplBase<Api, Helper extends ElementApiHelper<Api>>
            implements ElementStyleApiBase<Api, Helper>,
            ChildrenApi<Api, Helper> {
        public Helper helper;

        @Override
        public Helper _helper() {
            return helper;
        }

    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static <T> T api(ElementNode node, ElementApiImplBase<? extends T, ?> api) {
        var helper = new ElementApiHelperImpl<T>(node);
        ((ElementApiImplBase) api).helper = helper;
        helper.api = (T) api;
        return (T) api;
    }

    public static <T> T addElement(ElementNode element, ElementApiImplBase<? extends T, ?> api) {
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
        return addElement(new IconElement(Objects.requireNonNull(icon)), new ElementApiImpl());
    }

    public interface InputApi extends ElementStyleApiBase<InputApi, ElementApiHelper<InputApi>> {
    }

    public static InputApi input(String value, Consumer<String> onChange) {
        InputElement node = new InputElement(Objects.requireNonNull(value), Objects.requireNonNull(onChange));
        node.focusable = true;
        class ApiImpl extends ElementApiImplBase<InputApi, ElementApiHelper<InputApi>> implements InputApi {
        }
        return addElement(node, new ApiImpl());
    }

    public static ElementStyleApi select(String value, List<String> options, Consumer<String> onChange) {
        SelectElement node = new SelectElement(value, options, onChange);
        node.focusable = true;
        class ApiImpl extends ElementApiImplBase<ElementStyleApi, ElementApiHelper<ElementStyleApi>>
                implements ElementStyleApi {
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

        private void beginStyleRule(Selector selector) {
            StyleRule rule = new StyleRule(selector, new Style());
            node.styleRules.add(rule);
            currentStyleRule = Optional.of(rule);
        }

        @Override
        public Api inline() {
            currentStyleRule = Optional.empty();
            return api;
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
                    beginStyleRule(selector);
                    return ElementApiHelperImpl.this.api;
                }

                @Override
                public Api inline() {
                    return ElementApiHelperImpl.this.inline();
                }

            }
            var helper = new Helper();
            helper.chainedSelectors.add(factory);
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
            beginStyleRule(selector);
            return api;
        }

    }

    public static class ElementWithChildrenApiImpl
            extends ElementApiImplBase<ElementWithChildrenApi, ElementApiHelper<ElementWithChildrenApi>>
            implements ElementWithChildrenApi {
    }

    public static class ElementApiImpl
            extends ElementApiImplBase<ElementStyleApi, ElementApiHelper<ElementStyleApi>>
            implements ElementStyleApi {
    }
}
