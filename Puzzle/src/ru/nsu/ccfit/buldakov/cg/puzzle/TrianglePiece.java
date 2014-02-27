package ru.nsu.ccfit.buldakov.cg.puzzle;

import java.awt.*;
import java.awt.geom.Point2D;

public class TrianglePiece implements PuzzlePiece {

    private Point2D.Double   center        = new Point2D.Double(0,0);
    private Point2D.Double[] verticesLocal = new Point2D.Double[3];
    private Point2D.Double[] verticesWorld = new Point2D.Double[3];
    private double angle;
    private double[] affineMatrix = new double[9];

    double u;
    double v;

    public TrianglePiece(double x, double y, double u, double v, double legLength, boolean upper) {

        this.u = u;
        this.v = v;

        center.setLocation(x, y);

        if (upper) {
            verticesLocal[0] = new Point2D.Double(-legLength / 3, -legLength / 3);
            verticesLocal[1] = new Point2D.Double( legLength*2/3, -legLength / 3);
            verticesLocal[2] = new Point2D.Double(-legLength / 3,  legLength*2/3);
        } else {
            verticesLocal[0] = new Point2D.Double( legLength / 3, -legLength*2/3);
            verticesLocal[1] = new Point2D.Double( legLength / 3,  legLength / 3);
            verticesLocal[2] = new Point2D.Double(-legLength*2/3,  legLength / 3);
        }

        for (int i = 0; i < 3; ++i) {
            verticesWorld[i] = new Point2D.Double();
        }

        initMatrix();

    }

    @Override
    public void move(int x, int y) {

    }

    @Override
    public void rotate(double angle) {
        this.angle = angle;
        setRotation(angle);
    }

    private void translate() {
        for (int i = 0; i < 3; i++) {
            verticesWorld[i].setLocation(affineMatrix[0] * verticesLocal[i].x + affineMatrix[3] * verticesLocal[i].y
                                                 + affineMatrix[6],
                                         affineMatrix[1] * verticesLocal[i].x + affineMatrix[4] * verticesLocal[i].y
                                                 + affineMatrix[7]);
            verticesWorld[i].setLocation(Math.round(verticesWorld[i].getX()), Math.round(verticesWorld[i].getY()));
        }
    }

    @Override
    public void draw(PuzzleGraphics g) {
        translate();
        textureMapping(g);
        for (int i = 0; i < 3; ++i) {
            g.drawBresenhamLine((int) verticesWorld[i].x, (int) verticesWorld[i].y,
                                (int) verticesWorld[(i + 1) % 3].x, (int) verticesWorld[(i + 1) % 3].y,
                                Color.black.getRGB());
        }
    }

    private void textureMapping(PuzzleGraphics g) {
        orderVerticesByY();

        double dx01 = 0, dx02 = 0, dx12 = 0;
        if (verticesWorld[2].getY() != verticesWorld[0].getY()) {
            dx02 = (verticesWorld[2].getX() - verticesWorld[0].getX())/(verticesWorld[2].getY() - verticesWorld[0].getY());
        }
        if (verticesWorld[1].getY() != verticesWorld[0].getY()) {
            dx01 = (verticesWorld[1].getX() - verticesWorld[0].getX())/(verticesWorld[1].getY() - verticesWorld[0].getY());
        }
        if (verticesWorld[2].getY() != verticesWorld[1].getY()) {
            dx12 = (verticesWorld[2].getX() - verticesWorld[1].getX())/(verticesWorld[2].getY() - verticesWorld[1].getY());
        }
        double wx0 = verticesWorld[0].getX();
        double wx1 = wx0;
        if (verticesWorld[0].getY() == verticesWorld[1].getY()) {
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

        for (int i = (int) verticesWorld[0].y; i < (int) verticesWorld[1].y; ++i) {
            for (int j = (int)Math.round(wx0); j <= Math.round(wx1); ++j) {
                Point2D.Double point = worldToLocal(j, i, angle);
                g.setPixel(j, i, g.getColorFromTexture(point, u, v));
            }
            wx0 += dx02;
            wx1 += dx01;
        }

        if (savedDx02 < dx12) {
            double tmp = savedDx02;
            savedDx02 = dx12;
            dx12 = tmp;
        }

        for (int i = (int) verticesWorld[1].y; i <= (int) verticesWorld[2].y; ++i){
            for (int j = (int)Math.round(wx0); j <= (int)Math.round(wx1); j++){
                Point2D.Double point = worldToLocal(j, i, angle);
                g.setPixel(j, i, g.getColorFromTexture(point, u, v));
            }
            wx0 += savedDx02;
            wx1 += dx12;
        }
    }

    private void orderVerticesByY() {
        Point2D.Double tmp;
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

    private void setRotation(double angle) {
        angle = Math.toRadians(angle);
        affineMatrix[0] = Math.cos(angle);
        affineMatrix[1] = Math.sin(angle);
        affineMatrix[3] = - Math.sin(angle);
        affineMatrix[4] = Math.cos(angle);
    }

    private void initMatrix() {
        affineMatrix[0] = 1;
        affineMatrix[4] = 1;
        affineMatrix[6] = center.x;
        affineMatrix[7] = center.y;
        affineMatrix[8] = 1;
    }

    private Point2D.Double worldToLocal(int worldX, int worldY, double angle) {
        Point2D.Double local = new Point2D.Double(0,0);
        double matrix[] = new double[9];

        for (int i = 0; i < 9; i++) {
            matrix[i] = 0;
        }

        double angleR = Math.toRadians(angle);
        matrix[0] = Math.cos(angleR);
        matrix[1] = -Math.sin(angleR);
        matrix[3] = Math.sin(angleR);
        matrix[4] = Math.cos(angleR);
        matrix[6] = -center.x;
        matrix[7] = -center.y;
        matrix[8] = 1;
        local.x = Math.round( matrix[0]*(worldX-center.x) + matrix[3]*(worldY-center.y));
        local.y = Math.round( matrix[1]*(worldX-center.x) + matrix[4]*(worldY-center.y));

        return local;
    }

}
