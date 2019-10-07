package com.gabrielsoule.lighttree;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;

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
        HashMap<String, HashMap<String, String>> effectConfigSection = (HashMap<String, HashMap<String, String>>) config.getYamlObject().get("effect-keybinds");
        for(String key : effectConfigSection.keySet()) {
            HashMap<String, String> effectConfig = effectConfigSection.get(key);
            LightEffect e = null;
            try {
                e = (LightEffect) Class.forName("com.gabrielsoule.lighttree.effects." + effectConfig.get("name")).getConstructor(LightTree.class).newInstance(p);
            } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException ex) {
                ex.printStackTrace();
            }

            effects.put(key, e);
        }
    }
//
//    int[] sequence() {
//
//    }

}
