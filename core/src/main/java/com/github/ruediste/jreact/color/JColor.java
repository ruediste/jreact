
package com.github.ruediste.jreact.color;

import java.awt.Color;

public interface JColor {
    public static final JColor BLACK = rgb(0, 0, 0);
    public static final JColor WHITE = rgb(255, 255, 255);

    Color toColor();

    OkLch toOkLch();

    public static Rgb rgb(int r, int g, int b) {
        return new Rgb(r, g, b);
    }

    public static OkLch oklch(double l, double c, double h) {
        return new OkLch(l, c, h);
    }
}
