package ru.nsu.ccfit.buldakov.cg.is.Functions;

import java.awt.geom.Point2D;

public interface Function {

    public static double getMax(Function f, int a, int b, int c, int d, int k, int m) {
        Point2D.Double max = new Point2D.Double(Double.MIN_VALUE, Double.MIN_VALUE);
        double stepX = 1.0 * (b - a) / (k - 1);
        double stepY = 1.0 * (d - c) / (m - 1);
        double z;
        double value = Double.MIN_VALUE;
        for (double y = c; y <= d; y += stepY) {
            for (double x = a; x <= b; x += stepX) {
                if ((z = f.valueAt(x, y)) > value) {
                    max.setLocation(x, y);
                    value = z;
                }
            }
        }
        return value;
    }

    public static double getMin(Function f, int a, int b, int c, int d, int k, int m) {
        Point2D.Double min = new Point2D.Double(Double.MAX_VALUE, Double.MAX_VALUE);
        double stepX = 1.0 * (b - a) / (k - 1);
        double stepY = 1.0 * (d - c) / (m - 1);
        double z;
        double value = Double.MAX_VALUE;
        for (double y = c; y <= d; y += stepY) {
            for (double x = a; x <= b; x += stepX) {
                if ((z = f.valueAt(x, y)) < value) {
                    min.setLocation(x, y);
                    value = z;
                }
            }
        }
        return value;
    }

    public double valueAt(double x, double y);

}
