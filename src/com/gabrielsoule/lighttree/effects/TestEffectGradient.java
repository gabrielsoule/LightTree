package com.gabrielsoule.lighttree.effects;

import com.gabrielsoule.lighttree.ColorGradient;
import com.gabrielsoule.lighttree.LightEffect;
import com.gabrielsoule.lighttree.LightTree;

public class TestEffectGradient extends LightEffect {

    private ColorGradient gradient;


    @Override
    public void setup() {
        gradient = new ColorGradient(p.color(0, 255, 255), 0, p.color(100, 255, 255), 1);
    }

    @Override
    public void draw() {
        for(int i = 0; i < p.NUM_LIGHTS; i++) {
            setLight(i, gradient.get(i / (float) p.NUM_LIGHTS));
        }
    }
}
