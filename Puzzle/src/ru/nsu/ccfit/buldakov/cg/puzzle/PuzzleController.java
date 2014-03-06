package ru.nsu.ccfit.buldakov.cg.puzzle;

import ru.nsu.ccfit.buldakov.cg.puzzle.model.Puzzle;
import ru.nsu.ccfit.buldakov.cg.puzzle.model.PuzzlePiece;
import ru.nsu.ccfit.buldakov.cg.puzzle.model.TrianglePiece;
import ru.nsu.ccfit.buldakov.cg.puzzle.view.ControlPanel;
import ru.nsu.ccfit.buldakov.cg.puzzle.view.MainFrame;
import ru.nsu.ccfit.buldakov.cg.puzzle.view.PuzzleGraphics;
import ru.nsu.ccfit.buldakov.cg.puzzle.view.PuzzleView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

public class PuzzleController implements ActionListener {

    private Puzzle         puzzle         = new Puzzle();
    private PuzzleGraphics puzzleGraphics = PuzzleGraphics.getInstance();
    private PuzzleView   puzzleView;
    private ControlPanel controlPanel;

    private Timer timer = new Timer(1000 / 24, this);
    private int   step  = 0;

    private int puzzleSize;
    private int gridSize;
    private int startX = 0;
    private int startY = 0;

    public PuzzleController(int puzzleSize, int gridSize) {
        this.puzzleSize = puzzleSize;
        this.gridSize = gridSize;
        try {
            puzzleGraphics.loadTexture("images/puzzle.png");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setGUI(PuzzleView puzzleView, ControlPanel controlPanel) {
        this.controlPanel = controlPanel;
        this.puzzleView = puzzleView;
        startX = (this.puzzleView.getWidth() - this.puzzleSize) / 2;
        startY = (this.puzzleView.getHeight() - this.puzzleSize) / 2;
        triangulate(gridSize);
        puzzle.setEndPoints(this.puzzleView.getWidth() / 2, this.puzzleView.getHeight() / 2);
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

        centerX = startX + legLength * centerX;
        centerY = startY + legLength * centerY;

        double u = 1.0 * gridX / gridSize + centerCoefficient / 12;
        double v = 1.0 * gridY / gridSize + centerCoefficient / 12;

        TrianglePiece triangle = new TrianglePiece(centerX, centerY, u, v, legLength, upper);
        puzzle.add(triangle);
    }

    public void nextStep(int step) {
        this.step = step % 360;
        System.out.println(this.step);
        for (PuzzlePiece piece : puzzle.getPieces()) {
            piece.translate(step);
        }
        puzzleView.repaint();
    }

    public void startAnimation() {
        timer.start();
        ++step;
        nextStep(step);
        controlPanel.moveSlider(step);
    }

    public void stopAnimation() {
        timer.stop();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        startAnimation();
    }

    public void enableBlending() {
        puzzleGraphics.enableBlending();
        puzzleView.repaint();
    }

    public void enableFiltration() {
        puzzleGraphics.enableFiltration();
        puzzleView.repaint();
    }

    public void disableBlending() {
        puzzleGraphics.disableBlending();
        puzzleView.repaint();
    }

    public void disableFiltration() {
        puzzleGraphics.disableFiltration();
        puzzleView.repaint();
    }

    public void reset() {
        puzzle.setEndPoints(puzzleView.getWidth() / 2, puzzleView.getHeight() / 2);
        puzzle.reset();
        step = 0;
        nextStep(step);
        puzzleView.repaint();
    }

    public void findPuzzlePieceUnderCursor(int x, int y) {
        PuzzlePiece pieceUnderCursor = puzzle.getPuzzlePiece(x, y);
        if (null != pieceUnderCursor)
            MainFrame.info.setText("Total pixels: " + pieceUnderCursor.getPixels() +
                                           ", border pixels: " + pieceUnderCursor.getBorderPixels() +
                                           ", opaque pixels: " + pieceUnderCursor.getOpaquePixels());
        else
            MainFrame.info.setText("Nothing under the cursor...");
    }
}
