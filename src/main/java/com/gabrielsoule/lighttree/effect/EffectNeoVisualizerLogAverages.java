package com.gabrielsoule.lighttree.effect;

import com.gabrielsoule.lighttree.LightEffect;
import ddf.minim.analysis.FFT;
import processing.core.PConstants;

public class EffectNeoVisualizerLogAverages extends LightEffect {

    private FFT fft;

    private int octaveWidth = 10;
    private int bandsPerOctave = 4;
    private int lightsPerBand = 1;
    private int minFrequency = 0;
    private int maxFrequency = 1000;
    private float[] maxima;
    private float[] currentMaxima;
    private int[] drawBuffer;
    private float[] bandBrightness;
    private int lightIndex;

    @Override
    public void setup() {
        this.fft = new FFT(p.audioInput.bufferSize(), p.audioInput.sampleRate());
        fft.window(FFT.BARTLETT);
        fft.logAverages(octaveWidth, bandsPerOctave);
        this.maxima = new float[fft.avgSize()];
        this.currentMaxima = new float[fft.avgSize()];
        this.drawBuffer = new int[fft.avgSize() * lightsPerBand];
        this.bandBrightness = new float[fft.avgSize()];
    }

    @Override
    public void draw() {
        fft.forward(p.audioInput.mix);
        lightIndex = 0;
        for(int i = 0; i < fft.avgSize(); i++) {
            if(minFrequency < fft.getAverageCenterFrequency(i)
                    && fft.getAverageCenterFrequency(i) < maxFrequency)
            {
                p.fill(p.color(0, 255, 255));
                p.rectMode(PConstants.CORNER);
                p.rect(40 + i * 20, 700, 20, -fft.getAvg(i) * 3);
                p.fill(120, 255, 255);
                p.rect(40 + i * 20, (700 - currentMaxima[i] * 3), 20, 2);
                p.rect(100, 100, 100, 100);
                float avg = fft.getAvg(i);
                if(avg >= currentMaxima[i]) {
                    currentMaxima[i] = avg;
                    maxima[i] = avg;
                } else {
                    currentMaxima[i] = currentMaxima[i] - maxima[i] * (0.1f / p.frameRate);
                }

                float brightness = (fft.getAvg(i) / currentMaxima[i]);
                if(fft.getAvg(i) < 0.01f) brightness = 0;
                if(brightness > bandBrightness[i]) {
                    bandBrightness[i] = brightness;
                } else {
                    bandBrightness[i] = bandBrightness[i] - bandBrightness[i] * (0.5f / p.frameRate);
                }

                for(int j = 0; j < lightsPerBand; j++) {

                    drawBuffer[lightIndex + j] = p.color(fft.getAverageCenterFrequency(i) / 2 % 360, 255, 255 * bandBrightness[i]);
                }

                lightIndex += lightsPerBand;
            }
        }

        for(int i = 0; i < p.NUM_LIGHTS; i++) {
            setLight((i + p.millis() / 100) % p.NUM_LIGHTS, drawBuffer[i % lightIndex]);
        }
    }
}
