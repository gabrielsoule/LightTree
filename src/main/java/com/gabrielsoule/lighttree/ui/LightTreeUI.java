package com.gabrielsoule.lighttree.ui;

import com.gabrielsoule.lighttree.ColorGradient;
import com.gabrielsoule.lighttree.LightEffect;
import com.gabrielsoule.lighttree.LightTree;
import com.gabrielsoule.lighttree.ui.components.*;
import com.gabrielsoule.lighttree.util.Color;
import com.gabrielsoule.lightui.*;
import processing.core.PConstants;
import processing.core.PFont;
import processing.core.PGraphics;

import java.util.HashMap;

public class LightTreeUI {


    public static class GradientPanelComponent extends GraphicsComponent {

        public GradientPanelComponent(String name) {
            super(name);
        }

        @Override
        public void draw() {

        }

        @Override
        public void createGraphics(PGraphics graphics) {

        }

        @Override
        public void createMask(PGraphics graphics) {
            super.createMask(graphics);
        }

        @Override
        public void drawGraphics(PGraphics graphics) {
            super.drawGraphics(graphics);
        }

        @Override
        public void drawMask(PGraphics graphics) {
            super.drawMask(graphics);
        }
    }

    public class LightVisualizerComponent extends GraphicsComponent {

        public RectComponent border;

        public LightVisualizerComponent(String name) {
            super(name);
            this.addChild(new UIComponent("test"));
        }

        @Override
        public void createGraphics(PGraphics graphics) {

        }
//
//        @Override
        public void createMask(PGraphics graphics) {
            graphics.noSmooth();
            graphics.noStroke();
            graphics.fill(p.color(0,0, 255));
            graphics.rect(0, 0, graphics.width, graphics.height, graphics.height / 2 + 1);
        }

        @Override
        public void drawGraphics(PGraphics graphics) {
            graphics.noStroke();
            for(int i = 0; i < LightTree.getInstance().NUM_LIGHTS; i++) {
                graphics.fill((LightTree.getInstance().lightColors[i] & (0xFFFFFF)) > 0
                        ? LightTree.getInstance().lightColors[i] : LightTree.getInstance().newUI.backgroundColor);
                    graphics.rect(
                            ((graphics.width / (float) LightTree.getInstance().NUM_LIGHTS) * i),
                            0,
                            (width / (float) LightTree.getInstance().NUM_LIGHTS) * (i + 1),
                            graphics.height);
            }
        }
    }

    public class LUIAccentColorText extends TextComponent {
        public LUIAccentColorText(String name, String text, PFont font, int fontSize) {
            super(name, text, font, fontSize);
//            this.setTextVerticalOffset(-0.1f);
        }

        @Override
        public void draw() {
            this.setTextColor(borderColor);
            super.draw();
        }
    }

    public class LUIVisualizer extends UIComponent {
        private int numLightsX;
        private int numLightsY;
        private int lightDiameter;
        private int lightMarginX;
        private int lightMarginY;

        public int calculatedWidth;
        public int calculatedHeight;

        public LUIVisualizer(String name, int numLightsX, int numLightsY, int lightDiameter, int lightMarginX, int lightMarginY) {
            super(name);
            this.numLightsX = numLightsX;
            this.numLightsY = numLightsY;
            this.lightDiameter = lightDiameter;
            this.lightMarginX = lightMarginX;
            this.lightMarginY = lightMarginY;
            this.calculatedWidth = (this.numLightsX * (this.lightDiameter + this.lightMarginX)) - this.lightMarginX;
            this.calculatedHeight = (this.numLightsY * (this.lightDiameter + this.lightMarginY)) - this.lightMarginY;
        }

        @Override
        public void draw() {
            p.ellipseMode(PConstants.CENTER);
            p.noStroke();
            int index = 0;
            for(int x = this.rawPosX; x < this.calculatedWidth + rawPosX; x += lightDiameter + lightMarginX) {
                for(int y = this.rawPosY; y < this.calculatedHeight + rawPosY; y += lightDiameter + lightMarginY) {
                    int color = LightTree.getInstance().lightColors[index];
                    if((color & 0xFFFFFF) > 0) {
                        p.fill(LightTree.getInstance().lightColors[index]);
                    } else {
                        p.fill(backgroundColor);
                    }
                    float scaledDiameter =lightDiameter * 0.7f + 0.3f * (lightDiameter * (Color.getBrightness(color) / 255f));
                    p.ellipse(x + lightDiameter / 2f, y + lightDiameter / 2f, scaledDiameter, scaledDiameter);
                    index++;
                }
            }
        }
    }

