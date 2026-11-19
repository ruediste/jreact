package com.github.ruediste.jreact.theme;

public interface ColorTokens<Self> {
    /**
     * Primary body text and icon color on the default background.
     */
    Self foreground();

    /**
     * Default page canvas surface color.
     */
    Self background();

    /**
     * Elevated container surface color for cards and dashboard panels.
     */
    Self card();

    /**
     * Text and icon color on top of the {@link #card()} surface.
     */
    Self cardForeground();

    /**
     * Floating overlay surface color for popovers, dropdown menus, selects, and tooltips.
     */
    Self popover();

    /**
     * Text and icon color on top of the {@link #popover()} surface.
     */
    Self popoverForeground();

    /**
     * High-emphasis brand color for primary buttons, active switches, and selected badges.
     */
    Self primary();

    /**
     * Text and icon color on top of the {@link #primary()} color.
     */
    Self primaryForeground();

    /**
     * Lower-emphasis filled action color for secondary buttons and badges.
     */
    Self secondary();

    /**
     * Text and icon color on top of the {@link #secondary()} color.
     */
    Self secondaryForeground();

    /**
     * Subdued surface color for skeletons, disabled states, and table headers.
     */
    Self muted();

    /**
     * De-emphasized text color for helper and placeholder text.
     */
    Self mutedForeground();

    /**
     * Interactive highlight color for hovered and focused elements such as ghost buttons,
     * menu items, and table rows.
     */
    Self accent();

    /**
     * Text and icon color on top of the {@link #accent()} color.
     */
    Self accentForeground();

    /**
     * Error state and destructive action color.
     */
    Self destructive();

    /**
     * Text and icon color on top of the {@link #destructive()} color.
     */
    Self destructiveForeground();

    /**
     * Default border and divider color. This is a color token; border width is separate.
     */
    Self border();

    /**
     * Border color for form controls, and optionally their background color in dark themes.
     */
    Self input();

    /**
     * Keyboard focus ring color. This is a color token; ring width and offset are separate.
     */
    Self ring();
}
