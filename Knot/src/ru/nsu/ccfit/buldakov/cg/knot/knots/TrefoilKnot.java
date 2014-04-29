package ru.nsu.ccfit.buldakov.cg.knot.knots;

import javafx.geometry.Point3D;

public class TrefoilKnot implements Knot{
    @Override
    public Point3D valueAt(double t) {
        double x = Math.sin(t) + 2 * Math.sin(2 * t);
        double y = Math.cos(t) - 2 * Math.cos(2 * t);
        double z = - Math.sin(3 * t);
        return new Point3D(x, y, z);
    }

    @Override
    public Point3D tangentAt(double t) {
        double x = Math.cos(t) + 2 * 2 * Math.cos(2 * t);
        double y = - Math.sin(t) + 2 * 2 * Math.sin(2 * t);
        double z = - 3 * Math.cos(3 * t);
        return new Point3D(x, y, z);
    }

    @Override
    public Point3D derivativeAt(double t) {
        double x = - Math.sin(t) - 4 * 2 * Math.sin(2 * t);
        double y = - Math.cos(t) + 4 * 2 * Math.cos(2 * t);
        double z = 9 * Math.sin(3 * t);
        return new Point3D(x, y, z);
    }
}
