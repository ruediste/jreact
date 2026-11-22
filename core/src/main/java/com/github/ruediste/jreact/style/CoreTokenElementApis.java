package com.github.ruediste.jreact.style;

import java.util.function.Consumer;

import com.antepod.taffy.style.LengthPercentage;
import com.antepod.taffy.style.LengthPercentageAuto;
import com.github.ruediste.jreact.color.JColor;
import com.github.ruediste.jreact.style.CoreStyleApis.StyleApiHelper;
import com.github.ruediste.jreact.style.StyleProperties.BorderRadius;

public class CoreTokenElementApis {

    private CoreTokenElementApis() {
    }

    public interface RadiusTokenApiHelper<TRadiusApi> {
        public TRadiusApi radius(Consumer<BorderRadius> apply);
    }

    public interface RadiusTokenElementApi<Self, TRadiusApi, Helper extends RadiusTokenApiHelper<TRadiusApi> & StyleApiHelper<Self>>
            extends ApiHelperProvider<Helper> {
        default TRadiusApi borderRadius() {
            return _helper().radius(v -> {
                _helper().putStyle(StyleProperties.BORDER_TOP_LEFT_RADIUS, v);
                _helper().putStyle(StyleProperties.BORDER_TOP_RIGHT_RADIUS, v);
                _helper().putStyle(StyleProperties.BORDER_BOTTOM_LEFT_RADIUS, v);
                _helper().putStyle(StyleProperties.BORDER_BOTTOM_RIGHT_RADIUS, v);
            });
        }

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

        default TResult color(JColor color, double alpha) {
            return color(color.withAlpha(alpha));
        }
    }

    public interface ColorTokenElementApiHelper<TColorApi> {
        public TColorApi color(Consumer<JColor> apply);
    }

    public interface ColorTokenElementApi<Self, TColorApi, Helper extends ColorTokenElementApiHelper<TColorApi> & StyleApiHelper<Self>>
            extends ApiHelperProvider<Helper> {
        default TColorApi color() {
            return _helper().color(v -> _helper().putStyle(StyleProperties.COLOR, v));
        }

        default TColorApi backgroundColor() {
            return _helper().color(v -> _helper().putStyle(StyleProperties.BACKGROUND_COLOR, v));
        }

        default TColorApi borderColor() {
            return _helper().color(v -> {
                _helper().putStyle(StyleProperties.BORDER_TOP_COLOR, v);
                _helper().putStyle(StyleProperties.BORDER_RIGHT_COLOR, v);
                _helper().putStyle(StyleProperties.BORDER_BOTTOM_COLOR, v);
                _helper().putStyle(StyleProperties.BORDER_LEFT_COLOR, v);
            });
        }

        default TColorApi borderTopColor() {
            return _helper().color(v -> _helper().putStyle(StyleProperties.BORDER_TOP_COLOR, v));
        }

        default TColorApi borderRightColor() {
            return _helper().color(v -> _helper().putStyle(StyleProperties.BORDER_RIGHT_COLOR, v));
        }

        default TColorApi borderBottomColor() {
            return _helper().color(v -> _helper().putStyle(StyleProperties.BORDER_BOTTOM_COLOR, v));
        }

        default TColorApi borderLeftColor() {
            return _helper().color(v -> _helper().putStyle(StyleProperties.BORDER_LEFT_COLOR, v));
        }

        default TColorApi ringColor() {
            return _helper().color(v -> _helper().putStyle(StyleProperties.RING_COLOR, v));
        }
    }

    public interface SpacingTokenElementApiHelper {
        LengthPercentage spacingToLength(double value);

        LengthPercentageAuto spacingToLengthAuto(double value);

        com.antepod.taffy.style.Dimension spacingToDimension(double value);
    }

