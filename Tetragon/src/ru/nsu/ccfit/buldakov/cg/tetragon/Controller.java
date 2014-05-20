package ru.nsu.ccfit.buldakov.cg.tetragon;

import javafx.geometry.Point2D;
import ru.nsu.ccfit.buldakov.cg.tetragon.model.Scene;
import ru.nsu.ccfit.buldakov.cg.tetragon.model.Tetragon;
import ru.nsu.ccfit.buldakov.cg.tetragon.model.Texture;
import ru.nsu.ccfit.buldakov.cg.tetragon.model.Vertex;

import java.util.Random;

public class Controller {

    private final Scene scene;
    private Random random = new Random();
    private boolean filtration;

    public Controller(Scene scene) {
        this.scene = scene;
        random.setSeed(System.currentTimeMillis());
    }

    public void addNewPoint(Point2D point2D) {
        scene.addVertex(new Vertex(point2D));
    }

    public Scene getScene() {
        return scene;
    }

    public void clearScene() {
        scene.clear();
    }

    public void addConvex() {
        Point2D A = new Point2D(Math.abs(1.0*(random.nextInt() % scene.getWidth())/scene.getWidth()), Math.abs(1.0*(random.nextInt() % scene.getHeight())/scene.getHeight()));
        Point2D C = new Point2D(Math.abs(1.0*(random.nextInt() % scene.getWidth())/scene.getWidth()), Math.abs(1.0*(random.nextInt() % scene.getHeight())/scene.getHeight()));
        Point2D AC = C.subtract(A);
        Point2D B = A.add(AC.multiply(random.nextDouble())).add(new Point2D(-AC.getY(), AC.getX()).multiply(random.nextDouble()));
        Point2D D = A.add(AC.multiply(random.nextDouble())).add(new Point2D(AC.getY(), -AC.getX()).multiply(random.nextDouble()));
        B = new Point2D(B.getX() < 0 ? 0 : B.getX() > 1 ? 1 : B.getX(), B.getY() < 0 ? 0 : B.getY() > 1 ? 1 : B.getY());
        D = new Point2D(D.getX() < 0 ? 0 : D.getX() > 1 ? 1 : D.getX(), D.getY() < 0 ? 0 : D.getY() > 1 ? 1 : D.getY());
        scene.addTetragon(A, B, C, D, Tetragon.Type.CONVEX);
    }

    public void addConcave() {
        Point2D A = new Point2D(Math.abs(1.0*(random.nextInt() % scene.getWidth())/scene.getWidth()), Math.abs(1.0*(random.nextInt() % scene.getHeight())/scene.getHeight()));
        Point2D B = new Point2D(Math.abs(1.0*(random.nextInt() % scene.getWidth())/scene.getWidth()), Math.abs(1.0*(random.nextInt() % scene.getHeight())/scene.getHeight()));
        Point2D C = new Point2D(Math.abs(1.0*(random.nextInt() % scene.getWidth())/scene.getWidth()), Math.abs(1.0*(random.nextInt() % scene.getHeight())/scene.getHeight()));
        Point2D AC = C.subtract(A);
        double factor = random.nextDouble();
        Point2D PseudoD = A.add(AC.multiply(0.2 * factor + 0.4));
        Point2D PseudoDB = B.subtract(PseudoD);
        Point2D D = PseudoD.add(PseudoDB.multiply(0.2*factor + 0.2));
        scene.addTetragon(A, B, C, D, Tetragon.Type.CONCAVE);
    }

    public void enableFiltration() {
        filtration = true;
    }

    public void disableFiltration() {
        filtration = false;
    }

    public boolean filtrationEnabled() {
        return filtration;
    }
}
