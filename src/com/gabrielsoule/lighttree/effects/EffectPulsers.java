package com.gabrielsoule.lighttree.effects;

import com.gabrielsoule.lighttree.ColorGradient;
import com.gabrielsoule.lighttree.LightEffect;
import com.gabrielsoule.lighttree.LightTree;

import java.util.ArrayList;

public class EffectPulsers extends LightEffect {
    public float speed;
    public int direction; // 1 indicates moving left,
    public int focusPoint;
    public ColorGradient color;
    public float gradientFalloff;
    public int[] foci;

    public ArrayList<Pulser> pulsers = new ArrayList<>();

    public EffectPulsers(LightTree p, float speed, ColorGradient color, float gradientFalloff, int... foci) {
        super(p);
        this.speed = speed;
        this.direction = direction;
        this.focusPoint = focusPoint;
        this.color = color;
        this.gradientFalloff = gradientFalloff;
        this.foci = foci;
    }

    @Override
    public void draw() {
        //spawn new
        if(p.beatDetector.beat()) {
            pulsers.add(new Pulser(focusPoint, speed, ))
        }
    }

    private void setLight(int index, int color) {

    }

    private class Pulser {
        public float birthDate;
        public float position;
        public float speed;
        public int direction;
        public ColorGradient color;
        public float gradientFalloff;
        public int length;

        boolean destroyed = false;
        int destroyPos;

        public Pulser(float startingPos, float speed, int direction, ColorGradient color, float gradientFalloff) {
            this.length = (int) ((speed * 60) / (float) p.beatDetector.getEstBPM() + 1);
            this.speed = speed / p.frameRate; //easier to work with distance per frame rather than per second
            this.birthDate = p.millis();
            this.position = startingPos;
            this.direction = direction;
            this.color = color;
            this.gradientFalloff = gradientFalloff;
        }

        public void moveAndDraw() {
            this.position += speed;
            int startPosition = Math.round(position);
            for(int i = 0; i <  length; i++) {
                setLight(startPosition, color.get(i / (float) length));
                startPosition += direction;
            }
        }

        public void draw() {

        }

        public int visualPosition() {
            return Math.round(position);
        }

        public void destroy() {
            if(!destroyed) {
                destroyPos = visualPosition();
                destroyed = true;
            }
        }
    }
}
