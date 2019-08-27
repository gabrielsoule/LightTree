package com.gabrielsoule.lighttree;

import java.util.HashMap;

public class ColorGradient {

    private HashMap<Float, Integer> colors;

//    private float farLeft, farRight; //cache the furthest left and right colors just to make things fast on addcolor

    public ColorGradient(int... args) {
        if(args.length % 2 != 0) {
            throw new IllegalArgumentException("Odd number of arguments. Each color must be paired with a float in between 0 and 1");
        }

        colors = new HashMap<>();
        colors.put(0f, 0);
        colors.put(1f, 1);
    }

    public int get(float position) {
        if(position < 0 || position > 1) {
            throw new IllegalArgumentException("Color position not between 0 and 1");
        }

        //want to find adjacent colors to the specified location
        //first we must find adjacent indices
        float leftIndex  = 0;
        float rightIndex = 1;

        for(float index : colors.keySet()) {
            //tighter left bound?
            if(index > leftIndex && index <= position) {
                leftIndex = index;
            //tighter right bound?
            } else if (index < rightIndex && index >= position) {
                rightIndex = index;
            }
        }

        return lerpColor(position, colors.get(leftIndex), colors.get(rightIndex), leftIndex, rightIndex);
    }
    //lerp RGB values
    private int lerpColor(float index, int lColor, int rColor, float leftIndex, float rightIndex) {
        float t = (index - leftIndex) / (rightIndex - leftIndex);
        float r1 = (lColor >> 16) & 0xFF;
        float r2 = (rColor >> 16) & 0xFF;
        float g1 = (lColor >>  8) & 0xFF;
        float g2 = (rColor >>  8) & 0xFF;
        float b1 = lColor & 0xFF;
        float b2 = rColor & 0xFF;

        return  (0xFF << 24) |
                (Math.round(r1 + (r2 - r1) * t) << 16) |
                (Math.round(g1 + (g2 - g1) * t) << 8) |
                (Math.round(b1 + (b2 - b1) * t));
    }

    public ColorGradient addColor(int color, float position) {
        colors.put(position, color);
        return this;
    }
}
