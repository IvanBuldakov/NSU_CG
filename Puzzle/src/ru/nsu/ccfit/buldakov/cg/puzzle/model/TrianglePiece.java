package ru.nsu.ccfit.buldakov.cg.puzzle.model;

import ru.nsu.ccfit.buldakov.cg.puzzle.util.Coordinates;
import ru.nsu.ccfit.buldakov.cg.puzzle.view.PuzzleGraphics;

import java.awt.*;
import java.awt.geom.Point2D;

public class TrianglePiece implements PuzzlePiece {

    private double[]         affineMatrix  = new double[9];
    private Point2D.Double   center        = new Point2D.Double(0, 0);
    private Point2D.Double   start         = new Point2D.Double(0, 0);
    private Point2D.Double   end           = new Point2D.Double(0, 0);
    private Point2D.Double   delta         = new Point2D.Double(0, 0);
    private Point2D.Double[] verticesLocal = new Point2D.Double[3];
    private Point[]          verticesWorld = new Point[3];
    private double currentAngle;
    private double endAngle;
    private double deltaAngle;

    private double u;
    private double v;

    private int pixels;
    private int borderPixels;
    private int opaquePixels;

    private int prev;

    public TrianglePiece(double x, double y, double u, double v, double legLength, boolean upper) {

        setStartPoint(x, y);
        setCenterPoint(x, y);

        this.u = u;
        this.v = v;

        if (upper) {
            verticesLocal[0] = new Point2D.Double(-legLength / 3, -legLength / 3);
            verticesLocal[1] = new Point2D.Double(legLength * 2 / 3, -legLength / 3);
            verticesLocal[2] = new Point2D.Double(-legLength / 3, legLength * 2 / 3);
        } else {
            verticesLocal[0] = new Point2D.Double(legLength / 3, -legLength * 2 / 3);
            verticesLocal[1] = new Point2D.Double(legLength / 3, legLength / 3);
            verticesLocal[2] = new Point2D.Double(-legLength * 2 / 3, legLength / 3);
        }

        for (int i = 0; i < 3; ++i) {
            verticesWorld[i] = Coordinates.localToWorld(affineMatrix, verticesLocal[i], start, currentAngle);
        }

    }

    @Override
    public void setCenterPoint(double x, double y) {
        center.setLocation(x, y);
    }

    @Override
    public void setStartPoint(double x, double y) {
        start.setLocation(x, y);
    }

    @Override
    public void setEndPoint(double x, double y) {
        end.setLocation(x, y);
        delta.setLocation((end.x - start.x) / 180, (end.y - start.y) / 180);
    }

    @Override
    public void setEndAngle(double endAngle) {
        this.endAngle = Math.toRadians(endAngle);
        deltaAngle = this.endAngle / 180;
    }

    @Override
    public void reset() {
        center.setLocation(start.x, start.y);
        currentAngle = 0;
    }

    @Override
    public void translate(int t) {
        int dt = t - prev;
        double sign = 1;

        if (prev < 180 && 180 < t) {
            center.setLocation(end);
            currentAngle = endAngle;
            dt = t - 180;
        }

        if (t > 180) {
            sign = -1;
        }
        if (dt < 0 && t == 180) {
            sign = -1;
        }

        if (t < 180 && 180 < prev) {
            center.setLocation(end);
            currentAngle = endAngle;
            dt = 180 - t;
        }

        if (t == 0) {
            dt = 0;
            center.setLocation(start);
            currentAngle = 0;
        }

        center.x += sign * delta.x * dt;
        center.y += sign * delta.y * dt;
        currentAngle += sign * deltaAngle * dt;

        for (int i = 0; i < 3; ++i) {
            verticesWorld[i] = Coordinates.localToWorld(affineMatrix, verticesLocal[i], center, currentAngle);
        }
        prev = t;
    }

    @Override
    public int getBorderPixels() {
        return borderPixels;
    }

    @Override
    public void draw(PuzzleGraphics g) {
        pixels = 0;
        borderPixels = 0;
        opaquePixels = 0;
        textureMapping(g);
        for (int i = 0; i < 3; ++i) {
            borderPixels += g.drawBresenhamLine(verticesWorld[i].x, verticesWorld[i].y,
                                                verticesWorld[(i + 1) % 3].x, verticesWorld[(i + 1) % 3].y,
                                                Color.black.getRGB());
        }
    }

