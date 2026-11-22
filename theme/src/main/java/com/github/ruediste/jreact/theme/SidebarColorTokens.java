package com.github.ruediste.jreact.theme;

import java.util.function.Consumer;

import com.github.ruediste.jreact.color.JColor;

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

    public class Values {
        public JColor sidebar;
        public JColor sidebarForeground;
        public JColor sidebarPrimary;
        public JColor sidebarPrimaryForeground;
        public JColor sidebarAccent;
        public JColor sidebarAccentForeground;
        public JColor sidebarBorder;
        public JColor sidebarRing;
    }

    public class Impl<Api> implements SidebarColorTokens<Api> {

        private Values values;
        private Api api;
        private Consumer<JColor> apply;

        public Impl(Values values, Api api, Consumer<JColor> apply) {
            this.values = values;
            this.api = api;
            this.apply = apply;
        }

        @Override
        public Api sidebar() {
            apply.accept(values.sidebar);
            return api;
        }

        @Override
        public Api sidebarForeground() {
            apply.accept(values.sidebarForeground);
            return api;
        }

        @Override
        public Api sidebarPrimary() {
            apply.accept(values.sidebarPrimary);
            return api;
        }

        @Override
        public Api sidebarPrimaryForeground() {
            apply.accept(values.sidebarPrimaryForeground);
            return api;
        }

        @Override
        public Api sidebarAccent() {
            apply.accept(values.sidebarAccent);
            return api;
        }

        @Override
        public Api sidebarAccentForeground() {
            apply.accept(values.sidebarAccentForeground);
            return api;
        }

        @Override
        public Api sidebarBorder() {
            apply.accept(values.sidebarBorder);
            return api;
        }

        @Override
        public Api sidebarRing() {
            apply.accept(values.sidebarRing);
            return api;
        }
    }
}
