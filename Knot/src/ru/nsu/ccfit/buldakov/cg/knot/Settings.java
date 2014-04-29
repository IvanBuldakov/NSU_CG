package ru.nsu.ccfit.buldakov.cg.knot;

import ru.nsu.ccfit.buldakov.cg.knot.knots.Knot;
import ru.nsu.ccfit.buldakov.cg.knot.knots.TrefoilKnot;
import ru.nsu.ccfit.buldakov.cg.knot.splines.BezierSplineFactory;
import ru.nsu.ccfit.buldakov.cg.knot.splines.SplineFactory;

public class Settings {

    private Knot knot = new TrefoilKnot();
    private SplineFactory splineFactory = new BezierSplineFactory(3);
    private boolean showBoundingBox;
    private int numOfSplines = 60;

    public boolean needToShowBoundingBox() {
        return showBoundingBox;
    }

    public void showBoundingBox() {
        showBoundingBox = true;
    }

    public void hideBoundingBox() {
        showBoundingBox = false;
    }

    public void setNumOfSplines(int numOfSplines) {
        this.numOfSplines = numOfSplines;
    }

    public int getNumOfSplines() {
        return numOfSplines;
    }

    public Knot getKnot() {
        return knot;
    }

    public void setKnot(Knot knot) {
        this.knot = knot;
    }

    public SplineFactory getSplineFactory() {
        return splineFactory;
    }

    public void setSplineFactory(SplineFactory splineFactory) {
        this.splineFactory = splineFactory;
    }
}
