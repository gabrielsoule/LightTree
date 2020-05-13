package com.gabrielsoule.lighttree.effect;

import com.gabrielsoule.lighttree.LightEffect;
import com.gabrielsoule.lighttree.LightTree;
import ddf.minim.analysis.HammingWindow;
import ddf.minim.analysis.WindowFunction;

import java.util.ArrayDeque;
import java.util.Arrays;

public class EffectMitchellVisualizer extends LightEffect {
    int rowmax = 24;
    float MAX_N = 3840.0f; //7680.0; (15 second memory)
    float[] rawScores = new float[rowmax];
    float[] inScores = new float[rowmax];
    float[] avg = new float[rowmax];
//    int[] limits = {0, 100, 200, 300, 400, 510, 630, 770, 920, 1080, 1270, 1480, 1720, 2000, 2320, 2700, 3150, 3700, 4400, 5300, 6400, 7700, 9500, 12000, 15000};

    int[] limits = {0, 25, 50, 77, 150, 200, 225, 250, 275, 300, 400, 600, 900, 1300, 1700, 2200, 2950, 3400, 4000, 5000, 6100, 7700, 9500, 12000, 15000};
    float total = 0;
    float rawScore;
    float colorMod = 120;
    float lastMove;

    // Light / Simluator variables
    int numLights = 512;
    boolean lightsOn = false;
    int rotator = 0;

    int current = 0;
    int current2 = 300;
    int numNodes = 1;
    int colorTest = 0;

    int mode = 0;
    boolean dir = true;

    WindowFunction window = new HammingWindow();

    @Override
    public void setup() {

    }

    @Override
    public void draw() {
        colorMod += (1 / (float) p.FRAME_RATE) * 16;
        colorMod = colorMod % 360;

        if (p.millis() - lastMove > 200) {
            lastMove = p.millis();
            rotator = (rotator + 1) % p.NUM_LIGHTS;
        }

        analysis();
        treeLightSections();
    }


    void analysis(){
        // perform a forward FFT on the samples in the input buffer
        p.fft.forward(p.audioInput.mix);
        total = 0;

        // for each of the defined ranges
        for(int i=0; i<rowmax; i++){

            // sum  up the "band"
            rawScore = 0;
            for(int j=limits[i]; j<limits[(i)+1]; j++){
                rawScore += p.fft.getFreq(j);
            }

            // get average of "band" (out of 100)
            rawScores[i] = rawScore;
            //if(rawScore > max[i]) max[i] = rawScore;
            //float score = (rawScore/max[i]);
            avg[i] = rollAvg(rawScore, avg[i]);
            //if(i==0) println(rawScore);
            rawScores[i] = LightTree.map(rawScore, 0, avg[i]*5, 0, 1);
            if(inScores[i] != rawScores[i]){
                inScores[i] += (rawScores[i] - inScores[i])/5f;
            }
        }
//        LightTree.log(Arrays.toString(inScores));
        total /= 24;
    }

    float rollAvg(float new_v, float avg){
        avg -= (avg / MAX_N);
        avg += (new_v / MAX_N);

        return avg;
    }

//    private void analysis(){
//        // perform a forward FFT on the samples in the input buffer
//        p.fft.forward(p.audioInput.mix);
//        total = 0;
//
//        // for each of the defined ranges
//        for(int i=0; i<rowmax; i++){
//
//            // sum  up the "band"
//            rawScore = 0;
//            for(int j=limits[i]; j<limits[(i)+1]; j++){
//                rawScore += p.fft.getFreq(j) * 100;
//            }
//
//            // get average of "band" (out of 100)
//            rawScores[i] = rawScore;
//            max[i] = EWMAFactor * max[i] + (1 - EWMAFactor) * rawScore;
//            if(rawScore > max[i]) max[i] = rawScore;
//            float score = (rawScore/max[i]);
//            total += score;
//            if(score < inScores[i]) {
//                score = -LightTree.abs(score-inScores[i])/10;
//            }
//            if(score > inScores[i]) {
//                score = LightTree.abs(score-inScores[i])/5;
//            }
//            inScores[i] += score;
//
//        }
//
//        int totalMaxScore = 0;
//        for (int i = 0; i < max.length; i++) {
//            totalMaxScore += max[i];
//        }
//
////        LightTree.log("%s", totalMaxScore);
//
//        total /= 24;
//    }


    private void treeLightSections(){
        for (int i = 0; i < p.NUM_LIGHTS; i++) {
            float localIndex = 23 - LightTree.abs(i % 47 - 23);
            float hue = getHue(colorMod + ((localIndex) / 23) * 120);
            float localScore = inScores[LightTree.round(localIndex)];
            float b = localScore;
            //if(localScore < 0.25) b *= 0.5;
            b = (4 * localScore + inScores[0]) / 5f; // fifth of each light is bass level
            b = LightTree.constrain(b, 0, 1);
//            LightTree.log(b);
            setLight((i + rotator) % (p.NUM_LIGHTS - 1), p.color(hue, 50 + (b * 205), b * 255));
       }
    }

    int getHue(int start){ return (start+120)%360; }
    float getHue(float start){ return (start+120)%360; }
}
