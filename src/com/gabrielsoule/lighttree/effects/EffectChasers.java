package com.gabrielsoule.lighttree.effects;

import com.gabrielsoule.lighttree.LightEffect;

public class EffectChasers extends LightEffect {
    @Override
    public void draw() {

    }

}

// Demonstration that pokes colors into the LEDs directly instead
// of mapping them to pixels on the screen. You may want to do this
// if you have your own mapping scheme that doesn't fit well with
// a 2D display.
//
//    OPC opc;
//    int current = 0;
//    int current2 = 300;
//    int numNodes = 1;
//    Chase[] chasers;
//
//    void setup()
//    {
//        opc = new OPC(this, "127.0.0.1", 7890);
//        opc.setPixelCount(512);
//        frameRate(20);
//        colorMode(HSB, 255);
//        chasers = new Chase[numNodes];
//        for(int i=0; i<numNodes; i++){
//            float dirF = round(random(0,2));
//            int dir;
//            if(dirF > 1) dir =1;
//            else dir = -1;
//            chasers[i] = new Chase(round(random(0, 511)), dir, round(random(2, 20)));
//        }
//    }
//
//    void draw()
//    {
//        // RAINBOW FADE!!!!!
//        for(int k=0; k<chasers.length; k++){
//            for (int i = 0; i < chasers[k].leng; i++) {
//
//                int index = chasers[k].curr +i;
//
//                float hue = chasers[k].hue;
//                float brightness;
//                if(chasers[k].dir > 0){
//                    brightness = 255 - (chasers[k].leng - i) * ((float)255/chasers[k].leng);
//                }
//                else{
//                    brightness = (chasers[k].leng - (i+1)) * ((float)255/chasers[k].leng);
//                }
//                int bright = round(brightness);
//                opc.setPixel(index, color(hue, 200, bright));
//            }
//            chasers[k].move();
//        }
//
//
//        // When you haven't assigned any LEDs to pixels, you have to explicitly
//        // write them to the server. Otherwise, this happens automatically after draw().
//        opc.writePixels();
//    }
//
//    void keyPressed(){
//        if(key == 'q'){
//            float dirF = round(random(0,2));
//            int dir;
//            if(dirF > 1) dir =1;
//            else dir = -1;
//            Chase newC = new Chase(round(random(0, 511)), dir, round(random(2, 20)));
//            chasers = (Chase[])append(chasers, newC);
//            numNodes = chasers.length;
//        }
//        if(key == 'a'){
//            chasers = (Chase[])shorten(chasers);
//            numNodes = chasers.length;
//        }
//    }
//
//
//class Chase{
//    int start, dir, leng, hue;
//    int curr;
//
//    Chase(int s, int d, int l){
//        hue = round(random(255));
//        start = s;
//        curr = s;
//        dir = d;
//        leng = l;
//    }
//
//    void move(){
//        curr += dir;
//        if(curr < 0) curr = 511;
//        if(curr > 511) curr = 0;
//    }
//
//}
//
//    void setLight(int index, color c) {
//        int segment = (int) (index / 64f);
//        switch(segment) {
//            case 0:
//                index = 255 - index;
//                break;
//            case 1:
//                index = 128 + (index - 64);
//                break;
//            case 2:
//                index = 64 - (index - 128);
//                break;
//            case 3:
//                index = index - 128;
//                break;
//            default:
//                break;
//        }
//
//        opc.setPixel(index, c);
//    }
//
