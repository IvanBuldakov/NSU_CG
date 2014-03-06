package ru.nsu.ccfit.buldakov.cg.puzzle.view;

import ru.nsu.ccfit.buldakov.cg.puzzle.PuzzleController;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class ControlPanel extends JPanel {

    private static final int ROTATION_MIN  = 0;
    private static final int ROTATION_MAX  = 360;
    private static final int ROTATION_INIT = 0;

    private JCheckBox filterCheckBox  = new JCheckBox("Filter");
    private JCheckBox blendCheckBox   = new JCheckBox("Blend");
    private JButton   initButton      = new JButton("Init");
    private JButton   startButton     = new JButton("Start");
    private JButton   stopButton      = new JButton("Stop");
    private JSlider   animationSlider = new JSlider(ROTATION_MIN, ROTATION_MAX, ROTATION_INIT);

    public ControlPanel(final PuzzleController puzzleController) {
        setBorder(javax.swing.BorderFactory.createEtchedBorder());

        animationSlider.setMajorTickSpacing(ROTATION_MAX / 2);
        animationSlider.setMinorTickSpacing(10);
        animationSlider.setPaintLabels(true);
        animationSlider.setPaintTicks(true);
        animationSlider.setAutoscrolls(true);

        GroupLayout controlPanelLayout = new GroupLayout(this);

        controlPanelLayout.setHorizontalGroup(
                controlPanelLayout.createSequentialGroup()
                        .addContainerGap(10, 20)
                        .addComponent(filterCheckBox)
                        .addComponent(blendCheckBox)
                        .addContainerGap(10, 20)
                        .addComponent(startButton, 65, 75, 85)
                        .addComponent(stopButton, 65, 75, 85)
                        .addComponent(initButton, 65, 75, 85)
                        .addContainerGap(10, 20)
                        .addComponent(animationSlider)
        );

        controlPanelLayout.setVerticalGroup(
                controlPanelLayout.createParallelGroup(Alignment.CENTER)
                        .addComponent(filterCheckBox)
                        .addComponent(blendCheckBox)
                        .addComponent(startButton, 20, 30, 40)
                        .addComponent(stopButton, 20, 30, 40)
                        .addComponent(initButton, 20, 30, 40)
                        .addComponent(animationSlider)
        );

        setLayout(controlPanelLayout);

        filterCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (filterCheckBox.isSelected()) {
                    puzzleController.enableFiltration();
                } else {
                    puzzleController.disableFiltration();
                }
            }
        });

        blendCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (blendCheckBox.isSelected()) {
                    puzzleController.enableBlending();
                } else {
                    puzzleController.disableBlending();
                }
            }
        });

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                puzzleController.startAnimation();
            }
        });
        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                puzzleController.stopAnimation();
            }
        });
        initButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                puzzleController.stopAnimation();
                puzzleController.reset();
                moveSlider(0);
            }
        });
        animationSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int t = ((JSlider) e.getSource()).getValue();
                puzzleController.nextStep(t);
            }
        });
    }

    public void moveSlider(int step) {
        animationSlider.setValue(step);
    }
}
