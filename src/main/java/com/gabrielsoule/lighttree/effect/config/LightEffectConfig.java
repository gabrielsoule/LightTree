package com.gabrielsoule.lighttree.effect.config;

import com.gabrielsoule.lighttree.LightTree;
import com.gabrielsoule.lighttree.util.Color;

import java.util.ArrayList;
import java.util.HashMap;

public class LightEffectConfig {

    public HashMap<String, LightEffectOption> options = new HashMap<>();

    //Options directly loaded from config. This process could be rewritten,
    //since its been in the code forever, since the pre-UI days, but for
    //compatability reasons (and time constraints) it will stay how it is.
    private HashMap<String, Float> floatOptions     = new HashMap<>();
    private HashMap<String, Boolean> booleanOptions = new HashMap<>();
    private HashMap<String, String> stringOptions   = new HashMap<>();

    private ArrayList<Integer> colors = new ArrayList<>();
    private int nextColorIndex = -1;

    public void registerOption(String key, LightEffectOption option) {
        this.options.put(key, option);
    }

    public boolean getBooleanFromConfig(String key) {
        return booleanOptions.get(key);
    }

    public void setBooleanFromConfig(String key, boolean value) {
        this.booleanOptions.put(key, value);
    }

    public float getFloatFromConfig(String key) {
        return floatOptions.get(key);
    }

    public void setFloatFromConfig(String key, float value) {
        this.floatOptions.put(key, value);
    }

    public String getStringFromConfig(String key) {
        return stringOptions.get(key);
    }

    public void setStringFromConfig(String key, String value) {
        this.stringOptions.put(key, value);
    }

    public HashMap<String, LightEffectOption> getOptions() {
        return options;
    }

    public void addColor(int color){
        colors.add(color);
    }

    public int nextColor() {
        nextColorIndex = (nextColorIndex + 1) % colors.size();
        return getColor(nextColorIndex);
    }

    public ArrayList<Integer> getColors() {
        return colors;
    }

    public int getColor(int i) {
        if(colors.get(i) == Color.RANDOM) {
            return LightTree.getInstance().color(LightTree.getInstance().random(0, 360), 255, 255);
        } else {
            return colors.get(i);
        }
    }
}
