package com.gabrielsoule.lighttree;

import com.gabrielsoule.lighttree.util.MathUtil;

import java.util.HashMap;
import java.util.function.Supplier;

public class ColorGradient {

    private HashMap<Float, Supplier<Integer>> colors;
    private boolean fixBrightness = false;
    private float curveLerpExponent = 2f;

    public ColorGradient(float... args) {
        this();
        if(args.length % 2 != 0) {
            throw new IllegalArgumentException("Odd number of arguments. Each color must be paired with a float in between 0 and 1");
        }
        for (int i = 0; i < args.length; i += 2) {
            int color = (int) args[i];
            colors.put(args[i+1], () -> (color));
        }
    }

    public ColorGradient() {
        colors = new HashMap<>();
        colors.put(0f, () -> 0xFF000000);
        colors.put(1f, () -> 0xFFFFFFFF);
    }

    public int get(float position) {
/*        if(position < 0 || position > 1) {
            throw new IllegalArgumentException("Gradient position " + position + " is not between 0 and 1");
        }*/

        if(position < 0) {
            position = 0;
        } else if(position > 1) {
            position = 1;
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

        return lerpColor(position, colors.get(leftIndex).get(), colors.get(rightIndex).get(), leftIndex, rightIndex);
    }
    //lerp RGB values, properly lerping alpha
    private int lerpColor(float index, int lColor, int rColor, float leftIndex, float rightIndex) {
        index = LightTree.constrain(index, 0, 1);
        float t = (index - leftIndex) / (rightIndex - leftIndex);
        float a1 = (lColor >> 24) & 0xFF;
        float a2 = (rColor >> 24) & 0xFF;
        float r1 = (lColor >> 16) & 0xFF;
        float r2 = (rColor >> 16) & 0xFF;
        float g1 = (lColor >>  8) & 0xFF;
        float g2 = (rColor >>  8) & 0xFF;
        float b1 = lColor & 0xFF;
        float b2 = rColor & 0xFF;

        //I have no idea what is going on here. What the fuck was I thinking? What does it do? Don't touch it!
        int a =  (int) (fixBrightness ? MathUtil.curvedLerp(a1, a2, t, curveLerpExponent) : a1 + (a2 - a1) * t);
        return  a << 24 |
                    ((int) (r1 + (r2 - r1) * t) << 16) |
                    ((int) (g1 + (g2 - g1) * t) << 8) |
                    ((int) (b1 + (b2 - b1) * t));
    }

    public ColorGradient putColor(float position, int color) {
        return putColor(position, () -> color);
    }

    public ColorGradient putColor( float position, Supplier<Integer> color) {
        colors.put(position, color);
        return this;
    }
    
    public ColorGradient linearApparentBrightness(boolean toggle) {
        this.fixBrightness = true;
        return this;
    }
    
    public ColorGradient brightnessCurveExponent(float curve) {
        this.curveLerpExponent = curve;
        return this;
    }
}
