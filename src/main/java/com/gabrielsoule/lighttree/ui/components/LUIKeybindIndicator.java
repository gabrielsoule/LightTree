package com.gabrielsoule.lighttree.ui.components;

import com.gabrielsoule.lighttree.LightTree;
import com.gabrielsoule.lightui.RectComponent;
import com.gabrielsoule.lightui.TextComponent;
import com.gabrielsoule.lightui.UIComponent;

import java.util.function.Supplier;

public class LUIKeybindIndicator extends UIComponent {
    private final String key;
    private final Supplier<Integer> color;
    private final TextComponent text;
    private final RectComponent rect;

    public LUIKeybindIndicator(String name, String key, Supplier<Integer> color) {
        super(name);
        this.key = key;
        this.color = color;
        this.rect = (RectComponent) this.addChild(new RectComponent("Border", 0x00FFFFFF, 0, 1, LightTree.getInstance().newUI.borderCornerRadius));
        this.text = (TextComponent) this.addChild(new TextComponent("KeyText", key.toUpperCase(), LightTree.getInstance().newUI.fontRegular, 15));
        this.text.setTextVerticalOffset(-0.1f);
    }

    @Override
    public void draw() {
        text.setTextColor(color.get());
        rect.setStrokeColor(color.get());
        super.draw();
    }
}
