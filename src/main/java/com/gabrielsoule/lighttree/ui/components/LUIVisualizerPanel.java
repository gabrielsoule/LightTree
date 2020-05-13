package com.gabrielsoule.lighttree.ui.components;

import com.gabrielsoule.lighttree.LightTree;
import com.gabrielsoule.lighttree.util.Color;
import com.gabrielsoule.lightui.UIComponent;
import processing.core.PConstants;

public class LUIVisualizerPanel extends UIComponent {

    private int numLightsX;
    private int numLightsY;
    private int lightDiameter;
    private float lightMarginX;
    private float lightMarginY;

    public LUIVisualizerPanel(String name, int numLightsX, int numLightsY, int lightDiameter) {
        super(name);
        this.numLightsX = numLightsX;
        this.numLightsY = numLightsY;
        this.lightDiameter = lightDiameter;
        this.setOffsets(0,0, 0, 0);
        this.setAnchors(0, 1, 0, 1);
    }

    @Override
    public void draw() {
        p.ellipseMode(PConstants.CENTER);
        p.noStroke();
        int index = 0;
//        for(int x = (int) (this.rawPosX + lightMarginX); x < this.width + rawPosX; x += 2 * lightRadius + lightMarginX) {
//            for(int y = this.rawPosY; y < this.height + rawPosY; y += 2 * lightRadius + lightMarginY) {
//                int color = LightTree.getInstance().lightColors[index];
//                if((color & 0xFFFFFF) > 0) {
//                    p.fill(color);
//                } else {
//                    p.fill(LightTree.getInstance().newUI.backgroundColor);
//                }
//                float scaledDiameter = lightRadius * 0.7f + 0.3f * (lightRadius * (Color.getBrightness(color) / 255f));
//                p.ellipse(x + lightRadius, y + lightRadius, scaledDiameter, scaledDiameter);
//                index++;
//            }
//        }

        float x = (this.rawPosX + lightMarginX);
        for(int i = 0; i < numLightsX; i++) {
            float y = (this.rawPosY + lightMarginY);
            for(int j = 0; j < numLightsY; j++) {
                int color = LightTree.getInstance().lightColors[index];
                if((color & 0xFFFFFF) > 0) {
                    p.fill(color);
                } else {
                    p.fill(LightTree.getInstance().newUI.backgroundColor);
                }
                float scaledDiameter = lightDiameter * 0.6f + 0.4f * (lightDiameter * (Color.getBrightness(Color.bakeAlpha(color)) / 255f));
                p.ellipse(x + lightDiameter / 2f, y + lightDiameter / 2f, scaledDiameter, scaledDiameter);
                index++;
                y += lightDiameter + lightMarginY;
            }
            x += lightDiameter + lightMarginX;
        }
    }

    @Override
    public void onPostResize() {
        this.lightMarginX = (this.width  - (lightDiameter * numLightsX)) / (numLightsX  + 1f);
        this.lightMarginY = (this.height - (lightDiameter * numLightsY)) / (numLightsY  + 1f);

    }
}
