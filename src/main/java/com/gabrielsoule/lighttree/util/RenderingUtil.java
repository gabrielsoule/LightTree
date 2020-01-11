package com.gabrielsoule.lighttree.util;

import com.gabrielsoule.lighttree.ColorGradient;
import com.gabrielsoule.lighttree.LightTree;

public class RenderingUtil {
    public static LightTree p = LightTree.getInstance();
    public static int X_AXIS = 1;
    public static int Y_AXIS = -1;
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

        for(int j = y; j < y + height; j++) {
            for(int i = x; i < x + width; i++) {
                if(i < x + border ||
                i > (x + width) - border ||
                j < y + border ||
                j > (y + height) - border) {
                    p.pixels[j * p.width + i] =
                            borderGradient.get(borderGradientDirection == X_AXIS ? (i - x) / (float) width : (j - y) / (float) height);
                } else {
                    p.pixels[j * p.width + i] =
                            boxGradient.get(boxGradientDirection == X_AXIS ? (i - x) / (float) width : (j - y) / (float) height);

                }
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
                                       ColorGradient borderColor,
                                       int borderGradientDirection) {
        for(int i = x; i < x + width; i++) {
            for(int j = y; j < y + height; j++) {

            }
        }
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
        for(int i = x; i < x + width; i++) {
            for(int j = y; j < y + height; j++) {

            }
        }
    }

}
