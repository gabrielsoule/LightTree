package com.gabrielsoule.lighttree.effect;

import com.gabrielsoule.lighttree.Color;
import com.gabrielsoule.lighttree.LightEffect;
import com.gabrielsoule.lighttree.LightTree;

public class EffectMitchellVisualizer extends LightEffect {
    private int rowmax = 24;
    private float[] rawScores = new float[rowmax];
    private float[] inScores = new float[rowmax];
    private float[] max = new float[rowmax];
    private int[] limits = {0, 100, 200, 300, 400, 510, 630, 770, 920, 1080, 1270, 1480, 1720, 2000, 2320, 2700, 3150, 3700, 4400, 5300, 6400, 7700, 9500, 12000, 15000};
    private float total = 0;
    private float rawScore;
    private float colorMod = 120f;
    private int rotator = 0;
    private float lastMove = 0;

    @Override
    public void setup() {

    }

    @Override
    public void configure(int[] integerConfig, Color[] colorConfig) {

    }

    @Override
    public void configure(int[] integerConfig, int[] colorConfig) {

    }

    @Override
    public void draw() {
        colorMod += (1 / (float) p.FRAME_RATE) * 2;
        colorMod = colorMod % 360;

        if (p.millis() - lastMove > 200) {
            lastMove = p.millis();
            rotator = (rotator + 1) % p.NUM_LIGHTS;
        }

        analysis();
        treeLightSections();
    }

    private void analysis(){
        // perform a forward FFT on the samples in the input buffer
        p.fft.forward(p.audioInput.mix);
        total = 0;

        // for each of the defined ranges
        for(int i=0; i<rowmax; i++){

            // sum  up the "band"
            rawScore = 0;
            for(int j=limits[i]; j<limits[(i)+1]; j++){
                rawScore += p.fft.getFreq(j) * 100;
            }

            // get average of "band" (out of 100)
            rawScores[i] = rawScore;
            if(rawScore > max[i]) max[i] = rawScore;
            float score = (rawScore/max[i]);
            total += score;
            if(score < inScores[i]) {
                score = -LightTree.abs(score-inScores[i])/10;
            }
            if(score > inScores[i]) {
                score = LightTree.abs(score-inScores[i])/5;
            }
            inScores[i] += score;

        }

        total /= 24;
    }


    private void treeLightSections(){
        for(int i=0; i<p.NUM_LIGHTS; i++){
            float localIndex = 23 - LightTree.abs(i%47 - 23);
            float hue = colorMod+((localIndex)/23)*120;
            float localScore = inScores[LightTree.round(localIndex)];
            float b = localScore + inScores[0]/2;
            if(localScore < 0.25) b *= 0.5;
            b = LightTree.constrain(b, 0, 1);
            setLight((i+rotator)%(p.NUM_LIGHTS-1), p.color(hue,  50+(b*205), b*255));
        }

    }
}
