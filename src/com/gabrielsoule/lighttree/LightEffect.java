package com.gabrielsoule.lighttree;

public abstract class LightEffect {
    public LightTree p;
    public char key;
    public BeatDetector beatDetector;
    private int[] lightColors;
    private boolean sleeping = false;
    public ColorProvider colorProvider;

    public LightEffect() {
        this.p = LightTree.getInstance();
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
        this.lightColors[index] = color;
        p.setLight(index,  color);
    }

    int[] getLightColors() {
        return this.lightColors;
    }

    void flushColors() {
        for(int i = 0; i < lightColors.length; i++) {
            lightColors[i] = 0;
        }
    }
}
