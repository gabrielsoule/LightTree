package com.gabrielsoule.lighttree.effects;

import com.gabrielsoule.lighttree.LightEffect;
import com.gabrielsoule.lighttree.LightTree;

public class EffectChasers extends LightEffect {

    int current = 0;
    int current2 = 300;
    int numNodes = 1;
    Chase[] chasers;

    public EffectChasers(LightTree p) {
        super(p);
        chasers = new Chase[numNodes];
        for(int i=0; i<numNodes; i++){
            float dirF = p.round(p.random(0,2));
            int dir;
            if(dirF > 1) dir =1;
            else dir = -1;
            chasers[i] = new Chase(p.round(p.random(0, 255)), dir, p.round(p.random(2, 20)));
        }
    }

    @Override
    public void draw() {
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
                int bright = p.round(brightness);
                setLight(index, p.color(hue, 200, bright));
            }
            chasers[k].move();
        }

    }

    @Override
    public void keyPressed(){
        if(key == 'q'){
            float dirF = p.round(p.random(0,2));
            int dir;
            if(dirF > 1) dir =1;
            else dir = -1;
            Chase newC = new Chase(p.round(p.random(0, 254)), dir, p.round(p.random(2, 20)));
            chasers = (Chase[])p.append(chasers, newC);
            numNodes = chasers.length;
        }
        if(key == 'a'){
            chasers = (Chase[])p.shorten(chasers);
            numNodes = chasers.length;
        }
    }

    public class Chase{
        int start, dir, leng, hue;
        int curr;

        Chase(int s, int d, int l){
            hue = p.round(p.random(360));
            start = s;
            curr = s;
            dir = d;
            leng = l;
        }

        void move(){
            curr += dir;
            if(curr < 0) curr = 255;
            if(curr > 255) curr = 0;
        }
    }
}
