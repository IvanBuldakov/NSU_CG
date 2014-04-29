package ru.nsu.ccfit.buldakov.cg.knot.splines;

import javafx.geometry.Point3D;

import java.util.ArrayList;

public class BezierSpline implements Spline {

    private ArrayList<Point3D> points;

    public BezierSpline(ArrayList<Point3D> points) {
        this.points = points;
    }

    @Override
    public Point3D valueAt(double t) {
        int size = points.size();
        Point3D value = Point3D.ZERO;
        for (int i = 0; i < size; ++i) {
            double factor = binomial(size - 1, i) * Math.pow(t, i) * Math.pow(1 - t, size - 1 - i);
            value = value.add(points.get(i).multiply(factor));
        }
        return value;
    }

    private int binomial(final int n, final int k) {
        if (0 == k || n == k)
            return 1;
        return binomial(n - 1, k - 1) + binomial(n - 1, k);
    }

}
