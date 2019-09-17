package com.gabrielsoule.lighttree;

import ddf.minim.AudioInput;
import ddf.minim.analysis.BeatDetect;

public class BeatDetector {
    private final LightTree p;
    private final AudioInput audio;
    private BeatDetect minimDetector;
    //Configuration options
    private int ENGAGE_LOCK_TIME = 1500; //time to wait without a keypress before engaging
    private int OVERLAP_THRESHOLD = 400; //if a manual beat and an auto beat both trigger in this time, ignore one
    private int BEATS_TO_IGNORE = 3;
    private int BEATS_BEFORE_AUTO_GENERATE = 8;
    private int SYNC_NEW_BEAT_THRESHOLD;
    private float TUNING = -0.6f;
    boolean manualMode = true;
    private boolean beat = false;
    //    private boolean enqueueBeat = false;
    private int lastManualBeatTime = 0;
    //    private int lastAutoBeatTime;
    private int manualBeatCount;
    private int lastBeatTime;
    private int elapsed;
    private float estPeriod;
    private DetectionPhase phase = DetectionPhase.SLEEPING;
    private boolean debug = true;

    BeatDetector(LightTree p, AudioInput audioStream) {
        this.p = p;
        this.audio = audioStream;
        this.minimDetector = new BeatDetect();
        minimDetector.detectMode(BeatDetect.SOUND_ENERGY);
    }

    public boolean beat() {
        return beat;
    }

    public void tick() {
        beat = false;
        if(!manualMode) {
            if (phase == DetectionPhase.ADJUSTING && p.millis() - lastManualBeatTime > ENGAGE_LOCK_TIME) {
                phase = DetectionPhase.ENGAGED;
            }

            if (phase == DetectionPhase.SYNCHRONIZING && p.millis() - lastManualBeatTime > ENGAGE_LOCK_TIME) {
                print("System locked and engaged at " + this.getEstBPM() + " BPM with a period of " + estPeriod + "ms.");
                if (manualBeatCount > 0) {
                    this.estPeriod = 1000 / (Math.round(60000 / estPeriod + TUNING) / 60f);
                    print("Rounded to " + this.getEstBPM() + " with a period of " + estPeriod + "ms.");
                }
                phase = DetectionPhase.ENGAGED;
            }
            if ((phase == DetectionPhase.ENGAGED || phase == DetectionPhase.ADJUSTING ||
                    (phase == DetectionPhase.SYNCHRONIZING && manualBeatCount > BEATS_BEFORE_AUTO_GENERATE))
                    && p.millis() - lastBeatTime > estPeriod) {
                beat = true;
                print("Auto beat: " + p.millis());
                lastBeatTime = p.millis();
            }
        }
    }

    private void print(String message) {
        if (debug) System.out.println("[BeatGEN] " + message);
    }

    public void handleKeyPress() {
        if(manualMode) {
            this.beat = true;
        } else {
            switch (phase) {
                case SLEEPING:  //nothing has happened yet
                    print("Waking up. Keypress detected. Synchronizing beat to future keypresses...");
                    phase = DetectionPhase.SYNCHRONIZING;
                    lastBeatTime = lastManualBeatTime = p.millis();
                    break;
                case SYNCHRONIZING:
                    manualBeatCount++;
                    if (p.millis() - lastBeatTime > OVERLAP_THRESHOLD) {
                        beat = true;
                        print("Manual beat: " + p.millis());
                    }
                    if (manualBeatCount > BEATS_TO_IGNORE) {
                        elapsed += p.millis() - lastManualBeatTime;
                        print("Elapsed: " + elapsed);
                        estPeriod = elapsed / (manualBeatCount - BEATS_TO_IGNORE);
                        print("Est. Period: " + estPeriod);
                    }
                    lastBeatTime = lastManualBeatTime = p.millis();
                    break;
                case ADJUSTING:
                    manualBeatCount = 0; //reset manual beat count in case we re-synchronize
                    elapsed = 0;
                    print("Keypress detected. Synchronizing beat to future keypresses...");
                    lastBeatTime = lastManualBeatTime = p.millis();
                    phase = DetectionPhase.SYNCHRONIZING;
                    break;
                case ENGAGED:
                    phase = DetectionPhase.ADJUSTING;
                    if (p.millis() - lastBeatTime < estPeriod / 2) {
                        print("Estimated BPM too fast, or out of sync. Re-synchronizing to keypress.");
                        lastBeatTime = lastManualBeatTime = p.millis();
                    } else {
                        print("Estimated BPM too slow, or out of sync. Re-synchronizing to keypress.");
                        beat = true; //play beat
                        lastBeatTime = lastManualBeatTime = p.millis();
                    }
                    break;
            }

        }
    }

    public boolean ready() {
        return estPeriod != 0;
    }

    public int getEstBPM() {
        return (int) (60000 / estPeriod);
    }

    private enum DetectionPhase {SLEEPING, ADJUSTING, SYNCHRONIZING, ENGAGED}
}
