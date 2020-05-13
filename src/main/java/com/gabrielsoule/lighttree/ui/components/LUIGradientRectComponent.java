package com.gabrielsoule.lighttree.ui.components;

import com.gabrielsoule.lighttree.ColorGradient;
import com.gabrielsoule.lighttree.LightTree;
import com.gabrielsoule.lightui.GraphicsComponent;
import processing.core.PConstants;
import processing.core.PGraphics;

public class LUIGradientRectComponent extends GraphicsComponent {

    public static final int VERTICAL = 0;
    public static final int HORIZONTAL = 1;



    private ColorGradient gradient;
    private int direction;
    private boolean fill;
    private int strokeWeight;
    private int cornerRadius;
    private boolean dynamic;
    private int pixelsPerColor = 1;

    private int[] colorCache;
    private float incrementAmount;

    public LUIGradientRectComponent(String name, ColorGradient gradient, int direction) {
        this(name, gradient, direction, LightTree.getInstance().newUI.borderWidth, LightTree.getInstance().newUI.borderCornerRadius, true);
    }

    public LUIGradientRectComponent(String name, ColorGradient gradient, int direction, int strokeWeight, int cornerRadius, boolean fill) {
        this(name, gradient, direction, strokeWeight, cornerRadius, fill, true);
    }

    public LUIGradientRectComponent(String name, ColorGradient gradient, int direction, int strokeWeight, int cornerRadius, boolean fill, boolean dynamic) {
        super(name);
        this.gradient = gradient;
        this.fill = fill;
        this.direction = direction;
        this.strokeWeight = strokeWeight;
        this.cornerRadius = cornerRadius;
        this.dynamic = dynamic;
        this.useMask = true;
    }

    private void calculateColors() {
        float current = 0.0001f; //TODO fix the bug that is the reason for this....
        for(int i = 0; i < colorCache.length; i++) {
            colorCache[i] = gradient.get(current);
            current += incrementAmount;
        }
    }

    @Override
    public void createGraphics(PGraphics graphics) {
        colorCache = new int[(direction == HORIZONTAL ? graphics.width : graphics.height)];
        this.incrementAmount = 1 / (float) colorCache.length;
        calculateColors();
    }

    @Override
    public void createMask(PGraphics graphics) {
        graphics.rectMode(PConstants.CORNER);
        if(this.fill) graphics.fill(0xFFFFFF);
        else graphics.noFill();
        if(this.strokeWeight == 0) graphics.noStroke();
        else {
            graphics.stroke(0xFFFFFFFF);
            graphics.strokeWeight(strokeWeight);
        }

        graphics.rect(this.strokeWeight / 2,
                this.strokeWeight / 2,
                this.width - (this.strokeWeight - 1),
                this.height - (this.strokeWeight  -1),
                cornerRadius);

    }

    @Override
    public void drawGraphics(PGraphics graphics) {
//        graphics.rectMode(PConstants.CORNER);
//        if(this.fill) p.fill(0xFFFFFF);
//        else p.noFill();
//        if(this.strokeWeight == 0) p.noStroke();
//        else {
//            p.stroke(0xFFFFFFFF);
//            p.strokeWeight(strokeWeight);
//        }
//
//        p.rect(0,
//                0,
//                this.width,
//                this.height,
//                0);

        if(dynamic) {
            calculateColors();
            graphics.loadPixels();
//            for(int i = 0; i < graphics.pixels.length; i++) {
//                graphics.pixels[i] = 0xFFdd00dd;
//            }
            if(this.direction == VERTICAL) {
                for(int y = 0; y < graphics.height; y++) {
                    for(int x = 0; x < graphics.width; x++) {
                        graphics.pixels[y * graphics.width + x] = colorCache[y];
                    }
                }
            } else {
                for(int x = 0; x < graphics.width; x++) {
                    for(int y = 0; y < graphics.height; y++) {
                        graphics.pixels[y * graphics.width + x] = colorCache[x];
                    }
                }
            }

            graphics.updatePixels();
        }
    }

    @Override
    public void drawMask(PGraphics graphics) {
//        graphics.rectMode(PConstants.CORNER);
//        if(this.fill) graphics.fill(0xFFFFFF);
//        else graphics.noFill();
//        if(this.strokeWeight == 0) graphics.noStroke();
//        else graphics.strokeWeight(strokeWeight);
//
//        graphics.rect(strokeWeight / 2,
//                strokeWeight / 2,
//                graphics.width - strokeWeight / 2,
//                graphics.height - strokeWeight / 2,
//                borderRadius);
    }

    public ColorGradient getGradient() {
        return gradient;
    }

    public LUIGradientRectComponent setGradient(ColorGradient gradient) {
        this.gradient = gradient;
        return this;
    }

    public int getPixelsPerColor() {
        return pixelsPerColor;
    }

    public LUIGradientRectComponent setPixelsPerColor(int pixelsPerColor) {
        this.pixelsPerColor = pixelsPerColor;
        this.recalculate();
        return this;
    }
}
