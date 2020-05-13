package com.gabrielsoule.lighttree.ui.components;

import com.gabrielsoule.lighttree.LightEffect;
import com.gabrielsoule.lighttree.LightTree;
import com.gabrielsoule.lightui.RectComponent;

public class LUILightColorRectComponent extends RectComponent {

    public float gradientIndex;
    public LUILightColorRectComponent(String name, int strokeWeight) {
        super(name);
        this.setStrokeWeight(strokeWeight);
        this.setCornerRadius(LightTree.getInstance().newUI.borderCornerRadius);
        this.setFillColor(0x00FFFFFF);
    }

    public  LUILightColorRectComponent(String name, float  gradientIndex) {
        this(name, LightTree.getInstance().newUI.borderWidth, gradientIndex);
    }

    public LUILightColorRectComponent(String name, int strokeWeight, float gradientIndex) {
        this(name, strokeWeight);
        this.gradientIndex = gradientIndex;
    }

    @Override
    public void draw() {
        this.setStrokeColor(LightTree.getInstance().newUI.uiGradient.get(0.3f));
        this.setFillColor(0x00FFFFFF);
        super.draw();
    }

    public float getGradientIndex() {
        return gradientIndex;
    }

    public void setGradientIndex(float gradientIndex) {
        this.gradientIndex = gradientIndex;
    }
}
