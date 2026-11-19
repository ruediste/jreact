package com.github.ruediste.jreact.theme;

/**
 * Color tokens for application navigation sidebars.
 */
public interface SidebarColorTokens<Self> {
    /**
     * Base sidebar surface color.
     */
    Self sidebar();

    /**
     * Text and icon color on the sidebar surface.
     */
    Self sidebarForeground();

    /**
     * Color for active navigation items and sidebar call-to-action elements.
     */
    Self sidebarPrimary();

    /**
     * Text and icon color on top of the {@link #sidebarPrimary()} color.
     */
    Self sidebarPrimaryForeground();

    /**
     * Color for hovered navigation items and open navigation groups.
     */
    Self sidebarAccent();

    /**
     * Text and icon color on top of the {@link #sidebarAccent()} color.
     */
    Self sidebarAccentForeground();

    /**
     * Color for dividers inside the sidebar.
     */
    Self sidebarBorder();

    /**
     * Keyboard focus ring color for controls inside the sidebar.
     */
    Self sidebarRing();
}
