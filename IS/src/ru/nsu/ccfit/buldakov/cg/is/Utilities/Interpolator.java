package ru.nsu.ccfit.buldakov.cg.is.Utilities;

import java.awt.*;

public final class Interpolator {

    public static Color interpolate(double value, double fromValue, double toValue, Color fromColor, Color toColor) {
        int red = (int) Math.floor(fromColor.getRed() * Math.abs(toValue - value) / Math.abs(toValue - fromValue) +
                                           toColor.getRed() * Math.abs(value - fromValue) / Math.abs(toValue - fromValue));
        int green = (int) Math.floor(fromColor.getGreen() * Math.abs(toValue - value) / Math.abs(toValue - fromValue) +
                                             toColor.getGreen() * Math.abs(value - fromValue) / Math.abs(toValue - fromValue));
        int blue = (int) Math.floor(fromColor.getBlue() * Math.abs(toValue - value) / Math.abs(toValue - fromValue) +
                                            toColor.getBlue() * Math.abs(value - fromValue) / Math.abs(toValue - fromValue));
        if (red   > 255) red   = 255;
        if (green > 255) green = 255;
        if (blue  > 255) blue  = 255;
        return new Color(red, green, blue);
    }

}
