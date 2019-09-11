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
    public Pulser[] collisionArray;
    public ArrayList<Pulser> pulsers = new ArrayList<>();

    public EffectPulsers(LightTree p, float speed, ColorGradient color, float gradientFalloff, int... foci) {
        super(p);
        this.speed = speed;
        this.direction = direction;
        this.focusPoint = focusPoint;
        this.color = color;
        this.gradientFalloff = gradientFalloff;
        this.foci = foci;
        this.collisionArray = new Pulser[p.numLights];
    }

    @Override
    public void draw() {
        //spawn new
        if(p.beatDetector.beat() && p.beatDetector.ready()) {
            for(int focus : foci) {
                pulsers.add(new Pulser(focus, speed,  1, color, gradientFalloff));
                pulsers.add(new Pulser(focus, speed, -1, color, gradientFalloff));
            }
        }
    }

    private class Pulser {
        public float birthDate;
        public float startingPos;
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
            this.position = this.startingPos = startingPos;
            this.direction = direction;
            this.color = color;
            this.gradientFalloff = gradientFalloff;
        }

        public void moveAndDraw() {
            this.position += speed;
            int drawPos = Math.round(position);
            if(position < 0 || position > p.NUM_LIGHTS) {
                this.destroy();
            }

            if(collisionArray[drawPos] != null && drawPos != this.startingPos) {
                collisionArray[drawPos].destroy();
                this.destroy();
                collisionArray[drawPos] = null;
            }

            for(int i = 0; i <  length; i++) {
                if(!(destroyed && ((direction == 1 && drawPos > destroyPos) || ((direction == -1 && drawPos < destroyPos))))) {
                    //if it is NOT the case that the pulser has been destroyed and the desired light to turn on is ahead
                    //of the point at which the pulser was destroyed, set the light
                    setLight(drawPos, color.get(i / (float) length));
                }
                drawPos += direction;
            }

            if(drawPos > destroyPos) {
                pulsers.remove(this); //cleanup
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
