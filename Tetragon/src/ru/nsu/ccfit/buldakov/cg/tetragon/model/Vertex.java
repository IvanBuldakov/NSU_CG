package ru.nsu.ccfit.buldakov.cg.tetragon.model;

import javafx.geometry.Point2D;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Vertex {

    private Point2D coordinates;
    private static final int size = 4;

    public Vertex(Point2D coordinates) {
        this.coordinates = coordinates;
    }

    public double getX() {
        return coordinates.getX();
    }

    public double getY() {
        return coordinates.getY();
    }

    public void draw(BufferedImage canvas, Color color) {
        Graphics2D g = canvas.createGraphics();
        g.setColor(color);
        int startX = (int)Math.round(getX()*canvas.getWidth() - size / 2);
        int startY = (int)Math.round(getY()*canvas.getHeight() - size / 2);
        int endX = (int)Math.round(getX()*canvas.getWidth() + size / 2);
        int endY = (int)Math.round(getY()*canvas.getHeight() + size / 2);
        for (int y = startY; y < endY; ++y) {
            for (int x = startX; x < endX; ++x) {
                g.drawLine(x, y, x, y);
            }
        }
    }
}
