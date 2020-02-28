package com.gabrielsoule.lighttree.effect;

import com.gabrielsoule.lighttree.LightEffect;

public class EffectNull extends LightEffect {
    @Override
    public void setup() {

    }

    @Override
    public void draw() {
        for(int i = 0; i < p.NUM_LIGHTS; i++) {
            setLight(i, 0);
        }
    }
}
