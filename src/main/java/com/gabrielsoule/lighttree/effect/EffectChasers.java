package com.gabrielsoule.lighttree.effect;

import com.gabrielsoule.lighttree.*;
import com.gabrielsoule.lighttree.util.Color;
import com.gabrielsoule.lighttree.util.MathUtil;

import java.util.ArrayList;

public class EffectChasers extends LightEffect {

    private ArrayList<Chaser> chasers;
    private ArrayList<Chaser> chasersToRemove;

    @Override
    public void setup() {
        chasers = new ArrayList<>();
        chasersToRemove = new ArrayList<>();
        for (int i = 0; i < config.getFloatFromConfig("num-chasers"); i++) {
            spawnChaser();
        }
    }
//

    @Override
    public void draw() {
        flushColors();
        int chasersToSpawn = 0;
        for(Chaser chaser : chasers) {
            chaser.moveAndDraw();
            if(chaser.outOfBounds) {
                chasersToSpawn++;
            }
        }

        for (int i = 0; i < chasersToSpawn; i++) {
            spawnChaser();
        }
        chasers.removeIf(chaser -> chaser.outOfBounds);
    }

    private void spawnChaser() {
        float velocity = p.random(this.config.getFloatFromConfig("min-velocity"), this.config.getFloatFromConfig("max-velocity"));
        velocity = p.random(0, 1) > 0.5f ? velocity : -velocity; //random direction
        int c2 = this.config.getColor(1);
        c2 = Color.setAlpha(c2, (int) (255 * this.config.getFloatFromConfig("tail-alpha")));
        chasers.add(
                new Chaser(
                        new ColorGradient(this.config.getColor(0), 0, c2, 1),
                        (int) p.random(0, p.NUM_LIGHTS),
                        (int) p.random(this.config.getFloatFromConfig("min-length"), this.config.getFloatFromConfig("max-length")),
                        velocity)
        );
    }

    public class Chaser {
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
            this.position += velocity / (float) p.FRAME_RATE;
            int chaserHeadPosition = Math.round(position); //actual light index to draw head
            int drawPosition = chaserHeadPosition;
            for(int i = 0; i < length; i++) {
                setLight(drawPosition, color.get(i / (float) length));
                drawPosition -= direction;
            }


            if(!MathUtil.between(chaserHeadPosition, 0 - length, p.NUM_LIGHTS + length)) {
                this.outOfBounds = true;
            }
        }
    }
}
