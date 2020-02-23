package com.gabrielsoule.lighttree.effect;

import com.gabrielsoule.lighttree.LightEffect;

public class EffectRainbowPulse extends LightEffect {
    @Override
    public void setup() {

    }

    @Override
    public void draw() {
        for(int i = 0; i < p.NUM_LIGHTS; i++) {
            setLight(i, p.color(((i + p.millis() * 0.1f) * 2f) % 360, 255, 255));
        }
    }
}
