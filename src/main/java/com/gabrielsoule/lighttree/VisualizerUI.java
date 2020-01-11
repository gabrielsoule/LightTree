package com.gabrielsoule.lighttree;

import com.gabrielsoule.lighttree.util.Color;
import com.gabrielsoule.lighttree.util.RenderingUtil;
import processing.core.PFont;

public class VisualizerUI {

    private LightTree p;
    private PFont font;

    int lightBoxSize = 20;
    int lightBoxMarginHoriz = 22;
    int lightBoxMarginVert = 11;
    int numLightBoxesWide = 16;
    int numLightBoxesHigh = 32;

    private int visualizerSectionWidth;
    private int backgroundColor;

    private ColorGradient backgroundGradient;

    public VisualizerUI() {
        p = LightTree.getInstance();
        backgroundGradient = new ColorGradient(p.color(0, 0, 255), 0, p.color(0, 0, 0), 1);
        font = p.createFont("Exo2-Regular.ttf", 32);
        p.textFont(font);
    }

    public void drawUI() {

        /* === draw background ===*/
        backgroundColor = Color.bakeAlpha(Color.setAlpha(Color.getAverageColor(p.lightColors), 60));

        backgroundGradient.putColor(backgroundColor, 0);
        backgroundGradient.putColor(backgroundColor, 1);

        ColorGradient test1 = new ColorGradient(p.color(0, 255, 255), 0, p.color(125, 255, 255), 1);
        ColorGradient test2 = new ColorGradient(p.color(180, 255, 255), 0, p.color(220, 255, 255), 1);

        p.loadPixels();
        for(int j = 0; j < p.height; j++) {
            for(int i = 0; i < p.width; i++) {
                p.pixels[j * p.width + i] = this.backgroundGradient.get(j / (float) p.height);
            }
        }
        p.updatePixels();

        RenderingUtil.drawGradientBox(10, 10, 200, 50, p.CORNER, test1, RenderingUtil.X_AXIS, 5, test2, RenderingUtil.Y_AXIS);


        /* === draw LED visualizer in center === */
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
                p.fill((p.lightColors[lightIndex] & (0xFFFFFF)) > 0 ? p.lightColors[lightIndex] : backgroundColor);
                float scaleFactor = Color.getBrightness(p.lightColors[lightIndex]);
                scaleFactor = scaleFactor / 128f;
                scaleFactor = scaleFactor / 4;
                scaleFactor = scaleFactor + 0.75f;
//                System.out.println(scaleFactor);
//                scaleFactor *= 0.2;
//                print(scaleFactor);

//                rect(x + lightBoxSize / 2, y + lightBoxSize / 2, lightBoxSize * scaleFactor, lightBoxSize * scaleFactor, (lightBoxSize * scaleFactor) / 2f);
                if((p.lightColors[lightIndex] & 0xFFFFFF) > 0)
                    p.ellipse(x + lightBoxSize / 2f, y + lightBoxSize / 2f, lightBoxSize * scaleFactor, lightBoxSize * scaleFactor);
                lightIndex++;

                y += lightBoxSize + lightBoxMarginVert;
            }
            y = (int) ((p.height / 2f) -
                    ((numLightBoxesHigh / 2f) * (lightBoxMarginVert + lightBoxSize) - lightBoxMarginVert / 2));
            x += lightBoxSize + lightBoxMarginHoriz;
        }
    }
}
