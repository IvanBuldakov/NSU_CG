package ru.nsu.ccfit.buldakov.cg.puzzle.view;

import ru.nsu.ccfit.buldakov.cg.puzzle.PuzzleController;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    public static JLabel info = new JLabel("Nothing under the cursor...");

    public static final int puzzleSize = 128;
    public static final int sceneSize  = 512;
    public static final int gridSize   = 4;

    PuzzleController puzzleController = new PuzzleController(puzzleSize, gridSize);

    public MainFrame() {
        setTitle("Puzzle");
        setMinimumSize(new Dimension(620, 620));

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        PuzzleView puzzleView = new PuzzleView(puzzleController);
        puzzleView.setPreferredSize(new Dimension(sceneSize, sceneSize));
        puzzleView.setMaximumSize(new Dimension(sceneSize, sceneSize));
        puzzleView.setSize(new Dimension(sceneSize, sceneSize));

        ControlPanel controlPanel = new ControlPanel(puzzleController);

        puzzleController.setGUI(puzzleView, controlPanel);

        gbc.anchor = GridBagConstraints.PAGE_START;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weighty = 1.0;
        add(info, gbc);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(puzzleView, gbc);
        gbc.anchor = GridBagConstraints.PAGE_END;
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weighty = 1.0;
        add(controlPanel, gbc);
        setLocationRelativeTo(null);
    }

}
