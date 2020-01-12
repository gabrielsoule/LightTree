package com.gabrielsoule.lighttree;

import com.gabrielsoule.lighttree.util.Color;

import java.util.ArrayList;
import java.util.HashMap;

public class LightEffectConfig {
    private HashMap<String, Float> floatOptions     = new HashMap<>();
    private HashMap<String, Boolean> booleanOptions = new HashMap<>();
    private HashMap<String, String> stringOptions   = new HashMap<>();

    private ArrayList<Integer> colors = new ArrayList<>();

    private int nextColorIndex = -1;

//    private Object get(String key) {
//        if(options.containsKey(key)) {
//            return options.get(key);
//        } else {
//            throw new IllegalArgumentException("Unable to find option " + key + ", did you make a typo?");
//        }
//    }

    public boolean getBoolean(String key) {
        return booleanOptions.get(key);
    }

    public float getFloat(String key) {
        return floatOptions.get(key);
    }

    public String getString(String key) {
        return stringOptions.get(key);
    }

    public void setBoolean(String key, boolean value) {
        booleanOptions.put(key, value);
    }

    public void setFloat(String key, float value) {
        floatOptions.put(key, value);
    }

    public void setString(String key, String value) {
        stringOptions.put(key, value);
    }

    public void setColor(int index, int color) {
        colors.add(index, color);
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
