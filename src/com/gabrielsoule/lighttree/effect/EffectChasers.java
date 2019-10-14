package com.gabrielsoule.lighttree.effect;

import com.gabrielsoule.lighttree.Color;
import com.gabrielsoule.lighttree.LightEffect;
import com.gabrielsoule.lighttree.LightTree;
import processing.core.PApplet;

public class EffectChasers extends LightEffect {

    int current = 0;
    int current2 = 300;
    int numNodes = 1;
    Chase[] chasers;
    int frames = 0;

//    public EffectChasers(LightTree p) {
//        super();
//        chasers = new Chase[numNodes];
//        for(int i=0; i<numNodes; i++){
//            float dirF = PApplet.round(p.random(0,2));
//            int dir;
//            if(dirF > 1) dir =1;
//            else dir = -1;
//            chasers[i] = new Chase(PApplet.round(p.random(0, 255)), dir, PApplet.round(p.random(2, 20)));
//        }
//    }

    @Override
    public void setup() {
        chasers = new Chase[numNodes];
        for(int i=0; i<numNodes; i++){
            float dirF = PApplet.round(p.random(0,2));
            int dir;
            if(dirF > 1) dir =1;
            else dir = -1;
            chasers[i] = new Chase(PApplet.round(p.random(0, 511 - 64)), dir, PApplet.round(p.random(2, 20)));
        }
    }

    @Override
    public void configure(int[] integerConfig, Color[] colorConfig) {

    }

    @Override
    public void configure(int[] integerConfig, int[] colorConfig) {

    }

    @Override
    public void draw() {
        frames++;
        for(int k=0; k<chasers.length; k++){
            for (int i = 0; i < chasers[k].leng; i++) {

                int index = chasers[k].curr +i;

                float hue = chasers[k].hue;
                float brightness;
                if(chasers[k].dir > 0){
                    brightness = 255 - (chasers[k].leng - i) * ((float)255/chasers[k].leng);
                }
                else{
                    brightness = (chasers[k].leng - (i+1)) * ((float)255/chasers[k].leng);
                }
                int bright = PApplet.round(brightness);
                setLight(index, p.color(hue, 200, bright));
            }
            if(frames % 2 == 0) chasers[k].move();
        }

    }

    @Override
    public void keyPressed(){
//        System.out.println(key);
        if(p.key == 'q'){
//            System.out.println("key press");
            float dirF = PApplet.round(p.random(0,2));
            int dir;
            if(dirF > 1) dir =1;
            else dir = -1;
            Chase newC = new Chase(PApplet.round(p.random(0, 254)), dir, PApplet.round(p.random(2, 20)));
            chasers = (Chase[]) PApplet.append(chasers, newC);
            numNodes = chasers.length;
        }
        if(p.key == 'a'){
            chasers = (Chase[]) PApplet.shorten(chasers);
            numNodes = chasers.length;
        }
    }

    public class Chase{
        int start, dir, leng, hue;
        int curr;

        Chase(int s, int d, int l){
            hue = PApplet.round(p.random(360));
            start = s;
            curr = s;
            dir = d;
            leng = l;
        }

        void move(){
            curr += dir;
            if(curr < 0) curr = 447;
            if(curr > 446) curr = 0;
        }
    }
}
