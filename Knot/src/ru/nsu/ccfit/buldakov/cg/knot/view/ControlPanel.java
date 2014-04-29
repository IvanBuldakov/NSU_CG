package ru.nsu.ccfit.buldakov.cg.knot.view;

import ru.nsu.ccfit.buldakov.cg.knot.Settings;
import ru.nsu.ccfit.buldakov.cg.knot.knots.TorusKnot;
import ru.nsu.ccfit.buldakov.cg.knot.knots.TrefoilKnot;
import ru.nsu.ccfit.buldakov.cg.knot.splines.BezierSplineFactory;
import ru.nsu.ccfit.buldakov.cg.knot.splines.CustomSplineFactory;

import javax.swing.*;
import java.awt.*;

public class ControlPanel extends JPanel {

    private final Settings settings;
    private static final int SPLINES_MIN  = 6;
    private static final int SPLINES_MAX  = 60;

    public ControlPanel(Settings settings) {

        this.settings = settings;

        JPanel knots = initKnots();
        JPanel splines = initSplines();
        JPanel options = initOptions();

        GroupLayout controlPanelLayout = new GroupLayout(this);
        controlPanelLayout.setVerticalGroup(
                controlPanelLayout.createSequentialGroup()
                        .addComponent(knots)
                        .addComponent(splines)
                        .addComponent(options)
        );

        controlPanelLayout.setHorizontalGroup(
                controlPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(knots, 150, 150, 150)
                        .addComponent(splines, 150, 150, 150)
                        .addComponent(options, 150, 150, 150)
        );

        setLayout(controlPanelLayout);

    }

    private JPanel initKnots() {
        JPanel knots = new JPanel();
        knots.setLayout(new GridLayout(2, 1));
        knots.setBorder(BorderFactory.createTitledBorder("Knots"));

        JRadioButton trefoilKnot = new JRadioButton("Trefoil Knot");
        trefoilKnot.setSelected(true);
        trefoilKnot.addItemListener(e -> {
            if (trefoilKnot.isEnabled()) {
                settings.setKnot(new TrefoilKnot());
                getTopLevelAncestor().repaint();
            }
        });
        knots.add(trefoilKnot);
        JRadioButton torusKnot = new JRadioButton("Torus Knot");
        torusKnot.addItemListener(e -> {
            if (torusKnot.isEnabled()) {
                settings.setKnot(new TorusKnot());
                getTopLevelAncestor().repaint();
            }
        });
        knots.add(torusKnot);

        ButtonGroup knotsGroup = new ButtonGroup();
        knotsGroup.add(trefoilKnot);
        knotsGroup.add(torusKnot);

        return knots;
    }

    private JPanel initSplines() {
        JPanel splines = new JPanel();
        splines.setLayout(new GridLayout(2, 1));
        splines.setBorder(BorderFactory.createTitledBorder("Splines"));

        JRadioButton bezierSpline = new JRadioButton("Bezier Spline");
        bezierSpline.setSelected(true);
        bezierSpline.addItemListener(e -> {
            if (bezierSpline.isSelected()) {
                settings.setSplineFactory(new BezierSplineFactory(3));
                getTopLevelAncestor().repaint();
            }
        });
        splines.add(bezierSpline);
        JRadioButton customSpline = new JRadioButton("Custom Spline");
        customSpline.addItemListener(e -> {
            if (customSpline.isSelected()) {
                settings.setSplineFactory(new CustomSplineFactory());
                getTopLevelAncestor().repaint();
            }
        });
        splines.add(customSpline);

        ButtonGroup splinesGroup = new ButtonGroup();
        splinesGroup.add(bezierSpline);
        splinesGroup.add(customSpline);

        return splines;
    }

    private JPanel initOptions() {
        JPanel options = new JPanel();
        options.setLayout(new GridLayout(3, 1));
        options.setBorder(BorderFactory.createTitledBorder("Options"));

        JLabel numOfSplines = new JLabel("Number of Splines: " + settings.getNumOfSplines());
        options.add(numOfSplines);

        JSlider splinesSlider = new JSlider(SPLINES_MIN, SPLINES_MAX, settings.getNumOfSplines());
        splinesSlider.addChangeListener(e -> {
            int n = ((JSlider) e.getSource()).getValue();
            settings.setNumOfSplines(n);
            numOfSplines.setText("Number of Splines: " + n);
            getTopLevelAncestor().repaint();
        });
        options.add(splinesSlider);

        JCheckBox showBoundingBox = new JCheckBox("Bounding box");
        showBoundingBox.addItemListener(e -> {
            if (showBoundingBox.isSelected()) {
                settings.showBoundingBox();
            } else {
                settings.hideBoundingBox();
            }
            getTopLevelAncestor().repaint();
        });
        options.add(showBoundingBox);

        return options;
    }
}
