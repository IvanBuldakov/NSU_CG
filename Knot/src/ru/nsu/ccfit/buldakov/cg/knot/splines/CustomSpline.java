package ru.nsu.ccfit.buldakov.cg.knot.splines;

import javafx.geometry.Point3D;

import java.util.ArrayList;

public class CustomSpline implements Spline {

    ArrayList<Point3D> points;

    public CustomSpline(ArrayList<Point3D> points) {
        assert (points.size() != 6);
        this.points = points;
    }

    @Override
    public Point3D valueAt(double t) {
        double matrix[][] = {
            {  -6,  15,  -10,   0, 0, 1},
            {  -3,   8,   -6,   0, 1, 0},
            {-0.5, 1.5, -1.5, 0.5, 0, 0},
            {   6, -15,   10,   0, 0, 0},
            {  -3,   7,   -4,   0, 0, 0},
            { 0.5,  -1,  0.5,   0, 0, 0}
        };

        double factors[] = {0, 0, 0, 0, 0, 0};
        Point3D value = Point3D.ZERO;

        for (int i = 0; i < 6; ++i) {
            for (int j = 0; j < 6; ++j) {
                factors[i] += matrix[i][j] * Math.pow(t, 5 - j);
            }
            value = value.add(points.get(i).multiply(factors[i]));
        }

        return value;
    }

}
