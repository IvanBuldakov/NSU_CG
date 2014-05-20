package ru.nsu.ccfit.buldakov.cg.tetragon;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MipMapGenerator {
    public static void main (String[] args) {
        try {
            BufferedImage img = ImageIO.read(ClassLoader.getSystemResourceAsStream("images/MIP_0.png"));
            if (img. getWidth() == 1024) {
                int LoD = 0;
                while (img.getWidth() > 1 && img.getHeight() > 1) {
                    BufferedImage level = new BufferedImage(img.getWidth() / 2, img.getHeight() / 2, BufferedImage.TYPE_INT_ARGB);
                    for (int y = 0; y < level.getHeight(); ++y) {
                        for (int x = 0; x < level.getWidth(); ++x) {
                            int colorLT = img.getRGB(2*x,   2*y  );
                            int colorRT = img.getRGB(2*x+1, 2*y  );
                            int colorLB = img.getRGB(2*x,   2*y+1);
                            int colorRB = img.getRGB(2*x+1, 2*y+1);
                            int r = ((colorLT >> 16 & 0xFF) + (colorRT >> 16 & 0xFF) + (colorLB >> 16 & 0xFF) + (colorRB >> 16 & 0xFF));
                            int g = ((colorLT >> 8  & 0xFF) + (colorRT >> 8  & 0xFF) + (colorLB >> 8  & 0xFF) + (colorRB >> 8  & 0xFF));
                            int b = ((colorLT       & 0xFF) + (colorRT       & 0xFF) + (colorLB       & 0xFF) + (colorRB       & 0xFF));
                            r /= 4;
                            g /= 4;
                            b /= 4;
                            int color = (0xFF << 24) | (r << 16) | (g << 8) | b;
                            level.setRGB(x, y, color);
                        }
                    }
                    File out = new File("res/images/MIP_" + (++LoD) + ".png");
                    out.delete();
                    out.createNewFile();
                    ImageIO.write(level, "png", out);
                    img = level;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
