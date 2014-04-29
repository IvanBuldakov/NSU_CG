package ru.nsu.ccfit.buldakov.cg.knot.splines;

import javafx.geometry.Point3D;

import java.util.ArrayList;

public class CustomSplineFactory implements SplineFactory {

    @Override
    public Spline create(ArrayList<Point3D> points) {
        return new CustomSpline(points);
    }

    @Override
    public int degree() {
        return 2;
    }

    @Override
    public boolean requiresTangent() {
        return true;
    }

    @Override
    public boolean requiresDerivative() {
        return true;
    }

}
