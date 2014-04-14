package ru.nsu.ccfit.buldakov.cg.is.Utilities;

import java.awt.*;
import java.awt.image.BufferedImage;

public final class Dither {

    public static BufferedImage FloydSteinbergDithering(BufferedImage image, Color[] colors) {
        MathColor[] palette = new MathColor[colors.length];
        for (int i = 0; i < colors.length; ++i) {
            palette[i] = new MathColor(colors[i]);
        }
        int w = image.getWidth();
        int h = image.getHeight();

        MathColor[][] colorMap = new MathColor[h][w];

        for (int y = 0; y < h; ++y) {
            for (int x = 0; x < w; ++x) {
                colorMap[y][x] = new MathColor(image.getRGB(x, y));
            }
        }

        for (int y = 0; y < h; ++y) {
            for (int x = 0; x < w; ++x) {

                MathColor oldColor = colorMap[y][x];
                MathColor newColor = oldColor.findClosestPaletteColor(palette);
                image.setRGB(x, y, newColor.toRGB());

                MathColor err = oldColor.sub(newColor);

                if (x + 1 < w) {
                    colorMap[y][x + 1] = colorMap[y][x + 1].add(err.mul(7. / 16));
                }
                if (x > 0 && y + 1 < h) {
                    colorMap[y + 1][x - 1] = colorMap[y + 1][x - 1].add(err.mul(3. / 16));
                }
                if (y + 1 < h) {
                    colorMap[y + 1][x] = colorMap[y + 1][x].add(err.mul(5. / 16));
                }
                if (x + 1 < w && y + 1 < h) {
                    colorMap[y + 1][x + 1] = colorMap[y + 1][x + 1].add(err.mul(1. / 16));
                }
            }
        }
        return image;
    }

}