    private LightTree p;
    private Canvas canvas;

    public int averageHue;
    public int averageColor;
    public int backgroundColor; //let's cache this for efficiency
    public int borderColor;

    public ColorGradient uiGradient;
    public ColorGradient effectGradient;

    public int borderWidth = 3;
    public int borderCornerRadius = 4;
    public int mainPanelsMargin = 20;
    public PFont fontRegular;
    public PFont fontBold;
    public final PFont fontLight;


    public HashMap<LightEffect, ColorGradient> effectGradients;

    public LightTreeUI() {
        this.p = LightTree.getInstance();
        this.effectGradients = new HashMap<>();
        this.fontRegular = p.createFont("futura-demi.otf", 200);
//        this.fontBold = p.createFont("Proxima Nova Bold.otf", 200);
        this.fontBold = p.createFont("futura-heavy.otf", 200);
        this.fontLight = p.createFont("futura-medium.otf", 200);
    }


    public void loadUI() {
        this.canvas = new Canvas(p);
        canvas.setDebug(true);

        for(LightEffect effect : LightTree.getInstance().sequencer.getEffects().values()) {
            effectGradients.put(effect, new ColorGradient());
            float current = 0f;
            float increment = 1 / (float) (effect.getConfig().getColors().size() - 1);
            effectGradients.get(effect).putColor(1f, //yiiikes this fucking line lmao
                    () -> effect.getConfig().getColors().get(effect.getConfig().getColors().size() - 1)
                            == Color.RANDOM ? p.color((p.millis() / 30) % 359, 255, 255) :
                            effect.getConfig().getColors().get(effect.getConfig().getColors().size() - 1));
            effectGradients.get(effect).putColor(0, //yiiikes this fucking line lmao
                    () -> effect.getConfig().getColors().get(0)
                            == Color.RANDOM ? p.color((p.millis() / 30) % 359, 255, 255) :
                            effect.getConfig().getColors().get(0));
            for(int color : effect.getConfig().getColors()) {
                effectGradients.get(effect).putColor(current,
                        () -> color == Color.RANDOM ? p.color((p.millis() / 30) % 359, 255, 255) : color);
                current += increment;
            }
        }

        LightEffect activeEffect = LightTree.getInstance().sequencer.getActiveEffects().get(0);
        this.effectGradient = effectGradients.get(activeEffect);

        this.uiGradient = new ColorGradient();
        this.uiGradient.putColor(0, 0xFF000000);
        this.uiGradient.putColor(1, 0xFFFFFFFF);

        LUILightColorRectComponent visualizerContainer = new LUILightColorRectComponent("VisualizerContainer", borderWidth);
        visualizerContainer.setCornerRadius(borderCornerRadius);
        visualizerContainer.setAnchors(0.5f, 0.75f, 0, 1);
        visualizerContainer.setOffsets(mainPanelsMargin, -mainPanelsMargin / 2, mainPanelsMargin,  -mainPanelsMargin);
        visualizerContainer.setStrokeAnchor(RectComponent.INNER);
        visualizerContainer.setDebug(false);
        canvas.addChild(visualizerContainer);

        visualizerContainer.addChild(new LUIVisualizerPanel("Visualizer", 16, 32, 20));

//        LUILightColorRectComponent effectsContainer = new LUILightColorRectComponent("EffectsContainer", borderWidth);
//        LUIGradientRectComponent effectsContainer =
//                new LUIGradientRectComponent("EffectsContainer", LightTree.getInstance().newUI.effectGradient, LUIGradientRectComponent.HORIZONTAL) {
//                    @Override
//                    public void drawGraphics(PGraphics graphics) {
//                        this.setGradient(LightTree.getInstance().newUI.effectGradient);
//                        super.drawGraphics(graphics);
//                    }
//                };
        UIComponent effectsContainer = new UIComponent("EffectsContainer");
        effectsContainer.setAnchors(0f, 0.5f, 0, 1);
        effectsContainer.setOffsets(mainPanelsMargin / 2, -mainPanelsMargin / 2, mainPanelsMargin,  -mainPanelsMargin);
        effectsContainer.setDebug(false);
        canvas.addChild(effectsContainer);
        effectsContainer.addChild(new LUIEffectPanel("EffectsPanel"))
                .setAnchors(0, 1, 0, 1)
                .setOffsets(0, 0, 0, 0);
//
        LUILightColorRectComponent tempoContainer = new LUILightColorRectComponent("TempoContainer", 0);
        tempoContainer.setCornerRadius(borderCornerRadius);
        tempoContainer.setAnchors(0.75f, 1, 0, 1);
        tempoContainer.setOffsets(mainPanelsMargin / 2, -mainPanelsMargin, mainPanelsMargin,  -mainPanelsMargin);
        canvas.addChild(tempoContainer);

        tempoContainer.addChild(new LUITempoPanel("TempoPanel"));
    }



//    public LightTreeUI(LightTree p) {
//        this.p = p;
//        this.canvas = new Canvas(p);
//        this.fontRegular = p.createFont("ProximaNova-Regular.otf", 200);
//        this.fontBold = p.createFont("Proxima Nova Bold.otf", 200);
//
//
//        canvas.addChild(new RectComponent("background"){
//            @Override
//            public void draw() {
//
//
//                this.setFillColor(backgroundColor);
//                super.draw();
//            }
//        })
//                .setAnchors(0, 1, 0, 1)
//                .setOffsets(0, 0, 0, 0);
//        UIComponent leftSection = canvas.addChild(new RectComponent("LeftSection", 0x00FFFFFF, 0, 3, 5) {
//            @Override
//            public void draw() {
//                this.setStrokeColor(accentColor);
//                super.draw();
//            }
//        })
//                .setAnchors(0, 0, 0, 1)
//                .setOffsets(20, 420, 20, -20).setDebug(false);
//        LUIVisualizer visualizer = new LUIVisualizer("Visualizer", 16, 32, 15, 6, 8);
//        visualizer.setAnchors(0.5f, 0.5f, 0.5f, 0.5f);
//        visualizer.setOffsets(-visualizer.calculatedWidth / 2, visualizer.calculatedWidth / 2,
//                -visualizer.calculatedHeight / 2, visualizer.calculatedHeight / 2);
//        visualizer.setDebug(false);
//        leftSection.addChild(visualizer);
////        UIComponent topSection = canvas.addChild(new UIComponent("topSection"))
////                .setAnchors(0, 1, 0, 0)
////                .setOffsets(0, 0, 0,  200);
////        topSection.addChild(new LightVisualizerComponent("Visualizer"))
////                .setAnchors(0, 1, 1, 1)
////                .setOffsets(20, -20, 0, 20);
////
////        UIComponent bpmTextSection = topSection.addChild(new RectComponent("bpmTextSection"){
////            @Override
////            public void draw() {
////                this.setFillColor(Color.bakeAlpha(Color.setAlpha(averageColor, 50)));
////                this.setStrokeColor(accentColor);
////                super.draw();
////            }
////        }
////                .setCornerRadius(20)
////                .setAnchors(0, 0.2f, 0, 1)
////                .setOffsets(20, -20, 20, -20)
////
////        );
////
////        bpmTextSection.addChild(new LUIAccentColorText("BPMNumber", "", fontBold, 50) {
////            @Override
////            public void draw() {
////                this.setText(Integer.toString(LightTree.getInstance().beatDetector.getEstBPM()));
////                super.draw();
////            }
////        }
////        .setVerticalAlignment(TextComponent.TOP)
////        .setHorizontalAlignment(TextComponent.LEFT)
////        .setOffsets(20, 0, 0, 0))
////        .setDebug(true);
//
//
//    }


    public void draw() {
        LightEffect activeEffect = LightTree.getInstance().sequencer.getActiveEffects().get(0);

//        float current = 0;
//        float increment = 1 / (float) (activeEffect.getConfig().getColors().size() - 1);
//        for(int color : activeEffect.getConfig().getColors()) {
//            effectGradients.get(activeEffect).putColor(current,
//                    () -> color == Color.RANDOM ? p.color(p.millis() / 100, 255, 255) : color);
//            current += increment;
//        }

        this.effectGradient = effectGradients.get(activeEffect);

        averageColor = Color.getAverageColor(LightTree.getInstance().lightColors);
        averageHue = Color.getHue(averageColor);
        int saturation = Color.getSaturation(averageColor);
        uiGradient.putColor(0.5f, p.color(averageHue, saturation, 255));
        backgroundColor = saturation > 10 ? uiGradient.get(0.05f) : 0xFF000000;
        borderColor = uiGradient.get(0.3f);
//        backgroundColor = Color.bakeAlpha(Color.setAlpha(averageColor, 25));
//        accentColor = Color.bakeAlpha(Color.setAlpha(averageColor, 255));
        p.background(backgroundColor);
        this.canvas.draw();
    }

}
