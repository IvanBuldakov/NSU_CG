package ru.nsu.ccfit.buldakov.cg.puzzle.view;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class PuzzleGraphics {

    private static volatile PuzzleGraphics instance;

    private BufferedImage texture;
    private BufferedImage scene;
    private Color backgroundColor = new Color(238, 238, 238);

    private boolean bilinearFiltrationEnabled;
    private boolean alphaBlendingEnabled;

    private PuzzleGraphics() {
    }

    public static PuzzleGraphics getInstance() {
        if (null == instance) {
            synchronized (PuzzleGraphics.class) {
                if (null == instance) {
                    instance = new PuzzleGraphics();
                }
            }
        }
        return instance;
    }

    public void loadTexture(String path) throws IOException {
        texture = ImageIO.read(ClassLoader.getSystemResourceAsStream(path));
        clearScene();
    }

    public int getSourceColor(Point2D.Double local, double u, double v) {
        int texturePixelX = (int) Math.round(local.x + u * texture.getWidth());
        if (texturePixelX >= texture.getWidth()) {
            texturePixelX = texture.getWidth() - 1;
        }
        if (texturePixelX < 0) {
            texturePixelX = 0;
        }

        int texturePixelY = (int) Math.round(local.y + v * texture.getHeight());
        if (texturePixelY >= texture.getHeight()) {
            texturePixelY = texture.getHeight() - 1;
        }
        if (texturePixelY < 0) {
            texturePixelY = 0;
        }

        int result = 0;

        try {
            result = texture.getRGB(texturePixelX, texturePixelY);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Index out of bounds: (" + texturePixelX + ", " + texturePixelY + ")");
        }
        return result;
    }

    public int getFiltratedColor(Point2D.Double local, double u, double v) {
        double textureCoordX = local.x + u * texture.getWidth();
        double textureCoordY = local.y + v * texture.getHeight();

        double xRatio = textureCoordX - Math.floor(textureCoordX);
        double yRatio = textureCoordY - Math.floor(textureCoordY);

        int texturePixelX = (int) Math.floor(textureCoordX);
        int texturePixelY = (int) Math.floor(textureCoordY);

        if (texturePixelX >= texture.getWidth()) {
            texturePixelX = texture.getWidth() - 1;
        }
        if (texturePixelY >= texture.getHeight()) {
            texturePixelY = texture.getHeight() - 1;
        }
        if (texturePixelX <= 0) {
            texturePixelX = 0;
        }
        if (texturePixelY <= 0) {
            texturePixelY = 0;
        }

        int texturePixelXInc = (texturePixelX < texture.getWidth() - 1) ? texturePixelX + 1 : texturePixelX;
        int texturePixelYInc = (texturePixelY < texture.getHeight() - 1) ? texturePixelY + 1 : texturePixelY;

        int result = 0;
        int neighbours[] = new int[4];
        neighbours[0] = texture.getRGB(texturePixelX, texturePixelY);
        neighbours[1] = texture.getRGB(texturePixelXInc, texturePixelY);
        neighbours[2] = texture.getRGB(texturePixelX, texturePixelYInc);
        neighbours[3] = texture.getRGB(texturePixelXInc, texturePixelYInc);

        double tmp;
        for (int i = 0; i < 4; i++) {
            tmp = ((neighbours[0] >> i * 8) & 0xFF) * (1 - xRatio) * (1 - yRatio)
                    + ((neighbours[1] >> i * 8) & 0xFF) * xRatio * (1 - yRatio)
                    + ((neighbours[2] >> i * 8) & 0xFF) * (1 - xRatio) * yRatio
                    + ((neighbours[3] >> i * 8) & 0xFF) * xRatio * yRatio;
            result = result | ((int) tmp << i * 8);
        }

        return result;
    }

    public int getAlphaColor(int x, int y, int foregroundColor) {
        int backgroundColor = scene.getRGB(x, y);
        double alpha = ((foregroundColor >> 24) & 0xFF) / 255.0;
        int resultColor = (int)(alpha * 255) << 24;
        for (int i = 0; i < 3; ++i) {
            int backgroundColorComponent = (backgroundColor >> i * 8) & 0xFF;
            int foregroundColorComponent = (foregroundColor >> i * 8) & 0xFF;
            double resultColorComponent = backgroundColorComponent
                    + (foregroundColorComponent - backgroundColorComponent) * alpha;
            resultColor |= (((int) resultColorComponent) << i * 8);
        }
        return resultColor;
    }

    public void setPixel(int x, int y, int color) {
        scene.setRGB(x, y, color);
    }

    private int sign(int num) {
        return (num > 0) ? 1 : (num < 0) ? -1 : 0;
    }

    public int drawBresenhamLine(int xStart, int yStart, int xEnd, int yEnd, int color) {

        int drawnPixels = 0, x, y, lengthX, lengthY, incX, incY, pdx, pdy, shortest, longest, err;

        x = xEnd - xStart;
        y = yEnd - yStart;
        incX = sign(x);
        incY = sign(y);
        lengthX = Math.abs(x);
        lengthY = Math.abs(y);

        if (lengthX > lengthY) {
            pdx = incX;
            pdy = 0;
            shortest = lengthY;
            longest = lengthX;
        } else {
            pdx = 0;
            pdy = incY;
            shortest = lengthX;
            longest = lengthY;
        }

        x = xStart;
        y = yStart;
        err = longest / 2;
        setPixel(x, y, color);
        ++drawnPixels;

        for (int t = 0; t < longest; t++) {
            err -= shortest;
            if (err < 0) {
                err += longest;
                x += incX;
                y += incY;
            } else {
                x += pdx;
                y += pdy;
            }
            setPixel(x, y, color);
            ++drawnPixels;
        }
        return drawnPixels;
    }

    public boolean isBilinearFiltrationEnabled() {
        return bilinearFiltrationEnabled;
    }

    public boolean isAlphaBlendingEnabled() {
        return alphaBlendingEnabled;
    }

    public void enableBlending() {
        alphaBlendingEnabled = true;
    }

    public void enableFiltration() {
        bilinearFiltrationEnabled = true;
    }

    public void disableBlending() {
        alphaBlendingEnabled = false;
    }

    public void disableFiltration() {
        bilinearFiltrationEnabled = false;
    }

    private void clearScene() {
        scene = new BufferedImage(texture.getWidth() * 4, texture.getHeight() * 4, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < scene.getHeight(); ++i) {
            for (int j = 0; j < scene.getWidth(); ++j) {
                scene.setRGB(j, i, backgroundColor.getRGB());
            }
        }
    }

    public void paint(Graphics g) {
        g.drawImage(scene, 0, 0, null);
        clearScene();
    }

}
