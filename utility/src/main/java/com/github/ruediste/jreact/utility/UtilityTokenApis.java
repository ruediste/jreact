package com.github.ruediste.jreact.utility;

import java.awt.Font;
import java.util.function.Consumer;

import com.antepod.taffy.style.Dimension;
import com.antepod.taffy.style.LengthPercentageAuto;
import com.github.ruediste.jreact.CoreElementApis;
import com.github.ruediste.jreact.style.ApiHelperProvider;
import com.github.ruediste.jreact.style.CoreStyleApis.StyleApiHelper;
import com.github.ruediste.jreact.style.CoreTokenElementApis;
import com.github.ruediste.jreact.style.StyleProperties;
import com.github.ruediste.jreact.style.StyleProperties.BorderStyle;

public interface UtilityTokenApis {

    public class RadiusThemeValues {
        public double sm;
        public double md;
        public double lg;
        public double xl;

        public void initialize(double base) {
            sm = base * 0.5;
            md = base;
            lg = base * 1.5;
            xl = base * 2;
        }
    }

    public interface RadiusTokenApi<Api> {
        Api sm();

        Api md();

        Api lg();

        Api xl();
    }

    public class RadiusTokenApiImpl<Api> implements RadiusTokenApi<Api> {
        private final RadiusThemeValues values;
        private Consumer<Double> apply;
        private Api api;

        public RadiusTokenApiImpl(RadiusThemeValues values, Consumer<Double> apply, Api api) {
            this.values = values;
            this.apply = apply;
            this.api = api;
        }

        @Override
        public Api sm() {
            apply.accept(values.sm);
            return api;
        }

        @Override
        public Api md() {
            apply.accept(values.md);
            return api;
        }

        @Override
        public Api lg() {
            apply.accept(values.lg);
            return api;
        }

        @Override
        public Api xl() {
            apply.accept(values.xl);
            return api;
        }
    }

    public class FontTokenValues {
        private static final double DEFAULT_BASE_FONT_SIZE = 16;

        public String fontSans;
        public String fontSerif;
        public String fontMono;

        public TextSetting textXs;
        public TextSetting textSm;
        public TextSetting textBase;
        public TextSetting textLg;
        public TextSetting textXl;
        public TextSetting text2xl;
        public TextSetting text3xl;
        public TextSetting text4xl;

        public double leadingNone;
        public double leadingTight;
        public double leadingSnug;
        public double leadingNormal;
        public double leadingRelaxed;
        public double leadingLoose;
        public double leading3;
        public double leading4;
        public double leading5;
        public double leading6;
        public double leading7;
        public double leading8;
        public double leading9;
        public double leading10;

        public FontTokenValues() {
            fontSans = Font.SANS_SERIF;
            fontSerif = Font.SERIF;
            fontMono = Font.MONOSPACED;
            initializeTextSettings(DEFAULT_BASE_FONT_SIZE);
            initializeLineHeights(DEFAULT_BASE_FONT_SIZE);
        }

        /**
         * Initializes the text size tokens from a base font size in logical pixels.
         */
        public void initializeTextSettings(double base) {
            textXs = new TextSetting((int) Math.round(base * 0.75), base);
            textSm = new TextSetting((int) Math.round(base * 0.875), base * 1.25);
            textBase = new TextSetting((int) Math.round(base), base * 1.5);
            textLg = new TextSetting((int) Math.round(base * 1.125), base * 1.75);
            textXl = new TextSetting((int) Math.round(base * 1.25), base * 1.75);
            text2xl = new TextSetting((int) Math.round(base * 1.5), base * 2);
            text3xl = new TextSetting((int) Math.round(base * 1.875), base * 2.25);
            text4xl = new TextSetting((int) Math.round(base * 2.25), base * 2.5);
        }

        /**
         * Initializes the line height tokens from a base font size in logical pixels.
         */
        public void initializeLineHeights(double base) {
            leadingNone = base;
            leadingTight = base * 1.25;
            leadingSnug = base * 1.375;
            leadingNormal = base * 1.5;
            leadingRelaxed = base * 1.625;
            leadingLoose = base * 2;
            leading3 = base * 0.75;
            leading4 = base;
            leading5 = base * 1.25;
            leading6 = base * 1.5;
            leading7 = base * 1.75;
            leading8 = base * 2;
            leading9 = base * 2.25;
            leading10 = base * 2.5;
        }
    }

    public record TextSetting(int fontSize, double lineHeight) {
    }

    public interface FontUtilityApiHelper<Api> {
        FontTokenValues getFontThemeValues();
    }

