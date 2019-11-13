package com.gabrielsoule.lighttree.effect;

import com.gabrielsoule.lighttree.Color;
import com.gabrielsoule.lighttree.LightEffect;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;

public class EffectStrobe extends LightEffect {

    int[] segments;
    int segmentPointer = 0;

    @Override
    public void setup() {
//        ArrayList<Integer> segmentArray = new ArrayList<>();
//        for(int i = 0; i < p.NUM_LIGHTS; i += config.getInt("lights-per-segment")) {
//            segmentArray.add(i);
//        }
//
//        Collections.shuffle(segmentArray);
//        this.segments = segmentArray.toArray();

    }

    @Override
    public void configure(int[] integerConfig, Color[] colorConfig) {

    }

    @Override
    public void configure(int[] integerConfig, int[] colorConfig) {

    }

    @Override
    public void draw() {
        int frameModPeriod = p.frameCount % ((int) (p.FRAME_RATE / config.getFloat("frequency")));
        int numLightsToFlash = (int) (config.getFloat("lights-per-segment") * config.getFloat("segments-per-flash"));
        int turnOffLightsFrame = (int) ((p.FRAME_RATE / config.getFloat("frequency")) * config.getFloat("strobe-on-mult"));
        if(turnOffLightsFrame == 0) {
            turnOffLightsFrame = 1;
        }
        if (frameModPeriod == 0) {
            //green light
            for (int i = segmentPointer; i < segmentPointer + numLightsToFlash; i++) {
                setLight(i % p.NUM_LIGHTS, config.nextColor());
            }
        } else if(frameModPeriod == turnOffLightsFrame) {
            for (int i = segmentPointer; i < segmentPointer + numLightsToFlash; i++) {
                setLight(i % p.NUM_LIGHTS, 0);
            }

            segmentPointer = (segmentPointer + numLightsToFlash);
        }
    }
}
