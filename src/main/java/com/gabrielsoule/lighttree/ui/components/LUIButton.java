package com.gabrielsoule.lighttree.ui.components;

import com.gabrielsoule.lighttree.ColorGradient;
import com.gabrielsoule.lighttree.LightTree;
import com.gabrielsoule.lightui.InteractableComponent;
import com.gabrielsoule.lightui.RectComponent;
import com.gabrielsoule.lightui.TextComponent;

import java.util.function.Supplier;

public class LUIButton extends InteractableComponent {

    private RectComponent rect;
    private TextComponent text;

    private boolean useLightColors;
    private Supplier<Integer> colorSupplier;
    private ColorGradient colorGradient;
    private int lastColor;

    private int fillColor;
    private int strokeColor;
    private int textColor;

    private int inactiveFillColor;
    private int inactiveStrokeColor;
    private int inactiveTextColor;
    private int hoverFillColor;
    private int hoverStrokeColor;
    private int hoverTextColor;
    private int pressedFillColor;
    private int pressedStrokeColor;
    private int pressedTextColor;

    public LUIButton(String name, String text, int textSize) {
        this(name, text, textSize, () -> LightTree.getInstance().newUI.uiGradient.get(0.5f), LightTree.getInstance().newUI.borderWidth);
    }

    public LUIButton(String name, String text, int textSize, Supplier<Integer> colorSupplier, int strokeWidth) {
        super(name);
        this.colorSupplier = colorSupplier;
        this.colorGradient = new ColorGradient();
        this.colorGradient.putColor(0, 0xFF000000);
        this.colorGradient.putColor(0.5f, colorSupplier.get());
        this.colorGradient.putColor(1, 0xFFFFFFFF);
        this.rect = new RectComponent("BackgroundRect", colorSupplier.get(), colorSupplier.get(), strokeWidth);
        this.rect.setOffsets(0, 0, 0, 0);
        this.rect.setAnchors(0, 1, 0, 1);
        this.rect.setCornerRadius(LightTree.getInstance().newUI.borderCornerRadius);
        this.addChild(rect);
        this.text = new TextComponent("Text", text, LightTree.getInstance().newUI.fontBold, textSize);
        this.text.setOffsets(0, 0, 0, 0);
        this.text.setAnchors(0, 1, 0, 1);
        this.text.setVerticalAlignment(TextComponent.CENTER);
        this.text.setHorizontalAlignment(TextComponent.CENTER);
        this.text.setTextVerticalOffset(-0.1f);
        this.rect.addChild(this.text);

    }

    @Override
    public void setup() {
        this.calculateColors();
    }

    //
//    public LUIButton(String name, String text, int textSize, int fillColor, int strokeColor, int strokeWidth) {
//        this(name, text, textSize, () -> fillColor, () -> strokeColor, strokeWidth);
//    }

    private void calculateColors() {
        colorGradient.putColor(0.5f, colorSupplier.get());
        this.inactiveFillColor = 0x00FFFFFF;
        this.inactiveStrokeColor = colorSupplier.get();
        this.inactiveTextColor = colorGradient.get(0.6f);
        this.hoverFillColor = colorGradient.get(0.1f);
        this.hoverStrokeColor = colorGradient.get(0.6f);
        this.hoverTextColor = inactiveTextColor;
        this.pressedFillColor = hoverStrokeColor;
        this.pressedStrokeColor = hoverStrokeColor;
        this.pressedTextColor = colorGradient.get(0.05f);
    }

    @Override
    public void draw() {
        super.draw();
        if(colorSupplier.get() != lastColor) {
            lastColor = colorSupplier.get();
            calculateColors();
        }

        switch (this.getState()){
            case INACTIVE:
                this.fillColor = inactiveFillColor;
                this.strokeColor = inactiveStrokeColor;
                this.textColor = inactiveTextColor;
                break;
            case HOVER:
                this.fillColor = hoverFillColor;
                this.strokeColor = hoverStrokeColor;
                this.textColor = hoverTextColor;
                break;
            case PRESSED:
                this.fillColor = pressedFillColor;
                this.strokeColor = pressedStrokeColor;
                this.textColor = pressedTextColor;

        }

        rect.setFillColor(fillColor);
        rect.setStrokeColor(strokeColor);
        text.setTextColor(textColor);
    }

    public RectComponent getRect() {
        return rect;
    }

    public TextComponent getTextComponent() {
        return text;
    }

    public Supplier<Integer> getColorSupplier() {
        return colorSupplier;
    }

    public void setColorSupplier(Supplier<Integer> colorSupplier) {
        this.colorSupplier = colorSupplier;
    }

    //    @Override
//    public void onMouseClick() {
//        rect.setFillColor(pressedFillColor);
//        rect.setStrokeColor(pressedStrokeColor);
//        text.setTextColor(pressedTextColor);
//    }
//
//    @Override
//    public void onMouseRelease() {
//        rect.setFillColor(fillColor);
//        rect.setStrokeColor(strokeColor);
//        text.setTextColor(textColor);
//    }
//
//    @Override
//    public void onMouseHoverEnter() {
//        rect.setFillColor(hoverFillColor);
//        rect.setStrokeColor(hoverStrokeColor);
//        text.setTextColor(hoverTextColor);
//    }
//
//    @Override
//    public void onMouseDragExit() {
//        rect.setFillColor(fillColor);
//        rect.setStrokeColor(strokeColor);
//        text.setTextColor(textColor);
//    }
//
//    @Override
//    public void onMouseHoverExit() {
//        rect.setFillColor(fillColor);
//        rect.setStrokeColor(strokeColor);
//        text.setTextColor(textColor);
//    }
}
