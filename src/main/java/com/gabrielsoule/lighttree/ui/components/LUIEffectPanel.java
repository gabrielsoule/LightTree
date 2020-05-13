package com.gabrielsoule.lighttree.ui.components;

import com.gabrielsoule.lighttree.ColorGradient;
import com.gabrielsoule.lighttree.LightEffect;
import com.gabrielsoule.lighttree.LightTree;
import com.gabrielsoule.lighttree.effect.config.LightEffectOption;
import com.gabrielsoule.lighttree.effect.config.NumericalOption;
import com.gabrielsoule.lighttree.effect.config.ToggleOption;
import com.gabrielsoule.lighttree.ui.LightTreeUI;
import com.gabrielsoule.lightui.*;
import org.w3c.dom.css.Rect;
import processing.core.PGraphics;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;


//The BIG one.
//Oh boy.
public class LUIEffectPanel extends UIComponent {

    public class LUIEffectList extends VerticalListComponent{

        public class LUIEffectListItem extends ToggleComponent {

            private final RectComponent leftBorderCover;
            private LUIEffectOptionsPanel optionsPanel;
            private LightEffect effect;
            LUIGradientRectComponent rect;
            TextComponent text;
            LUIGradientRectComponent selectedOutline;
            private float gradientPos;

            public LUIEffectListItem(String name, LightEffect effect, LUIEffectOptionsPanel optionsPanel) {
                super(name);
                this.effect = effect;
                this.optionsPanel = optionsPanel;
                optionsPanel.hide();
                this.text = (TextComponent) this.addChild(
                        new TextComponent("Text", effect.getName(), LightTree.getInstance().newUI.fontBold, 22));
                this.text.setHorizontalAlignment(TextComponent.LEFT);
                this.rect =
                        (LUIGradientRectComponent) this.addChild(
                                new LUIGradientRectComponent("Outline", new ColorGradient(), LUIGradientRectComponent.HORIZONTAL) {
                                    @Override
                                    public void draw() {
                                        super.draw();
                                    }
                                });
                this.rect.hide();
                this.leftBorderCover = new RectComponent("borderCover",0, 0, 0) {
                    @Override
                    public void draw() {
                        this.setFillColor(LightTree.getInstance().newUI.backgroundColor);
                        super.draw();
                    }
                };
                int borderWidth = LightTree.getInstance().newUI.borderWidth;
                leftBorderCover.setOffsets(-borderWidth, 1, borderWidth, -borderWidth + 1);
                leftBorderCover.setAnchors(1, 1, 0, 1);
                leftBorderCover.hide();
                leftBorderCover.setDebug(false);
                rect.addChild(leftBorderCover);
            }

            @Override
            public void draw() {
                super.draw();
                if(this.isToggled()) {
                    this.rect.getGradient().putColor(0, LightTree.getInstance().newUI.effectGradient.get(0.001f));
                    this.rect.getGradient().putColor(1, LightTree.getInstance().newUI.effectGradient.get(gradientPos));
                    this.text.setTextColor(LightTree.getInstance().newUI.effectGradient.get(0.001f));
                    this.text.setFontSize(24);

                } else if(this.getState() == InteractionState.HOVER){
                    this.text.setTextColor(LightTree.getInstance().newUI.effectGradient.get(0.5f));
                    this.text.setFontSize(24);
                } else {
                    this.text.setTextColor(0x60FFFFFF);
                    this.text.setFontSize(22);
                }

                this.text.setOffsets(20, 0,  0, 0);
            }

            @Override
            public void onPostResize() {
                this.gradientPos = 0.25f;
            }

            @Override
            public void onToggleEnable() {
                this.rect.show();
                this.leftBorderCover.show();
                LightTree.getInstance().sequencer.setActiveEffect(effect);
                this.optionsPanel.show();

            }

            @Override
            public void onToggleDisable() {
                this.rect.hide();
                this.leftBorderCover.hide();
                this.optionsPanel.hide();
            }
        }

        private ToggleGroup toggleGroup;
        private HashMap<LightEffect, LUIEffectListItem> listItemsCache;
        private LightEffect activeEffect;

        public LUIEffectList(String name) {
            super(name, STACK_ELEMENTS, CENTER);
            this.toggleGroup = (ToggleGroup) this.addChild(new ToggleGroup("EffectToggleGroup"));
            this.setSpacing(10);
            this.listItemsCache = new HashMap<>();
        }


//        public void addItems(Collection<LightEffect> items) {
//            for(LightEffect effect : items) {
//                LUIEffectListItem item = new LUIEffectListItem(effect.getName(), effect);
//                if(LightTree.getInstance().sequencer.getActiveEffects().get(0) == effect) {
//                    item.setToggled(true);
//                    this.activeEffect = effect;
//                }
//
//                item.setOffsets(0, LightTree.getInstance().newUI.borderWidth - 1, 0, 60);
//                this.addElement(item);
//                this.toggleGroup.addToggle(item);
//                listItemsCache.put(effect,  item);
//
//            }
//        }

