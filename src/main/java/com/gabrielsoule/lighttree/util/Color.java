package com.gabrielsoule.lighttree.util;

import com.gabrielsoule.lighttree.LightTree;
import processing.core.PApplet;

import java.util.ArrayList;

public class Color {

    public static int RANDOM = 0x00000000;
    public static int BLACK  = 0x000000FF;
    public static int WHITE  = 0xFFFFFFFF;

    public static int HSB(int hue, int saturation, int brightness) {
        return LightTree.getInstance().color(hue, saturation, brightness);
    }


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

    /**
     * Returns an average of the supplied colors using the sum-of-squares method.
     * Ignores black.
     * Does not ignore alpha--keep this in mind.
     */
    public static int getAverageColor(int[] colors) {
        int count = 0;
        int red = 0;
        int green = 0;
        int blue = 0;
        int alpha = 0;
        for (int i = 0; i < colors.length; i++) {
            int color = colors[i];
            if((color >> 8) != 0 && ((color >> 24) & 0xFF) != 0) {
                count++;
                red += getRed(color);
                green += getGreen(color);
                blue += getBlue(color);
                alpha += getAlpha(color);
            }
        }

        return count == 0 ? 0 : ((alpha / count) & 0xFF) << 24 |
                ((red / count) & 0xFF) << 16 |
                ((green / count) & 0xFF) << 8 |
                ((blue / count) & 0xFF);
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

    /**
     * Processing supports colors with an alpha value (opacity), but the lights do not!
     * This method bakes a color's alpha value into its RGB values (assuming a black background--this works well for the lights)
     * by reducing RGB as appropriate and setting the return alpha value to 255.
     * Against a black background, both input and output should look identical.
     */
    public static int bakeAlpha(int color) {
        float alphaFrac = getAlpha(color) / 255f;
        return  (0xFF << 24) |
                ((int)(((color >> 16) & 0xFF) * alphaFrac) << 16) |
                ((int)(((color >> 8) & 0xFF) * alphaFrac) << 8) |
                ((int) ((color & 0xFF) * alphaFrac));
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
        alpha = PApplet.constrain(alpha, 0, 255);
        return alpha << 24 |
                getRed(color) << 16 |
                getGreen(color) << 8 |
                getBlue(color);

    }

    //Borrowed from https://stackoverflow.com/questions/23090019/fastest-formula-to-get-hue-from-rgb
    public static int getHue(int color) {

        int red = getRed(color);
        int green = getGreen(color);
        int blue = getBlue(color);

        float min = LightTree.min(LightTree.min(red, green), blue);
        float max = LightTree.max(LightTree.max(red, green), blue);

        if (min == max) {
            return 0;
        }

        float hue = 0f;
        if (max == red) {
            hue = (green - blue) / (max - min);

        } else if (max == green) {
            hue = 2f + (blue - red) / (max - min);

        } else {
            hue = 4f + (red - green) / (max - min);
        }

        hue = hue * 60;
        if (hue < 0) hue = hue + 360;

        return LightTree.round(hue);
    }

    public static int getSaturation(int color) {
        int red = getRed(color);
        int green = getGreen(color);
        int blue = getBlue(color);

        float min = LightTree.min(LightTree.min(red, green), blue);
        float max = LightTree.max(LightTree.max(red, green), blue);
        if(max == 0) return 0;
        else return LightTree.round(((max - min) / max) * 255);
    }

    public static int getBrightness(int color) {
        return PApplet.max(getRed(color), Math.max(getGreen(color), getBlue(color)));
    }
//
//    public static int setBrightness(int color, int brightness) {
//        float mult = brightness / (float) getBrightness(color);

}
