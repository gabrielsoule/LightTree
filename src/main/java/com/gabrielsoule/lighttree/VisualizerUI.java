package com.gabrielsoule.lighttree;

import processing.core.PFont;

public class VisualizerUI {

    private LightTree p;
    private PFont font;

    public VisualizerUI() {
        p = LightTree.getInstance();
        font = p.createFont("Exo2-Regular.ttf", 32);
        p.textFont(font);
    }

    public void drawUI() {

        /* === draw LED visualizer in center === */
        int lightBoxSize = 20;
        int lightBoxMarginHoriz = 10;
        int lightBoxMarginVert = 10;
        int numLightBoxesWide = 16;
        int numLightBoxesHigh = 32;
        int lightBoxAreaWidth = ((lightBoxSize + lightBoxMarginHoriz) * numLightBoxesWide) - lightBoxMarginHoriz;
        int lightBoxAreaHeight = ((lightBoxSize + lightBoxMarginVert) * numLightBoxesHigh) - lightBoxMarginVert;

//        int x = (width / 2) - (lightBoxAreaWidth / 2) ;/
        int x = (int) ((p.width / 2f) -
                ((numLightBoxesWide / 2f) * (lightBoxMarginHoriz + lightBoxSize) - lightBoxMarginHoriz / 2));
        int y = (int) ((p.height / 2f) -
                ((numLightBoxesHigh / 2f) * (lightBoxMarginVert + lightBoxSize) - lightBoxMarginVert / 2));

        p.noStroke();
        p.rectMode(p.CENTER);

        int lightIndex = 0;
        for (int i = 0; i < numLightBoxesWide; i++) {
            for (int j = 0; j < numLightBoxesHigh; j++) {
//                fill(lightColors[((i + 1) * (j + 1)) - 1]);
                p.fill(p.lightColors[lightIndex]);
                float scaleFactor = ColorUtil.getBrightness(p.lightColors[lightIndex]);
                scaleFactor = scaleFactor / 128f;
                scaleFactor = scaleFactor / 4;
                scaleFactor = scaleFactor + 0.75f;
//                System.out.println(scaleFactor);
//                scaleFactor *= 0.2;
//                print(scaleFactor);

                lightIndex++;
//                rect(x + lightBoxSize / 2, y + lightBoxSize / 2, lightBoxSize * scaleFactor, lightBoxSize * scaleFactor, (lightBoxSize * scaleFactor) / 2f);
                p.ellipse(x + lightBoxSize / 2f, y + lightBoxSize / 2f, lightBoxSize * scaleFactor, lightBoxSize * scaleFactor);
                y += lightBoxSize + lightBoxMarginVert;
            }
            y = (int) ((p.height / 2f) -
                    ((numLightBoxesHigh / 2f) * (lightBoxMarginVert + lightBoxSize) - lightBoxMarginVert / 2));
            x += lightBoxSize + lightBoxMarginHoriz;
        }
    }
}
