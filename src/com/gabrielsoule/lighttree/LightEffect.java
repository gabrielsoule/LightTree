package com.gabrielsoule.lighttree;

public abstract class LightEffect {
    public LightTree p;

    public LightEffect(LightTree applet) {
        this.p = applet;
    }

    public LightEffect() { }

    public abstract void draw();

    public void setLight(int index, int color) {
        p.setLight(index,  color);
    }
}
