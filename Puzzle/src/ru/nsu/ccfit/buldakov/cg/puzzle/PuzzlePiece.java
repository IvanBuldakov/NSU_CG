package ru.nsu.ccfit.buldakov.cg.puzzle;

public interface PuzzlePiece {

    public abstract void move(int x, int y);
    public abstract void rotate(double angle);
    public abstract void draw(PuzzleGraphics g);

}
