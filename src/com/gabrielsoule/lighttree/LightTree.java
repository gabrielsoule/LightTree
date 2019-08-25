package com.gabrielsoule.lighttree;

import ddf.minim.AudioInput;
import ddf.minim.Minim;
import ddf.minim.analysis.BeatDetect;
import processing.core.PApplet;

import java.util.HashMap;

public class LightTree extends PApplet {

    public static void main(String[] args) {
        PApplet.main("com.gabrielsoule.lightree.RaveTree");
    }

    @Override
    public void settings() {
        size(1000, 600);
    }

    public OPC opc;
    public final int numLights = 512;
    public Minim minim;
    public AudioInput audioInput;
    public BeatDetect beat;
    public int[] lightColors;
    public HashMap<Character, LightEffect> effects;
    public LightEffect activeEffect;

    @Override
    public void setup() {
        this.opc = new OPC(this, "lightpi.local", 7890);
        minim = new Minim(this);
        audioInput = minim.getLineIn();
        frameRate(60);
        beat = new BeatDetect();
        beat.detectMode(BeatDetect.SOUND_ENERGY);
        colorMode(HSB, 360, 255, 255);
    }

    @Override
    public void draw() {
        opc.writePixels();

        background(0);
        stroke(255);
        for(int i = 0; i < audioInput.bufferSize() - 1; i++)
        {
            line(i, 50 + audioInput.left.get(i)*50, i+1, 50 + audioInput.left.get(i+1)*50);
            line(i, 150 + audioInput.right.get(i)*50, i+1, 150 + audioInput.right.get(i+1)*50);
        }
    }

    public void drawSimulator() {
        rectMode(CORNER);
        int frameMargin = min(height, width) / 20;
        int lightBoxHeight = 10;
        int boxMargin = lightBoxHeight / 2;
        int verticalLength = 4;
        int frameWidth = ((lightBoxHeight + boxMargin) * (64 - verticalLength) - boxMargin);
        int x = (width / 2) - (frameWidth / 2);
        int y = frameMargin;
        for(int i = 0; i < 8; i++) {
            //draw the HORIZONTAL component of each row
            boolean goingRight = i % 2 == 0;
            if(goingRight) {
                x = (width / 2) - (frameWidth / 2);
            } else {
                x = (width / 2) + (frameWidth / 2);
            }
            for(int j = i * 64; j < (i * 64) + (64 - verticalLength); i++) {
                if(goingRight) {
                    rect(x, y, lightBoxHeight, lightBoxHeight);
                    x += lightBoxHeight + boxMargin;
                }
                else {
                    rect(x - lightBoxHeight, y - lightBoxHeight, lightBoxHeight, lightBoxHeight);
                    x -= lightBoxHeight + boxMargin;
                }
            }

            for(int k = i * 64 + 64 - verticalLength; k < (i + 1) *  64; i++) {

            }
            if(goingRight) x -= lightBoxHeight + boxMargin; //move X to be under the last pixel
            else x += boxMargin;

            x = (width / 2) - (frameWidth / 2); //reset X for next draw
            //draw the VERTICAL component of each row
        }

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

    private void setLight(int index, int c) {
        c = fixColor(c);
        lightColors[index] = c;
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

    public int fixColor(int oldRGB) {
//        int oldRGB = parent.color(hue, saturation, brightness);
        //bunch of bit shifting fuckery to turn AAAAAAAARRRRRRRRGGGGGGGGBBBBBBBB into AAAAAAAAGGGGGGGGRRRRRRRRBBBBBBBB
        return (((oldRGB >> 24) & 0xFF) << 24) | (((oldRGB >> 8) & 0xFF) << 16) | (((oldRGB >> 16) & 0xFF) << 8) | (oldRGB & 0xFF);
    }


}
