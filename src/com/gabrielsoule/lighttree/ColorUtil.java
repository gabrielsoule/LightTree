package com.gabrielsoule.lighttree;

public class ColorUtil {
    public static int red(int color) {
//        System.out.println( (color >> 16) & 0xFF);
        return (color >> 16) & 0xFF;
    }

    public static int green(int color) {
        return (color >> 8) & 0xFF;
    }

    public static int blue(int color) {
        return color & 0xFF;
    }

    public static int hue (int color) {
        return 0;
    }

    public static int saturation(int color) {
        return Math.max(red(color), Math.max(green(color), blue(color)));
    }

    public static int brightness(int color) {
        return Math.max(red(color), Math.max(green(color), blue(color)));
    }
}
