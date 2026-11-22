package com.github.ruediste.jreact.style;

import java.util.function.Consumer;

import com.github.ruediste.jreact.color.JColor;
import com.github.ruediste.jreact.style.CoreStyleApis.StyleApiHelper;
import com.github.ruediste.jreact.style.StyleProperties.BorderRadius;

public class CoreTokenApis {

    private CoreTokenApis() {
    }

    public interface RadiusTokenApiHelper<TRadiusApi> {
        public TRadiusApi radius(Consumer<BorderRadius> apply);
    }

    public interface RadiusTokenApi<Self, TRadiusApi, Helper extends RadiusTokenApiHelper<TRadiusApi> & StyleApiHelper<Self>>
            extends ApiHelperProvider<Helper> {
        default TRadiusApi borderTopLeftRadius() {
            return _helper().radius(v -> _helper().putStyle(StyleProperties.BORDER_TOP_LEFT_RADIUS, v));
        }

        default TRadiusApi borderTopRightRadius() {
            return _helper().radius(v -> _helper().putStyle(StyleProperties.BORDER_TOP_RIGHT_RADIUS, v));
        }

        default TRadiusApi borderBottomLeftRadius() {
            return _helper().radius(v -> _helper().putStyle(StyleProperties.BORDER_BOTTOM_LEFT_RADIUS, v));
        }

        default TRadiusApi borderBottomRightRadius() {
            return _helper().radius(v -> _helper().putStyle(StyleProperties.BORDER_BOTTOM_RIGHT_RADIUS, v));
        }
    }

    public interface ColorHelper<TResult> {
        TResult color(JColor color);
    }

    public interface ColorTokenApiHelper<TColorApi> {
        public TColorApi color(Consumer<JColor> apply);
    }

    public interface ColorTokenApi<Self, TColorApi, Helper extends ColorTokenApiHelper<TColorApi> & StyleApiHelper<Self>>
            extends ApiHelperProvider<Helper> {
        default TColorApi color() {
            return _helper().color(v -> _helper().putStyle(StyleProperties.COLOR, v));
        }

        default TColorApi backgroundColor() {
            return _helper().color(v -> _helper().putStyle(StyleProperties.BACKGROUND_COLOR, v));
        }
    }
}
