package ru.nsu.ccfit.buldakov.cg.tetragon.model;

import javafx.geometry.Point2D;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Tetragon {

    private Type type;
    private ArrayList<Vertex> vertices;
    private ArrayList<Point2D> verticesWorld = new ArrayList<>();
    private ArrayList<Point2D> verticesImage = new ArrayList<>();
    private Texture            tex           = Texture.load();
    private double[] factors;

    public Tetragon(ArrayList<Vertex> vertices, Type type) {
        assert vertices.size() == 4;
        this.vertices = new ArrayList<>(vertices);
        this.type = type;
        initImageVertices();
    }

    public Tetragon(Point2D a, Point2D b, Point2D c, Point2D d, Type type) {
        vertices = new ArrayList<>(4);
        vertices.add(new Vertex(a));
        vertices.add(new Vertex(b));
        vertices.add(new Vertex(c));
        vertices.add(new Vertex(d));
        this.type = type;
        initImageVertices();
    }

    private void initImageVertices() {
        verticesImage.add(new Point2D(0, 0));
        verticesImage.add(new Point2D(1, 0));
        verticesImage.add(new Point2D(1, 1));
        verticesImage.add(new Point2D(0, 1));
    }

    public void draw(BufferedImage canvas, boolean filtration) {
        for (Vertex vertex : vertices) {
            verticesWorld.add(new Point2D(Math.round(vertex.getX() * canvas.getWidth()), Math.round(vertex.getY() * canvas.getHeight())));
        }
        Graphics g = canvas.createGraphics();
        if (type == Type.CONVEX) {
            factors = calculatePerspectiveCoefficients();
            textureMapping(g, verticesWorld.get(0), verticesWorld.get(1), verticesWorld.get(3), filtration);
            textureMapping(g, verticesWorld.get(1), verticesWorld.get(3), verticesWorld.get(2), filtration);
        } else if (type == Type.CONCAVE) {
            factors = calculateTopAffineCoefficients();
            textureMapping(g, verticesWorld.get(0), verticesWorld.get(1), verticesWorld.get(3), filtration);
            factors = calculateBottomAffineCoefficients();
            textureMapping(g, verticesWorld.get(1), verticesWorld.get(3), verticesWorld.get(2), filtration);
        }
        drawEdges(g);
        drawVertices(canvas);
        verticesWorld.clear();
    }

    private double[] calculateTopAffineCoefficients() {
        int x0 = (int) verticesWorld.get(0).getX();
        int y0 = (int) verticesWorld.get(0).getY();
        int x1 = (int) verticesWorld.get(1).getX();
        int y1 = (int) verticesWorld.get(1).getY();
        int x3 = (int) verticesWorld.get(3).getX();
        int y3 = (int) verticesWorld.get(3).getY();
        int det = (x3-x0)*(y1-y0)-(x1-x0)*(y3-y0);
        return new double[] {1.0*(y0-y3)/det, 1.0*(x3-x0)/det, 1.0*(x0*(y3-y0)-(x3-x0)*y0)/det, 1.0*(y1-y0)/det, 1.0*(x0-x1)/det, 1.0*((x1-x0)*y0-(y1-y0)*x0)/det, 0, 0};
    }

    private double[] calculateBottomAffineCoefficients() {
        int x1 = (int) verticesWorld.get(1).getX();
        int y1 = (int) verticesWorld.get(1).getY();
        int x3 = (int) verticesWorld.get(3).getX();
        int y3 = (int) verticesWorld.get(3).getY();
        Point2D CB = verticesWorld.get(1).subtract(verticesWorld.get(2));
        Point2D A = verticesWorld.get(3).add(CB);
        int x0 = (int) A.getX();
        int y0 = (int) A.getY();
        int det = (x3-x0)*(y1-y0)-(x1-x0)*(y3-y0);
        return new double[] {1.0*(y0-y3)/det, 1.0*(x3-x0)/det, 1.0*(x0*(y3-y0)-(x3-x0)*y0)/det, 1.0*(y1-y0)/det, 1.0*(x0-x1)/det, 1.0*((x1-x0)*y0-(y1-y0)*x0)/det, 0, 0};
    }

    private void drawEdges(Graphics g) {
        g.setColor(Color.RED);
        for (int i = 0; i < vertices.size(); ++i) {
            int X0 = (int) Math.round(verticesWorld.get(i).getX());
            int Y0 = (int) Math.round(verticesWorld.get(i).getY());
            int X1 = (int) Math.round(verticesWorld.get((i + 1) % vertices.size()).getX());
            int Y1 = (int) Math.round(verticesWorld.get((i + 1) % vertices.size()).getY());
            g.drawLine(X0, Y0, X1, Y1);
        }
    }

    private void drawVertices(BufferedImage canvas) {
        for (Vertex vertex : vertices) {
            vertex.draw(canvas, Color.BLUE);
        }
    }

    double[] calculatePerspectiveCoefficients() {
        double[][] A = new double[8][8];
        double[]   b = new double[8];
        int k = 0;
        for (int i = 0; i < 8; ++i) {
            A[i][3 * (i % 2)] = verticesWorld.get(k).getX();
            A[i][1 + 3 * (i % 2)] = verticesWorld.get(k).getY();
            A[i][2 + 3 * (i % 2)] = 1;
            if (i % 2 == 0) {
                A[i][6] = -verticesImage.get(k).getX() * verticesWorld.get(k).getX();
                A[i][7] = -verticesImage.get(k).getX() * verticesWorld.get(k).getY();
                b[i] = verticesImage.get(k).getX();
            } else {
                A[i][6] = -verticesImage.get(k).getY() * verticesWorld.get(k).getX();
                A[i][7] = -verticesImage.get(k).getY() * verticesWorld.get(k).getY();
                b[i] = verticesImage.get(k).getY();
            }
            k = (i%2 == 1) ? k+1 : k;
        }
        return solveEquation(A, b);
    }

    private double[] solveEquation(double[][] a, double[] b) {
        int n = a.length;
        for (int row = 0; row < n; row++) {
            int best = row;
            for (int i = row + 1; i < n; i++)
                if (Math.abs(a[best][row]) < Math.abs(a[i][row]))
                    best = i;
            double[] tt = a[row];
            a[row] = a[best];
            a[best] = tt;
            double t = b[row];
            b[row] = b[best];
            b[best] = t;
            for (int i = row + 1; i < n; i++)
                a[row][i] /= a[row][row];
            b[row] /= a[row][row];
            for (int i = 0; i < n; i++) {
                double x = a[i][row];
                if (i != row && x != 0) {
                    for (int j = row + 1; j < n; j++)
                        a[i][j] -= a[row][j] * x;
                    b[i] -= b[row] * x;
                }
            }
        }
        return b;
    }

    private void textureMapping(Graphics g, Point2D a, Point2D b, Point2D c, boolean filtration) {
        Point2D[] triangleVertices = { a, b, c };
        triangleVertices = orderVerticesByY(triangleVertices);
        double dx01 = 0, dx02 = 0, dx12 = 0;
        if (triangleVertices[2].getY() != triangleVertices[0].getY()) {
            dx02 = 1.0 * (triangleVertices[2].getX() - triangleVertices[0].getX()) / (triangleVertices[2].getY() - triangleVertices[0].getY());
        }
        if (triangleVertices[1].getY() != triangleVertices[0].getY()) {
            dx01 = 1.0 * (triangleVertices[1].getX() - triangleVertices[0].getX()) / (triangleVertices[1].getY() - triangleVertices[0].getY());
        }
        if (triangleVertices[2].getY() != triangleVertices[1].getY()) {
            dx12 = 1.0 * (triangleVertices[2].getX() - triangleVertices[1].getX()) / (triangleVertices[2].getY() - triangleVertices[1].getY());
        }
        double wx0 = triangleVertices[0].getX();
        double wx1 = wx0;
        if (triangleVertices[0].getY() == triangleVertices[1].getY()) {
            if (triangleVertices[0].getX() < triangleVertices[1].getX()) {
                wx0 = triangleVertices[0].getX();
                wx1 = triangleVertices[1].getX();
            } else {
                wx0 = triangleVertices[1].getX();
                wx1 = triangleVertices[0].getX();
            }
        }
        double savedDx02 = dx02;

        if (dx02 > dx01) {
            double tmp = dx02;
            dx02 = dx01;
            dx01 = tmp;
        }

        for (int i = (int) triangleVertices[0].getY(); i < (int) triangleVertices[1].getY(); ++i) {
            for (int j = (int) Math.round(wx0); j <= (int) Math.round(wx1); ++j) {
                Point2D texPoint = worldToTex(factors, j, i);
                Point2D nextPoint = worldToTex(factors, j+1, i+1);
                Point2D dPoint = nextPoint.subtract(texPoint);
                double d = (dPoint.getX() > dPoint.getY()) ? dPoint.getX() : dPoint.getY();
                try {
                    int color;
                    if (filtration)
                        color = tex.getFiltratedPixel(texPoint, d);
                    else
                        color = tex.getPixel(texPoint, d);
                    g.setColor(new Color(color));
                } catch (Throwable e) {}
                g.drawLine(j, i, j, i);
            }
            wx0 += dx02;
            wx1 += dx01;
        }

        if (savedDx02 < dx12) {
            double tmp = savedDx02;
            savedDx02 = dx12;
            dx12 = tmp;
        }

        for (int i = (int) triangleVertices[1].getY(); i <= (int) triangleVertices[2].getY(); ++i) {
            for (int j = (int) Math.round(wx0); j <= (int) Math.round(wx1); j++) {
                Point2D texPoint = worldToTex(factors, j, i);
                Point2D nextPoint = worldToTex(factors, j + 1, i + 1);
                Point2D dPoint = nextPoint.subtract(texPoint);
                double d = (dPoint.getX() > dPoint.getY()) ? dPoint.getX() : dPoint.getY();
                try {
                    int color;
                    if (filtration)
                        color = tex.getFiltratedPixel(texPoint, d);
                    else
                        color = tex.getPixel(texPoint, d);
                    g.setColor(new Color(color));
                } catch (Throwable e) {}
                g.drawLine(j, i, j, i);
            }
            wx0 += savedDx02;
            wx1 += dx12;
        }
    }

    private Point2D[] orderVerticesByY(Point2D[] vertices) {
        Point2D tmp;
        if (vertices[1].getY() < vertices[0].getY()) {
            tmp = vertices[0];
            vertices[0] = vertices[1];
            vertices[1] = tmp;
        }
        if (vertices[2].getY() < vertices[0].getY()) {
            tmp = vertices[0];
            vertices[0] = vertices[2];
            vertices[2] = tmp;
        }
        if (vertices[1].getY() > vertices[2].getY()) {
            tmp = vertices[2];
            vertices[2] = vertices[1];
            vertices[1] = tmp;
        }
        return vertices;
    }

    private Point2D worldToTex(double[] factors, int x, int y) {
        double u = (factors[0]*x + factors[1]*y + factors[2]) / (factors[6]*x + factors[7]*y + 1);
        double v = (factors[3]*x + factors[4]*y + factors[5]) / (factors[6]*x + factors[7]*y + 1);
        return new Point2D(u, v);
    }

    public static enum Type {
        CONVEX,
        CONCAVE
    }

}
