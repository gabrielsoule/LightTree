package com.gabrielsoule.lighttree.ui.components;

import com.gabrielsoule.lighttree.ColorGradient;
import com.gabrielsoule.lighttree.LightTree;
import com.gabrielsoule.lightui.*;
import processing.core.PConstants;

public class LUITempoPanel extends UIComponent {

    private class LUIFrequencyVisualizer extends UIComponent {
        float padding;
        int barWidth;
        int[] bands = {0, 17, 25, 37, 50, 75, 100, 150, 200, 275, 350, 425, 500, 600, 700, 1000, 1500, 2000};
        float[] scores;
        float[] maxima;
        float maximum;
         ColorGradient gradient;
         ColorGradient bgGradient;

        public LUIFrequencyVisualizer(String name, int padding) {
            super(name);
            this.setDebug(false);
            this.padding = padding;
            gradient = new ColorGradient();
            bgGradient = new ColorGradient();
            maxima = new float[bands.length - 1];
            scores = new float[bands.length - 1];
        }

        @Override
        public void onPostResize() {
            this.barWidth = LightTree.round(this.width / ((bands.length - 1) + bands.length * 0.3f));
            this.padding = (barWidth * 0.3f);
//            this.barWidth = LightTree.round((this.width - padding * (bands.length)) / (float) (bands.length - 1));
        }

        @Override
        public void draw() {
            gradient.putColor(0f, LightTree.getInstance().newUI.borderColor);
            gradient.putColor(1f, LightTree.getInstance().newUI.effectGradient.get(0));
            bgGradient.putColor(0f, LightTree.getInstance().newUI.backgroundColor);
            bgGradient.putColor(0.85f, LightTree.getInstance().newUI.backgroundColor);
            bgGradient.putColor(1f, LightTree.getInstance().newUI.uiGradient.get(0.7f));
            float x = this.rawPosX + padding;
            for(int i = 0; i < bands.length - 1; i++) { //for each frequency band
                float score = 0;

                for (int j = bands[i]; j < bands[i + 1]; j++) {
                    score += LightTree.getInstance().fft.getFreq(j);
                }


                score = score / (bands[i + 1] - bands[i]);
                score = score * 2;
                scores[i] = score;
                if(score > maximum) {
                    maximum = score;
                }
                if(score > maxima[i]) {
                    maxima[i] = score;
                } else {
                    maxima[i] -= 0.1 * (1 / (float) LightTree.getInstance().FRAME_RATE) * maxima[i];
                }
                float heightMultiplier = (LightTree.pow(i / 6f, 2.5f) / 2) + 0.7f;
                float heightMultiplierNext = (LightTree.pow((i + 1) / 6f, 2.5f) / 2) + 0.7f;
                p.stroke(p.color(0, 0, 50));
                p.strokeCap(PConstants.ROUND);
                p.strokeWeight(2);
                if (i != bands.length - 2)
                    p.line(x + barWidth / 2f,
                            (this.rawPosY + this.height) - heightMultiplier * 20,
                            x + barWidth + padding + barWidth / 2f,
                            (this.rawPosY + this.height) - heightMultiplierNext * 20);
                p.noStroke();

//                p.fill(gradient.get(0.8f));
//                p.rect(x, this.rawPosY + this.height - maxima[i] * heightMultiplier, barWidth, -2);
                x += barWidth + padding;
            }
        }

        @Override
        public void drawPixels() {
            int barX = (int) (this.rawPosX + padding);
            p.rectMode(PConstants.CORNER);
            for(int i = 0; i < bands.length - 1; i++) { //for each frequency band
//                p.rect(barX, this.rawPosY + this.height, barWidth, -score * heightMultiplier);
                float heightMultiplier = (LightTree.pow(i / 6f, 2.5f) / 2) + 0.7f;
                int baseY = this.rawPosY + this.height - LightTree.getInstance().newUI.borderWidth;
                float yMax = (baseY + 1) - maxima[i] * heightMultiplier;
                for(int y = baseY; y > yMax; y--) {
                    float percentage = (y - baseY) / (yMax - baseY) + 0.01f;
                    for(int x = barX; x < barX + barWidth; x++) {
                        p.pixels[y * p.width + x] = bgGradient.get(percentage);
                    }
                }

                gradient.putColor(1, LightTree.getInstance().newUI.effectGradient.get(0.001f + i / (float) (bands.length - 1)));
                baseY = this.rawPosY + this.height - LightTree.getInstance().newUI.borderWidth + 1;
                yMax = (baseY + 0) + -scores[i] * heightMultiplier;
//                yMax = (yMax / maximum) * this.height;
                for(int y = baseY; y > yMax; y--) {
                    float percentage = (y - baseY) / (yMax - baseY) + 0.01f;
                    percentage = percentage * 0.7f;
                    for(int x = barX; x < barX + barWidth; x++) {
                        p.pixels[y * p.width + x] = gradient.get(percentage);
                    }
                }
                barX += barWidth + padding;
            }
        }
    }

