package com.github.ruediste.jreact.utility;

import java.util.function.Consumer;

import com.github.ruediste.jreact.CoreElements;
import com.github.ruediste.jreact.style.ApiHelperProvider;
import com.github.ruediste.jreact.style.StyleProperties;
import com.github.ruediste.jreact.style.Styles.StyleApiHelper;

public class ThemeApis {
    private ThemeApis() {
    }

    public interface FontApi<Self, Helper extends CoreElements.NodeAccessApiHelper<Self>>
            extends ApiHelperProvider<Helper> {
        Self fontSans();

        Self fontSerif();

        Self fontMono();
    }

    public interface ColorApiHelper<ColorApi> {
        ColorApi applyColor(Consumer<OkLch> consumer);
    }

    public interface ElementColorApi<Self, ColorApi, Helper extends ColorApiHelper<ColorApi> & StyleApiHelper<Self>>
            extends ApiHelperProvider<Helper> {
        default ColorApi backgroundColor() {
            return _helper().applyColor(color -> _helper().putStyle(StyleProperties.BACKGROUND, color.toColor()));
        }

        default ColorApi color() {
            return _helper().applyColor(color -> _helper().putStyle(StyleProperties.COLOR, color.toColor()));
        }
    }
}
