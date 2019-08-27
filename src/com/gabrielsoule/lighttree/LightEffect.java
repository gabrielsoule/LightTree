package com.gabrielsoule.lighttree;

public abstract class LightEffect {
    public LightTree p;

    public LightEffect(LightTree applet) {
        this.p = applet;
        this.setup();
    }

    public LightEffect() { }

    public abstract void draw();

    public abstract void setup();

    public void setLight(int index, int color) {
        p.setLight(index,  color);
    }
}
