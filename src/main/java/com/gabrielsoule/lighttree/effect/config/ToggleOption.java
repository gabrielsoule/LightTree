package com.gabrielsoule.lighttree.effect.config;

public class ToggleOption extends LightEffectOption {

    private boolean defaultValue;
    private boolean value;
    private String toggleKey;

    public ToggleOption(String optionText, boolean defaultValue , String toggleKey) {
        super(optionText);
        this.defaultValue = defaultValue;
        this.value = defaultValue;
        this.toggleKey = toggleKey;
    }

    public boolean getDefaultValue() {
        return defaultValue;
    }

    public boolean getValue() {
        return value;
    }

    public String getToggleKey() {
        return toggleKey;
    }
}
