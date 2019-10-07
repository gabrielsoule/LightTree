package com.gabrielsoule.lighttree.effects;

import com.gabrielsoule.lighttree.*;

import java.util.ArrayList;
import java.util.Iterator;

public class EffectPulsers extends LightEffect {
    public float speed; //lights per second
    public int direction; // 1 indicates moving left,
    public int focusPoint;
    public ColorGradient color;
    public float gradientFalloff;
    public int[] foci;
    public Pulser[] collisionArray;
    public ArrayList<Pulser> pulsers = new ArrayList<>();

//    public EffectPulsers(LightTree p, float speed, ColorGradient color, float gradientFalloff, int... foci) {
//        super(p);
//        this.speed = speed;
//        this.direction = direction;
//        this.focusPoint = focusPoint;
//        this.color = color;
//        this.gradientFalloff = gradientFalloff;
//        this.foci = foci;
//        this.collisionArray = new Pulser[p.NUM_LIGHTS];
//    }

    @Override
    public void setup() {

    }

    @Override
    public void configure(int[] integerConfig, Color[] colorConfig) {

    }

    @Override
    public void draw() {
        //spawn new
        if((p.beatDetector.beat() && p.beatDetector.ready()) || (p.beatDetector.beat() && p.beatDetector.manualMode)) {
            for(int focus : foci) {
                pulsers.add(new Pulser(focus, speed,  1, color, gradientFalloff));
                pulsers.add(new Pulser(focus, speed, -1, color, gradientFalloff));
            }
        }

        pulsers.removeIf(p -> p.markForRemoval);

        for(Pulser p : pulsers) {
            p.moveAndDraw();
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

        public int spawnBound;
        public int destroyBound;

        boolean destroyed = false;
        boolean markForRemoval = false;
        int destroyPos;

        public Pulser(int startingPos, float speed, int direction, ColorGradient color, float gradientFalloff) {
            this.length = p.beatDetector.manualMode ? 50 : (int) ((speed * 60) / (float) p.beatDetector.getEstBPM() + 1);
            this.speed = speed / p.frameRate; //easier to work with distance per frame rather than per second
            this.birthDate = p.millis();
            this.position = this.startingPos = startingPos;
            this.direction = direction;
            this.color = color;
            this.gradientFalloff = gradientFalloff;

            this.spawnBound = startingPos;
            this.destroyBound = direction == 1 ? p.NUM_LIGHTS : 0;

        }

        public void moveAndDraw() {
            this.position += speed * direction;
            int drawPos = Math.round(position);
            if(!destroyed && (position < 0 || visualPosition() >= p.NUM_LIGHTS)) {
                this.destroy();
                System.out.println("Destroying due to out of bounds");
            }
            else if(!destroyed && collisionArray[drawPos] != null && drawPos != this.startingPos) {
                collisionArray[drawPos].destroy();
                this.destroy();
                System.out.println("Destroying due to collision");
                collisionArray[drawPos] = null;
            }

            for(int i = 0; i <  length; i++) {
//                if(!destroyed || ((direction == 1 && drawPos <= destroyPos) || (direction == -1 && drawPos >= destroyPos)))
//                {
////                if(!(destroyed && ((direction == 1 && drawPos > destroyPos) || ((direction == -1 && drawPos < destroyPos))))) {
//                    //if it is NOT the case that the pulser has been destroyed and the desired light to turn on is ahead
//                    //of the point at which the pulser was destroyed, set the light
//                    setLight(drawPos, color.get(i / (float) length));
//                }

                if(MathUtil.between(drawPos, spawnBound, destroyBound)) {
                    setLight(drawPos, color.get(i / (float) length));
                }
                drawPos -= direction;
            }


            if(destroyed && (direction == 1 ? drawPos > destroyPos : drawPos < destroyPos)) {
//                pulsers.remove(this); //cleanup
                System.out.println("Marking for full removal");
                this.markForRemoval = true;
            }
        }


        public int visualPosition() {
            return Math.round(position);
        }

        public void destroy() {
            if(!destroyed) {
                destroyPos = visualPosition();
                destroyBound = visualPosition();
                destroyed = true;
            }
        }
    }
}
