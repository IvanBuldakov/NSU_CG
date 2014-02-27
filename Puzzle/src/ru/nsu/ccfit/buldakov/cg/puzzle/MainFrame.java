package ru.nsu.ccfit.buldakov.cg.puzzle;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame{

    public static final int puzzleSize = 256;
    public static final int gridSize = 4;

    PuzzleController puzzleController = new PuzzleController(puzzleSize, gridSize);

    public MainFrame() {
        setTitle("Puzzle");
        setLayout(new FlowLayout());
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        PuzzleView puzzleView = new PuzzleView(puzzleController);
        ControlPanel controlPanel = new ControlPanel(puzzleController);
        puzzleView.setPreferredSize(new Dimension(puzzleSize*2, puzzleSize*2));
        puzzleView.setSize(new Dimension(puzzleSize*2, puzzleSize*2));
        puzzleController.setGUI(puzzleView, controlPanel);
        add(puzzleView);

        add(controlPanel);
        pack();
        setLocationRelativeTo(null);
    }

}
