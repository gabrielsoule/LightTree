package com.gabrielsoule.lighttree.effect;

import com.gabrielsoule.lighttree.LightEffect;
import com.gabrielsoule.lighttree.LightTree;
import com.gabrielsoule.lighttree.util.Color;
import ddf.minim.analysis.FFT;
import processing.core.PConstants;


import java.util.Arrays;
import java.util.stream.Collectors;

public class EffectNeoVisualizerBeta extends LightEffect {

    private FFT fft;
    private int[] bands = {25, 50, 100, 300, 500, 2000, 5000};
    private int[] lightsPerBand = {10, 10, 10, 7, 6, 4};
    private int[] colors = {p.color(0, 255, 255),
            p.color(20, 255, 255),
            p.color(50, 255, 255),
            p.color(80, 255, 255),
            p.color(180, 255, 255),
            p.color(230, 255 ,255),
            p.color(300, 255, 255)
    };
    private int[] hues = {0, 20, 50, 80, 180, 230, 300};
//    private int ranges = new int{0, }
    private float[] maxima;
    private float[] currentMaxima;
    private float[] bandStrength;
    private int[] drawBuffer;

    private float[] deltas = new float[3];
    private int iter = 0;
    private float lastScore = 0;
    private int lastBeatTimestamp;
    private int delay = 100;

    @Override
    public void setup() {
        this.fft = new FFT(p.audioInput.bufferSize(), p.audioInput.sampleRate());
        fft.window(FFT.BARTLETT);
        this.maxima = new float[bands.length - 1];
        this.currentMaxima = new float[bands.length - 1];
//        this.drawBuffer = new int[fft.avgSize() * lightsPerBand];
        this.bandStrength = new float[bands.length - 1];
        int totalLights = 0;
        for (int value : lightsPerBand) {
            totalLights += value;
        }
        drawBuffer = new int[totalLights * 2];
    }

    @Override
    public void draw() {
        fft.forward(p.audioInput.mix);

        int drawBufferIndex = 0;

        for(int i = 0; i < bands.length - 1; i++) {
            //sum band
            float avg = 0;
            for(int freq = bands[i]; freq < bands[i + 1]; freq++) {
                avg += fft.getFreq(freq);
            }

            //divide by number of frequencies to get average score per band
            avg = avg / (bands[i + 1] - bands[i]);
//
//            p.fill(p.color(0, 255, 255));
//            p.rectMode(PConstants.CORNER);
//            p.rect(40 + i * 20, 700, 20, -avg * 3);
//            p.fill(120, 255, 255);
//            p.rect(40 + i * 20, (700 - maxima[i] * 3), 20, 2);
//            p.fill(0, 0, 150);
//            p.rect(40 + i * 20, (700 - currentMaxima[i] * 3), 20, 2);
//            p.rect(100, 100, 100, 100);

            if(avg >= currentMaxima[i]) {
                currentMaxima[i] = avg;
                maxima[i] = avg;
            } else {
                currentMaxima[i] = currentMaxima[i] - maxima[i] * (0.1f / p.frameRate);
            }

            float strength = (avg / currentMaxima[i]);
            if(avg < 0.01f) strength = 0;
            if(strength > bandStrength[i]) {
                bandStrength[i] = strength;
            } else {
                bandStrength[i] = bandStrength[i] - bandStrength[i] * (2 / p.frameRate);
            }

            int numLights = LightTree.constrain(LightTree.round(LightTree.pow(bandStrength[i], 3) * lightsPerBand[i] * 1.2f), 0, lightsPerBand[i]);
            for(int j = 0; j < numLights; j++) {
                drawBuffer[drawBufferIndex + j] = p.color((hues[i]) % 360, 255, 255 * (bandStrength[i] / 1.4f));
            }
            for(int j = numLights; j < lightsPerBand[i]; j++) {
                drawBuffer[drawBufferIndex + j] = 0;
            }
            drawBufferIndex += lightsPerBand[i];
        }

        float score = 0;
        for(int i = 25; i < 50; i++) {
            score += fft.getFreq(i);
        }

        score = score / 25f;
        float deltaSum = 0;
        for(int i = iter - 1; i > iter - 3; i--) {
            if(i >= 0) {
                deltaSum = deltaSum + deltas[i % 3];
            }
        }
//        System.out.println(Arrays.toString(deltas));
//        System.out.println(score - lastScore);
//        System.out.println(deltaSum);
//        System.out.println(currentMaxima[0]);
        if(deltaSum > currentMaxima[0] / 1.8f && p.millis() - lastBeatTimestamp > delay)
        {
            this.lastBeatTimestamp = p.millis();
            System.out.println("beat");
        }

        deltas[iter % 3] = score - lastScore;
        lastScore = score;

        //clear the rest of the buffer
        for(int i = drawBufferIndex; i < drawBuffer.length; i++) {
            drawBuffer[i] = 0;
        }

        for(int i = 0; i < drawBuffer.length / 2; i++) {
            drawBuffer[drawBuffer.length - (i + 1)] = drawBuffer[i];
        }

        for(int i = 0; i < p.NUM_LIGHTS; i++){
            setLight((i + p.millis() / 100) % p.NUM_LIGHTS,
                    drawBuffer[i % drawBuffer.length]);
        }

        iter++;
    }
}
