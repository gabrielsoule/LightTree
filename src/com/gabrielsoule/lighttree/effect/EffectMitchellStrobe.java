package com.gabrielsoule.lighttree.effect;

import com.gabrielsoule.lighttree.Color;
import com.gabrielsoule.lighttree.LightEffect;

public class EffectMitchellStrobe extends LightEffect {

    private int frameCount = 0;

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
        frameCount++;
        for(int i=0; i<p.NUM_LIGHTS; i++){
            if(frameCount%16==0){
                if(i > 0 && i <64)setLight(i, p.color(0, 0, 255));
                else setLight(i, p.color(0, 0, 0));
            }
            else if(frameCount%16==2){
                if(i > 64 && i <128)setLight(i, p.color(0, 0, 255));
                else setLight(i, p.color(0, 0, 0));
            }
            else if(frameCount%16==4){
                if(i > 128 && i <192)setLight(i, p.color(0, 0, 255));
                else setLight(i, p.color(0, 0, 0));
            }
            else if(frameCount%16==6){
                if(i > 192 && i <256)setLight(i, p.color(0, 0, 255));
                else setLight(i, p.color(0, 0, 0));
            }
            if(frameCount%16==8){
                if(i > 256 && i <320)setLight(i, p.color(0, 0, 255));
                else setLight(i, p.color(0, 0, 0));
            }
            else if(frameCount%16==10){
                if(i > 320 && i <384)setLight(i, p.color(0, 0, 255));
                else setLight(i, p.color(0, 0, 0));
            }
            else if(frameCount%16==12){
                if(i > 384 && i <448)setLight(i, p.color(0, 0, 255));
                else setLight(i, p.color(0, 0, 0));
            }
            else if(frameCount%16==14){
                if(i > 448 && i < p.NUM_LIGHTS)setLight(i, p.color(0, 0, 255));
                else setLight(i, p.color(0, 0, 0));
            }
        }
    }
}
