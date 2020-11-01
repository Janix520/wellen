package de.hfkbremen.ton.examples_ext;

import de.hfkbremen.ton.DSP;
import processing.core.PApplet;

public class SketchExampleDSPPassThrough extends PApplet {

    public void settings() {
        size(640, 480);
    }

    public void setup() {
        DSP.dumpAudioDevices();
        DSP.start(this, 1, 1);
    }

    public void draw() {
        background(255);
        stroke(0);
        final int mBufferSize = DSP.buffer_size();
        if (DSP.buffer() != null) {
            for (int i = 0; i < mBufferSize; i++) {
                final float x = map(i, 0, mBufferSize, 0, width);
                point(x, map(DSP.buffer()[i], -1, 1, 0, height));
            }
        }
    }

    public void audioblock(float[] pOutputSamples, float[] pInputSamples) {
        for (int i = 0; i < pInputSamples.length; i++) {
            pOutputSamples[i] = pInputSamples[i] * 0.25f;
        }
    }

    public static void main(String[] args) {
        PApplet.main(SketchExampleDSPPassThrough.class.getName());
    }
}
