package com.github.ruediste.jreact.utility;

import java.awt.Font;
import java.util.function.Consumer;

import com.github.ruediste.jreact.CoreElements;
import com.github.ruediste.jreact.style.ApiHelperProvider;
import com.github.ruediste.jreact.style.CoreStyleApis.StyleApiHelper;
import com.github.ruediste.jreact.style.StyleProperties;

public class ThemeApis {
    private ThemeApis() {
    }

    public class RadiusThemeValues {
        public double sm;
        public double md;
        public double lg;
        public double xl;
    }

    public interface RadiusApi<Api> {
        Api sm();

        Api md();

        Api lg();

        Api xl();
    }

    public class RadiusApiImpl<Api> implements RadiusApi<Api> {
        private final RadiusThemeValues values;
        private Consumer<Double> apply;
        private Api api;

        public RadiusApiImpl(RadiusThemeValues values, Consumer<Double> apply, Api api) {
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

    public class FontThemeValues {
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

        public FontThemeValues() {
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

    public interface FontApiHelper<Api> {
        FontThemeValues getFontThemeValues();
    }

    public interface FontApi<Self, Helper extends CoreElements.NodeAccessApiHelper<Self> & StyleApiHelper<Self> & FontApiHelper<Self>>
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
}