    public interface FontTokenElementApi<Self, Helper extends CoreElementApis.NodeAccessApiHelper<Self> & StyleApiHelper<Self> & FontUtilityApiHelper<Self>>
            extends ApiHelperProvider<Helper> {
        default Self fontSans() {
            return _helper().putStyle(StyleProperties.FONT_FAMILY, _helper().getFontThemeValues().fontSans);
        }

        default Self fontSerif() {
            return _helper().putStyle(StyleProperties.FONT_FAMILY, _helper().getFontThemeValues().fontSerif);
        }

        default Self fontMono() {
            return _helper().putStyle(StyleProperties.FONT_FAMILY, _helper().getFontThemeValues().fontMono);
        }

        /** Extra-small text size token. */
        default Self textXs() {
            TextSetting s = _helper().getFontThemeValues().textXs;
            _helper().putStyle(StyleProperties.FONT_SIZE, s.fontSize);
            return _helper().putStyle(StyleProperties.LINE_HEIGHT, s.lineHeight);
        }

        /** Small text size token. */
        default Self textSm() {
            TextSetting s = _helper().getFontThemeValues().textSm;
            _helper().putStyle(StyleProperties.FONT_SIZE, s.fontSize);
            return _helper().putStyle(StyleProperties.LINE_HEIGHT, s.lineHeight);
        }

        /** Base text size token. */
        default Self textBase() {
            TextSetting s = _helper().getFontThemeValues().textBase;
            _helper().putStyle(StyleProperties.FONT_SIZE, s.fontSize);
            return _helper().putStyle(StyleProperties.LINE_HEIGHT, s.lineHeight);
        }

        /** Large text size token. */
        default Self textLg() {
            TextSetting s = _helper().getFontThemeValues().textLg;
            _helper().putStyle(StyleProperties.FONT_SIZE, s.fontSize);
            return _helper().putStyle(StyleProperties.LINE_HEIGHT, s.lineHeight);
        }

        /** Extra-large text size token. */
        default Self textXl() {
            TextSetting s = _helper().getFontThemeValues().textXl;
            _helper().putStyle(StyleProperties.FONT_SIZE, s.fontSize);
            return _helper().putStyle(StyleProperties.LINE_HEIGHT, s.lineHeight);
        }

        /** 2x-large text size token. */
        default Self text2xl() {
            TextSetting s = _helper().getFontThemeValues().text2xl;
            _helper().putStyle(StyleProperties.FONT_SIZE, s.fontSize);
            return _helper().putStyle(StyleProperties.LINE_HEIGHT, s.lineHeight);
        }

        /** 3x-large text size token. */
        default Self text3xl() {
            TextSetting s = _helper().getFontThemeValues().text3xl;
            _helper().putStyle(StyleProperties.FONT_SIZE, s.fontSize);
            return _helper().putStyle(StyleProperties.LINE_HEIGHT, s.lineHeight);
        }

        /** 4x-large text size token. */
        default Self text4xl() {
            TextSetting s = _helper().getFontThemeValues().text4xl;
            _helper().putStyle(StyleProperties.FONT_SIZE, s.fontSize);
            return _helper().putStyle(StyleProperties.LINE_HEIGHT, s.lineHeight);
        }

        /** Normal font weight token. */
        default Self fontNormal() {
            return _helper().putStyle(StyleProperties.BOLD, false);
        }

        /** Bold font weight token. */
        default Self fontBold() {
            return _helper().putStyle(StyleProperties.BOLD, true);
        }

        /** No line height token. */
        default Self leadingNone() {
            return _helper().putStyle(StyleProperties.LINE_HEIGHT, _helper().getFontThemeValues().leadingNone);
        }

        /** Tight line height token. */
        default Self leadingTight() {
            return _helper().putStyle(StyleProperties.LINE_HEIGHT, _helper().getFontThemeValues().leadingTight);
        }

        /** Snug line height token. */
        default Self leadingSnug() {
            return _helper().putStyle(StyleProperties.LINE_HEIGHT, _helper().getFontThemeValues().leadingSnug);
        }

        /** Normal line height token. */
        default Self leadingNormal() {
            return _helper().putStyle(StyleProperties.LINE_HEIGHT, _helper().getFontThemeValues().leadingNormal);
        }

        /** Relaxed line height token. */
        default Self leadingRelaxed() {
            return _helper().putStyle(StyleProperties.LINE_HEIGHT, _helper().getFontThemeValues().leadingRelaxed);
        }

        /** Loose line height token. */
        default Self leadingLoose() {
            return _helper().putStyle(StyleProperties.LINE_HEIGHT, _helper().getFontThemeValues().leadingLoose);
        }

        /** Line height token for the {@code leading-3} scale. */
        default Self leading3() {
            return _helper().putStyle(StyleProperties.LINE_HEIGHT, _helper().getFontThemeValues().leading3);
        }

        /** Line height token for the {@code leading-4} scale. */
        default Self leading4() {
            return _helper().putStyle(StyleProperties.LINE_HEIGHT, _helper().getFontThemeValues().leading4);
        }

        /** Line height token for the {@code leading-5} scale. */
        default Self leading5() {
            return _helper().putStyle(StyleProperties.LINE_HEIGHT, _helper().getFontThemeValues().leading5);
        }

        /** Line height token for the {@code leading-6} scale. */
        default Self leading6() {
            return _helper().putStyle(StyleProperties.LINE_HEIGHT, _helper().getFontThemeValues().leading6);
        }

        /** Line height token for the {@code leading-7} scale. */
        default Self leading7() {
            return _helper().putStyle(StyleProperties.LINE_HEIGHT, _helper().getFontThemeValues().leading7);
        }

        /** Line height token for the {@code leading-8} scale. */
        default Self leading8() {
            return _helper().putStyle(StyleProperties.LINE_HEIGHT, _helper().getFontThemeValues().leading8);
        }

        /** Line height token for the {@code leading-9} scale. */
        default Self leading9() {
            return _helper().putStyle(StyleProperties.LINE_HEIGHT, _helper().getFontThemeValues().leading9);
        }

        /** Line height token for the {@code leading-10} scale. */
        default Self leading10() {
            return _helper().putStyle(StyleProperties.LINE_HEIGHT, _helper().getFontThemeValues().leading10);
        }
    }