        public void addItem(LightEffect effect, LUIEffectOptionsPanel panel) {
            LUIEffectListItem item = new LUIEffectListItem(effect.getName(), effect, panel);
            if(LightTree.getInstance().sequencer.getActiveEffects().get(0) == effect) {
                item.setToggled(true);
                this.activeEffect = effect;
            }

            item.setOffsets(0, LightTree.getInstance().newUI.borderWidth - 1, 0, 60);
            this.addElement(item);
            this.toggleGroup.addToggle(item);
            listItemsCache.put(effect,  item);
        }

        @Override
        public void draw() {
            if((LightTree.getInstance().sequencer.getActiveEffects().get(0) != activeEffect)) {
                this.activeEffect = LightTree.getInstance().sequencer.getActiveEffects().get(0);
                this.listItemsCache.get(activeEffect).onMouseClick();
            }
            super.draw();
        }
    }



    public class LUIEffectOptionsPanel extends UIComponent {

        private final LightEffect effect;
        private VerticalListComponent optionsList;

        public class LUIEffectToggleOption extends UIComponent {

            private ToggleComponent toggleArea;
            TextComponent infoString;
            HorizontalListComponent list;

            LUILightColorRectComponent outerRect;
            RectComponent check;
            LUIKeybindIndicator key;
            public LUIEffectToggleOption(String name, ToggleOption option) {
                super(name);
                this.infoString = new TextComponent("infoString", option.getOptionText(), LightTree.getInstance().newUI.fontLight,22){
                    @Override
                    public void draw() {
                        this.setTextColor(0x99FFFFFF);
                        super.draw();
                    }
                };
                this.infoString.setAnchors(0, 1, 0, 1);
                this.infoString.setHorizontalAlignment(TextComponent.LEFT);
                this.infoString.setOffsets(10, 0, 0, 0);
                this.infoString.setTextVerticalOffset(-0.1f);
                this.addChild(infoString);
                this.list = new HorizontalListComponent("list", HorizontalListComponent.STACK_ELEMENTS, HorizontalListComponent.RIGHT);
                list.setAnchors(1, 1, 0.5f, 0.5f);
                list.setOffsets(0, 0, -15, 15);
                list.setSpacing(15);

                this.key = (LUIKeybindIndicator) (new LUIKeybindIndicator("keybindIndicator", option.getToggleKey(), () -> 0x99FFFFFF))
                        .setOffsets(-10, 10, 5, -5);
                this.check = (RectComponent) new RectComponent("check", 0, 0, 0, 0){
                    @Override
                    public void draw() {
                        setFillColor(LightTree.getInstance().newUI.effectGradient.get(0.9f));
                    }
                }.setAnchors(0.2f, 0.8f, 0.2f, 0.8f);
                this.toggleArea = new ToggleComponent("Toggle") {
                    @Override
                    public void onToggleEnable() {
                        if(check != null) check.show();
                    }

                    @Override
                    public void onToggleDisable() {
                        if(check != null) check.hide();
                    }
                };
                toggleArea.setOffsets(-15, 15, 0, 0);
                this.outerRect = (LUILightColorRectComponent) new LUILightColorRectComponent("toggleBoxOuter", 0.5f)
                        .setOffsets(0, 0, 0, 0);
                toggleArea.addChild(outerRect);
                outerRect.addChild(check);
                if(option.getDefaultValue()) {
                    toggleArea.onToggleEnable();
                } else {
                    toggleArea.onToggleDisable();
                }



                list.addElement(toggleArea);
                list.addElement(key);
                this.addChild(list);
            }
        }

        public class LUIEffectNumericalOption extends UIComponent {

            TextComponent infoString;

            HorizontalListComponent list;
            LUIKeybindIndicator decrementKey;
            LUIKeybindIndicator incrementKey;
            LUIButton incrementButton;
            LUIButton decrementButton;
            TextComponent valueText;

