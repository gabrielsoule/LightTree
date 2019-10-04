package com.gabrielsoule.lighttree;

public abstract class LightEffect {
    public LightTree p;
    public char key;
    public BeatDetector beatDetector;
    private boolean instant;

    public LightEffect(LightTree applet) {
        this.p = applet;
        this.beatDetector = p.beatDetector;
    }

    public LightEffect() { }

    public abstract void setup();

    public abstract void configure(int[] integerConfig, Color[] colorConfig);

    public abstract void draw();

    public boolean instant() {return false;}

    public void keyPressed() {}

    public void setLight(int index, int color) {
        p.setLight(index,  color);
    }
}
