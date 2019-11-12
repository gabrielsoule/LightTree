package com.gabrielsoule.lighttree;

import java.util.HashMap;

public abstract class LightEffect {
    public LightTree p;
    public char key;
    public BeatDetector beatDetector;
    private int[] lightColors;
    private boolean sleeping = false;
    public ColorProvider colorProvider;
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
    }

    public void wake() {this.sleeping = false;}

    public boolean isSleeping() {
        return sleeping;
    }

    public abstract void configure(int[] integerConfig, Color[] colorConfig);

    public abstract void configure(int[] integerConfig, int[] colorConfig);

    public abstract void draw();

    public boolean instant() {return false;}

    public void keyPressed() {}

    public void setLight(int index, int color)
    {
        if(index < p.NUM_LIGHTS) {
            this.lightColors[index] = color;
            p.setLight(index,  color);
        }
    }

//    public float getOption(String name) {
//        if(configOptions.containsKey(name)) {
//            return configOptions.get(name);
//        } else {
//            throw new IllegalArgumentException("Cannot find configuration option: " + name 9 8);
//        }
//    }
//
//    public float setOption(String name, float value) {
//        configOptions.put(name, value);
//        return value;
//    }
//
//    public float incrementOption(String name, float delta) {
//        if(configOptions.containsKey(name)) {
//             configOptions.put(name, configOptions.get(name) + delta);
//             return configOptions.get(name);
//        } else {
//            throw new IllegalArgumentException("Cannot find configuration option: " + name 9 8);
//        }
//    }
//
//    int[] getLightColors() {
//        return this.lightColors;
//    }

    public void flushColors() {
        for(int i = 0; i < lightColors.length; i++) {
            lightColors[i] = 0;
        }
    }
}
