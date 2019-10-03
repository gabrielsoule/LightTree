package com.gabrielsoule.lighttree.effects;

import com.gabrielsoule.lighttree.ColorGradient;
import com.gabrielsoule.lighttree.LightEffect;
import com.gabrielsoule.lighttree.LightTree;

import java.util.*;

public class EffectFlashSegments extends LightEffect {

    //change this based on actual hardware setup
    private final int LIGHTS_PER_SEGMENT = 64;
    private float duration = 2f;
    private ColorGradient gradient;

    //we scramble this array during setup so that segments turn on randomly
    private Deque<Segment> segments;
    public EffectFlashSegments(LightTree p, int startColor, int endColor) {
        super(p);
        this.gradient = new ColorGradient(startColor, 0, endColor, 1);

        ArrayList<Segment> segmentArray = new ArrayList<>();

        for(int i = 0; i < p.NUM_LIGHTS / LIGHTS_PER_SEGMENT; i++) {
            segmentArray.add(new Segment(i * LIGHTS_PER_SEGMENT, gradient));
        }
        Collections.shuffle(segmentArray);
        segments = new ArrayDeque<>(segmentArray);
    }

    @Override
    public void setup() {
    }

    @Override
    public void draw() {
        if(p.beatDetector.beat()) {
//            segments.get(0).start();
            Segment segment = segments.removeLast();
            segment.start();
            segments.addFirst(segment); //back of the line
        }

        for(Segment s: segments) {
            s.draw();
        }
    }

    private class Segment {
        int startTime;
        int startLight;
        ColorGradient gradient;
        boolean active;

        public Segment(int startLight, ColorGradient gradient) {
            this.startLight = startLight;
            this.gradient = gradient;
        }

        public void start() {
            System.out.println("Starting segment " + this.startLight);
            active = true;
            this.startTime = p.millis();
        }

        public void draw() {
            if(active) {
                //most dance songs are around 120 BPM. since on the first beat we won't know what the delta is,
                //we use 500 as a default value which is a 120BPM song's beat period in ms
                System.out.println(this.startLight+ ":" + p.millis());
                System.out.println(this.startLight+ ":" + startTime);
                System.out.println(this.startLight+ ":" + p.beatDetector.getLastBeatDelta());
                float progress =
                        (p.millis() - startTime) / (p.beatDetector.getLastBeatDelta() == 0 ? 500f : p.beatDetector.getLastBeatDelta());
                progress /= duration; //divide by duration factor
                System.out.println(progress);
                if(progress >= 1) {
                    active = false;
                    return;
                }
                for(int i = startLight; i < startLight + LIGHTS_PER_SEGMENT; i++) {
                    setLight(i, this.gradient.get(progress));
                }
            }
        }

    }
}