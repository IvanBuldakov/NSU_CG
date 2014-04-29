package ru.nsu.ccfit.buldakov.cg.knot.knots;

import javafx.geometry.Point3D;

public interface Knot {

    public Point3D valueAt(double t);

    public Point3D tangentAt(double t);

    public Point3D derivativeAt(double t);

    public default double from() {
        return 0;
    }

    public default double to() {
        return 2 * Math.PI;
    }

}
