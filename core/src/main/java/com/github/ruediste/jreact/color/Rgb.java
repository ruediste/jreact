package com.github.ruediste.jreact.color;

public final class Rgb implements JColor {

    public final int r;
    public final int g;
    public final int b;
    public final int a;

    public Rgb(int r, int g, int b, int a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    public Rgb(int r, int g, int b) {
        this(r, g, b, 255);
    }

    public Rgb(String hex) {
        this(
                Integer.valueOf(hex.substring(1, 3), 16),
                Integer.valueOf(hex.substring(3, 5), 16),
                Integer.valueOf(hex.substring(5, 7), 16),
                hex.length() == 9 ? Integer.valueOf(hex.substring(7, 9), 16) : 255);
    }

    @Override
    public java.awt.Color toColor() {
        return new java.awt.Color(r, g, b, a);
    }

    @Override
    public OkLch toOkLch() {
        return OkLch.fromRgb(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Rgb other)) {
            return false;
        }
        return r == other.r && g == other.g && b == other.b && a == other.a;
    }

    @Override
    public int hashCode() {
        int result = Integer.hashCode(r);
        result = 31 * result + Integer.hashCode(g);
        result = 31 * result + Integer.hashCode(b);
        result = 31 * result + Integer.hashCode(a);
        return result;
    }

    @Override
    public String toString() {
        String rgb = String.format("#%02x%02x%02x", r, g, b);
        return a == 255 ? rgb : rgb + String.format("%02x", a);
    }
}
