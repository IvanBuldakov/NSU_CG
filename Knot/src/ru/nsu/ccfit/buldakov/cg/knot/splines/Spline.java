package ru.nsu.ccfit.buldakov.cg.knot.splines;

import javafx.geometry.Point3D;

public interface Spline {

    public Point3D valueAt(double t);

}
