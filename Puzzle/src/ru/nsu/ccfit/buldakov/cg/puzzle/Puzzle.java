package ru.nsu.ccfit.buldakov.cg.puzzle;

import java.util.ArrayList;
import java.util.List;

public class Puzzle {

    List<PuzzlePiece> pieces = new ArrayList<PuzzlePiece>();

    public void add(PuzzlePiece puzzlePiece) {
        pieces.add(puzzlePiece);
    }

    public List<PuzzlePiece> getPieces() {
        return pieces;
    }

}
