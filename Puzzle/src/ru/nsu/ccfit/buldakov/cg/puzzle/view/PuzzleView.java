package ru.nsu.ccfit.buldakov.cg.puzzle.view;

import ru.nsu.ccfit.buldakov.cg.puzzle.PuzzleController;
import ru.nsu.ccfit.buldakov.cg.puzzle.model.PuzzlePiece;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PuzzleView extends JPanel {

    private PuzzleController puzzleController;
    private PuzzleGraphics graphics = PuzzleGraphics.getInstance();

    public PuzzleView(final PuzzleController puzzleController) {
        this.puzzleController = puzzleController;
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
                puzzleController.findPuzzlePieceUnderCursor(e.getX(), e.getY());
            }
        });
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        drawPuzzle(g);
    }

    private void drawPuzzle(Graphics g) {
        for (PuzzlePiece puzzlePiece : puzzleController.getPuzzlePieces()) {
            puzzlePiece.draw(graphics);
        }
        graphics.paint(g);
    }

}
