package com.gabrielsoule.lighttree;

import com.gabrielsoule.lighttree.util.Color;
import com.gabrielsoule.lighttree.util.RenderingUtil;
import processing.core.PFont;

import java.util.ArrayDeque;
import java.util.Deque;

public class LightUI {

    private LightTree p;
    private PFont fontBold;
    private PFont fontRegular;

    //TODO add these constants as options in config.yml
    private int lightBoxSize = 20;
    private int lightBoxMarginHoriz = 22;
    private int lightBoxMarginVert = 11;
    private int numLightBoxesWide = 16;
    private int numLightBoxesHigh = 32;

    private int effectBoxMarginHoriz = 40;
    private int effectBoxMarginVert = 20;
    private int effectBoxHeight = 70;
    private int effectBoxBorderSize = 5;
    private int effectBoxTextMarginLeft = 26;
    private int effectBoxColorIndicatorSize = 36;
    private int effectBoxColorIndicatorMargin = 10;

    private int bpmTextY = 180;
    private int bpmTextSize = 80;

    private int logTextSize = 10;
    private int logTextSpaceVert = 20;
    private int logTextMaxLines = 80;
    private int logTextNumLines = 0;
    private int logTextMarginHoriz = 40;
    public Deque<String> logText = new ArrayDeque<>();

    private int visualizerSectionWidth;
    private int visualizerSectionHeight;
    private int backgroundColor;

    private ColorGradient backgroundGradient;

    public LightUI() {
        p = LightTree.getInstance();
        backgroundGradient = new ColorGradient(p.color(0, 0, 255), 0, p.color(0, 0, 0), 1);
        fontBold = p.createFont("Exo2-Bold.ttf", 150);
        fontRegular = p.createFont("Exo2-Regular.ttf", 150);
        p.textFont(fontBold);
        visualizerSectionWidth = (lightBoxSize + lightBoxMarginHoriz) * numLightBoxesWide - lightBoxMarginHoriz;
        visualizerSectionHeight = (lightBoxSize + lightBoxMarginVert) * numLightBoxesHigh - lightBoxMarginVert;
    }

    public void drawUI() {
        /* === draw background ===*/
        backgroundColor = Color.bakeAlpha(Color.setAlpha(Color.getAverageColor(p.lightColors), 60));
        p.background(backgroundColor);

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
                if((p.lightColors[lightIndex] & 0xFFFFFF) > 0)
                    p.ellipse(x + lightBoxSize / 2f, y + lightBoxSize / 2f, lightBoxSize * scaleFactor, lightBoxSize * scaleFactor);
                lightIndex++;

                y += lightBoxSize + lightBoxMarginVert;
            }
            y = (int) ((p.height / 2f) -
                    ((numLightBoxesHigh / 2f) * (lightBoxMarginVert + lightBoxSize) - lightBoxMarginVert / 2));
            x += lightBoxSize + lightBoxMarginHoriz;
        }

        /* === Draw effect boxes on left of display */

        int textColor = p.color(p.hue(backgroundColor), 90, 255);

        int effectBoxWidth = ((p.displayWidth / 2) - (visualizerSectionWidth / 2)) - effectBoxMarginHoriz * 2;
        int boxY = p.displayHeight - (((p.displayHeight / 2) - (visualizerSectionHeight / 2)) + effectBoxHeight );
        for(LightEffect activeEffect : p.sequencer.getActiveEffects()) {
            ColorGradient effectGradient = new ColorGradient();
            int colorIndicatorX = (effectBoxMarginHoriz + effectBoxWidth) - (effectBoxBorderSize + effectBoxColorIndicatorMargin + effectBoxColorIndicatorSize / 2 +
                    (effectBoxColorIndicatorSize + effectBoxColorIndicatorMargin) * (activeEffect.config.getColors().size() - 1)); //jesus christ. I'm so sorry
            for (int i = 0; i < activeEffect.config.getColors().size(); i++) {
                int color = activeEffect.config.getColors().get(i);
                if(color == Color.RANDOM) {
                    color = p.color((p.millis() * 0.1f) % 360, 255, 255);
                }
                effectGradient.putColor(color, i / ((float) activeEffect.config.getColors().size() - 1));

                p.fill(color);
                p.ellipse(colorIndicatorX, boxY + effectBoxHeight  / 2, effectBoxColorIndicatorSize, effectBoxColorIndicatorSize);
                colorIndicatorX += effectBoxColorIndicatorSize + effectBoxColorIndicatorMargin;
            }

            int color = activeEffect.config.getColors().get(activeEffect.config.getColors().size() - 1);
            if(color == Color.RANDOM) {
                color = p.color((p.millis() * 0.1f) % 360, 255, 255);
            }
            effectGradient.putColor(color, 1);

            color = activeEffect.config.getColors().get(0);
            if(color == Color.RANDOM) {
                color = p.color((p.millis() * 0.1f) % 360, 255, 255);
            }
            effectGradient.putColor(color, 0);

            RenderingUtil.drawGradientBox(
                    effectBoxMarginHoriz,
                    boxY,
                    effectBoxWidth,
                    effectBoxHeight,
                    p.CORNER,
                    0,
                    effectBoxBorderSize,
                    effectGradient,
                    RenderingUtil.X_AXIS);
            p.textFont(fontBold, 30);
            p.textAlign(p.LEFT);
            p.fill(textColor);
            p.text(activeEffect.getName(), effectBoxMarginHoriz + effectBoxTextMarginLeft + effectBoxBorderSize, boxY + 45);
            boxY -= effectBoxHeight + effectBoxMarginVert;
        }

        /* === draw BPM stuff ===*/
        p.fill(textColor);
        p.textAlign(p.CENTER);
        p.textFont(fontBold, 130);
        p.text(122, (p.width /2 - visualizerSectionWidth / 2) / 2, bpmTextY);
        float bpmTextWidth = p.textWidth(Integer.toString(122));
        p.textFont(fontBold, 30);
        p.text("BPM", (p.width / 2 - visualizerSectionWidth / 2) / 2 + bpmTextWidth / 2 + 40f, bpmTextY);

        /* === draw log ===*/
        p.textAlign(p.LEFT, p.BOTTOM);
        p.textFont(fontRegular, 18);
        ColorGradient logTextGradient = new ColorGradient(textColor, 1, backgroundColor, 0.2f, backgroundColor, 0);
        int logTextX = (p.displayWidth / 2 + visualizerSectionWidth / 2) + logTextMarginHoriz;
        int logTextY = p.displayHeight - (((p.displayHeight / 2) - (visualizerSectionHeight / 2)));
        for(String logEntry : this.logText) {
            p.fill(logTextGradient.get(logTextY / (float) p.height));
//            p.fill(textColor);
            p.text(logEntry, logTextX, logTextY);
            logTextY -= logTextSpaceVert;
            if(logTextY < 0) break;
        }

    }

    void addLogEntry(String entry) {
        logText.push(entry);
        if(logTextNumLines >= logTextMaxLines) {
            logText.pop();
        } else {
            logTextNumLines++;
        }
    }
}
