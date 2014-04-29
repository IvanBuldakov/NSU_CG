package ru.nsu.ccfit.buldakov.cg.knot.knots;

import javafx.geometry.Point3D;

public class TorusKnot implements Knot {
    @Override
    public Point3D valueAt(double t) {
        double x = Math.cos(4 * t) + 0.45 * Math.cos(-1 * t);
        double y = Math.sin(4 * t) + 0.45 * Math.sin(-1 * t);
        double z = Math.sin(5 * t);
        return new Point3D(x, y, z);
    }

    @Override
    public Point3D tangentAt(double t) {
        double x = -4 * Math.sin(4 * t) + 0.45 * Math.cos(-1 * t);
        double y =  4 * Math.cos(4 * t) - 0.45 * Math.sin(-1 * t);
        double z =  5 * Math.cos(5 * t);
        return new Point3D(x, y, z);
    }

    @Override
    public Point3D derivativeAt(double t) {
        double x = - 16 * Math.cos(4 * t) - 0.45 * Math.cos(-1 * t);
        double y = - 16 * Math.sin(4 * t) - 0.45 * Math.sin(-1 * t);
        double z = - 25 * Math.sin(5 * t);
        return new Point3D(x, y, z);
    }
}
