package com.gabrielsoule.lighttree;

import processing.core.PApplet;

/**
 * Advanced color class that supports "random" as a color, and includes
 * built-in methods for baking alpha, changing components, adding colors, and so forth.
 * Can be used interchangeably with Processing's integer color format,
 * which is a 32 bit integer of the format AAAAAAAARRRRRRRRGGGGGGGGBBBBBBBB
 * but using this class is a lot easier than bit shifting everything.
 */
public class Color {
    static PApplet p;
    private int color;

    public Color(int hue, int saturation, int value) {
        this.color = p.color(hue, saturation, value);
    }



    public int get() {
        if(color == -1) {
            color = p.color(p.random(0, 360), 255, 255);
        }

        return color;
    }

    public int red() {
        return (color >> 16) & 0xFF;
    }

    public int green() {
        return (color >> 8) & 0xFF;
    }

    public int blue() {
        return color & 0xFF;
    }

    public int alpha() {
        return (color >> 24) & 0xFF;
    }

    public Color red(int red) {

    }

    public Color green(int green) {

    }

    public Color blue(int blue) {

    }

    public Color alpha(int alpha) {

    }

    private void bakeAlpha() {

    }


}
