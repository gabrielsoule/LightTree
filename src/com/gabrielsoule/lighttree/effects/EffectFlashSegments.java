package com.gabrielsoule.lighttree.effects;

import com.gabrielsoule.lighttree.ColorGradient;
import com.gabrielsoule.lighttree.LightEffect;
import com.gabrielsoule.lighttree.LightTree;

import java.util.*;

public class EffectFlashSegments extends LightEffect {

    //change this based on actual hardware setup
    private final int LIGHTS_PER_SEGMENT = 64;
    private ColorGradient color;

    //we scramble this array during setup so that segments turn on randomly
    private final Deque<Integer> segments = new ArrayDeque<>();
    public EffectFlashSegments(LightTree p, int startColor, int endColor) {
        for(int i = 0; i < p.NUM_LIGHTS / LIGHTS_PER_SEGMENT; i++) {
            segments.addFirst(i * p.NUM_LIGHTS);
        }
    }

    @Override
    public void setup() {
    }

    @Override
    public void draw() {

    }
}
