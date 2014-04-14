package ru.nsu.ccfit.buldakov.cg.is.View;

import ru.nsu.ccfit.buldakov.cg.is.Canvas;
import ru.nsu.ccfit.buldakov.cg.is.Settings;

import javax.swing.*;

public class ControlPanel extends JPanel {

    public static JLabel statusBarX = new JLabel("x = ", SwingConstants.CENTER);
    public static JLabel statusBarY = new JLabel("y = ", SwingConstants.CENTER);
    public static JLabel statusBarZ = new JLabel("z = ", SwingConstants.CENTER);
    private final Canvas canvas;
    private Settings  settings       = Settings.getInstance();
    private JCheckBox grid           = new JCheckBox("Grid");
    private JCheckBox isolines       = new JCheckBox("Isolines");
    private JCheckBox interpolation  = new JCheckBox("Interpolation");
    private JCheckBox dithering      = new JCheckBox("Dithering");
    private JButton   settingsButton = new JButton("Settings");
    private JButton   clearButton    = new JButton("Clear");

    public ControlPanel(Canvas canvas) {
        this.canvas = canvas;
        GroupLayout controlPanelLayout = new GroupLayout(this);
        controlPanelLayout.setVerticalGroup(
                controlPanelLayout.createSequentialGroup()
                        .addComponent(statusBarX)
                        .addComponent(statusBarY)
                        .addComponent(statusBarZ)
                        .addComponent(grid)
                        .addComponent(isolines)
                        .addComponent(interpolation)
                        .addComponent(dithering)
                        .addComponent(settingsButton)
                        .addComponent(clearButton)
        );

        controlPanelLayout.setHorizontalGroup(
                controlPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(statusBarX, 100, 100, 100)
                        .addComponent(statusBarY, 100, 100, 100)
                        .addComponent(statusBarZ, 100, 100, 100)
                        .addComponent(grid)
                        .addComponent(isolines)
                        .addComponent(interpolation)
                        .addComponent(dithering)
                        .addComponent(settingsButton, 100, 100, 100)
                        .addComponent(clearButton, 100, 100, 100)
        );

        setLayout(controlPanelLayout);

        settingsButton.addActionListener(e -> new SettingsDialog((JFrame) getTopLevelAncestor()).setVisible(true));
        clearButton.addActionListener(e -> {
            this.canvas.clearUserIsolines();
            getTopLevelAncestor().repaint();
        });
        clearButton.setToolTipText("Removes user isolines");
        grid.addItemListener(e -> {
            if (grid.isSelected()) {
                settings.displayGrid();
            } else {
                settings.hideGrid();
            }
            getTopLevelAncestor().repaint();
        });
        isolines.addItemListener(e -> {
            if (isolines.isSelected()) {
                settings.displayIsolines();
            } else {
                settings.hideIsolines();
            }
            getTopLevelAncestor().repaint();
        });
        interpolation.addItemListener(e -> {
            if (interpolation.isSelected()) {
                settings.enableInterpolation();
                dithering.setSelected(false);
            } else {
                settings.disableInterpolation();
            }
            getTopLevelAncestor().repaint();
        });
        dithering.addItemListener(e -> {
            if (dithering.isSelected()) {
                settings.enableDithering();
                interpolation.setSelected(false);
            } else {
                settings.disableDithering();
            }
            getTopLevelAncestor().repaint();
        });

    }

}
