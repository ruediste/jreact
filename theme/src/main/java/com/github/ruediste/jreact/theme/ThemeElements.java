package com.github.ruediste.jreact.theme;

import static com.github.ruediste.jreact.utility.ColorPalette.COLOR_PALETTE;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import javax.swing.Icon;

import com.github.ruediste.jreact.CoreElementApis.ChildrenApi;
import com.github.ruediste.jreact.CoreElementApis.ElementStyleApiBase;
import com.github.ruediste.jreact.JReactContext;
import com.github.ruediste.jreact.color.JColor;
import com.github.ruediste.jreact.elements.DivElement;
import com.github.ruediste.jreact.elements.ElementNode;
import com.github.ruediste.jreact.elements.IconElement;
import com.github.ruediste.jreact.elements.InputElement;
import com.github.ruediste.jreact.elements.SelectElement;
import com.github.ruediste.jreact.elements.SpanElement;
import com.github.ruediste.jreact.elements.TextElement;
import com.github.ruediste.jreact.theme.ThemeElementApis.CombinedColorTokens;
import com.github.ruediste.jreact.theme.ThemeElementApis.CombinedColorTokensHelper;
import com.github.ruediste.jreact.theme.ThemeElementApis.ElementThemeApiHelper;
import com.github.ruediste.jreact.utility.UtilityElements;

public class ThemeElements {
    public UtilityElements.UtilityTokenValues utilityValues;
    public ThemeTokenValues themeValues;

    public ThemeElements(UtilityElements.UtilityTokenValues utilityValues, ThemeTokenValues themeValues) {
        this.utilityValues = utilityValues;
        this.themeValues = themeValues;
    }

    public static interface ElementApi
            extends ThemeElementApis.ElementThemeApiBase<ElementApi, ElementApiHelper<ElementApi>> {
    }

    public static interface ElementWithChildrenApi
            extends
            ThemeElementApis.ElementThemeApiBase<ElementWithChildrenApi, ElementApiHelper<ElementWithChildrenApi>>,
            ChildrenApi<ElementWithChildrenApi, ElementApiHelper<ElementWithChildrenApi>> {
    }

    public static interface InputApi
            extends ThemeElementApis.ElementThemeApiBase<InputApi, ElementApiHelper<InputApi>> {
    }

    public interface ElementApiHelper<Api> extends ElementThemeApiHelper<Api> {
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private <T> T api(ElementNode node, ElementApiImplBase<? extends T> api) {
        var helper = new ElementApiHelperImpl<T>(node, utilityValues, themeValues);
        api.helper = (ElementApiHelperImpl) helper;
        helper.api = (T) api;
        return (T) api;
    }

    private <T> T addElement(ElementNode element, ElementApiImplBase<? extends T> api) {
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

    static class ElementApiImpl extends ElementApiImplBase<ElementApi> implements ElementApi {
    }

    static class ElementWithChildrenApiImpl extends ElementApiImplBase<ElementWithChildrenApi>
            implements ElementWithChildrenApi {
    }

    static class InputApiImpl extends ElementApiImplBase<InputApi> implements InputApi {
    }

    public static class ThemeTokenValues {
        public ColorTokens.Values colorThemeValues = new ColorTokens.Values();

        {
            colorThemeValues.initialize(COLOR_PALETTE.sky100(), COLOR_PALETTE.sky900(), COLOR_PALETTE.violet800(),
                    COLOR_PALETTE.orange500());
        }

    }

    public static class ElementApiHelperImpl<Api> extends UtilityElements.ElementApiHelperImpl<Api>
            implements ElementApiHelper<Api> {
        private ThemeTokenValues themeValues;

        public ElementApiHelperImpl(ElementNode node, UtilityElements.UtilityTokenValues utilityValues,
                ThemeTokenValues themeValues) {
            super(node, utilityValues);
            this.themeValues = themeValues;
        }

        @Override
        public CombinedColorTokens<Api, CombinedColorTokensHelper<Api>> color(Consumer<JColor> apply) {
            return new CombinedColorTokens<Api, CombinedColorTokensHelper<Api>>() {

                CombinedColorTokensHelper<Api> helper = new CombinedColorTokensHelper<Api>() {

                    @Override
                    public Api color(JColor color) {
                        apply.accept(color);
                        return api;
                    }

                    @Override
                    public ColorTokens.Values colorValues() {
                        return themeValues.colorThemeValues;
                    }
                };

                @Override
                public CombinedColorTokensHelper<Api> _helper() {
                    return helper;
                }
            };
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
