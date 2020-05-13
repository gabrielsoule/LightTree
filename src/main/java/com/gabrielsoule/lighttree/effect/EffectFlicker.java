package com.gabrielsoule.lighttree.effect;

import com.gabrielsoule.lighttree.LightEffect;
import com.gabrielsoule.lighttree.LightTree;
import com.gabrielsoule.lighttree.util.Color;

public class EffectFlicker extends LightEffect {

    private float maxAmplitude = Integer.MIN_VALUE;
    private float minAmplitude = Integer.MAX_VALUE;

    @Override
    public void setup() {
        for(float i = 0; i < 1; i = i + 0.003f) {
            maxAmplitude = LightTree.max(maxAmplitude, brightnessFunc(i));
            minAmplitude = LightTree.min(minAmplitude, brightnessFunc(i));
        }

        maxAmplitude -= minAmplitude;

        LightTree.log("Maximum and minimum amplitudes for FLICKER set to: %s, %s", maxAmplitude, minAmplitude);
    }

    @Override
    public void draw() {
        for(int i = 0; i < p.NUM_LIGHTS; i++) {
            float brightness =
                    brightnessFunc((((i / (float) p.NUM_LIGHTS) + p.millis() * 0.000009f) % 1.0f) - minAmplitude) / maxAmplitude;
            setLight(i, Color.setAlpha(config.getColors().get(i % config.getColors().size()), (int) LightTree.abs((0.4f + brightness) * 255)));
        }
    }

    public float brightnessFunc(float in) {
        return LightTree.sin(2*LightTree.pow(in, 2)) / 3f +
                LightTree.sin(15 * in) / 5f +
                LightTree.sin(100 * in) / 10f +
                LightTree.sin(300 * in);
    }
}
