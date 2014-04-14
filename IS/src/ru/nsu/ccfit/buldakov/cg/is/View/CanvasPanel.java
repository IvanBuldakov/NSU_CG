package ru.nsu.ccfit.buldakov.cg.is.View;

import ru.nsu.ccfit.buldakov.cg.is.Canvas;
import ru.nsu.ccfit.buldakov.cg.is.Settings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class CanvasPanel extends JPanel {

    private final Canvas canvas;
    private Settings settings = Settings.getInstance();

    public CanvasPanel(Canvas canvas) {
        this.canvas = canvas;
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
                findInCanvas(e.getX(), e.getY());
            }
        });
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (settings.needToDisplayIsolines()) {
                    canvas.addNewValueAt(e.getX(), e.getY());
                    getTopLevelAncestor().repaint();
                }
            }
        });
    }

    private void findInCanvas(int x, int y) {
        double dxPix = 1.0 * settings.getWidth() / getWidth();
        double dyPix = 1.0 * settings.getHeight() / getHeight();
        double z = canvas.getFunction().valueAt(settings.getA() + x * dxPix, settings.getC() + y * dyPix);
        DecimalFormat df = new DecimalFormat("#.####");
        df.setRoundingMode(RoundingMode.HALF_UP);
        ControlPanel.statusBarX.setText("x = " + df.format(settings.getA() + x * dxPix));
        ControlPanel.statusBarY.setText("y = " + df.format(settings.getC() + y * dyPix));
        ControlPanel.statusBarZ.setText("z = " + df.format(z));
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (canvas.getWidth() != getWidth() || canvas.getHeight() != getHeight()) {
            canvas.resize(getWidth(), getHeight());
        }
        canvas.paint(g, 0, 0);
        if (settings.needToDisplayGrid()) {
            drawGrid(g);
        }
    }

    private void drawGrid(Graphics g) {
        double dx = 1.0 * getWidth() / (settings.getK() - 1);
        double dy = 1.0 * getHeight() / (settings.getM() - 1);
        g.setColor(Color.black);
        for (double i = 0; i <= getWidth(); i += dx) {
            g.drawLine((int) Math.round(i), 0, (int) Math.round(i), getHeight());
        }
        for (double i = 0; i <= getHeight(); i += dy) {
            g.drawLine(0, (int) Math.round(i), getWidth(), (int) Math.round(i));
        }
    }

}