    /**
     * Tailwind {@code gap-*}, {@code gap-x-*}, and {@code gap-y-*} helpers backed
     * by the spacing scale.
     */
    public interface GapTokenElementApi<Self, Helper extends CoreTokenElementApis.SpacingTokenElementApiHelper & StyleApiHelper<Self>>
            extends ApiHelperProvider<Helper> {
        default Self gap(int value) {
            return gap((float) value);
        }

        default Self gap(float value) {
            _helper().putStyle(StyleProperties.ROW_GAP, _helper().spacingToLength(value));
            return _helper().putStyle(StyleProperties.COLUMN_GAP, _helper().spacingToLength(value));
        }

        default Self gapX(int value) {
            return gapX((float) value);
        }

        default Self gapX(float value) {
            return _helper().putStyle(StyleProperties.COLUMN_GAP, _helper().spacingToLength(value));
        }

        default Self gapY(int value) {
            return gapY((float) value);
        }

        default Self gapY(float value) {
            return _helper().putStyle(StyleProperties.ROW_GAP, _helper().spacingToLength(value));
        }
    }

    /**
     * Tailwind {@code size-*}, {@code w-full}, and {@code h-full} helpers.
     */
    public interface SizeTokenElementApi<Self, Helper extends CoreTokenElementApis.SpacingTokenElementApiHelper & StyleApiHelper<Self>>
            extends ApiHelperProvider<Helper> {
        default Self size(int value) {
            return size((float) value);
        }

        default Self size(float value) {
            Dimension dimension = _helper().spacingToDimension(value);
            _helper().putStyle(StyleProperties.WIDTH, dimension);
            return _helper().putStyle(StyleProperties.HEIGHT, dimension);
        }

        default Self size(double value) {
            Dimension dimension = Dimension.length((float) value);
            _helper().putStyle(StyleProperties.WIDTH, dimension);
            return _helper().putStyle(StyleProperties.HEIGHT, dimension);
        }

        default Self wFull() {
            return _helper().putStyle(StyleProperties.WIDTH, Dimension.percent(1f));
        }

        default Self hFull() {
            return _helper().putStyle(StyleProperties.HEIGHT, Dimension.percent(1f));
        }

        default Self sizeFull() {
            _helper().putStyle(StyleProperties.WIDTH, Dimension.percent(1f));
            return _helper().putStyle(StyleProperties.HEIGHT, Dimension.percent(1f));
        }
    }

