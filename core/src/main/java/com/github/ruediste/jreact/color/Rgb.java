package com.github.ruediste.jreact.color;

public record Rgb(int r, int g, int b, int a) implements JColor {

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
}
