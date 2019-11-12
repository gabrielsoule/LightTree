package com.gabrielsoule.lighttree;

import java.util.ArrayList;
import java.util.HashMap;

public class LightEffectConfig {
    private HashMap<String, Object> options = new HashMap<>();
    private ArrayList<Integer> colors = new ArrayList<>();
    private int nextColorIndex = -1;

    private Object get(String key) {
        if(options.containsKey(key)) {
            return options.get(key);
        } else {
            throw new IllegalArgumentException("Unable to find option " + key + ", did you make a typo?");
        }
    }

    public int getInt(String key) {
        return (int) get(key);
    }

    public boolean getBoolean(String key) {
        return (boolean) get(key);
    }

    public float getFloat(String key) {
        return (float) get(key);
    }

    public String getString(String key) {
        return (String) get(key);
    }

    public void setColor(int index, int color) {
        colors.add(index, color);
    }

    public void setOption(String key, Object value) {
        this.options.put(key, value);
    }

    public void addColor(int color){
        colors.add(color);
    }

    public int nextColor() {
        nextColorIndex = (nextColorIndex + 1) % colors.size();
        return getColor(nextColorIndex);
    }


    public int getColor(int i) {
        if(colors.get(i) == -1) {
            return LightTree.getInstance().color(LightTree.getInstance().random(0, 360), 255, 255);
        } else {
            return colors.get(i);
        }
    }
}
