package com.gabrielsoule.lighttree;

import java.awt.event.KeyEvent;
import java.lang.reflect.Field;
import java.util.HashMap;

public class KeyboardListener {
    private boolean[] activeKeys;
    private boolean[] pressedKeys;
    public  boolean[] releasedKeys;
    private HashMap<String, Integer> keycodes;

    private final int KEYCODE_RANGE = 223;

    public KeyboardListener() {
        this.keycodes = new HashMap<>();
        this.activeKeys = new boolean[KEYCODE_RANGE];
        this.pressedKeys = new boolean[KEYCODE_RANGE];
        this.releasedKeys = new boolean[KEYCODE_RANGE];
        LightTree.log("Importing keyboard codes from KeyEvent.class");
        for(Field f : KeyEvent.class.getFields()) {
            if(f.getName().startsWith("VK_")) {
                try {
                    keycodes.put(f.getName().substring(3), f.getInt(null));
//                    LightTree.log("Binding " + f.getName().substring(3) + " to " + f.getInt(null));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    int getKeycode(String keyName) {
        Integer keycode = keycodes.get(keyName);
        return keycode;
    }

    void tick() {
        for (int i = 0; i < KEYCODE_RANGE; i++) {
            if(pressedKeys[i]) {
//                LightTree.log("Resetting pressed key %s", pressedKeys[i]);
            }
            if(releasedKeys[i]) {
//                LightTree.log("Resetting released key %s", releasedKeys[i]);
            }

            this.pressedKeys[i] = this.releasedKeys[i] = false;
        }
    }
//
//    private int getKeyCode(String key) {
//        try {
//            return KeyEvent.class.getField("VK_" + key.toUpperCase()).getInt(null);
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (NoSuchFieldException e) {
//            System.out.println("[ERROR] Key " + key + " does not exist!");
//            e.printStackTrace();
//        }
//    }

    void handleKeyPress(int keycode) {
        LightTree.log("Handling hardware key press for keycode (keycode)", keycode);
        pressedKeys[keycode] = activeKeys[keycode] = true;
    }

    void handleKeyReleased(int keycode) {
        activeKeys[keycode] = false;
        releasedKeys[keycode] = true;
    }

    public boolean keyPressed(String key) {
//        LightTree.log("Checking whether key %s (keycode %s) is pressed", key, keycodes.get(key.toUpperCase()));
        return activeKeys[keycodes.get(key.toUpperCase())];
    }

    public boolean keyDown(String key) {
        return pressedKeys[keycodes.get(key.toUpperCase())];
    }

    public boolean keyReleased(String key) {
        return releasedKeys[keycodes.get(key.toUpperCase())];
    }
}
