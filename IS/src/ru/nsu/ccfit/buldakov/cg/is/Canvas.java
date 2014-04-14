package ru.nsu.ccfit.buldakov.cg.is;

import ru.nsu.ccfit.buldakov.cg.is.Functions.Function;
import ru.nsu.ccfit.buldakov.cg.is.Functions.LegendFunction;
import ru.nsu.ccfit.buldakov.cg.is.Utilities.Dither;
import ru.nsu.ccfit.buldakov.cg.is.Utilities.Interpolator;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Canvas {

    private Settings settings = Settings.getInstance();

    private BufferedImage canvas;
    private Function      function;
    private ArrayList<Double> calculatedValues = new ArrayList<>();
    private ArrayList<Double> userValues       = new ArrayList<>();
    private ArrayList<Double> values           = new ArrayList<>();

    public Canvas(Function function) {
        this.function = function;
    }

    public int getWidth() {
        return (null == canvas) ? 0 : canvas.getWidth();
    }

    public int getHeight() {
        return (null == canvas) ? 0 : canvas.getHeight();
    }

    public Function getFunction() {
        return function;
    }

    public void resize(int width, int height) {
        canvas = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    }

    public void paint(Graphics g, int x, int y) {
        double dx = 1.0 * getWidth() / settings.getWidth();
        double dy = 1.0 * getHeight() / settings.getHeight();
        double dxPix = 1.0 / dx;
        double dyPix = 1.0 / dy;
        Color[] colors = settings.getColors();
        calculateNewValues();
        if (!(function instanceof LegendFunction)) {
            settings.setValues(calculatedValues);
        }
        for (int yPix = 0; yPix < getHeight(); ++yPix) {
            for (int xPix = 0; xPix < getWidth(); ++xPix) {
                int color;
                double value = function.valueAt(settings.getA() + xPix * dxPix, settings.getC() + yPix * dyPix);
                int idx = getColorIndex(value);
                if (settings.isInterpolationEnabled() || settings.isDitheringEnabled()) {
                    color = Interpolator.interpolate(value, calculatedValues.get(idx), calculatedValues.get(idx + 1), colors[idx], colors[idx + 1]).getRGB();
                } else {
                    color = colors[idx].getRGB();
                }
                canvas.setRGB(xPix, yPix, color);
            }
        }

        if (settings.isDitheringEnabled()) {
            canvas = Dither.FloydSteinbergDithering(canvas, colors);
        }

        g.drawImage(canvas, x, y, null);
        if (!(function instanceof LegendFunction) && settings.needToDisplayIsolines()) {
            drawIsolines(g, x, y);
        }

    }

    private void calculateNewValues() {
        calculatedValues.clear();
        double min = Function.getMin(function, settings.getA(), settings.getB(), settings.getC(), settings.getD(), settings.getK(), settings.getM());
        double max = Function.getMax(function, settings.getA(), settings.getB(), settings.getC(), settings.getD(), settings.getK(), settings.getM());
        double dz = (max - min) / (settings.getN() + 1);
        for (int i = 0; i < settings.getN() + 2; ++i) {
            calculatedValues.add(min + i * dz);
        }
    }

    private int getColorIndex(double value) {
        int idx = 0;
        if (value >= calculatedValues.get(1)) {
            ++idx;
            while (idx <= settings.getN() && value >= calculatedValues.get(idx)) {
                ++idx;
            }
            --idx;
        }
        return idx;
    }

    private void drawIsolines(Graphics g, int x, int y) {
        values.clear();
        values.addAll(calculatedValues);
        values.addAll(userValues);
        double dx = 1. * settings.getWidth() / (settings.getK() - 1);
        double dy = 1. * settings.getHeight() / (settings.getM() - 1);
        double f1, f2, f3, f4;
        g.setColor(settings.getISColor());
        for (double yGrid = settings.getC(); yGrid < settings.getD(); yGrid += dy) {
            for (double xGrid = settings.getA(); xGrid < settings.getB(); xGrid += dx) {
                f1 = function.valueAt(xGrid, yGrid);
                f2 = function.valueAt(xGrid+dx, yGrid);
                f3 = function.valueAt(xGrid, yGrid+dy);
                f4 = function.valueAt(xGrid+dx, yGrid+dy);
                for (double value : values) {
                    int found = 0;
                    boolean foundUp = false;
                    boolean foundDown = false;
                    boolean foundLeft = false;
                    boolean foundRight = false;
                    Point2D.Double up = new Point2D.Double();
                    Point2D.Double down = new Point2D.Double();
                    Point2D.Double left = new Point2D.Double();
                    Point2D.Double right = new Point2D.Double();
                    if (value > Math.min(f1, f2) && value <= Math.max(f1, f2)) {
                        ++found;
                        foundUp = true;
                        up.setLocation(xGrid + dx * Math.abs(value - f1) / Math.abs(f2 - f1), yGrid);
                    }
                    if (value > Math.min(f3, f4) && value <= Math.max(f3, f4)) {
                        ++found;
                        foundDown = true;
                        down.setLocation(xGrid + dx * Math.abs(value - f3) / Math.abs(f4 - f3), yGrid + dy);
                    }
                    if (value > Math.min(f1, f3) && value <= Math.max(f1, f3)) {
                        ++found;
                        foundLeft = true;
                        left.setLocation(xGrid, yGrid + dy * Math.abs(value - f1) / Math.abs(f3 - f1));
                    }
                    if (value > Math.min(f2, f4) && value <= Math.max(f2, f4)) {
                        ++found;
                        foundRight = true;
                        right.setLocation(xGrid + dx, yGrid + dy * Math.abs(value - f2) / Math.abs(f4 - f2));
                    }
                    if (1 == found) {
                        Point2D.Double point = up;
                        if (foundDown) point = down;
                        if (foundLeft) point = left;
                        if (foundRight) point = right;
                        g.drawLine((int)Math.round((getWidth() *(point.x - settings.getA()) / settings.getWidth()))  + x,
                                   (int)Math.round((getHeight()*(point.y - settings.getC()) / settings.getHeight())) + y,
                                   (int)Math.round((getWidth() *(point.x   - settings.getA()) / settings.getWidth()))  + x,
                                   (int)Math.round((getHeight()*(point.y   - settings.getC()) / settings.getHeight())) + y);
                    } else if (2 == found) {
                        Point2D.Double from = null;
                        Point2D.Double to = null;
                        if (foundUp) from = up;
                        if (foundDown && null == from)
                            from = down;
                        else if (foundDown)
                            to = down;
                        if (foundLeft && null == from)
                            from = left;
                        else if (foundLeft)
                            to = left;
                        if (foundRight && null == from)
                            from = right;
                        else if (foundRight)
                            to = right;
                        g.drawLine((int)Math.round((getWidth() *(from.x - settings.getA()) / settings.getWidth()))  + x,
                                   (int)Math.round((getHeight()*(from.y - settings.getC()) / settings.getHeight())) + y,
                                   (int)Math.round((getWidth() *(to.x   - settings.getA()) / settings.getWidth()))  + x,
                                   (int)Math.round((getHeight()*(to.y   - settings.getC()) / settings.getHeight())) + y);
                    } else if (4 == found){
                        double center = function.valueAt(xGrid + dx / 2, yGrid + dy / 2);
                        Point2D.Double from1;
                        Point2D.Double to1;
                        Point2D.Double from2;
                        Point2D.Double to2;
                        if ((f1 - value) * (value - center) > 0) {
                            from1 = up;
                            to1 = left;
                            from2 = right;
                            to2 = down;
                        } else {
                            from1 = up;
                            to1 = right;
                            from2 = left;
                            to2 = down;
                        }
                        g.drawLine((int)Math.round((getWidth() *(from1.x - settings.getA()) / settings.getWidth()))  + x,
                                   (int)Math.round((getHeight()*(from1.y - settings.getC()) / settings.getHeight())) + y,
                                   (int)Math.round((getWidth() *(to1.x   - settings.getA()) / settings.getWidth()))  + x,
                                   (int)Math.round((getHeight()*(to1.y   - settings.getC()) / settings.getHeight())) + y);
                        g.drawLine((int)Math.round((getWidth() *(from2.x - settings.getA()) / settings.getWidth()))  + x,
                                   (int)Math.round((getHeight()*(from2.y - settings.getC()) / settings.getHeight())) + y,
                                   (int)Math.round((getWidth() *(to2.x   - settings.getA()) / settings.getWidth()))  + x,
                                   (int)Math.round((getHeight()*(to2.y   - settings.getC()) / settings.getHeight())) + y);
                    }
                }
            }
        }
    }

    public void addNewValueAt(int x, int y) {
        double dxPix = 1.0 * settings.getWidth() / getWidth();
        double dyPix = 1.0 * settings.getHeight() / getHeight();
        double z = function.valueAt(settings.getA() + x * dxPix, settings.getC() + y * dyPix);
        userValues.add(z);
    }

    public void clearUserIsolines() {
        userValues.clear();
    }

}