    private class LUITempoBPMDisplay extends UIComponent {
        TextComponent bpm;
        TextComponent bpmUnitIndicator;

        int bpmFontSize;

        public LUITempoBPMDisplay(String name) {
            super(name);
            this.bpm = (TextComponent) this.addChild(new TextComponent("bpm"));
            bpm.setFont(LightTree.getInstance().newUI.fontBold);
            bpm.setHorizontalAlignment(TextComponent.CENTER);
            bpm.setVerticalAlignment(TextComponent.CENTER);
            bpm.setTextVerticalOffset(-0.1f);

            this.bpmUnitIndicator = (TextComponent) this.addChild(new TextComponent("bpmUnitIndicator"));
            bpmUnitIndicator.setOffsetRight(-20);
            bpmUnitIndicator.setFont(LightTree.getInstance().newUI.fontRegular);
            bpmUnitIndicator.setVerticalAlignment(TextComponent.CENTER);
            bpmUnitIndicator.setHorizontalAlignment(TextComponent.RIGHT);
            bpmUnitIndicator.setText("BPM");


        }

        @Override
        public void draw() {
            bpm.setTextColor(LightTree.getInstance().newUI.uiGradient.get(0.6f));
            String bpmText = Integer.toString(LightTree.getInstance().beatDetector.getEstBPM());
            if(bpmText.length() > 3) {
                bpm.setFontSize((int) (bpmFontSize * 0.8f));
            } else {
                bpm.setFontSize(bpmFontSize);
            }
            bpm.setText(bpmText);
            bpmUnitIndicator.setTextColor(LightTree.getInstance().newUI.borderColor);
        }

        @Override
        public void onPostResize() {
            bpmFontSize = this.width / 3;
            bpm.setFontSize(bpmFontSize);
            bpmUnitIndicator.setFontSize(this.width / 15);
        }
    }

    private class LUITempoOptionsPanel extends  UIComponent {




        public LUITempoOptionsPanel(String name) {
            super(name);
        }

        @Override
        public void setup() {

        }
    }

    public int margin;

