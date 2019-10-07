package com.gabrielsoule.lighttree.effects;

import com.gabrielsoule.lighttree.Color;
import com.gabrielsoule.lighttree.ColorGradient;
import com.gabrielsoule.lighttree.LightEffect;
import com.gabrielsoule.lighttree.LightTree;
import processing.core.PApplet;

public class TestEffectGradient extends LightEffect {

    private ColorGradient gradient;

    public TestEffectGradient(LightTree p) {
        super(p);
        gradient = new ColorGradient(
                p.color(40, 255, 255), 0,
                p.color(40, 255, 255), 0.2f,
                p.color(120, 255, 255), 0.5f,
                p.color(270, 255, 255, 255), 0.8f,
                p.color(270, 255, 255, 255), 1);
    }

    @Override
    public void setup() {

    }

    @Override
    public void configure(int[] integerConfig, Color[] colorConfig) {

    }

    @Override
    public void draw() {
        for(int i = 0; i < p.NUM_LIGHTS; i++) {
            setLight(i, gradient.get(i / (float) p.NUM_LIGHTS));
        }
    }
}
