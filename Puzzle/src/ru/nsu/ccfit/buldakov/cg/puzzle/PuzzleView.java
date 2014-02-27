package ru.nsu.ccfit.buldakov.cg.puzzle;

import javax.swing.*;
import java.awt.*;

public class PuzzleView extends JPanel {

    private PuzzleController puzzleController;
    private PuzzleGraphics graphics = PuzzleGraphics.getInstance();

    public PuzzleView(PuzzleController puzzleController) {
        this.puzzleController = puzzleController;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        drawPuzzle(g);
    }

    private void drawPuzzle(Graphics g) {
        for (PuzzlePiece puzzlePiece: puzzleController.getPuzzlePieces()) {
            puzzlePiece.draw(graphics);
        }
        g.drawImage(graphics.getScene(), 0, 0, null);
    }

}
