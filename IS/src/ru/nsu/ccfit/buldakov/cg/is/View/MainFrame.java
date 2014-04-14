package ru.nsu.ccfit.buldakov.cg.is.View;

import ru.nsu.ccfit.buldakov.cg.is.Canvas;
import ru.nsu.ccfit.buldakov.cg.is.Functions.LegendFunction;
import ru.nsu.ccfit.buldakov.cg.is.Functions.MainFunction;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private boolean first = true;

    public MainFrame() {

        setTitle("IS");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(640, 480));

        Canvas canvas = new Canvas(new MainFunction());
        Canvas legend = new Canvas(new LegendFunction());

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.05;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.VERTICAL;
        add(new ControlPanel(canvas), gbc);

        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.weightx = 0.05;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        add(new LegendPanel(legend), gbc);

        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 8;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        add(new CanvasPanel(canvas), gbc);

        pack();
        setLocationRelativeTo(null);

    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (first) {
            first = false;
            new SettingsDialog(this).setVisible(true);
        }
    }

}
