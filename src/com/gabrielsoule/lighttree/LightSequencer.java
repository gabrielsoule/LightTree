package com.gabrielsoule.lighttree;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LightSequencer {
    private LightTree p;
    private HashMap<String, LightEffect> effects;
    private ArrayList<LightEffect> activeEffects;
    private final float LIGHT_FALLOFF_EXPONENT = 0.8f;

    public LightSequencer(LightTree p) {
        this.p = p;
        this.effects = new HashMap<>();
        this.activeEffects = new ArrayList<>();
    }

    public void loadFromConfig(Config config) {
        HashMap<String, Object> effectConfigSection = (HashMap<String, Object>) config.getYamlObject().get("effect-keybinds");
        for(String key : effectConfigSection.keySet()) {
            HashMap<String, Object> effectConfig = (HashMap<String, Object>) effectConfigSection.get(key);
            try {
                LightTree.debug("Instantiating LightEffect: com.gabrielsoulr.lighttree.effect." + effectConfig.get("name"));
                LightEffect e = (LightEffect) Class.forName("com.gabrielsoule.lighttree.effect." + effectConfig.get("name")).getConstructor().newInstance();
                e.p = this.p;
                List<Integer> integerConfig = (List<Integer>) effectConfig.get("options");

            } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException ex) {
                ex.printStackTrace();
            }

        }
    }


    int[] sequence() {
        for(String key : effects.keySet()) {
            if(p.keyboardListener.keyPressed(key)) {
                LightEffect effect = effects.get(key);
                if(activeEffects.contains(effect)) {
                    LightTree.debug("Deactivating effect " + effect.getClass().getSimpleName());
                    activeEffects.remove(effect);
                    effect.sleep();
                } else {
                    LightTree.debug("Activating effect " + effect.getClass().getSimpleName());
                    activeEffects.add(effect);
                    effect.wake();
                }
            }

            for(LightEffect effect : activeEffects) {
                if(effect.isSleeping()) {
                    LightTree.debug("Deactivating sleeping effect " + effect.getClass().getSimpleName());
                    activeEffects.remove(effect);
                }

                effect.draw();
            }
        }

        return null; //TODO literally anything but this
    }

}
