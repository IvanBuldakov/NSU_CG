package ru.nsu.ccfit.buldakov.cg.puzzle;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ControlPanel extends JPanel {

    private PuzzleController puzzleController;

    JButton start = new JButton("Start");
    JButton stop  = new JButton("Stop");

    JCheckBox blendCheckBox;
    JSlider   animationSlider;
    JTextArea text;

    int minStep     = 0;
    int currentStep = minStep;
    int maxStep     = 360;

    public ControlPanel(final PuzzleController puzzleController) {
        this.puzzleController = puzzleController;
        setPreferredSize(new Dimension(400, 200));
        setLayout(new FlowLayout());
        add(start);
        start.addActionListener(this.puzzleController);
        add(stop);
        stop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                puzzleController.stopAnimation();
            }
        });
        stop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                puzzleController.stopAnimation();
            }
        });
        //blendCheckBox = new JCheckBox("Blend", false);
        //add(blendCheckBox);
        text = new JTextArea();

        animationSlider = new JSlider(minStep, maxStep, currentStep);
        animationSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                puzzleController.nextStep(animationSlider.getValue());
                text.setText(String.valueOf(animationSlider.getValue()));
            }
        });
        add(animationSlider);
        add(text);
    }

    public void moveSlider(int value) {
        //currentStep = (currentStep + 1) % maxStep;
        currentStep = value;
        animationSlider.setValue(currentStep);
    }

}
