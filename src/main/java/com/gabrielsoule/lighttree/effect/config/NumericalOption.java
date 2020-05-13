package com.gabrielsoule.lighttree.effect.config;

public class NumericalOption extends LightEffectOption{

    private float value;

    private final float defaultValue;
    private final float increment;
    private final String decrementKey;
    private final String incrementKey;
    private final float minValue;
    private final float maxValue;

    public NumericalOption(String optionText, float defaultValue, float increment, String decrementKey, String incrementKey) {
        this(optionText, defaultValue, increment, decrementKey, incrementKey, 0, 0);
    }

    public NumericalOption(String optionText, float defaultValue, float increment, String decrementKey, String incrementKey, float minValue, float maxValue) {
        super(optionText);
        this.defaultValue = defaultValue;
        this.value = defaultValue;
        this.increment = increment;
        this.decrementKey = decrementKey;
        this.incrementKey = incrementKey;
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    public float getValue() {
        return value;
    }

    public float getDefaultValue() {
        return defaultValue;
    }

    public float getIncrement() {
        return increment;
    }

    public float getMinValue() {
        return minValue;
    }

    public float getMaxValue() {
        return maxValue;
    }

    public String getDecrementKey() {
        return decrementKey;
    }

    public String getIncrementKey() {
        return incrementKey;
    }
}
