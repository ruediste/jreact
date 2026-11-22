package com.github.ruediste.jreact.utility;

import java.util.function.Consumer;

import com.antepod.taffy.style.LengthPercentage;
import com.antepod.taffy.style.LengthPercentageAuto;
import com.github.ruediste.jreact.CoreElementApis.ChildrenApi;
import com.github.ruediste.jreact.CoreElementApis.ElementStyleApiBase;
import com.github.ruediste.jreact.CoreElements;
import com.github.ruediste.jreact.JReactContext;
import com.github.ruediste.jreact.color.JColor;
import com.github.ruediste.jreact.elements.DivElement;
import com.github.ruediste.jreact.elements.ElementNode;
import com.github.ruediste.jreact.style.StyleProperties.BorderRadius;
import com.github.ruediste.jreact.utility.UtilityElementApis.UtilityElementApiHelper;
import com.github.ruediste.jreact.utility.UtilityTokenApis.FontTokenValues;
import com.github.ruediste.jreact.utility.UtilityTokenApis.RadiusThemeValues;
import com.github.ruediste.jreact.utility.UtilityTokenApis.RadiusTokenApi;

public class UtilityElements {
    UtilityTokenValues values;

    public UtilityElements(UtilityTokenValues values) {
        this.values = values;
    }

    public static interface ElementApi
            extends UtilityElementApis.UtilityElementApiBase<ElementApi, UtilityElementApiHelper<ElementApi>> {
    }

    public static interface ElementWithChildrenApi
            extends
            UtilityElementApis.UtilityElementApiBase<ElementWithChildrenApi, ElementApiHelper<ElementWithChildrenApi>>,
            ChildrenApi<ElementWithChildrenApi, ElementApiHelper<ElementWithChildrenApi>> {
    }

    public interface ElementApiHelper<Api> extends UtilityElementApiHelper<Api> {
    }

    private <T> T api(ElementNode node) {
        var api = new ElementApiImplBase<T>();
        return api(node, api);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private <T> T api(ElementNode node, ElementApiImplBase<? extends T> api) {
        var helper = new ElementApiHelperImpl<T>(node, values);
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
        div().borderTopLeftRadius().sm().color(JColor.rgb(255, 10, 10)).fontSans()
                .marginTop(12.5f).children(() -> {
                });
    }

    public class UtilityTokenValues {
        public double spacingBase = 8;
        public FontTokenValues fontThemeValues = new FontTokenValues();
        public RadiusThemeValues radiusThemeValues = new RadiusThemeValues();

        {
            fontThemeValues.initializeLineHeights(12);
            fontThemeValues.initializeTextSettings(12);
            radiusThemeValues.initialize(5);
        }
    }

    public static class ElementApiHelperImpl<Api> extends CoreElements.ElementApiHelperImpl<Api>
            implements ElementApiHelper<Api> {

        private UtilityTokenValues values;

        public ElementApiHelperImpl(ElementNode node, UtilityTokenValues values) {
            super(node);
            this.values = values;
        }

        @Override
        public FontTokenValues getFontThemeValues() {
            return values.fontThemeValues;
        }

        @Override
        public RadiusTokenApi<Api> radius(Consumer<BorderRadius> apply) {
            return new UtilityTokenApis.RadiusTokenApiImpl<Api>(values.radiusThemeValues,
                    v -> apply.accept(BorderRadius.of(v)),
                    api);
        }

        @Override
        public LengthPercentage spacingToLength(double value) {
            return LengthPercentage.length((float) (value * values.spacingBase));
        }

        @Override
        public LengthPercentageAuto spacingToLengthAuto(double value) {
            return LengthPercentageAuto.length((float) (value * values.spacingBase));
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
