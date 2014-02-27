package ru.nsu.ccfit.buldakov.cg.puzzle;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

public class PuzzleController implements ActionListener {

    private Puzzle puzzle = new Puzzle();
    private PuzzleView   puzzleView;
    private ControlPanel controlPanel;

    Timer timer = new Timer(1000 / 24, this); // TRUE CINEMA 24p
    int   step  = 0;

    private int puzzleSize;
    private int gridSize;
    private int startX = 0;
    private int startY = 0;

    public PuzzleController(int puzzleSize, int gridSize) {
        this.puzzleSize = puzzleSize;
        this.gridSize = gridSize;
        try {
            PuzzleGraphics.getInstance().loadTexture("images/puzzle.png");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setGUI(PuzzleView puzzleView, ControlPanel controlPanel) {
        this.controlPanel = controlPanel;
        this.puzzleView = puzzleView;
        startX = (this.puzzleView.getWidth() - this.puzzleSize)/2;
        startY = (this.puzzleView.getHeight() - this.puzzleSize)/2;
        triangulate(gridSize);
    }

    public List<PuzzlePiece> getPuzzlePieces() {
        return puzzle.getPieces();
    }

    public void triangulate(int gridSize) {
        int legLength = puzzleSize / gridSize;
        for (int i = 0; i < gridSize; ++i) {
            for (int j = 0; j < gridSize; ++j) {
                addTriangle(j, i, legLength, true);
                addTriangle(j, i, legLength, false);
            }
        }
    }

    private void addTriangle(int gridX, int gridY, double legLength, boolean upper) {
        double centerCoefficient = 1;
        if (!upper) {
            centerCoefficient = 2;
        }

        double centerX = gridX + centerCoefficient / 3;
        double centerY = gridY + centerCoefficient / 3;

        centerX = startX + legLength*centerX;
        centerY = startY + legLength*centerY;

        double u = 1.0 * gridX / gridSize + centerCoefficient / 12;
        double v = 1.0 * gridY / gridSize + centerCoefficient / 12;

        puzzle.add(new TrianglePiece(centerX, centerY, u, v, legLength, upper));
    }

    public void nextStep(double angle) {
        for (PuzzlePiece piece : puzzle.getPieces()) {
            piece.rotate(angle);
        }
        puzzleView.repaint();
    }

    public void startAnimation() {

        timer.start();
        step = (step + 1) % 360;
        controlPanel.moveSlider(step);
        nextStep(step);

    }

    public void stopAnimation() {
        timer.stop();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        startAnimation();
    }

    public void enableBlending() {

    }
    public void enableFiltration() {

    }
    public void disableBlending() {

    }
    public void disableFiltration() {

    }

}
