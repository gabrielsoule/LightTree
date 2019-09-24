package com.gabrielsoule.lighttree.effects;

import com.gabrielsoule.lighttree.LightEffect;

public class EffectFlashSegments extends LightEffect {

    //change this based on actual hardware setup
    private final int LIGHTS_PER_SEGMENT = 64;

    //we scramble this array during setup so that segments turn on randomly
    private final int[] segments = new int[p.NUM_LIGHTS / LIGHTS_PER_SEGMENT];

    @Override
    public void setup() {
        for(int i = 0; i < segments.length; i++) {
            segments[i] = LIGHTS_PER_SEGMENT * i;
        }
    }

    @Override
    public void draw() {

    }
}
