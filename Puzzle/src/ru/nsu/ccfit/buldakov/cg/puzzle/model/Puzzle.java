package ru.nsu.ccfit.buldakov.cg.puzzle.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Puzzle {

    private List<PuzzlePiece> pieces = new ArrayList<PuzzlePiece>();

    public void add(PuzzlePiece puzzlePiece) {
        pieces.add(puzzlePiece);
    }

    public List<PuzzlePiece> getPieces() {
        return pieces;
    }

    public void setEndPoints(int centerX, int centerY) {
        Random random = new Random(System.currentTimeMillis());
        for (PuzzlePiece piece: pieces) {
            int x = centerX + getRandomSign(random)*192 + getRandomSign(random)*random.nextInt()%24;
            int y = centerY + getRandomSign(random)*random.nextInt()%192 + getRandomSign(random)*random.nextInt()%24;

            if(getRandomSign(random) > 0) {
                int tmp = x;
                //noinspection SuspiciousNameCombination,SuspiciousNameCombination
                x = y;
                y = tmp;
            }
            piece.setEndPoint(x, y);
            piece.setEndAngle(random.nextInt() % 360);
        }
    }

    private int getRandomSign(Random random) {
        return (random.nextInt() % 2) == 0 ? 1 : -1;
    }

    public void reset() {
        for (PuzzlePiece piece : pieces) {
            piece.reset();
        }
    }

    public PuzzlePiece getPuzzlePiece(int x, int y) {
        PuzzlePiece pieceUnderCursor = null;
        for (PuzzlePiece piece : pieces) {
            if (piece.contains(x, y)) {
                pieceUnderCursor = piece;
            }
        }
        return pieceUnderCursor;
    }
}