    /**
     * Tailwind auto-margin helpers.
     */
    public interface AutoMarginTokenElementApi<Self, Helper extends StyleApiHelper<Self>>
            extends ApiHelperProvider<Helper> {
        default Self mAuto() {
            _helper().putStyle(StyleProperties.MARGIN_TOP, LengthPercentageAuto.AUTO);
            _helper().putStyle(StyleProperties.MARGIN_RIGHT, LengthPercentageAuto.AUTO);
            _helper().putStyle(StyleProperties.MARGIN_BOTTOM, LengthPercentageAuto.AUTO);
            return _helper().putStyle(StyleProperties.MARGIN_LEFT, LengthPercentageAuto.AUTO);
        }

        default Self mxAuto() {
            _helper().putStyle(StyleProperties.MARGIN_LEFT, LengthPercentageAuto.AUTO);
            return _helper().putStyle(StyleProperties.MARGIN_RIGHT, LengthPercentageAuto.AUTO);
        }

        default Self myAuto() {
            _helper().putStyle(StyleProperties.MARGIN_TOP, LengthPercentageAuto.AUTO);
            return _helper().putStyle(StyleProperties.MARGIN_BOTTOM, LengthPercentageAuto.AUTO);
        }

        default Self mtAuto() {
            return _helper().putStyle(StyleProperties.MARGIN_TOP, LengthPercentageAuto.AUTO);
        }

        default Self mrAuto() {
            return _helper().putStyle(StyleProperties.MARGIN_RIGHT, LengthPercentageAuto.AUTO);
        }

        default Self mbAuto() {
            return _helper().putStyle(StyleProperties.MARGIN_BOTTOM, LengthPercentageAuto.AUTO);
        }

        default Self mlAuto() {
            return _helper().putStyle(StyleProperties.MARGIN_LEFT, LengthPercentageAuto.AUTO);
        }
    }

    /**
     * Tailwind flex helpers.
     */
    public interface FlexTokenElementApi<Self, Helper extends StyleApiHelper<Self>>
            extends ApiHelperProvider<Helper> {
        default Self flex1() {
            _helper().putStyle(StyleProperties.FLEX_GROW, 1.0);
            _helper().putStyle(StyleProperties.FLEX_SHRINK, 1.0);
            return _helper().putStyle(StyleProperties.FLEX_BASIS, Dimension.percent(0f));
        }

        default Self grow() {
            return _helper().putStyle(StyleProperties.FLEX_GROW, 1.0);
        }

        default Self grow0() {
            return _helper().putStyle(StyleProperties.FLEX_GROW, 0.0);
        }

        default Self shrink() {
            return _helper().putStyle(StyleProperties.FLEX_SHRINK, 1.0);
        }

        default Self shrink0() {
            return _helper().putStyle(StyleProperties.FLEX_SHRINK, 0.0);
        }
    }

    /**
     * Tailwind border-width helpers. Since JReact has no stylesheet preflight, the
     * helpers also set the border style to solid.
     */
    public interface BorderTokenElementApi<Self, Helper extends StyleApiHelper<Self>>
            extends ApiHelperProvider<Helper> {
        default Self border() {
            return border(1.0);
        }

        default Self border0() {
            return border(0.0);
        }

        default Self border2() {
            return border(2.0);
        }

        default Self border4() {
            return border(4.0);
        }

        default Self border8() {
            return border(8.0);
        }

        default Self border(double width) {
            _helper().putStyle(StyleProperties.BORDER_TOP_STYLE, BorderStyle.SOLID);
            _helper().putStyle(StyleProperties.BORDER_RIGHT_STYLE, BorderStyle.SOLID);
            _helper().putStyle(StyleProperties.BORDER_BOTTOM_STYLE, BorderStyle.SOLID);
            _helper().putStyle(StyleProperties.BORDER_LEFT_STYLE, BorderStyle.SOLID);
            _helper().putStyle(StyleProperties.BORDER_TOP_WIDTH, width);
            _helper().putStyle(StyleProperties.BORDER_RIGHT_WIDTH, width);
            _helper().putStyle(StyleProperties.BORDER_BOTTOM_WIDTH, width);
            return _helper().putStyle(StyleProperties.BORDER_LEFT_WIDTH, width);
        }
    }

    /**
     * Tailwind ring helpers.
     */
    public interface RingTokenElementApi<Self, Helper extends StyleApiHelper<Self>>
            extends ApiHelperProvider<Helper> {
        default Self ring() {
            return ringWidth(3);
        }

        default Self ringWidth(int value) {
            return _helper().putStyle(StyleProperties.RING_WIDTH, (double) value);
        }

        default Self ringOffset(int value) {
            return _helper().putStyle(StyleProperties.RING_OFFSET, (double) value);
        }
    }

}
