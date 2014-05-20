package ru.nsu.ccfit.buldakov.cg.tetragon.view;

import ru.nsu.ccfit.buldakov.cg.tetragon.Controller;

import javax.swing.*;

public class ControlPanel extends JPanel {

    private JButton   clearButton      = new JButton("Clear");
    private JCheckBox filter           = new JCheckBox("Filter");
    private JButton   addConvexButton  = new JButton("Add Convex");
    private JButton   addConcaveButton = new JButton("Add Concave");

    public ControlPanel(Controller controller) {

        GroupLayout controlPanelLayout = new GroupLayout(this);
        controlPanelLayout.setVerticalGroup(
                controlPanelLayout.createSequentialGroup()
                        .addComponent(addConvexButton)
                        .addComponent(addConcaveButton)
                        .addComponent(clearButton)
                        .addComponent(filter)
        );

        controlPanelLayout.setHorizontalGroup(
                controlPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(addConvexButton, 100, 100, 100)
                        .addComponent(addConcaveButton, 100, 100, 100)
                        .addComponent(clearButton, 100, 100, 100)
                        .addComponent(filter)
        );

        setLayout(controlPanelLayout);
        filter.addItemListener(e -> {
            if (filter.isSelected()) {
                controller.enableFiltration();
            } else {
                controller.disableFiltration();
            }
            getTopLevelAncestor().repaint();
        });
        addConvexButton.addActionListener(e -> {
            controller.addConvex();
            getTopLevelAncestor().repaint();
        });
        addConcaveButton.addActionListener(e -> {
            controller.addConcave();
            getTopLevelAncestor().repaint();
        });
        clearButton.addActionListener(e -> {
            controller.clearScene();
            getTopLevelAncestor().repaint();
        });
    }
}
