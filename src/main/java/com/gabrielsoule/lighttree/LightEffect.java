package com.gabrielsoule.lighttree;

import java.util.HashMap;

public abstract class LightEffect {
    public LightTree p;
    public BeatDetector beatDetector;
    private int[] lightColors;
    private boolean sleeping = false;
    private String name;

    public LightEffect() {
        this.p = LightTree.getInstance();
        this.config = new LightEffectConfig();
        this.beatDetector = p.beatDetector;
        this.lightColors = new int[p.NUM_LIGHTS];
    }
    public abstract void setup();

    public void sleep() {
        this.sleeping = true;
    }

    public void wake() {this.sleeping = false;}

    public void onWake() {}

    public void onSleep() {}

    public boolean isSleeping() {
        return sleeping;
    }

    public abstract void draw();

    public void keyPressed() {}

    public void setLight(int index, int color)
    {
        if(index >= 0 && index < p.NUM_LIGHTS) {
            this.lightColors[index] = color;
            p.setLight(index,  color);
        }
    }

    public  void flushColors() {
        for(int i = 0; i < lightColors.length; i++) {
            lightColors[i] = 0;
            p.setLight(i, 0);
        }
    }

    public LightEffectConfig config;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