    @Override
    public boolean contains(int x, int y) {
        Point p = new Point(x, y);
        return area(verticesWorld[0], verticesWorld[1], verticesWorld[2]) ==
                area(verticesWorld[0], verticesWorld[1], p) +
                        area(verticesWorld[1], verticesWorld[2], p) +
                        area(verticesWorld[2], verticesWorld[0], p);
    }

    @Override
    public int getPixels() {
        return pixels;
    }

    private int area(Point a, Point b, Point c) {
        return Math.abs((a.x - c.x) * (b.y - c.y) + (b.x - c.x) * (c.y - a.y));
    }

    @Override
    public int getOpaquePixels() {
        return opaquePixels;
    }

    private void textureMapping(PuzzleGraphics g) {
        orderVerticesByY();

        double dx01 = 0, dx02 = 0, dx12 = 0;
        if (verticesWorld[2].y != verticesWorld[0].y) {
            dx02 = 1.0 * (verticesWorld[2].x - verticesWorld[0].x) / (verticesWorld[2].y - verticesWorld[0].y);
        }
        if (verticesWorld[1].y != verticesWorld[0].y) {
            dx01 = 1.0 * (verticesWorld[1].x - verticesWorld[0].x) / (verticesWorld[1].y - verticesWorld[0].y);
        }
        if (verticesWorld[2].y != verticesWorld[1].y) {
            dx12 = 1.0 * (verticesWorld[2].x - verticesWorld[1].x) / (verticesWorld[2].y - verticesWorld[1].y);
        }
        double wx0 = verticesWorld[0].x;
        double wx1 = wx0;
        if (verticesWorld[0].y == verticesWorld[1].y) {
            if (verticesWorld[0].x < verticesWorld[1].x) {
                wx0 = verticesWorld[0].x;
                wx1 = verticesWorld[1].x;
            } else {
                wx0 = verticesWorld[1].x;
                wx1 = verticesWorld[0].x;
            }
        }
        double savedDx02 = dx02;

        if (dx02 > dx01) {
            double tmp = dx02;
            dx02 = dx01;
            dx01 = tmp;
        }

        for (int i = verticesWorld[0].y; i < verticesWorld[1].y; ++i) {
            for (int j = (int) Math.round(wx0); j <= Math.round(wx1); ++j) {
                Point2D.Double point = Coordinates.worldToLocal(affineMatrix, j, i, center, currentAngle);
                int color;
                if (g.isBilinearFiltrationEnabled()) {
                    color = g.getFiltratedColor(point, u, v);
                } else {
                    color = g.getSourceColor(point, u, v);
                }
                if (g.isAlphaBlendingEnabled()) {
                    color = g.getAlphaColor(j, i, color);
                    if ((color >> 24 & 0xFF) != 0xFF) {
                        --opaquePixels;
                    }
                }
                g.setPixel(j, i, color);
                ++opaquePixels;
                ++pixels;
            }
            wx0 += dx02;
            wx1 += dx01;
        }

        if (savedDx02 < dx12) {
            double tmp = savedDx02;
            savedDx02 = dx12;
            dx12 = tmp;
        }

        for (int i = verticesWorld[1].y; i <= verticesWorld[2].y; ++i) {
            for (int j = (int) Math.round(wx0); j <= (int) Math.round(wx1); j++) {
                Point2D.Double point = Coordinates.worldToLocal(affineMatrix, j, i, center, currentAngle);
                int color;
                if (g.isBilinearFiltrationEnabled()) {
                    color = g.getFiltratedColor(point, u, v);
                } else {
                    color = g.getSourceColor(point, u, v);
                }
                if (g.isAlphaBlendingEnabled()) {
                    color = g.getAlphaColor(j, i, color);
                    if ((color >> 24 & 0xFF) != 0xFF) {
                        --opaquePixels;
                    }
                }
                g.setPixel(j, i, color);
                ++opaquePixels;
                ++pixels;

            }
            wx0 += savedDx02;
            wx1 += dx12;
        }
    }

    private void orderVerticesByY() {
        Point tmp;
        if (verticesWorld[1].getY() < verticesWorld[0].getY()) {
            tmp = verticesWorld[0];
            verticesWorld[0] = verticesWorld[1];
            verticesWorld[1] = tmp;
        }
        if (verticesWorld[2].getY() < verticesWorld[0].getY()) {
            tmp = verticesWorld[0];
            verticesWorld[0] = verticesWorld[2];
            verticesWorld[2] = tmp;
        }
        if (verticesWorld[1].getY() > verticesWorld[2].getY()) {
            tmp = verticesWorld[2];
            verticesWorld[2] = verticesWorld[1];
            verticesWorld[1] = tmp;
        }
    }

}
