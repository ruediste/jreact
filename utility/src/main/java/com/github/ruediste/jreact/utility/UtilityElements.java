package com.github.ruediste.jreact.utility;

import java.util.function.Consumer;

import com.github.ruediste.jreact.CoreElementApis.ChildrenApi;
import com.github.ruediste.jreact.CoreElementApis.ElementStyleApiBase;
import com.github.ruediste.jreact.CoreElements;
import com.github.ruediste.jreact.JReactContext;
import com.github.ruediste.jreact.color.JColor;
import com.github.ruediste.jreact.elements.DivElement;
import com.github.ruediste.jreact.elements.ElementNode;
import com.github.ruediste.jreact.style.StyleProperties.BorderRadius;
import com.github.ruediste.jreact.utility.UtilityElementApis.ElementUtilityApiHelper;
import com.github.ruediste.jreact.utility.UtilityTokenApis.FontThemeValues;
import com.github.ruediste.jreact.utility.UtilityTokenApis.RadiusApi;
import com.github.ruediste.jreact.utility.UtilityTokenApis.RadiusThemeValues;

public class UtilityElements {
    public FontThemeValues fontThemeValues;
    public RadiusThemeValues radiusThemeValues;

    public UtilityElements(FontThemeValues fontThemeValues,
            RadiusThemeValues radiusThemeValues) {
        this.fontThemeValues = fontThemeValues;
        this.radiusThemeValues = radiusThemeValues;
    }

    public static interface ElementApi
            extends UtilityElementApis.ElementUtilityApiBase<ElementApi, ElementUtilityApiHelper<ElementApi>> {
    }

    public static interface ElementWithChildrenApi
            extends
            UtilityElementApis.ElementUtilityApiBase<ElementWithChildrenApi, ElementApiHelper<ElementWithChildrenApi>>,
            ChildrenApi<ElementWithChildrenApi, ElementApiHelper<ElementWithChildrenApi>> {
    }

    public interface ElementApiHelper<Api> extends ElementUtilityApiHelper<Api> {
    }

    private <T> T api(ElementNode node) {
        var api = new ElementApiImplBase<T>();
        return api(node, api);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private <T> T api(ElementNode node, ElementApiImplBase<? extends T> api) {
        var helper = new ElementApiHelperImpl<T>(node, fontThemeValues, radiusThemeValues);
        api.helper = (ElementApiHelperImpl) helper;
        helper.api = (T) api;
        return (T) api;
    }

    private <T> T addElement(ElementNode element) {
        JReactContext.addNode(element);
        return api(element);
    }

    public ElementWithChildrenApi div() {
        return addElement(new DivElement());
    }

    private void foo() {
        div().borderTopLeftRadius().sm().color(JColor.rgb(255, 10, 10)).fontSans().children(() -> {
        });
    }

    public class ElementApiHelperImpl<Api> extends CoreElements.ElementApiHelperImpl<Api>
            implements ElementApiHelper<Api> {

        private FontThemeValues fontThemeValues;
        private RadiusThemeValues radiusThemeValues;

        public ElementApiHelperImpl(ElementNode node, FontThemeValues fontThemeValues,
                RadiusThemeValues radiusThemeValues) {
            super(node);
            this.fontThemeValues = fontThemeValues;
            this.radiusThemeValues = radiusThemeValues;
        }

        @Override
        public FontThemeValues getFontThemeValues() {
            return fontThemeValues;
        }

        @Override
        public RadiusApi<Api> radius(Consumer<BorderRadius> apply) {
            return new UtilityTokenApis.RadiusApiImpl<Api>(radiusThemeValues, v -> apply.accept(BorderRadius.of(v)),
                    api);
        }
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
}
