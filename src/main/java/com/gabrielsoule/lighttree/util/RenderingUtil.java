package com.gabrielsoule.lighttree.util;

import com.gabrielsoule.lighttree.ColorGradient;
import com.gabrielsoule.lighttree.LightTree;

public class RenderingUtil {
    public static LightTree p = LightTree.getInstance();
    public static int X_AXIS = 1;
    public static int Y_AXIS = -1;

    /**
     * Draws a box with the specified values onto the screen. Due to the way processing's pixels[] array works, this
     * does NOT support transparency (alpha). Right now if any pixels have 0 alpha, it ignores them. TODO: fix this
     * Anchor should be PApplet.CORNER or PApplet.CENTER. Direction should be RenderingUtil.X_AXIS or RenderingUtil. Y_AXIS.
     * @param x
     * @param y
     * @param width
     * @param height
     * @param anchor
     * @param boxGradient
     * @param boxGradientDirection
     * @param border
     * @param borderGradient
     * @param borderGradientDirection
     */
    public static void drawGradientBox(int x,
                                       int y,
                                       int width,
                                       int height,
                                       int anchor,
                                       ColorGradient boxGradient,
                                       int boxGradientDirection,
                                       int border,
                                       ColorGradient borderGradient,
                                       int borderGradientDirection) {
        if(anchor == p.CENTER) {
            x -= width  / 2;
            height -= height / 2;
        } else if(anchor != p.CORNER) {
            throw new IllegalArgumentException("Bad anchor value! Should be CENTER or CORNER!");
        }

        p.loadPixels();

        int color;

        for(int j = y; j < y + height; j++) {
            for(int i = x; i < x + width; i++) {
                if(i < x + border ||
                i > (x + width) - border ||
                j < y + border ||
                j > (y + height) - border) {
                    color = borderGradient.get(borderGradientDirection == X_AXIS ? (i - x) / (float) width : (j - y) / (float) height);
                } else {
                    color = boxGradient.get(boxGradientDirection == X_AXIS ? (i - x) / (float) width : (j - y) / (float) height);
                }

                if(Color.getAlpha(color) != 0) p.pixels[j * p.width + i] = color;
            }
        }

        p.updatePixels();
    }

    public static void drawGradientBox(int x,
                                       int y,
                                       int width,
                                       int height,
                                       int anchor,
                                       int boxColor,
                                       int border,
                                       ColorGradient borderGradient,
                                       int borderGradientDirection) {
        if(anchor == p.CENTER) {
            x -= width  / 2;
            height -= height / 2;
        } else if(anchor != p.CORNER) {
            throw new IllegalArgumentException("Bad anchor value! Should be CENTER or CORNER!");
        }

        p.loadPixels();

        int color;

        for(int j = y; j < y + height; j++) {
            for(int i = x; i < x + width; i++) {
                if(i < x + border ||
                        i > (x + width) - border ||
                        j < y + border ||
                        j > (y + height) - border) {
                    color = borderGradient.get(borderGradientDirection == X_AXIS ? (i - x) / (float) width : (j - y) / (float) height);
                } else {
                    color = boxColor;
                }

                if(Color.getAlpha(color) != 0) p.pixels[j * p.width + i] = Color.setAlpha(color, 255);
            }
        }

        p.updatePixels();
    }

    public static void drawGradientBox(int x,
                                       int y,
                                       int width,
                                       int height,
                                       int anchor,
                                       int boxColor,
                                       int boxGradientDirection,
                                       int border,
                                       int borderColor,
                                       int borderGradientDirection) {
        if(anchor == p.CENTER) {
            x -= width  / 2;
            height -= height / 2;
        } else if(anchor != p.CORNER) {
            throw new IllegalArgumentException("Bad anchor value! Should be CENTER or CORNER!");
        }

        p.loadPixels();

        int color;

        for(int j = y; j < y + height; j++) {
            for(int i = x; i < x + width; i++) {
                if(i < x + border ||
                        i > (x + width) - border ||
                        j < y + border ||
                        j > (y + height) - border) {
                    color = borderColor;
                } else {
                    color = boxColor;
                }

                if(Color.getAlpha(color) != 0) p.pixels[j * p.width + i] = Color.setAlpha(color, 255);
            }
        }

        p.updatePixels();
    }

}