    public interface SpacingIntTokenElementApi<Self, Helper extends SpacingTokenElementApiHelper & StyleApiHelper<Self>>
            extends ApiHelperProvider<Helper> {
        default Self marginTop(int value) {
            return _helper().putStyle(StyleProperties.MARGIN_TOP, _helper().spacingToLengthAuto(value));
        }

        default Self marginBottom(int value) {
            return _helper().putStyle(StyleProperties.MARGIN_BOTTOM, _helper().spacingToLengthAuto(value));
        }

        default Self marginLeft(int value) {
            return _helper().putStyle(StyleProperties.MARGIN_LEFT, _helper().spacingToLengthAuto(value));
        }

        default Self marginRight(int value) {
            return _helper().putStyle(StyleProperties.MARGIN_RIGHT, _helper().spacingToLengthAuto(value));
        }

        default Self margin(int value) {
            var length = _helper().spacingToLengthAuto(value);
            _helper().putStyle(StyleProperties.MARGIN_TOP, length);
            _helper().putStyle(StyleProperties.MARGIN_BOTTOM, length);
            _helper().putStyle(StyleProperties.MARGIN_LEFT, length);
            return _helper().putStyle(StyleProperties.MARGIN_RIGHT, length);
        }

        default Self paddingTop(int value) {
            return _helper().putStyle(StyleProperties.PADDING_TOP, _helper().spacingToLength(value));
        }

        default Self paddingBottom(int value) {
            return _helper().putStyle(StyleProperties.PADDING_BOTTOM, _helper().spacingToLength(value));
        }

        default Self paddingLeft(int value) {
            return _helper().putStyle(StyleProperties.PADDING_LEFT, _helper().spacingToLength(value));
        }

        default Self paddingRight(int value) {
            return _helper().putStyle(StyleProperties.PADDING_RIGHT, _helper().spacingToLength(value));
        }

        default Self padding(int value) {
            var length = _helper().spacingToLength(value);
            _helper().putStyle(StyleProperties.PADDING_TOP, length);
            _helper().putStyle(StyleProperties.PADDING_BOTTOM, length);
            _helper().putStyle(StyleProperties.PADDING_LEFT, length);
            return _helper().putStyle(StyleProperties.PADDING_RIGHT, length);
        }
    }

    public interface SpacingFloatTokenElementApi<Self, Helper extends SpacingTokenElementApiHelper & StyleApiHelper<Self>>
            extends ApiHelperProvider<Helper> {
        default Self marginTop(float value) {
            return _helper().putStyle(StyleProperties.MARGIN_TOP, _helper().spacingToLengthAuto(value));
        }

        default Self marginBottom(float value) {
            return _helper().putStyle(StyleProperties.MARGIN_BOTTOM, _helper().spacingToLengthAuto(value));
        }

        default Self marginLeft(float value) {
            return _helper().putStyle(StyleProperties.MARGIN_LEFT, _helper().spacingToLengthAuto(value));
        }

        default Self marginRight(float value) {
            return _helper().putStyle(StyleProperties.MARGIN_RIGHT, _helper().spacingToLengthAuto(value));
        }

        default Self margin(float value) {
            var length = _helper().spacingToLengthAuto(value);
            _helper().putStyle(StyleProperties.MARGIN_TOP, length);
            _helper().putStyle(StyleProperties.MARGIN_BOTTOM, length);
            _helper().putStyle(StyleProperties.MARGIN_LEFT, length);
            return _helper().putStyle(StyleProperties.MARGIN_RIGHT, length);
        }

        default Self paddingTop(float value) {
            return _helper().putStyle(StyleProperties.PADDING_TOP, _helper().spacingToLength(value));
        }

        default Self paddingBottom(float value) {
            return _helper().putStyle(StyleProperties.PADDING_BOTTOM, _helper().spacingToLength(value));
        }

        default Self paddingLeft(float value) {
            return _helper().putStyle(StyleProperties.PADDING_LEFT, _helper().spacingToLength(value));
        }

        default Self paddingRight(float value) {
            return _helper().putStyle(StyleProperties.PADDING_RIGHT, _helper().spacingToLength(value));
        }

        default Self padding(float value) {
            var length = _helper().spacingToLength(value);
            _helper().putStyle(StyleProperties.PADDING_TOP, length);
            _helper().putStyle(StyleProperties.PADDING_BOTTOM, length);
            _helper().putStyle(StyleProperties.PADDING_LEFT, length);
            return _helper().putStyle(StyleProperties.PADDING_RIGHT, length);
        }
    }
}
