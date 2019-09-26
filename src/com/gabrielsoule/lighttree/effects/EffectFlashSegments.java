package com.gabrielsoule.lighttree.effects;

import com.gabrielsoule.lighttree.ColorGradient;
import com.gabrielsoule.lighttree.LightEffect;
import com.gabrielsoule.lighttree.LightTree;

import java.util.*;

public class EffectFlashSegments extends LightEffect {

    //change this based on actual hardware setup
    private final int LIGHTS_PER_SEGMENT = 64;
    private float duration = 0.3f;
    private ColorGradient color;

    //we scramble this array during setup so that segments turn on randomly
    private final List<Integer> segments = new ArrayList<>();
    public EffectFlashSegments(LightTree p, int startColor, int endColor) {
        for(int i = 0; i < p.NUM_LIGHTS / LIGHTS_PER_SEGMENT; i++) {
            segments.add(i * p.NUM_LIGHTS);
        }
        Collections.shuffle(segments);
    }

    @Override
    public void setup() {
    }

    @Override
    public void draw() {

    }

    private class Segment {
        float alphaDelta = (255 / ((p.beatDetector.getEstBPM() / 60f) * p.FRAME_RATE)) / duration;
        float alpha = 255;
        int color;
        int startLight;

        public void flash() {

        }

        public void draw() {
            if(alpha <= 0) {
                 alpha -= alphaDelta;
                for(int i = startLight; i < startLight + LIGHTS_PER_SEGMENT; i++) {
                    int c = color;
                    c
                }
            }
        }

    }
}