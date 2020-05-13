package com.gabrielsoule.lighttree.effect;

import com.gabrielsoule.lighttree.LightEffect;
import com.gabrielsoule.lighttree.LightTree;
import com.gabrielsoule.lighttree.effect.config.NumericalOption;
import com.gabrielsoule.lighttree.effect.config.ToggleOption;
import ddf.minim.analysis.FFT;
import processing.core.PConstants;

import java.util.Arrays;

public class EffectNeoVisualizer extends LightEffect {

    private FFT fft;
//    private int[] bands = {0, 10,15,20,30,40,50,60,70,100,130,180,250,300,360,440,500,600,700,850,1000,1200};
    private int[] bands = {0, 15,30,50, 65, 100,130,180,250,300, 330, 360,400, 440,500,600,650, 700, 850,1000,1200};

    //    private int
    int divisionsPerBand = 2;
    private float[] maxima;
    private float[] currentMaxima;
    private float[] minima;
    private float[] currentMinima;
    private float[] bandStrength;

    private int[] drawBuffer;

    private float[] deltas;
    private int lastBeatTimestamp;
    private int delay = 60;
    int minBeatFreq = 20;
    int maxBeatFreq = 60;
    int beatMod;
    float satMult = 0f;
    float brightMult = 1f;

    @Override
    public void setup() {
        this.getConfig().registerOption("colorChangeSpeed", new NumericalOption(
                "Color Change Speed",
                this.getConfig().getFloatFromConfig("colorChangeSpeed"),
                1f,
                "q",
                "w"));

        this.getConfig().registerOption("divisionsPerBand", new NumericalOption(
                "Divisions Per Frequency Band",
                this.getConfig().getFloatFromConfig("divisionsPerBand"),
                1f,
                "e",
                "r"));

        this.getConfig().registerOption("minBeatFreq", new NumericalOption(
                "Beat Detection Minimum Threshold",
                this.getConfig().getFloatFromConfig("minBeatFreq"),
                3f,
                "t",
                "y"));

        this.getConfig().registerOption("maxBeatFreq", new NumericalOption(
                "Beat Detection Maximum Threshold",
                this.getConfig().getFloatFromConfig("maxBeatFreq"),
                3f,
                "u",
                "i"));

        this.getConfig().registerOption("beatDelay", new NumericalOption(
                "Beat Cooldown Period (ms)",
                this.getConfig().getFloatFromConfig("beatDelay"),
                25,
                "o",
                "p"));


        this.getConfig().registerOption("doBeatFlash", new ToggleOption("Flash Lights On Beat", true, "["));


        this.fft = new FFT(p.audioInput.bufferSize(), p.audioInput.sampleRate());
        fft.window(FFT.BARTLETT);

        int[] newBands = new int[bands.length * divisionsPerBand - 1];
        int index = 0;
        for(int i = 0; i < bands.length - 1; i++) {
            int low = bands[i];
            int high = bands[i + 1];
            for(int j = 0; j < divisionsPerBand; j++) {
                newBands[index] = LightTree.round(low + (high - low) * (j / (float) divisionsPerBand));
                index++;
            }
        }

        newBands[newBands.length - 1] = bands[bands.length - 1];
        this.bands = newBands;


        this.maxima = new float[bands.length - 1];
        this.currentMaxima = new float[bands.length - 1];
        this.minima = new float[bands.length - 1];
        this.currentMinima = new float[bands.length - 1];
        Arrays.fill(currentMinima, 100000);
        this.bandStrength = new float[bands.length - 1];
        drawBuffer = new int[bands.length - 1];
        this.deltas = new float[bands.length - 1];
    }

    private int bandFunc(int x) {
        return LightTree.round(0.8f * LightTree.pow(x, 2.3f) + 15);
    }

    @Override
    public void draw() {

        fft.forward(p.audioInput.mix);

        int drawBufferIndex = 0;

        int beatWatchers = 0;
        int positives = 0;
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
            float scaleFact = (LightTree.pow(i / 6f, 2.5f) / 2) + 0.7f;
            p.fill(p.color(0, 255, 255));
            p.rectMode(PConstants.CORNER);
            p.noStroke();
            if(bands[i] > this.minBeatFreq && bands[i] < this.maxBeatFreq)
                p.fill(200, 255 * satMult, 255);
            p.rect(40 + i * 20, 700, 20, -avg * 6);
            p.fill(120, 255, 255);
            p.rect(40 + i * 20, (700 - currentMaxima[i] * 6), 20, 2);
            p.rect(40 + i * 20, (700 - maxima[i] * 6), 20, 2);
            p.rect(100, 100, 100, 100);
            if(avg >= maxima[i]) {
                maxima[i] = avg;
            }
            if(avg >= currentMaxima[i]) {
                currentMaxima[i] = avg;
            } else if(currentMaxima[i] - currentMinima[i] > maxima[i] / 2f){
                currentMaxima[i] = currentMaxima[i] - currentMaxima[i] * (0.04f / p.frameRate);
                maxima[i] = maxima[i] - maxima[i] * ((0.0001f * bands[i]) / p.frameRate);

            }

            if(avg <= minima[i]) {
                minima[i] = avg;
            }
            if(avg <= currentMinima[i]) {
                currentMinima[i] = avg;
            } else {
                currentMinima[i] = currentMinima[i] + currentMaxima[i] * 0.025f * (1f / p.FRAME_RATE);
            }

//            currentMinima[i] = 0.001f;


            float strength = ((avg - currentMinima[i]) / (currentMaxima[i] - currentMinima[i]));
            if(avg < 0.01f) strength = 0;
            if(strength > bandStrength[i]) {
                bandStrength[i] = strength;
            } else {
                bandStrength[i] = bandStrength[i] - bandStrength[i] * (2 / p.frameRate);
            }

            if(bands[i] > this.minBeatFreq && bands[i] < this.maxBeatFreq) {
                beatWatchers++;
                if(strength > 0.3f && strength - deltas[i] > 0.4f){
                    System.out.println("beat " + strength + " " + deltas[i]);
                    positives++;
                }
            }

//            int numLights = LightTree.constrain(LightTree.round(LightTree.pow(bandStrength[i], 3) * lightsPerBand[i] * 1.2f), 0, lightsPerBand[i]);
//            for(int j = 0; j < numLights; j++) {
//                drawBuffer[drawBufferIndex + j] = p.color((hues[i]) % 360, 255, 255 * (bandStrength[i] / 1.4f));
//            }
//            for(int j = numLights; j < lightsPerBand[i]; j++) {
//                drawBuffer[drawBufferIndex + j] = 0;
//            }

            drawBuffer[drawBufferIndex] = p.color((drawBufferIndex * 3 + beatMod) % 360, satMult * 255, 255 * ((brightMult + 1) * bandStrength[i]));
            deltas[i] = strength;
            drawBufferIndex += 1;
        }

        satMult = LightTree.constrain(satMult + 12f / (float) p.FRAME_RATE, 0 ,1);
        brightMult = LightTree.constrain(brightMult - 12f / (float) p.FRAME_RATE, 0, 1);

        if(positives / (float) beatWatchers >= 0.7f && p.millis() - lastBeatTimestamp > delay) {
            beatMod += 110;
            satMult = -0.3f;
            brightMult = 1f;
        }

        //clear the rest of the buffer
        for(int i = drawBufferIndex; i < drawBuffer.length; i++) {
            drawBuffer[i] = 0;
        }

        for(int i = 0; i < p.NUM_LIGHTS; i++){
            setLight((i + p.millis() / 500) % p.NUM_LIGHTS,
                    drawBuffer[i % drawBuffer.length]);
        }
    }
}
