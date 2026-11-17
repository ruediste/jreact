package com.github.ruediste.jreact.utility;

import java.awt.Color;

/**
 * A color in the OKLCH color space, a perceptually uniform cylindrical
 * transformation of OKLab.
 * <p>
 * The valid and useful ranges are:
 * <ul>
 * <li>{@code l} (lightness): {@code [0, 1]}, where 0 is black and 1 is white.
 * {@link #fromColor} always returns a value in this range.</li>
 * <li>{@code c} (chroma): {@code c >= 0}, where 0 is gray. Typical values are
 * {@code 0-0.02} for neutrals, {@code 0.02-0.08} for muted colors,
 * {@code 0.08-0.18} for accents and {@code 0.18-0.32} for vivid colors. Chroma
 * reachable from sRGB is at most about {@code 0.32} (depending on hue and
 * lightness); larger values are out of the sRGB gamut and are clamped by
 * {@link #toColor()}.</li>
 * <li>{@code h} (hue): {@code [0, 360)} degrees. Values outside this range are
 * accepted and interpreted modulo 360.</li>
 * <li>{@code alpha}: {@code [0, 1]}, where 0 is fully transparent and 1 is
 * fully opaque. Values outside this range are clamped by
 * {@link #toColor()}.</li>
 * </ul>
 * No constructor or factory validates its arguments; out-of-range values are
 * only clamped when converting to sRGB.
 *
 * @param l     lightness in {@code [0, 1]}
 * @param c     chroma, {@code >= 0}, where 0 is gray; typical values are
 *              {@code 0.02-0.32}, with about {@code 0.32} being the sRGB
 *              maximum
 * @param h     hue angle in degrees, in {@code [0, 360)}
 * @param alpha opacity in {@code [0, 1]}
 */
public record OkLch(double l, double c, double h, double alpha) {

    /**
     * Creates a fully opaque color, equivalent to
     * {@code new OkLch(l, c, h, 1.0)}.
     *
     * @param l lightness in {@code [0, 1]}
     * @param c chroma, {@code >= 0}, where 0 is gray; typical values are
     *          {@code 0.02-0.32}, with about {@code 0.32} being the sRGB maximum
     * @param h hue angle in degrees, in {@code [0, 360)}
     */
    public OkLch(double l, double c, double h) {
        this(l, c, h, 1.0);
    }

    /**
     * Converts the given sRGB color to OKLCH.
     *
     * @param color the sRGB color to convert
     * @return the corresponding OKLCH color, with {@code l} in {@code [0, 1]},
     *         {@code c >= 0}, {@code h} in {@code [0, 360)} and {@code alpha} in
     *         {@code [0, 1]}
     */
    public static OkLch fromColor(Color color) {
        double red = toLinearChannel(color.getRed() / 255.0);
        double green = toLinearChannel(color.getGreen() / 255.0);
        double blue = toLinearChannel(color.getBlue() / 255.0);

        double l = 0.4122214708 * red + 0.5363325363 * green + 0.0514459929 * blue;
        double m = 0.2119034982 * red + 0.6806995451 * green + 0.1073969566 * blue;
        double s = 0.0883024619 * red + 0.2817188376 * green + 0.6299787005 * blue;

        double l_ = Math.cbrt(l);
        double m_ = Math.cbrt(m);
        double s_ = Math.cbrt(s);

        double lightness = 0.2104542553 * l_ + 0.7936177850 * m_ - 0.0040720468 * s_;
        double a = 1.9779984951 * l_ - 2.4285922050 * m_ + 0.4505937099 * s_;
        double b = 0.0259040371 * l_ + 0.7827717662 * m_ - 0.8086757660 * s_;

        double chroma = Math.hypot(a, b);
        double hue = Math.toDegrees(Math.atan2(b, a));
        if (hue < 0) {
            hue += 360;
        }

        return new OkLch(lightness, chroma, hue, color.getAlpha() / 255.0);
    }

    /**
     * Converts this OKLCH color to sRGB. Channels outside the sRGB gamut are
     * clamped to the displayable range.
     *
     * @return the corresponding sRGB color
     */
    public Color toColor() {
        double hRad = Math.toRadians(h);
        double a = c * Math.cos(hRad);
        double b = c * Math.sin(hRad);

        double l_ = l + 0.3963377774 * a + 0.2158037573 * b;
        double m_ = l - 0.1055613458 * a - 0.0638541728 * b;
        double s_ = l - 0.0894841775 * a - 1.2914855480 * b;

        double l3 = l_ * l_ * l_;
        double m3 = m_ * m_ * m_;
        double s3 = s_ * s_ * s_;

        double red = 4.0767416621 * l3 - 3.3077115913 * m3 + 0.2309699292 * s3;
        double green = -1.2684380046 * l3 + 2.6097574011 * m3 - 0.3413193965 * s3;
        double blue = -0.0041960863 * l3 - 0.7034186147 * m3 + 1.7076147010 * s3;

        return new Color(
                toSrgbChannel(red),
                toSrgbChannel(green),
                toSrgbChannel(blue),
                clamp(alpha));
    }

    private static double toLinearChannel(double encoded) {
        return encoded <= 0.04045
                ? encoded / 12.92
                : Math.pow((encoded + 0.055) / 1.055, 2.4);
    }

    private static float toSrgbChannel(double linear) {
        double encoded = linear <= 0.0031308
                ? 12.92 * linear
                : 1.055 * Math.pow(linear, 1.0 / 2.4) - 0.055;
        return clamp(encoded);
    }

    private static float clamp(double value) {
        return (float) Math.max(0.0, Math.min(1.0, value));
    }
}
