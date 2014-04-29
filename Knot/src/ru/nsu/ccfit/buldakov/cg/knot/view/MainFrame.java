package ru.nsu.ccfit.buldakov.cg.knot.view;

import ru.nsu.ccfit.buldakov.cg.knot.Settings;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame{

    public MainFrame() {
        setTitle("Knot");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(640, 480));
        Settings settings = new Settings();
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        add(new Scene(settings), gbc);
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(new ControlPanel(settings), gbc);
        pack();
        setLocationRelativeTo(null);
    }

}
