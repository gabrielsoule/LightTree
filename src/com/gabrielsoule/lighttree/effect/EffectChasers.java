package com.gabrielsoule.lighttree.effect;

import com.gabrielsoule.lighttree.ColorGradient;
import com.gabrielsoule.lighttree.LightEffect;
import com.gabrielsoule.lighttree.MathUtil;

import java.util.ArrayList;

public class EffectChasers extends LightEffect {

    private ArrayList<Chaser> chasers;

    @Override
    public void setup() {
        chasers = new ArrayList<>();
        for(int i = 0; i < config.getFloat("num-chasers"); i++) {
            chasers.add(
                    new Chaser(
                            new ColorGradient(this.config.getColor(0), 0 , this.config.getColor(1), 1),
                            (int) p.random(0, p.NUM_LIGHTS),
                            (int) p.random(this.config.getFloat("min-length"), this.config.getFloat("max-length")),
                            p.random(this.config.getFloat("min-velocity"), this.config.getFloat("max-velocity")))
                    );
        }
    }

    @Override
    public void draw() {

    }

    private void spawnChaser() {

    }

    private class Chaser {
        public boolean outOfBounds;
        public float position;
        public int length;
        public float velocity;
        public int direction;
        public ColorGradient color;

        public Chaser(ColorGradient color, float position, int length, float velocity) {
            this.color = color;
            this.position = position;
            this.length = length;
            this.velocity = velocity;
            this.direction = velocity >= 0 ? 1 : -1;
            outOfBounds = false;
        }

        public void moveAndDraw() {
            this.position += velocity;
            int lightPosition = Math.round(position);
            for(int i = lightPosition; i < lightPosition + length; i+= direction){
                setLight(i, color.get((i - lightPosition) / (float) length));
            }

            if(!MathUtil.between(lightPosition, 0 - length, p.NUM_LIGHTS + length)) {
                this.outOfBounds = true;
            }
        }
    }
}
