package com.gabrielsoule.lighttree;

import java.util.ArrayList;

public class LightSequencer {
    private LightTree p;
    private ArrayList<LightEffect> activeEffects;
    private final float LIGHT_FALLOFF_EXPONENT = 0.8f;

    public LightSequencer(LightTree p) {
        this.p = p;
        this.activeEffects = new ArrayList<>();
    }

    int[] sequence() {

    }

}
