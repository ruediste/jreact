package com.github.ruediste.jreact.theme;

import com.github.ruediste.jreact.color.JColor;
import com.github.ruediste.jreact.color.OkLch;
import com.github.ruediste.jreact.style.ApiHelperProvider;
import com.github.ruediste.jreact.style.CoreTokenElementApis.ColorHelper;

public interface ColorTokens<TResult, Helper extends ColorHelper<TResult> & ColorTokens.ColorValuesHelper>
        extends ApiHelperProvider<Helper>, ChartColorTokens<TResult>, SidebarColorTokens<TResult> {
    /**
     * Primary body text and icon color on the default background.
     */
    default TResult foreground() {
        return _helper().color(_helper().colorValues().foreground);
    }

    /**
     * Default page canvas surface color.
     */
    default TResult background() {
        return _helper().color(_helper().colorValues().background);
    }

    /**
     * Elevated container surface color for cards and dashboard panels.
     */
    default TResult card() {
        return _helper().color(_helper().colorValues().card);
    }

    /**
     * Text and icon color on top of the {@link #card()} surface.
     */
    default TResult cardForeground() {
        return _helper().color(_helper().colorValues().cardForeground);
    }

    /**
     * Floating overlay surface color for popovers, dropdown menus, selects, and
     * tooltips.
     */
    default TResult popover() {
        return _helper().color(_helper().colorValues().popover);
    }

    /**
     * Text and icon color on top of the {@link #popover()} surface.
     */
    default TResult popoverForeground() {
        return _helper().color(_helper().colorValues().popoverForeground);
    }

    /**
     * High-emphasis brand color for primary buttons, active switches, and selected
     * badges.
     */
    default TResult primary() {
        return _helper().color(_helper().colorValues().primary);
    }

    /**
     * Text and icon color on top of the {@link #primary()} color.
     */
    default TResult primaryForeground() {
        return _helper().color(_helper().colorValues().primaryForeground);
    }

    /**
     * Lower-emphasis filled action color for secondary buttons and badges.
     */
    default TResult secondary() {
        return _helper().color(_helper().colorValues().secondary);
    }

    /**
     * Text and icon color on top of the {@link #secondary()} color.
     */
    default TResult secondaryForeground() {
        return _helper().color(_helper().colorValues().secondaryForeground);
    }

    /**
     * Subdued surface color for skeletons, disabled states, and table headers.
     */
    default TResult muted() {
        return _helper().color(_helper().colorValues().muted);
    }

    /**
     * De-emphasized text color for helper and placeholder text.
     */
    default TResult mutedForeground() {
        return _helper().color(_helper().colorValues().mutedForeground);
    }

    /**
     * Interactive highlight color for hovered and focused elements such as ghost
     * buttons,
     * menu items, and table rows.
     */
    default TResult accent() {
        return _helper().color(_helper().colorValues().accent);
    }

    /**
     * Text and icon color on top of the {@link #accent()} color.
     */
    default TResult accentForeground() {
        return _helper().color(_helper().colorValues().accentForeground);
    }

    /**
     * Error state and destructive action color.
     */
    default TResult destructive() {
        return _helper().color(_helper().colorValues().destructive);
    }

    /**
     * Text and icon color on top of the {@link #destructive()} color.
     */
    default TResult destructiveForeground() {
        return _helper().color(_helper().colorValues().destructiveForeground);
    }

    /**
     * Default border and divider color. This is a color token; border width is
     * separate.
     */
    default TResult border() {
        return _helper().color(_helper().colorValues().border);
    }

    /**
     * Border color for form controls, and optionally their background color in dark
     * themes.
     */
    default TResult input() {
        return _helper().color(_helper().colorValues().input);
    }

    /**
     * Keyboard focus ring color. This is a color token; ring width and offset are
     * separate.
     */
    default TResult ring() {
        return _helper().color(_helper().colorValues().ring);
    }

    @Override
    default TResult chart1() {
        return _helper().color(_helper().colorValues().chart1);
    }

    @Override
    default TResult chart2() {
        return _helper().color(_helper().colorValues().chart2);
    }

    @Override
    default TResult chart3() {
        return _helper().color(_helper().colorValues().chart3);
    }

    @Override
    default TResult chart4() {
        return _helper().color(_helper().colorValues().chart4);
    }

    @Override
    default TResult chart5() {
        return _helper().color(_helper().colorValues().chart5);
    }

    @Override
    default TResult sidebar() {
        return _helper().color(_helper().colorValues().sidebar);
    }

    @Override
    default TResult sidebarForeground() {
        return _helper().color(_helper().colorValues().sidebarForeground);
    }

    @Override
    default TResult sidebarPrimary() {
        return _helper().color(_helper().colorValues().sidebarPrimary);
    }

    @Override
    default TResult sidebarPrimaryForeground() {
        return _helper().color(_helper().colorValues().sidebarPrimaryForeground);
    }

    @Override
    default TResult sidebarAccent() {
        return _helper().color(_helper().colorValues().sidebarAccent);
    }

    @Override
    default TResult sidebarAccentForeground() {
        return _helper().color(_helper().colorValues().sidebarAccentForeground);
    }

    @Override
    default TResult sidebarBorder() {
        return _helper().color(_helper().colorValues().sidebarBorder);
    }

    @Override
    default TResult sidebarRing() {
        return _helper().color(_helper().colorValues().sidebarRing);
    }

    /**
     * Alpha variants of the semantic tokens, following the Tailwind
     * {@code color/opacity} syntax.
     */
    default TResult foreground(double alpha) {
        return _helper().color(_helper().colorValues().foreground, alpha);
    }

    default TResult background(double alpha) {
        return _helper().color(_helper().colorValues().background, alpha);
    }

    default TResult card(double alpha) {
        return _helper().color(_helper().colorValues().card, alpha);
    }

    default TResult cardForeground(double alpha) {
        return _helper().color(_helper().colorValues().cardForeground, alpha);
    }

    default TResult popover(double alpha) {
        return _helper().color(_helper().colorValues().popover, alpha);
    }

    default TResult popoverForeground(double alpha) {
        return _helper().color(_helper().colorValues().popoverForeground, alpha);
    }

    default TResult primary(double alpha) {
        return _helper().color(_helper().colorValues().primary, alpha);
    }

    default TResult primaryForeground(double alpha) {
        return _helper().color(_helper().colorValues().primaryForeground, alpha);
    }

    default TResult secondary(double alpha) {
        return _helper().color(_helper().colorValues().secondary, alpha);
    }

    default TResult secondaryForeground(double alpha) {
        return _helper().color(_helper().colorValues().secondaryForeground, alpha);
    }

    default TResult muted(double alpha) {
        return _helper().color(_helper().colorValues().muted, alpha);
    }

    default TResult mutedForeground(double alpha) {
        return _helper().color(_helper().colorValues().mutedForeground, alpha);
    }

    default TResult accent(double alpha) {
        return _helper().color(_helper().colorValues().accent, alpha);
    }

    default TResult accentForeground(double alpha) {
        return _helper().color(_helper().colorValues().accentForeground, alpha);
    }

    default TResult destructive(double alpha) {
        return _helper().color(_helper().colorValues().destructive, alpha);
    }

    default TResult destructiveForeground(double alpha) {
        return _helper().color(_helper().colorValues().destructiveForeground, alpha);
    }

    default TResult border(double alpha) {
        return _helper().color(_helper().colorValues().border, alpha);
    }

    default TResult input(double alpha) {
        return _helper().color(_helper().colorValues().input, alpha);
    }

    default TResult ring(double alpha) {
        return _helper().color(_helper().colorValues().ring, alpha);
    }

    public interface ColorValuesHelper {
        Values colorValues();
    }

    /**
     * Resolved color values backing the {@link ColorTokens} accessors.
     */
    public class Values {
        /**
         * Primary body text and icon color on the default background.
         */
        public JColor foreground;

        /**
         * Default page canvas surface color.
         */
        public JColor background;

        /**
         * Elevated container surface color for cards and dashboard panels.
         */
        public JColor card;

        /**
         * Text and icon color on top of the {@link #card} surface.
         */
        public JColor cardForeground;

        /**
         * Floating overlay surface color for popovers, dropdown menus, selects, and
         * tooltips.
         */
        public JColor popover;

        /**
         * Text and icon color on top of the {@link #popover} surface.
         */
        public JColor popoverForeground;

        /**
         * High-emphasis brand color for primary buttons, active switches, and selected
         * badges.
         */
        public JColor primary;

        /**
         * Text and icon color on top of the {@link #primary} color.
         */
        public JColor primaryForeground;

        /**
         * Lower-emphasis filled action color for secondary buttons and badges.
         */
        public JColor secondary;

        /**
         * Text and icon color on top of the {@link #secondary} color.
         */
        public JColor secondaryForeground;

        /**
         * Subdued surface color for skeletons, disabled states, and table headers.
         */
        public JColor muted;

        /**
         * De-emphasized text color for helper and placeholder text.
         */
        public JColor mutedForeground;

        /**
         * Interactive highlight color for hovered and focused elements such as ghost
         * buttons, menu items, and table rows.
         */
        public JColor accent;

        /**
         * Text and icon color on top of the {@link #accent} color.
         */
        public JColor accentForeground;

        /**
         * Error state and destructive action color.
         */
        public JColor destructive;

        /**
         * Text and icon color on top of the {@link #destructive} color.
         */
        public JColor destructiveForeground;

        /**
         * Default border and divider color.
         */
        public JColor border;

        /**
         * Border color for form controls, and optionally their background color in
         * dark themes.
         */
        public JColor input;

        /**
         * Keyboard focus ring color.
         */
        public JColor ring;

        /**
         * First categorical chart color.
         */
        public JColor chart1;

        /**
         * Second categorical chart color.
         */
        public JColor chart2;

        /**
         * Third categorical chart color.
         */
        public JColor chart3;

        /**
         * Fourth categorical chart color.
         */
        public JColor chart4;

        /**
         * Fifth categorical chart color.
         */
        public JColor chart5;

        /**
         * Base sidebar surface color.
         */
        public JColor sidebar;

        /**
         * Text and icon color on the sidebar surface.
         */
        public JColor sidebarForeground;

        /**
         * Color for active sidebar items.
         */
        public JColor sidebarPrimary;

        /**
         * Text and icon color on top of the sidebar primary color.
         */
        public JColor sidebarPrimaryForeground;

        /**
         * Color for hovered sidebar items.
         */
        public JColor sidebarAccent;

        /**
         * Text and icon color on top of the sidebar accent color.
         */
        public JColor sidebarAccentForeground;

        /**
         * Sidebar divider color.
         */
        public JColor sidebarBorder;

        /**
         * Sidebar keyboard focus ring color.
         */
        public JColor sidebarRing;

        private static final double LIGHTNESS_THRESHOLD = 0.65;
        private static final OkLch LIGHT_ON_COLOR = new OkLch(0.985, 0, 0);
        private static final OkLch DARK_ON_COLOR = new OkLch(0.205, 0, 0);

        /**
         * Initializes all color tokens from a small set of key colors. The remaining
         * tokens are derived in the OKLCH color space by adjusting lightness and
         * retaining a subtle neutral tint taken from the key colors.
         * <p>
         * Whether the palette is light or dark is determined by the lightness of
         * {@code background}.
         *
         * @param background  the default page background color
         * @param foreground  the default text and icon color on {@code background}
         * @param primary     the high-emphasis brand and action color
         * @param destructive the error and destructive action color
         */
        public void initialize(JColor background, JColor foreground, JColor primary,
                JColor destructive) {
            OkLch bg = toOklch(background);
            OkLch fg = toOklch(foreground);
            OkLch primaryLch = toOklch(primary);
            OkLch destructiveLch = toOklch(destructive);

            boolean dark = bg.l < 0.5;
            double direction = dark ? 1 : -1;
            double neutralHue = bg.c >= fg.c ? bg.h : fg.h;
            double neutralChroma = Math.min(Math.max(bg.c, fg.c), 0.012);

            this.background = background;
            this.foreground = foreground;

            this.card = elevated(bg, dark ? 0.06 : 0.02);
            this.popover = this.card;
            this.cardForeground = foreground;
            this.popoverForeground = foreground;

            this.primary = primary;
            this.primaryForeground = onColor(primaryLch);

            this.secondary = surface(bg, neutralChroma, neutralHue, direction * 0.04);
            this.secondaryForeground = foreground;
            this.muted = surface(bg, neutralChroma, neutralHue, direction * 0.03);
            this.mutedForeground = new OkLch(dark ? 0.70 : 0.55, neutralChroma, neutralHue);
            this.accent = surface(bg, neutralChroma, neutralHue, direction * 0.05);
            this.accentForeground = foreground;

            this.destructive = destructive;
            this.destructiveForeground = onColor(destructiveLch);

            this.border = surface(bg, neutralChroma, neutralHue, direction * 0.09);
            this.input = this.border;
            this.ring = primary;

            this.chart1 = primary;
            this.chart2 = hueShift(primaryLch, 72);
            this.chart3 = hueShift(primaryLch, 144);
            this.chart4 = hueShift(destructiveLch, 72);
            this.chart5 = hueShift(primaryLch, 216);

            this.sidebar = this.background;
            this.sidebarForeground = this.foreground;
            this.sidebarPrimary = this.primary;
            this.sidebarPrimaryForeground = this.primaryForeground;
            this.sidebarAccent = this.accent;
            this.sidebarAccentForeground = this.accentForeground;
            this.sidebarBorder = this.border;
            this.sidebarRing = this.ring;
        }

        private static OkLch hueShift(OkLch color, double degrees) {
            return new OkLch(color.l, color.c, (color.h + degrees) % 360, color.alpha);
        }

        private static OkLch toOklch(JColor color) {
            if (color instanceof OkLch oklch) {
                return oklch;
            }
            return OkLch.fromColor(color.toColor());
        }

        private static OkLch onColor(OkLch color) {
            return color.l < LIGHTNESS_THRESHOLD ? LIGHT_ON_COLOR : DARK_ON_COLOR;
        }

        private static OkLch elevated(OkLch background, double delta) {
            return new OkLch(clamp01(background.l + delta), background.c, background.h,
                    background.alpha);
        }

        private static OkLch surface(OkLch background, double chroma, double hue, double delta) {
            return new OkLch(clamp01(background.l + delta), chroma, hue, background.alpha);
        }

        private static double clamp01(double value) {
            return Math.max(0.0, Math.min(1.0, value));
        }
    }

}