            public LUIEffectNumericalOption(String name, NumericalOption option) {
                super(name);
                this.infoString = new TextComponent("infoString", option.getOptionText(), LightTree.getInstance().newUI.fontLight,22){
                    @Override
                    public void draw() {
                        this.setTextColor(0x99FFFFFF);
                        super.draw();
                    }
                };
                this.infoString.setAnchors(0, 1, 0, 1);
                this.infoString.setHorizontalAlignment(TextComponent.LEFT);
                this.infoString.setOffsets(10, 0, 0, 0);
                this.infoString.setTextVerticalOffset(-0.1f);
                this.addChild(infoString);
                this.list = new HorizontalListComponent("list", HorizontalListComponent.STACK_ELEMENTS, HorizontalListComponent.RIGHT);
                list.setAnchors(1, 1, 0.5f, 0.5f);
                list.setOffsets(0, 0, -15, 15);
                list.setSpacing(15);
                this.decrementKey = (LUIKeybindIndicator) (new LUIKeybindIndicator("keybindIndicator", option.getDecrementKey(), () -> 0x99FFFFFF))
                        .setOffsets(-10, 10, 5, -5);
                this.incrementKey = (LUIKeybindIndicator) (new LUIKeybindIndicator("keybindIndicator", option.getIncrementKey(), () -> 0x99FFFFFF))
                        .setOffsets(-10, 10, 5, -5);
                this.decrementButton = (LUIButton) (new LUIButton("DecrementButton", "-", 26)
                        .setOffsets(-15, 15, 0, 0));
                decrementButton.setColorSupplier(() -> getGradientColor(decrementButton));
                decrementButton.getRect().setCornerRadius(0);

                this.incrementButton = (LUIButton) (new LUIButton("DecrementButton", "+", 26))
                        .setOffsets(-15, 15, 0, 0);
                incrementButton.setColorSupplier(() -> getGradientColor(incrementButton));
                incrementButton.getRect().setCornerRadius(0);
                this.valueText = (TextComponent) (new TextComponent("ValueText", Float.toString(option.getValue()), LightTree.getInstance().newUI.fontBold, 20))
                        .setOffsets(-30, 30, 0, 0);

                list.addElement(decrementKey);
                list.addElement(decrementButton);
                list.addElement(valueText);
                list.addElement(incrementButton);
                list.addElement(incrementKey);

                this.addChild(list);
            }
        }


        public LUIEffectOptionsPanel(LightEffect effect) {
            super(effect.getName() + " Panel");
            this.effect = effect;
            this.optionsList = new VerticalListComponent("optionsList", VerticalListComponent.STACK_ELEMENTS, VerticalListComponent.TOP);
            optionsList.setSpacing(80);
            optionsList.setAnchors(0, 1, 0, 1);
            optionsList.setOffsets(10, -10, 10, -10);
            this.addChild(optionsList);
            for(String key : effect.getConfig().getOptions().keySet()) {
                LightEffectOption option = effect.getConfig().getOptions().get(key);
                if(option instanceof ToggleOption) {
                    optionsList.addElement(new LUIEffectToggleOption("fuckthis", (ToggleOption) option))
                            .setOffsets(0, 0, 0, 70)
                            .setDebug(false);
                } else if(option instanceof NumericalOption) {
                    optionsList.addElement(new LUIEffectNumericalOption("fuckthis", (NumericalOption) option))
                            .setOffsets(0, 0, 0, 70)
                            .setDebug(false);
                }
//                break;
            }
        }
    }

    LUIEffectList effectList;


    public LUIEffectPanel(String name) {
        super(name);

        LUIGradientRectComponent effectsContainer =
                new LUIGradientRectComponent("EffectOptionsContainer", LightTree.getInstance().newUI.effectGradient, LUIGradientRectComponent.HORIZONTAL) {
                    @Override
                    public void drawGraphics(PGraphics graphics) {
                        this.getGradient().putColor(0, LightTree.getInstance().newUI.effectGradient.get(0.25f));
                        this.getGradient().putColor(1, LightTree.getInstance().newUI.effectGradient.get(0.999f));
                        super.drawGraphics(graphics);
                    }
                };
        effectsContainer.setAnchors(0.3f, 1f, 0, 1);
        effectsContainer.setOffsets(0, 0, 0, 0);
        effectsContainer.setDebug(false);
        this.addChild(effectsContainer);

        this.effectList = (LUIEffectList) this.addChild(new LUIEffectList("EffectList"));
        this.effectList.setAnchors(0, 0.3f, 0, 1);
        this.effectList.setOffsets(0, 0, 0, 0);
        for(LightEffect effect : LightTree.getInstance().sequencer.getEffects().values()) {
            LUIEffectOptionsPanel optionsPanel = (LUIEffectOptionsPanel) effectsContainer.addChild(new LUIEffectOptionsPanel(effect))
                    .setOffsets(0, 0, 0, 0)
                    .setAnchors(0, 1,  0, 1)
                    .setDebug(false);
            effectList.addItem(effect, optionsPanel);

        }
//        effectList.addItems(LightTree.getInstance().sequencer.getEffects().values());

    }

    public int getGradientColor(UIComponent component) {
        return LightTree.getInstance().newUI.effectGradient.get((component.rawPosX  - this.rawPosX) / (float) this.width);
    }

}
