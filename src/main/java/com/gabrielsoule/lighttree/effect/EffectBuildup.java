package com.gabrielsoule.lighttree.effect;

import com.gabrielsoule.lighttree.LightEffect;
import edu.emory.mathcs.backport.java.util.Collections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static edu.emory.mathcs.backport.java.util.Collections.*;

public class EffectBuildup extends LightEffect {
    private ArrayList<Integer> lightOrder = new ArrayList<>();
    private int index = 0;
    @Override
    public void setup() {
        for(int i = 0; i < p.NUM_LIGHTS; i++) {
            lightOrder.add(i);
        }
    }

    @Override
    public void draw() {
        if(index < p.NUM_LIGHTS && p.beatDetector.beat()) {
            setLight(lightOrder.get(index), config.nextColor());
            index++;
        }
    }

    @Override
    public void onWake(){
        flushColors();
        index = 0;
        Collections.shuffle(lightOrder);
    }

    @Override
    public void onSleep() {

    }
}
