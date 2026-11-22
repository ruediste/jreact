
package com.github.ruediste.jreact.color;

import java.awt.Color;

public interface JColor {
    public static final JColor BLACK = rgb(0, 0, 0);
    public static final JColor WHITE = rgb(255, 255, 255);
    public static final JColor TRANSPARENT = new Rgb(0, 0, 0, 0);

    Color toColor();

    OkLch toOkLch();

    /**
     * Returns this color with the given opacity. The default implementation
     * converts through OKLCH; implementations with native alpha support should
     * override it to preserve their exact channels.
     *
     * @param alpha opacity in {@code [0, 1]}
     */
    default JColor withAlpha(double alpha) {
        OkLch color = toOkLch();
        return new OkLch(color.l, color.c, color.h, alpha);
    }

    public static Rgb rgb(int r, int g, int b) {
        return new Rgb(r, g, b);
    }

    public static OkLch oklch(double l, double c, double h) {
        return new OkLch(l, c, h);
    }
}
