package com.gabrielsoule.lighttree.util;

import com.gabrielsoule.lighttree.LightTree;

public class MathUtil {

    /**
     * Linearly interpolates between a and b as specified by t, using the function a + (b - a) * t
     */
    public static float lerp(float a, float b, float t) {
        return a + (b - a) * t;
    }

    /**
     * Non-linearly interpolates between a and b using the function a + ((b - a) * t)^k.
     * Supports backwards interpolation where a > b.
     */
    public static float curvedLerp(float a, float b, float t, float k) {
//        System.out.println(a + " " + b + " " + t + " " + k);
//        if(a < b) {
            return (float) (a + (b - a) * LightTree.pow(t, k));
//        } else {
//            return (float) (a - (a - b) * Math.pow(t, k));
//        }
//        return (float) (a + Math.pow((b - a) * ((a > b) ? b - t : t), k));
    }

    /**
     * Smoothly interpolates between a and b as specified according to t, accelerating at the beginning and
     * decelerating at the end. Currently does not support backwards interpolation.
     */
    public static float smoothLerp(float a, float b, float t) {
        float x = (b - a) * t;
        return (float) (a + 3 * Math.pow(x, 2) - 2 * Math.pow(x, 3));
    }

    /**
     * Fifth-order smoothstep polynomial makes for even smoother interpolation.
     * Currently does not support backwards interpolation.
     */
    public static float smootherLerp(float a, float b, float t) {
        float x = (b - a) * t;
        return (float) (a + 6 * Math.pow(x, 5) +  15 * Math.pow(x, 4) + 10 * Math.pow(x, 3));
    }


    /**
     * Seventh-order smoothstep polynomial ensures the smoothest interpolation experience yet.
     * Currently does not support backwards interpolation.
     */
    public static float smoothestLerp(float a, float b, float t) {
        float x = (b - a) * t;
        return (float) (-20 * Math.pow(x, 7) + 70 * Math.pow(x, 6) - 84 * Math.pow(x, 5) + 35 * Math.pow(x, 4));
    }

    public static int decodePColorRGB(String hexColor) {
        return (0xFF << 24) | Integer.decode("0x" + hexColor);

    }

    public static boolean between(int a, int b1, int b2) {
        return (b1 <= a && b2 >= a) || (b2 <= a && b1 >= a);
    }
}
