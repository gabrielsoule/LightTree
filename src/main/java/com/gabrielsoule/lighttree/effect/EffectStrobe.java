package com.gabrielsoule.lighttree.effect;

import com.gabrielsoule.lighttree.LightEffect;

public class EffectStrobe extends LightEffect {

    private int[] segments;
    private int segmentPointer = 0;

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
    public void draw() {
        int frameModPeriod = p.frameCount % ((int) (p.FRAME_RATE / config.getFloat("frequency")));
        int numLightsToFlash = (int) (config.getFloat("lights-per-segment") * config.getFloat("segments-per-flash"));
        int turnOffLightsFrame = (int) ((p.FRAME_RATE / config.getFloat("frequency")) * config.getFloat("strobe-on-mult"));
        if (turnOffLightsFrame == 0) {
            turnOffLightsFrame = 1;
        }
        if (frameModPeriod == 0) {
            //green light
            int color = config.nextColor();
            for (int i = segmentPointer; i < segmentPointer + numLightsToFlash; i++) {
                setLight(i % p.NUM_LIGHTS,
                        config.getString("color-mode").equalsIgnoreCase("PER_SEGMENT") ? color : config.nextColor());
            }
        } else if (frameModPeriod == turnOffLightsFrame) {
            for (int i = segmentPointer; i < segmentPointer + numLightsToFlash; i++) {
                setLight(i % p.NUM_LIGHTS, 0);
            }

            segmentPointer = (segmentPointer + numLightsToFlash) % p.NUM_LIGHTS;
        }
    }
}
