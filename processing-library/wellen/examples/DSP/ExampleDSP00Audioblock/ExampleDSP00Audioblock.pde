import wellen.*; 

/*
 * this example demonstrates how to write data directly into the audio buffer.
 *
 * by moving the mouse up and down values between [-1.0, 1.0] will be written into the audio buffer. while this
 * example does not create much coherent sounds it serves to demonstrate that samples can be produced from all sorts
 * of different sources.
 */

void settings() {
    size(640, 480);
}

void setup() {
    Wellen.dumpAudioInputAndOutputDevices();
    DSP.start(this);
    frameRate(120);
}

void draw() {
    background(255);
    DSP.draw_buffer(g, width, height);
}

void audioblock(float[] pOutputSamples) {
    for (int i = 0; i < pOutputSamples.length; i++) {
        pOutputSamples[i] = map(mouseY, 0, height, -1.0f, 1.0f);
    }
}
