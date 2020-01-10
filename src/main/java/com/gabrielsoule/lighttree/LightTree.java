package com.gabrielsoule.lighttree;

import ddf.minim.AudioInput;
import ddf.minim.Minim;
import ddf.minim.analysis.BeatDetect;
import ddf.minim.analysis.FFT;
import processing.core.PApplet;

import java.io.File;
import java.util.HashMap;

public class LightTree extends PApplet {

    private static LightTree instance;
    private static boolean DEBUG = true;
    public final int NUM_LIGHTS = 512;
    public final int FRAME_RATE = 30;

    public OPC opc;
    public Minim minim;
    public AudioInput audioInput;
    public BeatDetect beat;
    public int[] lightColors = new int[512];
    public HashMap<Character, LightEffect> effects;
    public LightEffect activeEffect;
    public BeatDetector beatDetector;
    public KeyboardListener keyboardListener;
    public LightSequencer sequencer;
    public Config config;
    public FFT fft;
    public VisualizerUI ui;


    public static void main(String[] args) {
        PApplet.main("com.gabrielsoule.lighttree.LightTree");
    }

    @Override
    public void settings() {
        size(1920, 1080);
    }


    @Override
    public void setup() {
        surface.setResizable(true);
        String myPathToDataFolder = dataPath("");
        println(myPathToDataFolder);
        instance = this;
        this.opc = new OPC(this, "lightpi.local", 7890);
        minim = new Minim(this);
        audioInput = minim.getLineIn();
        frameRate(FRAME_RATE);
        beat = new BeatDetect();
        beat.detectMode(BeatDetect.SOUND_ENERGY);
        this.fft = new FFT(audioInput.bufferSize(), audioInput.sampleRate());
        fft.window(FFT.BARTLETT);

        this.config = new Config("config.yml");
        colorMode(HSB, 360, 255, 255, 255);
        for(int i = 0; i < NUM_LIGHTS; i++) {
            lightColors[i] = color(0, 0, 0);
        }
        Color.p = this;
        ui = new VisualizerUI();
        beatDetector = new BeatDetector(this, audioInput);
        keyboardListener = new KeyboardListener();
        this.sequencer = new LightSequencer(this);
        this.sequencer.loadFromConfig(config);
    }


    @Override
    public void stop() {
        minim.stop();
        super.stop();
    }

    @Override
    public void draw() {
        opc.writePixels();

        background(0);
        stroke(255);

        beatDetector.tick();
//        if(keyboardListener.keyPressed(config.getKeybind("KEY_DOUBLE_TEMPO"))) {
//            beatDetector.setTempoMultiplier(beatDetector.get);
//        }
        int[] sequencerResult = sequencer.sequence();
        ui.drawUI();
        opc.writePixels();

        keyboardListener.tick();

    }

    public static void log(String msg, Object... args) {
        if(DEBUG) {
            System.out.println(String.format(msg, args));
        }
    }

    private void drawSimulator2() {
        int lightBoxSize = 20;
        int lightBoxMarginHoriz = 10;
        int lightBoxMarginVert = 10;
        int numLightBoxesWide = 16;
        int numLightBoxesHigh = 32;
        int lightBoxAreaWidth = ((lightBoxSize + lightBoxMarginHoriz) * numLightBoxesWide) - lightBoxMarginHoriz;
        int lightBoxAreaHeight = ((lightBoxSize + lightBoxMarginVert) * numLightBoxesHigh) - lightBoxMarginVert;
        int frameMargin = lightBoxMarginVert;

//        int x = (width / 2) - (lightBoxAreaWidth / 2) ;/
        int x = 10;
        int y = frameMargin;

        noStroke();
        rectMode(CENTER);

        int lightIndex = 0;
        for (int i = 0; i < numLightBoxesWide; i++) {
            for (int j = 0; j < numLightBoxesHigh; j++) {
//                fill(lightColors[((i + 1) * (j + 1)) - 1]);
                fill(lightColors[lightIndex]);
                float scaleFactor = ColorUtil.getBrightness(lightColors[lightIndex]);
                scaleFactor = scaleFactor / 128f;
                scaleFactor = scaleFactor / 4;
                scaleFactor = scaleFactor + 0.75f;
//                System.out.println(scaleFactor);
//                scaleFactor *= 0.2;
//                print(scaleFactor);

                lightIndex++;
//                rect(x + lightBoxSize / 2, y + lightBoxSize / 2, lightBoxSize * scaleFactor, lightBoxSize * scaleFactor, (lightBoxSize * scaleFactor) / 2f);
                ellipse(x + lightBoxSize / 2f, y + lightBoxSize / 2f, lightBoxSize * scaleFactor, lightBoxSize * scaleFactor);
                y += lightBoxSize + lightBoxMarginVert;
            }
            y = frameMargin;
            x += lightBoxSize + lightBoxMarginHoriz;
        }


    }

