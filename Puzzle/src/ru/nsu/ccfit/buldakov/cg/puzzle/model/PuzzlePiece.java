package ru.nsu.ccfit.buldakov.cg.puzzle.model;

import ru.nsu.ccfit.buldakov.cg.puzzle.view.PuzzleGraphics;

public interface PuzzlePiece {

    public void setCenterPoint(double x, double y);

    public void setStartPoint(double x, double y);

    public void setEndPoint(double x, double y);

    public void setEndAngle(double angle);

    public void reset();

    public void translate(int t);

    public void draw(PuzzleGraphics g);

    public boolean contains(int x, int y);

    public int getBorderPixels();

    public int getOpaquePixels();

    public int getPixels();
}
