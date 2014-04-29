package ru.nsu.ccfit.buldakov.cg.knot.view;

import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.util.Pair;
import ru.nsu.ccfit.buldakov.cg.knot.Settings;
import ru.nsu.ccfit.buldakov.cg.knot.splines.Spline;
import ru.nsu.ccfit.buldakov.cg.knot.splines.SplineFactory;
import ru.nsu.ccfit.buldakov.cg.knot.knots.Knot;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.*;

public class Scene extends JPanel {

    private Settings settings;

    private ArrayList<Point3D> boundingBox      = new ArrayList<>();
    private ArrayList<Point2D> boundingBoxEdges = new ArrayList<>();
    private Point3D            viewPoint        = Point3D.ZERO;
    private Point3D            planeCenter      = Point3D.ZERO;
    private Point3D            eye              = Point3D.ZERO;
    private double             theta            = 0;
    private double             psi              = 0;
    private double             thetaRad         = Math.toRadians(-90 + theta);
    private double             psiRad           = Math.toRadians(90 - psi);
    private double             planeToEye       = 2;
    private double             viewToPlane      = 2;
    private Point2D            pressed          = Point2D.ZERO;
    private Graphics g;

    public Scene(Settings settings) {
        super();
        this.settings = settings;
        init();
        addMouseWheelListener(e -> {
            int delta = e.getWheelRotation();
            viewToPlane -= 1. * delta / 10;
            recalculatePlaneCenter();
            recalculateEyePosition();
            repaint();
        });
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                pressed = new Point2D(e.getX(), e.getY());
            }
        });
        addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                theta -= (e.getX() - pressed.getX());
                psi += (e.getY() - pressed.getY());
                recalculateAngles();
                pressed = new Point2D(e.getX(), e.getY());
                recalculatePlaneCenter();
                recalculateEyePosition();
                repaint();
            }

            @Override
            public void mouseMoved(MouseEvent e) { }
        });
    }

    private void init() {
        recalculateAngles();
        recalculatePlaneCenter();
        recalculateEyePosition();
        setBoundingBoxEdges();
    }

    private void recalculateAngles() {
        thetaRad = Math.toRadians(-90 + theta);
        psiRad   = Math.toRadians( 90 - psi);
    }

    private void recalculatePlaneCenter() {
        double centerX = viewToPlane * Math.sin(psiRad) * Math.cos(thetaRad);
        double centerZ = viewToPlane * Math.sin(psiRad) * Math.sin(thetaRad);
        double centerY = viewToPlane * Math.cos(psiRad);
        planeCenter = new Point3D(centerX, centerY, centerZ);
    }

    private void recalculateEyePosition() {
        double eyeX = (viewToPlane + planeToEye) * Math.sin(psiRad) * Math.cos(thetaRad);
        double eyeZ = (viewToPlane + planeToEye) * Math.sin(psiRad) * Math.sin(thetaRad);
        double eyeY = (viewToPlane + planeToEye) * Math.cos(psiRad);
        eye         = new Point3D(eyeX, eyeY, eyeZ);
    }

    private void setBoundingBoxEdges() {
        boundingBoxEdges.add(new Point2D(0, 1));
        boundingBoxEdges.add(new Point2D(0, 2));
        boundingBoxEdges.add(new Point2D(0, 4));
        boundingBoxEdges.add(new Point2D(1, 3));
        boundingBoxEdges.add(new Point2D(1, 5));
        boundingBoxEdges.add(new Point2D(2, 3));
        boundingBoxEdges.add(new Point2D(2, 6));
        boundingBoxEdges.add(new Point2D(3, 7));
        boundingBoxEdges.add(new Point2D(4, 5));
        boundingBoxEdges.add(new Point2D(4, 6));
        boundingBoxEdges.add(new Point2D(5, 7));
        boundingBoxEdges.add(new Point2D(6, 7));
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        this.g = g;

        Knot knot = settings.getKnot();
        double from = knot.from();
        double to   = knot.to();
        SplineFactory factory = settings.getSplineFactory();
        int deg  = factory.degree();
        int numOfSplines = settings.getNumOfSplines();
        double step = (to - from) / numOfSplines;

        double minX = Double.MAX_VALUE;
        double maxX = Double.MIN_VALUE;
        double minY = Double.MAX_VALUE;
        double maxY = Double.MIN_VALUE;
        double minZ = Double.MAX_VALUE;
        double maxZ = Double.MIN_VALUE;

        clear();
        for (int i = 0; i < numOfSplines; ++i) {
            double t = i * step + from;
            ArrayList<Point3D> control = new ArrayList<>();
            for (int j = 0; j < deg; ++j) {
                double tmp = t + j * step / (deg - 1);
                Point3D point3D = knot.valueAt(tmp);
                control.add(point3D);
                if (factory.requiresTangent()) {
                    control.add(knot.tangentAt(tmp).multiply(step));
                }
                if (factory.requiresDerivative()) {
                    control.add(knot.derivativeAt(tmp).multiply(step*step));
                }
            }
            Spline spline = factory.create(control);

            double begin = 0;
            double end   = 1;

            Point3D begin3D = spline.valueAt(begin);
            Point3D end3D   = spline.valueAt(end);

            Point2D begin2D = project(begin3D);
            Point2D end2D   = project(end3D);

            if (isVisible(begin3D)) {
                setPixel(begin2D);
            }
            if (isVisible(end3D)) {
                setPixel(end2D);
            }

            Pair<Double, Point2D> beginCached = new Pair<>(begin, begin2D);
            Pair<Double, Point2D> endCached   = new Pair<>(end, end2D);

            Queue<Pair<Pair<Double, Point2D>, Pair<Double, Point2D>>> queue = new ArrayDeque<>();
            queue.add(new Pair<>(beginCached, endCached));

            minX = Math.min(minX, Math.min(begin3D.getX(), end3D.getX()));
            maxX = Math.max(maxX, Math.max(begin3D.getX(), end3D.getX()));
            minY = Math.min(minY, Math.min(begin3D.getY(), end3D.getY()));
            maxY = Math.max(maxY, Math.max(begin3D.getY(), end3D.getY()));
            minZ = Math.min(minZ, Math.min(begin3D.getZ(), end3D.getZ()));
            maxZ = Math.max(maxZ, Math.max(begin3D.getZ(), end3D.getZ()));
            while (!queue.isEmpty() && queue.size() < 1000) {
                Pair<Pair<Double, Point2D>, Pair<Double, Point2D>> interval = queue.poll();

                double start  = interval.getKey().getKey();
                double finish = interval.getValue().getKey();
                double middle = (start + finish) / 2;

                Point2D start2D  = interval.getKey().getValue();
                Point2D finish2D = interval.getValue().getValue();

                if (Math.abs(start2D.getX() - finish2D.getX()) > 1 ||
                        Math.abs(start2D.getY() - finish2D.getY()) > 1) {

                    Point3D middle3D = spline.valueAt(middle);
                    Point2D middle2D = project(middle3D);

                    if (isVisible(middle3D)) {
                        setPixel(middle2D);
                    }

                    Pair<Double, Point2D> middleCached = new Pair<>(middle, middle2D);
                    queue.add(new Pair<>(interval.getKey(), middleCached));
                    queue.add(new Pair<>(middleCached, interval.getValue()));

                    minX = Math.min(minX, middle3D.getX());
                    maxX = Math.max(maxX, middle3D.getX());
                    minY = Math.min(minY, middle3D.getY());
                    maxY = Math.max(maxY, middle3D.getY());
                    minZ = Math.min(minZ, middle3D.getZ());
                    maxZ = Math.max(maxZ, middle3D.getZ());
                }
            }
        }

        boundingBox.clear();
        boundingBox.add(new Point3D(minX, minY, minZ));
        boundingBox.add(new Point3D(minX, minY, maxZ));
        boundingBox.add(new Point3D(minX, maxY, minZ));
        boundingBox.add(new Point3D(minX, maxY, maxZ));
        boundingBox.add(new Point3D(maxX, minY, minZ));
        boundingBox.add(new Point3D(maxX, minY, maxZ));
        boundingBox.add(new Point3D(maxX, maxY, minZ));
        boundingBox.add(new Point3D(maxX, maxY, maxZ));

        for (int i = 0; settings.needToShowBoundingBox() && i < boundingBoxEdges.size(); ++i) {
            Point3D first3D  = boundingBox.get((int) boundingBoxEdges.get(i).getX());
            Point3D second3D = boundingBox.get((int) boundingBoxEdges.get(i).getY());
            boolean needToDraw;
            {
                boolean firstVisible = isVisible(first3D);
                boolean secondVisible = isVisible(second3D);

                if (!firstVisible && !secondVisible) {
                    needToDraw = false;
                } else if (firstVisible && secondVisible) {
                    needToDraw = true;
                } else {
                    needToDraw = true;
                    if (secondVisible) {
                        Point3D tmp = first3D;
                        first3D = second3D;
                        second3D = tmp;
                    }
                    double distanceFirst = distanceToPlane(first3D);
                    double distanceSecond = distanceToPlane(second3D);
                    double distance = distanceFirst + distanceSecond;

                    second3D = first3D.multiply(distanceSecond / distance).add(second3D.multiply(distanceFirst / distance));
                }
            }
            if (needToDraw) {
                Point2D first2d = project(first3D);
                Point2D second2d = project(second3D);
                g.drawLine((int)first2d.getX(), (int)first2d.getY(), (int)second2d.getX(), (int)second2d.getY());
            }
        }
    }

    private void clear() {
        g.setColor(Color.WHITE);
        g.clearRect(0, 0, getWidth(), getHeight());
        g.setColor(Color.BLACK);
    }

    private double signedDistanceToPlane(Point3D point3D) {
        return point3D.subtract(planeCenter).dotProduct(planeCenter.normalize());
    }

    private double distanceToPlane(Point3D point3D) {
        return Math.abs(signedDistanceToPlane(point3D));
    }

    private boolean isVisible(Point3D point3D) {
        return signedDistanceToPlane(point3D) * signedDistanceToPlane(eye) < 0;
    }

    private Point2D project(Point3D point3D) {
        double t = Math.toRadians(theta);
        double p = Math.toRadians(psi);

        double cosT = Math.cos(t);
        double sinT = Math.sin(t);
        double cosP = Math.cos(p);
        double sinP = Math.sin(p);

        double cosTcosP = cosT * cosP;
        double cosTsinP = cosT * sinP;
        double sinTcosP = sinT * cosP;
        double sinTsinP = sinT * sinP;

        double x0 = point3D.getX() - viewPoint.getX();
        double y0 = point3D.getY() - viewPoint.getY();
        double z0 = point3D.getZ() - viewPoint.getZ();

        double x1 = cosT * x0 + sinT * z0;
        double y1 = -sinTsinP * x0 + cosP * y0 + cosTsinP * z0;
        double z1 = cosTcosP * z0 - sinTcosP * x0 - sinP * y0;

        double x = x1 * planeToEye / (z1 + planeToEye + viewToPlane);
        double y = y1 * planeToEye / (z1 + planeToEye + viewToPlane);

        double width = getWidth();
        double height = getHeight();
        double scale = Math.min(width, height) / 4;

        return new Point2D (Math.round(width / 2 + scale * x), Math.round(height / 2 - scale * y));
    }

    private void setPixel(Point2D pixel) {
        if (pixel.getX() < 0 || pixel.getX() >= getWidth()
                || pixel.getY() < 0 || pixel.getY() >= getHeight())
            return;
        g.drawLine((int)pixel.getX(), (int)pixel.getY(), (int)pixel.getX(), (int)pixel.getY());
    }

}
