package com.gabrielsoule.lighttree;

import com.gabrielsoule.lighttree.effect.*;

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
        LightTree.log("Loading effects from config... OK");
        HashMap<String, Object> effectKeybindsSection = (HashMap<String, Object>) config.getYamlObject().get("effect-keybinds");
        for(String key : effectKeybindsSection.keySet()) {
            HashMap<String, Object> effectSection = (HashMap<String, Object>) effectKeybindsSection.get(key);
            try {
                LightTree.log("\nInstantiating LightEffect: com.gabrielsoule.lighttree.effect." + effectSection.get("name") + " and binding to key \'" + key + "\'");
                LightEffect effect = (LightEffect) Class.forName("com.gabrielsoule.lighttree.effect." + effectSection.get("name")).getConstructor().newInstance();
                effect.p = this.p;

                //load colors
                List<String> colorStrings = (List<String>) effectSection.get("colors");


                //load options
                HashMap<String, Object> effectOptions = (HashMap<String, Object>) effectSection.get("options");
                for(String optionName : effectOptions.keySet()) {
                    String value = effectOptions.get(optionName).toString();
                    try {
                        float floatValue = Float.parseFloat(value);
                        effect.config.setFloat(optionName, floatValue);
                        LightTree.log(String.format("Setting float option \'%s\' to value \'%s\'",optionName, floatValue));

                    } catch (NumberFormatException ex) {
                        if(value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")) {
                            boolean booleanValue = Boolean.parseBoolean(value);
                            effect.config.setBoolean(optionName, booleanValue);
                            LightTree.log(String.format("Setting boolean option \'%s\' to value \'%s\'",optionName, booleanValue));
                        } else {
                            effect.config.setString(optionName, value);
                            LightTree.log(String.format("Setting string option \'%s\' to value \'%s\'",optionName, value));

                        }
                    }
                }

                effects.put(key, effect);

            } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException ex) {
                System.out.println("ERROR: Something went wrong while loading effects from the configuration file. See stack trace for details: ");
                ex.printStackTrace();
            }

        }
    }

    public void testLoad() {
        EffectChasers chasers = new EffectChasers();
        chasers.setup();
        EffectFlashSegments flashSegments = new EffectFlashSegments();
        flashSegments.setup();
        EffectMitchellVisualizer visualizer = new EffectMitchellVisualizer();
        visualizer.setup();
        EffectPulsers pulsers = new EffectPulsers();
        pulsers.setup();
        EffectMitchellStrobe strobe = new EffectMitchellStrobe();
        strobe.setup();

        flashSegments.configure(new int[]{2, 2}, new int[]{0, 0});
        pulsers.configure(new int[]{5, 3, 0}, new int[]{0, 0});
        effects.put("1", visualizer);
        effects.put("2", chasers);
        effects.put("3", strobe);
        effects.put("4", pulsers);
        effects.put("5", flashSegments);
    }


    int[] sequence() {
        for(String key : effects.keySet()) {
            if(p.keyboardListener.keyPressed(key)) {
                LightTree.log("Activating effect bound to " + key);
                LightEffect effect = effects.get(key);
                activeEffects.clear();
                activeEffects.add(effect);
//                if(activeEffects.contains(effect)) {
//                    LightTree.log("Deactivating effect " + effect.getClass().getSimpleName());
//                    activeEffects.remove(effect);
//                    effect.sleep();
//                } else {
//                    LightTree.log("Activating effect " + effect.getClass().getSimpleName());
//                    activeEffects.add(effect);
//                    effect.wake();
//                }
            }

            for(LightEffect effect : activeEffects) {
                if(effect.isSleeping()) {
                    LightTree.log("Deactivating sleeping effect " + effect.getClass().getSimpleName());
                    activeEffects.remove(effect);
                }
                effect.draw();
            }
        }

        return null; //TODO literally anything but this
    }


    public HashMap<String, LightEffect> getEffects() {
        return effects;
    }

    public ArrayList<LightEffect> getActiveEffects() {
        return activeEffects;
    }
}
