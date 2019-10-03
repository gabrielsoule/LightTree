package com.gabrielsoule.lighttree;

public abstract class LightEffect {
    public LightTree p;
    public char key;
    public BeatDetector beatDetector;

    public LightEffect(LightTree applet) {
        this.p = applet;
        this.beatDetector = p.beatDetector;
    }

    public LightEffect() { }

    public abstract void setup(int[] integerConfig, Color[] colorConfig);

    public abstract void reconfigure(int[] integerConfig, Color[] colorConfig);

    public abstract void draw();

    public void keyPressed() {}

    public void setLight(int index, int color) {
        p.setLight(index,  color);
    }
}
