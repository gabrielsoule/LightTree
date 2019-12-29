package com.gabrielsoule.lighttree;

public class ColorUtil {
    public static int getRed(int color) {
        return (color >> 16) & 0xFF;
    }

    public static int getGreen(int color) {
        return (color >> 8) & 0xFF;
    }

    public static int getBlue(int color) {
        return color & 0xFF;
    }

    public static int getAlpha(int color) {
        return (color >> 24) & 0xFF;
    }

    public static int setRed(int color, int red) {
        if(red < 0 || red > 255) {
            throw new IllegalArgumentException("Cannot set a color's red value to " + red + " [0-255]");
        }
        return getAlpha(color) << 24 |
                red << 16 |
                getGreen(color) << 8 |
                getBlue(color);

    }

    public static int setGreen(int color, int green) {
        if(green < 0 || green > 255) {
            throw new IllegalArgumentException("Cannot set a color's green value to " + green + " [0-255]");
        }
        return getAlpha(color) << 24 |
                getRed(color) << 16 |
                green << 8 |
                getBlue(color);

    }

    public static int setBlue(int color, int blue) {
        if(blue < 0 || blue > 255) {
            throw new IllegalArgumentException("Cannot set a color's blue value to " + blue + " [0-255]");
        }
        return getAlpha(color) << 24 |
                getRed(color) << 16 |
                getGreen(color) << 8 |
                blue;

    }

    public static int setAlpha(int color, int alpha) {
        if(alpha < 0 || alpha > 255) {
            throw new IllegalArgumentException("Cannot set a color's alpha value to " + alpha + " [0-255]");
        }
        return alpha << 24 |
                getRed(color) << 16 |
                getGreen(color) << 8 |
                getBlue(color);

    }

    public static int hue (int color) {
        return 0;
    }

    public static int getSaturation(int color) {
        return Math.max(getRed(color), Math.max(getGreen(color), getBlue(color)));
    }

    public static int getBrightness(int color) {
        return Math.max(getRed(color), Math.max(getGreen(color), getBlue(color)));
    }
}
