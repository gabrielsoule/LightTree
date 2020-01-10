package com.gabrielsoule.lighttree;

import java.awt.event.KeyEvent;
import java.lang.reflect.Field;
import java.util.HashMap;

public class KeyboardListener {
    private boolean[] heldKeys;
    private boolean[] pressedKeys;
    public  boolean[] releasedKeys;
    private HashMap<String, Integer> keycodes;


    private int keysHeld = 0;
    private boolean keyPressedFlag = false;
    private String lastKeyPressed;

    private final int KEYCODE_RANGE = 223;

    public KeyboardListener() {
        this.keycodes = new HashMap<>();
        this.heldKeys = new boolean[KEYCODE_RANGE];
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
        keyPressedFlag = false;
        for (int i = 0; i < KEYCODE_RANGE; i++) {
//            if(pressedKeys[i]) {
////                LightTree.log("Resetting pressed key %s", pressedKeys[i]);
//            }
//            if(releasedKeys[i]) {
////                LightTree.log("Resetting released key %s", releasedKeys[i]);
//            }

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

    /**
     * Internal method called by the keypress method of PApplet.class
     */
    void handleKeyPress(int keycode) {
        if(keycode < KEYCODE_RANGE) {
            LightTree.log("Handling hardware key press for keycode (keycode)", keycode);
            pressedKeys[keycode] = heldKeys[keycode] = keyPressedFlag = true;
        }
    }

    /**
     * Internal method called by the keypress method of PApplet.class
     */
    void handleKeyReleased(int keycode) {
        if(keycode < KEYCODE_RANGE) {
            heldKeys[keycode] = false;
            releasedKeys[keycode] = true;
        }
    }

    public boolean keyPressed(String key) {
        this.lastKeyPressed = key;
        return pressedKeys[keycodes.get(key.toUpperCase())];
    }

    public boolean keyPressed() {
        return keyPressedFlag;
    }

    public String getLastKeyPressed() {
        return lastKeyPressed;
    }



    public boolean keyDown(String key) {
        return heldKeys[keycodes.get(key.toUpperCase())];
    }

    public boolean keyReleased(String key) {
        return releasedKeys[keycodes.get(key.toUpperCase())];
    }
}
