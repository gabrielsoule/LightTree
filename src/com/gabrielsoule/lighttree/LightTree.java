package com.gabrielsoule.lighttree;

import com.gabrielsoule.lighttree.effects.EffectChasers;
import com.gabrielsoule.lighttree.effects.EffectSimpleVisualizer;
import com.gabrielsoule.lighttree.effects.TestEffectGradient;
import ddf.minim.AudioInput;
import ddf.minim.Minim;
import ddf.minim.analysis.BeatDetect;
import processing.core.PApplet;

import java.util.HashMap;

public class LightTree extends PApplet {

    public static void main(String[] args) {
        PApplet.main("com.gabrielsoule.lighttree.LightTree");
    }

    @Override
    public void settings() {
        size(1200, 400);
    }

    public final int NUM_LIGHTS = 256;

    public OPC opc;
    public final int numLights = 512;
    public Minim minim;
    public AudioInput audioInput;
    public BeatDetect beat;
    public int[] lightColors = new int[512];
    public HashMap<Character, LightEffect> effects;
    public LightEffect activeEffect;
    public BeatDetector beatDetector;

    @Override
    public void setup() {
        this.opc = new OPC(this, "10.0.1.198", 7890);
        minim = new Minim(this);
        audioInput = minim.getLineIn();
        frameRate(30);
        beat = new BeatDetect();
        beat.detectMode(BeatDetect.SOUND_ENERGY);
        colorMode(HSB, 360, 255, 255, 255);
        this.activeEffect = new EffectChasers(this);
        for(int i = 0; i < 512; i++) {
            lightColors[i] = color(0, 0, 48);
        }

        beatDetector = new BeatDetector(this, audioInput);
    }

    @Override
    public void stop() {
        minim.stop();
        super.stop();
    }

    @Override
    public void draw() {
        opc.writePixels();

        background(0);
        stroke(255);

        beatDetector.tick();
//        if(beatDetector.beat()) System.out.println(beat);
        activeEffect.draw();
        drawSimulator();
        opc.writePixels();
    }

    private void drawSimulator() {
        rectMode(CORNER);
        fill(60, 255, 255);
        noStroke();
        int frameMargin = min(height, width) / 10;
        int boxHeight = 10;
        int boxMargin = boxHeight / 2;
        int verticalLength = 2;
        int frameWidth = ((boxHeight + boxMargin) * (64 - verticalLength) - boxMargin);
        int x = (width / 2) - (frameWidth / 2) + (boxHeight + boxMargin) / 2;
        int y = frameMargin;
        int draws = 0;

        //special case of the first pixel which sticks out
        fill(lightColors[0]);
        rect(x - (boxHeight + boxMargin), y, boxHeight, boxHeight,0);

        for(int i = 0; i < 8; i++) {
            fill(i * 22.5f, 255, 255);
            boolean goingRight = i % 2 == 0;
            for(int j = 0; j < 64 - verticalLength; j++) {
                fill(lightColors[64 * i + j + 1]);
                if(goingRight) {
                    rect(x, y, boxHeight, boxHeight, 0);
                    x += boxHeight + boxMargin;
                }
                else {
                    rect(x, y, boxHeight, boxHeight,0);
                    x -= boxHeight + boxMargin;
                }
                draws++;
            }
            if(goingRight) x -= boxHeight + boxMargin; //move X to be under the last pixel
            else x += boxHeight + boxMargin;

            for(int k = 64 - verticalLength; k < 64; k++) {
                fill(lightColors[64 * i + k + 1]);
                draws++;
                if(draws > 510) break;
                y += boxHeight + boxMargin;
                rect(x, y, boxHeight, boxHeight,0);
            }

            y += boxHeight + boxMargin;
        }

        rect(x - (boxHeight + boxMargin), y - (boxHeight + boxMargin), boxHeight, boxHeight,0);
        rectMode(CORNER);
    }


    void setGradient(int x, int y, float w, float h, int c1, int c2, int axis ) {

        noFill();

        if (axis == 1) {  // Top to bottom gradient
            for (int i = y; i <= y+h; i++) {
                float inter = map(i, y, y+h, 0, 1);
                int c = lerpColor(c1, c2, inter);
                stroke(c);
                line(x, i, x+w, i);
            }
        }
        else if (axis == 2) {  // Left to right gradient
            for (int i = x; i <= x+w; i++) {
                float inter = map(i, x, x+w, 0, 1);
                int c = lerpColor(c1, c2, inter);
                stroke(c);
                line(i, y, i, y+h);
            }
        }
    }

    void setLight(int index, int c) {
        lightColors[index] = c;
        c = fixColor(c);
        int segment = (int) (index / 64f);
        switch(segment) {
            case 0:
                index = 255 - index;
                break;
            case 1:
                index = 128 + (index - 64);
                break;
            case 2:
                index = 64 - (index - 128);
                break;
            case 3:
                index = index - 128;
                break;
            default:
                break;
        }
        opc.setPixel(index, c);
    }

    int lastPress;
    int totalTime;
    int presses;
    float avg;

    @Override
    public void keyPressed() {

        activeEffect.key = key;
        activeEffect.keyPressed();

        if(key == 'c') {
            presses++;
//            if(presses > 3) {
//                totalTime += millis() - lastPress;
//                print(millis() - lastPress);
//                avg = totalTime / (presses - 1);
//            }
            System.out.println(millis() - lastPress);

            lastPress = millis();

//            System.out.println(avg);
//            System.out.println(60000 / avg);

        } else if (key == 'b') {
            beatDetector.handleKeyPress();
        }
    }


//    /**
//     * Fadecandy server ignores alpha values, but often we want to use alpha values to indicate brightness.
//     * This method manually applies the color's alpha to its RGB channels so it can be used to control dimness
//     */
//    public int applyAlpha(int color)  {
//        int alphaFrac = ((color >> 24) & 0xFF) / 255;
//
//    }

    private int fixColor(int oldRGB) {
//        int oldRGB = parent.color(hue, saturation, brightness);
        //bunch of bit shifting fuckery to turn AAAAAAAARRRRRRRRGGGGGGGGBBBBBBBB into AAAAAAAAGGGGGGGGRRRRRRRRBBBBBBBB
        //also, since fadecandy doesn't do alpha values, simulate transparency by applying blackness that corresponds
        //to the alpha value
        float alphaFrac = ((oldRGB >> 24) & 0xFF) / (float) 255;
        return (((oldRGB >> 24) & 0xFF) << 24) |
                ((int)(((oldRGB >> 8) & 0xFF) * alphaFrac) << 16) |
                ((int)(((oldRGB >> 16) & 0xFF) * alphaFrac) << 8) |
                ((int) ((oldRGB & 0xFF) * alphaFrac));
    }


}
