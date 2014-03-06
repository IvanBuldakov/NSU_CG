package ru.nsu.ccfit.buldakov.cg.puzzle.util;

import java.awt.*;
import java.awt.geom.Point2D;

public class Coordinates {

    public static Point2D.Double worldToLocal(double[] matrix, int worldX, int worldY, Point2D.Double center, double angle) {
        Point2D.Double local = new Point2D.Double();
        setRotation(matrix, -angle);
        setTranslation(matrix, -center.x, -center.y);
        local.setLocation(matrix[0] * (worldX - center.x) + matrix[3] * (worldY - center.y),
                          matrix[1] * (worldX - center.x) + matrix[4] * (worldY - center.y));
        return local;
    }

    public static Point localToWorld(double[] matrix, Point2D.Double local, Point2D.Double center, double angle) {
        Point world = new Point();
        setRotation(matrix, angle);
        setTranslation(matrix, center.x, center.y);
        world.setLocation(matrix[0] * local.x + matrix[3] * local.y + matrix[6],
                          matrix[1] * local.x + matrix[4] * local.y + matrix[7]);
        return world;
    }

    public static void setRotation(double[] matrix, double angle) {
        matrix[0] = Math.cos(angle);
        matrix[1] = Math.sin(angle);
        matrix[3] = -Math.sin(angle);
        matrix[4] = Math.cos(angle);
        matrix[8] = 1;
    }

    public static void setTranslation(double[] matrix, double x, double y) {
        matrix[6] = x;
        matrix[7] = y;
    }

}
