package ru.nsu.ccfit.buldakov.cg.knot.splines;

import javafx.geometry.Point3D;

import java.util.ArrayList;

public class BezierSplineFactory implements SplineFactory{

    private int deg;

    public BezierSplineFactory(int deg) {
        this.deg = deg;
    }

    @Override
    public Spline create(ArrayList<Point3D> points) {
        assert (points.size() == degree());
        return new BezierSpline(points);
    }

    @Override
    public int degree() {
        return deg + 1;
    }

}