    //old visualizer, no longer used
    private void drawSimulator() {
        rectMode(CORNER);
        fill(60, 255, 255);
        noStroke();
        int frameMargin = min(height, width) / 10;
        int boxHeight = 10;
        int boxMargin = boxHeight / 2;
        int verticalLength = 2;
        int frameWidth = ((boxHeight + boxMargin) * (64 - verticalLength) - boxMargin);
        int x = (width / 2) - (frameWidth / 2) + (boxHeight + boxMargin) / 2;
        int y = frameMargin;
        int draws = 0;

        //special case of the first pixel which sticks out
        fill(lightColors[0]);
        rect(x - (boxHeight + boxMargin), y, boxHeight, boxHeight,0);

        for(int i = 0; i < 8; i++) {
            fill(i * 22.5f, 255, 255);
            boolean goingRight = i % 2 == 0;
            for(int j = 0; j < 64 - verticalLength; j++) {
                fill(lightColors[64 * i + j + 1]);
                if(goingRight) {
                    rect(x, y, boxHeight, boxHeight, 0);
                    x += boxHeight + boxMargin;
                }
                else {
                    rect(x, y, boxHeight, boxHeight,0);
                    x -= boxHeight + boxMargin;
                }
                draws++;
            }
            if(goingRight) x -= boxHeight + boxMargin; //move X to be under the last pixel
            else x += boxHeight + boxMargin;

            for(int k = 64 - verticalLength; k < 64; k++) {
                fill(lightColors[64 * i + k + 1]);
                draws++;
                if(draws > 510) break;
                y += boxHeight + boxMargin;
                rect(x, y, boxHeight, boxHeight,0);
            }

            y += boxHeight + boxMargin;
        }

        rect(x - (boxHeight + boxMargin), y - (boxHeight + boxMargin), boxHeight, boxHeight,0);
        rectMode(CORNER);
    }

    void setLight(int index, int c) {

        //quietly fail if desired light is invalid.
        //This makes dynamic effects much easier to write
        if(index < 0 || index >= NUM_LIGHTS) {
            return;
        }

        //Use the color value of -1 to indicate a RANDOM color (with full saturation)
//        if(c == -1) {
//            c = color(random(0, 360), 255, 255);
//        }

        lightColors[index] = c;
        c = fixColor(c);

        //tree-specific mapping, since segments are out of order.
        //TODO make this configurable, right now every installation needs a source edit... not good
        int segment = (int) (index / 64f);
        switch(segment){
            case 0:
                index = 319 - index;
                break;

            case 1:
                index = 320 + (index - 64);
                break;

            case 2:
                index = 255 - (index - 128);
                break;

            case 3:
                index = index - 64;
                break;

            case 4:
                index = 127 - (index - 256);
                break;

            case 5:
                index = index - 320;
                break;

            case 6:
                index = index;
        }
        opc.setPixel(index, c);
    }

    @Override
    public void keyPressed() {

//        activeEffect.key = key;
//        activeEffect.keyPressed();
//        System.out.println("key: ["+key+"]");
//        if (key == 'b') {
//            beatDetector.handleKeyPress();
//        }
//        for(LightEffect e : sequencer.getActiveEffects()) {
//            e.keyPressed();
//        }
        keyboardListener.handleKeyPress(keyCode);
    }

    @Override
    public void keyReleased() {
        keyboardListener.handleKeyReleased(keyCode);
    }

    /**
     * Lots of cheap Chinese LEDs use GRB, not RGB like you'd expect. This function converts RGB -> GRB, and also
     * bakes the alpha value into the RGB values of the input color as blackness, since the FC server doesn't use alpha
     * @param oldRGB color to convert
     * @return  the fixed color, ready to be directly fed into the OPC
     */
    private int fixColor(int oldRGB) {
        float alphaFrac = ((oldRGB >> 24) & 0xFF) / (float) 255;
        return (((oldRGB >> 24) & 0xFF) << 24) |
                ((int)(((oldRGB >> 8) & 0xFF) * alphaFrac) << 16) |
                ((int)(((oldRGB >> 16) & 0xFF) * alphaFrac) << 8) |
                ((int) ((oldRGB & 0xFF) * alphaFrac));
    }

    public static LightTree getInstance() {
        return instance;
    }
}
