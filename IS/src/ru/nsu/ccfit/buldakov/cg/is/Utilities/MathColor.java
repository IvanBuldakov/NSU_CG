package ru.nsu.ccfit.buldakov.cg.is.Utilities;

import java.awt.*;

public final class MathColor {

    private int r;
    private int g;
    private int b;

    public MathColor(int c) {
        Color color = new Color(c);
        this.r = color.getRed();
        this.g = color.getGreen();
        this.b = color.getBlue();
    }

    public MathColor(int r, int g, int b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public MathColor(Color color) {
        this.r = color.getRed();
        this.g = color.getGreen();
        this.b = color.getBlue();
    }

    public MathColor add(MathColor other) {
        return new MathColor(this.r + other.r, this.g + other.g, this.b + other.b);
    }

    public MathColor sub(MathColor other) {
        return new MathColor(this.r - other.r, this.g - other.g, this.b - other.b);
    }

    public MathColor mul(double d) {
        return new MathColor((int) Math.round(d * r),
                             (int) Math.round(d * g),
                             (int) Math.round(d * b));
    }

    public int toRGB() {
        return toColor().getRGB();
    }

    public Color toColor() {
        return new Color(clamp(r), clamp(g), clamp(b));
    }

    public int clamp(int c) {
        return Math.max(0, Math.min(255, c));
    }

    public MathColor findClosestPaletteColor(MathColor[] palette) {
        MathColor closest = palette[0];
        for (MathColor n : palette) {
            if (distance(n) < distance(closest)) {
                closest = n;
            }
        }
        return closest;
    }

    public int distance(MathColor other) {
        int Rdiff = other.r - this.r;
        int Gdiff = other.g - this.g;
        int Bdiff = other.b - this.b;
        return Rdiff * Rdiff + Gdiff * Gdiff + Bdiff * Bdiff;
    }

}
