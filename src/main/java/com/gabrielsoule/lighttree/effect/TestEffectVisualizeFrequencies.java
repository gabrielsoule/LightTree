package com.gabrielsoule.lighttree.effect;

import com.gabrielsoule.lighttree.LightEffect;
import com.gabrielsoule.lighttree.LightTree;

public class TestEffectVisualizeFrequencies extends LightEffect {
    private int MIN_FREQ = 0;
    private int MAX_FREQ = 15000;

    private int[] frequencies = new int[p.NUM_LIGHTS + 1];
    private int[] maxAmplitudes = new int[p.NUM_LIGHTS];
    private int[] amplitudes = new int[p.NUM_LIGHTS];
    private float[] relativeScores = new float[p.NUM_LIGHTS];

    @Override
    public void setup() {
        for(int i = 0; i < frequencies.length; i++) {
            frequencies[i] = (int) ((i / ((float) frequencies.length - 1)) * MAX_FREQ);
//            LightTree.log("freq %s", frequencies[i]);
        }
    }

    @Override
    public void draw() {
        p.fft.forward(p.audioInput.mix);
        // for each of the defined ranges
        for(int i = 0; i < frequencies.length - 1; i++){

            // sum  up the "band"
            int rawScore = 0;
            for(int j = frequencies[i]; j < frequencies[i + 1]; j++){
                rawScore += p.fft.getFreq(j);
            }

            // get average of "band" (out of 100)
            amplitudes[i] = rawScore;

            //
            maxAmplitudes[i] -= maxAmplitudes[i] / p.FRAME_RATE * 20;
            maxAmplitudes[i] += rawScore / (p.FRAME_RATE * 20);

            float relativeScore = rawScore / (float) (maxAmplitudes[i] * 5);
            relativeScores[i] = relativeScore;
//            if(score < inScores[i]) {
//                score = -LightTree.abs(score-inScores[i])/10;
//            }
//            if(score > inScores[i]) {
//                score = LightTree.abs(score-inScores[i])/5;
//            }
        }

        int totalMaxScore = 0;
        for (int i = 0; i < maxAmplitudes.length; i++) {
            totalMaxScore += maxAmplitudes[i];
        }
//        LightTree.log("%s", totalMaxScore);

        for (int i = 0; i < amplitudes.length; i++) {
            setLight(i, p.color(0, 255, relativeScores[i]));
        }
    }
}
