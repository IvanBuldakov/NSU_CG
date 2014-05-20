package ru.nsu.ccfit.buldakov.cg.tetragon.view;

import ru.nsu.ccfit.buldakov.cg.tetragon.Controller;
import ru.nsu.ccfit.buldakov.cg.tetragon.model.Scene;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame{
    public MainFrame() {
        setTitle("Tetragon");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(640, 480));
        Scene scene = new Scene();
        Controller controller = new Controller(scene);
        scene.setController(controller);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        add(new ScenePanel(controller), gbc);
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(new ControlPanel(controller), gbc);
        pack();
        setLocationRelativeTo(null);
    }
}
