package ru.nsu.ccfit.buldakov.cg.tetragon.view;

import javafx.geometry.Point2D;
import ru.nsu.ccfit.buldakov.cg.tetragon.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ScenePanel extends JPanel{

    private Controller controller;

    public ScenePanel(Controller controller) {
        this.controller = controller;
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                Point2D vertex = new Point2D(e.getX()*1.0/getWidth(), e.getY()*1.0/getHeight());
                controller.addNewPoint(vertex);
                repaint();
            }
        });
    }

    @Override
    public void paint(Graphics g) {
        controller.getScene().setSize(getWidth(), getHeight());
        controller.getScene().draw(g);
    }

}
