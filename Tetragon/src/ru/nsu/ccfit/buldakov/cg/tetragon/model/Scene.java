package ru.nsu.ccfit.buldakov.cg.tetragon.model;

import javafx.geometry.Point2D;
import ru.nsu.ccfit.buldakov.cg.tetragon.Controller;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Scene {

    private Controller controller;
    private BufferedImage       canvas      = null;
    private boolean             filtration  = false;
    private boolean             isResized   = false;
    private boolean             newVertex   = false;
    private boolean             newTetragon = false;
    private ArrayList<Tetragon> tetragons   = new ArrayList<>();
    private ArrayList<Vertex>   vertices    = new ArrayList<>();

    public void draw(Graphics g) {
        g.setColor(new Color(238, 238, 238));
        g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        if (isResized || filtration != controller.filtrationEnabled()) {
            for (Tetragon tetragon : tetragons) {
                tetragon.draw(canvas, controller.filtrationEnabled());
            }
            for (Vertex vertex : vertices) {
                vertex.draw(canvas, Color.BLACK);
            }
            isResized = false;
            filtration = controller.filtrationEnabled();
        } else if (newTetragon) {
            tetragons.get(tetragons.size()-1).draw(canvas, controller.filtrationEnabled());
            newTetragon = false;
        } else if (newVertex) {
            vertices.get(vertices.size()-1).draw(canvas, Color.BLACK);
            newVertex = false;
        }
        g.drawImage(canvas, 0, 0, null);
    }

    public void clear() {
        tetragons.clear();
        vertices.clear();
        canvas = new BufferedImage(canvas.getWidth(), canvas.getHeight(), BufferedImage.TYPE_INT_ARGB);
    }

    public void setSize(int width, int height) {
        if (canvas == null || canvas.getWidth() != width || canvas.getHeight() != height) {
            canvas = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            isResized = true;
        }
    }

    public int getWidth() {
        return canvas.getWidth();
    }

    public int getHeight() {
        return canvas.getHeight();
    }

    public void addVertex(Vertex vertex) {
        vertices.add(vertex);
        newVertex = true;
        if (vertices.size() == 4) {
            newVertex = false;
            Tetragon.Type type = hasInner() ? Tetragon.Type.CONCAVE : Tetragon.Type.CONVEX;
            addTetragon(new Tetragon(vertices, type));
            vertices.clear();
        }
    }

    private boolean hasInner() {
        assert vertices.size() == 4;
        for (int i = 0; i < 4; ++i) {
            Vertex p = vertices.get(i);
            if (area(vertices.get((i+1)%4), vertices.get((i+2)%4), vertices.get((i+3)%4)) ==
                    area(vertices.get((i+1)%4), vertices.get((i+2)%4), p) +
                            area(vertices.get((i+2)%4), vertices.get((i+3)%4), p) +
                            area(vertices.get((i+3)%4), vertices.get((i+1)%4), p)) {
                return true;
            }
        }
        return false;
    }

    public void addTetragon(Tetragon tetragon) {
        tetragons.add(tetragon);
        newTetragon = true;
    }

    public void addTetragon(Point2D a, Point2D b, Point2D c, Point2D d, Tetragon.Type type) {
        addTetragon(new Tetragon(a, b, c, d, type));
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    private double area(Vertex a, Vertex b, Vertex c) {
        return Math.abs((a.getX() - c.getX()) * (b.getY() - c.getY()) + (b.getX() - c.getX()) * (c.getY() - a.getY()));
    }
}
