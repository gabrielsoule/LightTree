package com.gabrielsoule.lighttree;

import com.gabrielsoule.lighttree.effect.*;
import com.gabrielsoule.lighttree.util.MathUtil;

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


    //TODO Migrate this method into Config.java. This entire class is stupid and should be eliminated;
    // it was designed for a feature that will never be implemented... most likely.
    public void loadFromConfig(Config config) {
        LightTree.log("Loading effects from config... OK");
        HashMap<String, Object> effectKeybindsSection = (HashMap<String, Object>) config.getYamlObject().get("effect-keybinds");
        for(String key : effectKeybindsSection.keySet()) {
            HashMap<String, Object> effectSection = (HashMap<String, Object>) effectKeybindsSection.get(key);
            try {
                LightTree.log("\nInstantiating LightEffect: com.gabrielsoule.lighttree.effect." + effectSection.get("effect") + " and binding to key \'" + key + "\'");
                LightEffect effect = (LightEffect) Class.forName("com.gabrielsoule.lighttree.effect." + effectSection.get("effect")).getConstructor().newInstance();
                effect.p = this.p;
                effect.setName((String) effectSection.get("name"));
                if(effect.getName().equals("NULL EFFECT")) {
                    this.activeEffects.add(effect);
                }

                //load colors
                List<String> colorStrings = (List<String>) effectSection.get("colors");
                for(String colorString : colorStrings) {
                    if(config.getColors().containsKey(colorString)) {
                        LightTree.log("Adding color %s to effect", colorString);
                        effect.getConfig().addColor(config.getColors().get(colorString));
                    } else {
                        LightTree.log("Adding color %s to effect", colorString);
                        effect.getConfig().addColor(MathUtil.decodePColorRGB(colorString));
                    }
                }

                //load options
                HashMap<String, Object> effectOptions = (HashMap<String, Object>) effectSection.get("options");
                if(effectOptions != null) {
                    for(String optionName : effectOptions.keySet()) {
                        String value = effectOptions.get(optionName).toString();
                        try {
                            float floatValue = Float.parseFloat(value);
                            effect.getConfig().setFloatFromConfig(optionName, floatValue);
                            LightTree.log(String.format("Setting float option \'%s\' to value \'%s\'",optionName, floatValue));

                        } catch (NumberFormatException ex) {
                            if(value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")) {
                                boolean booleanValue = Boolean.parseBoolean(value);
                                effect.getConfig().setBooleanFromConfig(optionName, booleanValue);
                                LightTree.log(String.format("Setting boolean option \'%s\' to value \'%s\'",optionName, booleanValue));
                            } else {
                                effect.getConfig().setStringFromConfig(optionName, value);
                                LightTree.log(String.format("Setting string option \'%s\' to value \'%s\'",optionName, value));

                            }
                        }
                    }
                }

                effects.put(key, effect);
                effect.setup();

            } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException ex) {
                System.out.println("ERROR: Something went wrong while loading effects from the configuration file. See stack trace for details: ");
                ex.printStackTrace();
            }
        }

        LightTree.log("Done loading light effects! Total effects loaded: %s", effects.keySet().size());
    }


    int[] sequence() {
        for(String key : effects.keySet()) {
            if(p.keyboardListener.keyPressed(key)) {
                LightTree.log("Activating effect bound to " + key);
                LightEffect effect = effects.get(key);
                if(activeEffects.contains(effect)) {
//                    LightTree.log("Deactivating effect " + effect.getClass().getSimpleName());
//                    activeEffects.remove(effect);
//                    effect.sleep();
                } else {
                    if(!activeEffects.isEmpty())
                    activeEffects.clear();
                    LightTree.log("Activating effect " + effect.getClass().getSimpleName());
                    activeEffects.add(effect);
                    effect.wake();
                }
            }
        }

        for(LightEffect effect : activeEffects) {
            if(effect.isSleeping()) {
                LightTree.log("Deactivating sleeping effect " + effect.getClass().getSimpleName());
                activeEffects.remove(effect);
            }
            effect.draw();
        }

        return null; //TODO literally anything but this
    }

    public void setActiveEffect(LightEffect effect) {
        activeEffects.clear();
        LightTree.log("Activating effect " + effect.getClass().getSimpleName());
        activeEffects.add(effect);
        effect.wake();
    }


    public HashMap<String, LightEffect> getEffects() {
        return effects;
    }

    public ArrayList<LightEffect> getActiveEffects() {
        return activeEffects;
    }
}
