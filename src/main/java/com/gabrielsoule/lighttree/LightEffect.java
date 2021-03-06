package com.gabrielsoule.lighttree;

import com.gabrielsoule.lighttree.effect.config.LightEffectConfig;

public abstract class LightEffect {
    public LightTree p;
    public BeatDetector beatDetector;
    private int[] lightColors;
    private boolean sleeping = false;
    private String name;
    public LightEffectConfig config;

    public LightEffect() {
        this.p = LightTree.getInstance();
        this.config = new LightEffectConfig();
        this.beatDetector = p.beatDetector;
        this.lightColors = new int[p.NUM_LIGHTS];
    }
    public abstract void setup();

    public void sleep() {
        this.sleeping = true;
        this.onSleep();
    }

    public void wake() {
        this.sleeping = false;
        this.onWake();
    }

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


    public LightEffectConfig getConfig() {
        return config;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
