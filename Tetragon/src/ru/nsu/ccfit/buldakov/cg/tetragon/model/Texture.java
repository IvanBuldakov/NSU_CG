package ru.nsu.ccfit.buldakov.cg.tetragon.model;

import javafx.geometry.Point2D;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class Texture {

    private static volatile Texture  instance = null;
    private ArrayList<BufferedImage> mip      = new ArrayList<>();
    private ArrayList<Double>        texSize  = new ArrayList<>();

    public static Texture load() {
        if (null == instance) {
            synchronized (Texture.class) {
                if (null == instance) {
                    instance = new Texture();
                }
            }
        }
        return instance;
    }

    private Texture() {
        try {
            for (int i = 0; i <= 10; ++i) {
                mip.add(ImageIO.read(ClassLoader.getSystemResourceAsStream("images/MIP_" + i + ".png")));
                texSize.add(1.0 / mip.get(i).getWidth());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getFiltratedPixel(Point2D UV, double pixSize) {
        int top = -1;
        int bottom = -1;
        for (int i = 0; i < texSize.size(); ++i) {
            if (pixSize < texSize.get(i)) {
                top = i;
                bottom = (i == 0) ? 0 : i-1;
                break;
            }
            if (pixSize == texSize.get(i)) {
                top = i;
                bottom = i;
                break;
            }
        }
        if (top == bottom) {
            BufferedImage tex = mip.get(top);
            return getFiltratedColor(tex, UV);
        }
        BufferedImage topTex = mip.get(top);
        BufferedImage bottomTex = mip.get(bottom);
        int topColor = getFiltratedColor(topTex, UV);
        int bottomColor = getFiltratedColor(bottomTex, UV);
        return interpolate(topColor, bottomColor, texSize.get(top), texSize.get(bottom), pixSize);
    }

    public int getPixel(Point2D UV, double pixSize) {
        int nearest = -1;
        for (int i = 0; i < texSize.size(); ++i) {
            if (pixSize < texSize.get(i)) {
                nearest = i;
                break;
            }
        }
        BufferedImage nearestTex = mip.get(nearest);
        return getFiltratedColor(nearestTex, UV);
    }

    private int interpolate(int color1, int color2, double value1, double value2, double value) {
        int r1 = color1 >> 16 & 0xFF;
        int g1 = color1 >> 8  & 0xFF;
        int b1 = color1 & 0xFF;
        int r2 = color2 >> 16 & 0xFF;
        int g2 = color2 >> 8 & 0xFF;
        int b2 = color2 & 0xFF;
        double factor = Math.abs(value2 - value1);
        double factor1 = Math.abs(value2 - value) / factor;
        double factor2 = Math.abs(value1 - value) / factor;
        int resR = (int) Math.round(r1 * factor1 + r2 * factor2) & 0xFF;
        int resG = (int) Math.round(g1 * factor1 + g2 * factor2) & 0xFF;
        int resB = (int) Math.round(b1 * factor1 + b2 * factor2) & 0xFF;
        return resR << 16 | resG << 8 | resB | 0xFF000000;
    }

    private int getFiltratedColor(BufferedImage texture, Point2D local) {
        double localX = local.getX()*texture.getWidth();
        double localY = local.getY()*texture.getHeight();
        double xRatio = localX - Math.floor(localX);
        double yRatio = localY - Math.floor(localY);
        int texturePixelX = (int) Math.floor(localX);
        int texturePixelY = (int) Math.floor(localY);
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

}
