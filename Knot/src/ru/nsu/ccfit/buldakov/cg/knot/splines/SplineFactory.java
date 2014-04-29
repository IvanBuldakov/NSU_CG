package ru.nsu.ccfit.buldakov.cg.knot.splines;

import javafx.geometry.Point3D;

import java.util.ArrayList;

public interface SplineFactory {

    public Spline create(ArrayList<Point3D> points);

    public int degree();

    public default boolean requiresTangent() {
        return false;
    }

    public default boolean requiresDerivative() {
        return false;
    }

}
