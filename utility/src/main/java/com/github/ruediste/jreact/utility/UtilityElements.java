package com.github.ruediste.jreact.utility;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import javax.swing.Icon;

import com.antepod.taffy.style.LengthPercentage;
import com.antepod.taffy.style.LengthPercentageAuto;
import com.github.ruediste.jreact.CoreElementApis.ChildrenApi;
import com.github.ruediste.jreact.CoreElements;
import com.github.ruediste.jreact.JReactContext;
import com.github.ruediste.jreact.color.JColor;
import com.github.ruediste.jreact.elements.DivElement;
import com.github.ruediste.jreact.elements.ElementNode;
import com.github.ruediste.jreact.elements.IconElement;
import com.github.ruediste.jreact.elements.InputElement;
import com.github.ruediste.jreact.elements.SelectElement;
import com.github.ruediste.jreact.elements.SpanElement;
import com.github.ruediste.jreact.elements.TextElement;
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
            extends UtilityElementApis.UtilityElementApiBase<ElementApi, ElementApiHelper<ElementApi>> {
    }

    public static interface ElementWithChildrenApi
            extends
            UtilityElementApis.UtilityElementApiBase<ElementWithChildrenApi, ElementApiHelper<ElementWithChildrenApi>>,
            ChildrenApi<ElementWithChildrenApi, ElementApiHelper<ElementWithChildrenApi>> {
    }

    public static interface InputApi
            extends UtilityElementApis.UtilityElementApiBase<InputApi, ElementApiHelper<InputApi>> {
    }

    public interface ElementApiHelper<Api> extends UtilityElementApiHelper<Api> {
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private <T> T api(ElementNode node, CoreElements.ElementApiImplBase<? extends T, ?> api) {
        var helper = new ElementApiHelperImpl<T>(node, values);
        ((CoreElements.ElementApiImplBase) api).helper = helper;
        helper.api = (T) api;
        return (T) api;
    }

    private <T> T addElement(ElementNode element, CoreElements.ElementApiImplBase<? extends T, ?> api) {
        JReactContext.addNode(element);
        return api(element, api);
    }

    public ElementWithChildrenApi div() {
        return addElement(new DivElement(), new ElementWithChildrenApiImpl());
    }

    public ElementWithChildrenApi span() {
        return addElement(new SpanElement(), new ElementWithChildrenApiImpl());
    }

    public ElementApi text(String text) {
        return addElement(new TextElement(Objects.requireNonNull(text)), new ElementApiImpl());
    }

    public ElementApi icon(Icon icon) {
        return addElement(new IconElement(Objects.requireNonNull(icon)), new ElementApiImpl());
    }

    public InputApi input(String value, Consumer<String> onChange) {
        InputElement node = new InputElement(Objects.requireNonNull(value), Objects.requireNonNull(onChange));
        node.focusable = true;
        return addElement(node, new InputApiImpl());
    }

    public ElementApi select(String value, List<String> options, Consumer<String> onChange) {
        SelectElement node = new SelectElement(value, options, onChange);
        node.focusable = true;
        return addElement(node, new ElementApiImpl());
    }

    static class ElementApiImpl
            extends CoreElements.ElementApiImplBase<ElementApi, ElementApiHelper<ElementApi>>
            implements ElementApi {
    }

    static class ElementWithChildrenApiImpl
            extends CoreElements.ElementApiImplBase<ElementWithChildrenApi, ElementApiHelper<ElementWithChildrenApi>>
            implements ElementWithChildrenApi {
    }

    static class InputApiImpl
            extends CoreElements.ElementApiImplBase<InputApi, ElementApiHelper<InputApi>>
            implements InputApi {
    }

    private void foo() {
        div().borderTopLeftRadius().sm().color(JColor.rgb(255, 10, 10)).fontSans()
                .marginTop(12.5f).children(() -> {
                });
    }

    public static class UtilityTokenValues {
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

        @Override
        public com.antepod.taffy.style.Dimension spacingToDimension(double value) {
            return com.antepod.taffy.style.Dimension.length((float) (value * values.spacingBase));
        }
    }
}
