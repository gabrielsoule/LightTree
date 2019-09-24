package com.gabrielsoule.lighttree.effects;

import com.gabrielsoule.lighttree.LightEffect;
import com.gabrielsoule.lighttree.LightTree;

public class EffectSimpleVisualizer extends LightEffect {
    public EffectSimpleVisualizer(LightTree lightTree) {
        super(lightTree);
    }

    @Override
    public void setup() {

    }

    @Override
    public void draw() {
        for (int i = 0; i < 256; i++) {
            setLight((i + (p.millis() / 5)) % 256, p.color((i / 512f) * 360, 255, 255));
        }
    }

}
