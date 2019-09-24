package com.gabrielsoule.lighttree;

public abstract class LightEffect {
    public LightTree p;
    public char key;

    public LightEffect(LightTree applet) {
        this.p = applet;
    }

    public LightEffect() { }

    public abstract void setup();

    public abstract void draw();

    public void keyPressed() {}

    public void setLight(int index, int color) {
        p.setLight(index,  color);
    }
}
