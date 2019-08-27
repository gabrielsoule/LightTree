package com.gabrielsoule.lighttree.effects;

import com.gabrielsoule.lighttree.ColorGradient;
import com.gabrielsoule.lighttree.LightEffect;
import com.gabrielsoule.lighttree.LightTree;
import processing.core.PApplet;

public class TestEffectGradient extends LightEffect {

    private ColorGradient gradient;

    public TestEffectGradient(LightTree p) {
        super(p);
        gradient = new ColorGradient(p.color(240, 255, 255), 0, p.color(0, 255, 255, 120), 1);
    }

    @Override
    public void draw() {
        for(int i = 0; i < p.NUM_LIGHTS; i++) {
            setLight(i, gradient.get(i / (float) p.NUM_LIGHTS));
        }
    }
}