    public LUITempoPanel(String name) {
        super(name);
        this.setAnchors(0, 1, 0, 1);
        this.setOffsets(0, 0, 0, 0);
        this.setDebug(false);
        p = LightTree.getInstance();
        margin = LightTree.getInstance().newUI.mainPanelsMargin;
        UIComponent freqVisualizerContainer = this.addChild(new LUILightColorRectComponent("FreqVisualizerContainer", LightTree.getInstance().newUI.borderWidth))
                .setAnchors(0, 1, 0,  0.2f)
                .setOffsets(0, 0, 0, -margin / 2)
                .addChild(new LUIFrequencyVisualizer("FreqVisualizer", 8))
                .setAnchors(0, 1, 0,  1f)
                .setOffsets(0, 0, 0, 0)
                .setDebug(false);

        UIComponent tempoManagementContainer = this.addChild(new LUILightColorRectComponent("BPMDisplayContainer", LightTree.getInstance().newUI.borderWidth))
                .setAnchors(0, 1, 0.2f, 0.8f)
                .setOffsets(0, 0, margin / 2, -margin / 2);
        tempoManagementContainer.addChild(new LUITempoBPMDisplay("BPMDisplay"))
                .setAnchors(0, 1,  0, 0.33f)
                .setOffsets(0, 0, 0, 0);
        int multiplierToggleMargin = 10;
        UIComponent autoManualContainer = tempoManagementContainer.addChild(new UIComponent("AutoManualContainer"))
                .setAnchors(0, 1, 0.5f, 0.5f)
                .setOffsets(margin, -margin, -30, 30)
                .setDebug(false);
        ToggleGroup autoManualToggles = (ToggleGroup) autoManualContainer.addChild(new ToggleGroup("autoManualToggleGroup"));
        autoManualToggles.addToggle((ToggleComponent) autoManualContainer.addChild(new LUIToggleButton("ManualButton", "MANUAL")
        .setToggled(true))
                .setAnchors(0f, 0.5f, 0, 1)
                .setOffsets(multiplierToggleMargin, -multiplierToggleMargin, 0, 0));
        autoManualToggles.addToggle((ToggleComponent) autoManualContainer.addChild(new LUIToggleButton("AutoButton", "AUTO"))
                .setAnchors(0.5f, 1f, 0, 1)
                .setOffsets(multiplierToggleMargin, -multiplierToggleMargin, 0, 0));

        UIComponent multipliersContainer = tempoManagementContainer.addChild(new UIComponent("AutoManualContainer"))
                .setAnchors(0, 1, 1, 1)
                .setOffsets(margin, -margin, -200, -30)
                .setDebug(false);

        multipliersContainer.addChild(new LUIToggleButton("x1", "X1", 20,
                () -> LightTree.getInstance().newUI.uiGradient.get(0.5f), 3).setToggled(true))
                .setAnchors(0, 0.33f, 0, 0.5f)
                .setOffsets(multiplierToggleMargin, -multiplierToggleMargin, 0, -multiplierToggleMargin);
        multipliersContainer.addChild(new LUIToggleButton("x2", "X2", 20, () -> 0xFF00ff5e, 3))
                .setAnchors(0.33f, 0.66f, 0f, 0.5f)
                .setOffsets(multiplierToggleMargin, -multiplierToggleMargin, 0, -multiplierToggleMargin);
        multipliersContainer.addChild(new LUIToggleButton("x4", "X4", 20, () -> 0xFFffe626, 3))
                .setAnchors(0.66f, 1f, 0f, 0.5f)
                .setOffsets(multiplierToggleMargin, -multiplierToggleMargin, 0, -multiplierToggleMargin);
        multipliersContainer.addChild(new LUIToggleButton("x8", "X8", 20, () -> 0xFFff6f00, 3))
                .setAnchors(0, 0.33f, 0.5f, 1f)
                .setOffsets(multiplierToggleMargin, -multiplierToggleMargin, multiplierToggleMargin, 0);
        multipliersContainer.addChild(new LUIToggleButton("x16", "X16", 20, () -> 0xFFff5454, 3))
                .setAnchors(0.33f, 0.66f, 0.5f, 1f)
                .setOffsets(multiplierToggleMargin, -multiplierToggleMargin, multiplierToggleMargin, 0);
        multipliersContainer.addChild(new LUIToggleButton("x32", "X32", 20, () -> 0xFFf347ff, 3))
                .setAnchors(0.66f, 1f, 0.5f, 1f)
                .setOffsets(multiplierToggleMargin, -multiplierToggleMargin, multiplierToggleMargin, 0);
        ToggleGroup multiplierToggleGroup = (ToggleGroup) multipliersContainer.addChild(new ToggleGroup("MultiplierToggleGroup"));
        for(UIComponent toggle : multipliersContainer.getChildren()) {
            if(toggle instanceof ToggleComponent) {
                multiplierToggleGroup.addToggle((ToggleComponent) toggle);
            }
        }



//        tempoManagementContainer.addChild(new LUIButton("Test",  "TEST", 29, () -> LightTree.getInstance().newUI.uiColors.get(0.5f), 3))
//                .setAnchors(0, 1, 0.33f, 0.66f)
//                .setOffsets(50, -50, 15, -15);
//        System.out.println(tempoManagementContainer.width);
//        tempoManagementContainer.addChild(new LUIGradientRectComponent(
//                "Test1",
//                new ColorGradient(p.color(0, 255, 255), 0, p.color(200, 255, 255), 1),
//                LUIGradientRectComponent.VERTICAL,
//                3,
//                LightTree.getInstance().newUI.borderCornerRadius,
//                true,
//                true))
//                .setAnchors(0, 1, 0.33f, 0.66f)
//                .setOffsets(50, -50, 15, -15)
//                .setDebug(false);
//        UIComponent test1 = tempoManagementContainer.addChild(new RectComponent("rect", p.color(0, 255, 255)) {
//            @Override
//            public void setup() {
//                System.out.println(this.width);
//                System.out.println(this.height);
//            }
//
//            @Override
//            public void draw() {
//                super.draw();
//                System.out.println(this.width);
//                System.out.println(this.height);
//            }
//        })
//                .setAnchors(0, 1, 0.33f, 0.66f)
//                .setOffsets(50, -50, 15, -15);
//        System.out.println(test1.width);
//        System.out.println(test1.height);




//        this.addChild(new LUILightColorRectComponent("TempoManagerContainer", LightTree.getInstance().newUI.borderWidth))
//                .setAnchors(0, 1, 0.4f, 1)
//                .setOffsets(0, 0, margin / 2, 0)
//                .addChild(new LUITempoOptionsPanel("TempoManager"))
//                .setAnchors(0, 1,  0, 1)
//                .setOffsets(0, 0, 0, 0);


//        this.insert(this.getContainer().getChildren().size(), new RectComponent("item")
//                .setFillColor(p.color(p.random(0, 360), 255, 255))
//                .setStrokeWeight(0)
//                .setStrokeColor(p.color(200, 255, 255))
//                .setOffsets(10, -10, 0, (int) p.random(40, 90))
//                .setDebug(true));
//        this.insert(this.getContainer().getChildren().size(), new RectComponent("item")
//                .setFillColor(p.color(p.random(0, 360), 255, 255))
//                .setStrokeWeight(0)
//                .setStrokeColor(p.color(200, 255, 255))
//                .setOffsets(10, -10, 0, (int) p.random(40, 90))
//                .setDebug(true));

//        this.insert(this.getContainer().getChildren().size(), new LUIFrequencyVisualizer("FreqVisualizer", 4)
//                .setOffsets(0, -0, 0, 200)
//                .setDebug(false));
//        this.insert(0, new LUIFrequencyVisualizer("BandVisualizer", 5))
//                 .setOffsets(30,
//                         -30,
//                         0, 200)
//                 .setDebug(true);
//
//        this.insert(1, new LUIFrequencyVisualizer("BandVisualizer", 5))
//                .setOffsets(30,
//                        -30,
//                        0, 200)
//                .setDebug(true);

    }
}
