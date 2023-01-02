import wellen.*; 
import wellen.dsp.*; 

import wellen.extra.daisysp.*;

Pluck mPluck;

ReverbSc mReverb;

int mMIDINoteCounter = 0;

final int[] mMIDINotes = {36, 48, 39, 51};

void settings() {
    size(640, 480);
}

void setup() {
    mReverb = new ReverbSc();
    mReverb.Init(Wellen.DEFAULT_SAMPLING_RATE);
    mReverb.SetFeedback(0.75f);
    mReverb.SetLpFreq(8000);
    mPluck = new Pluck();
    mPluck.Init();
    mPluck.SetDecay(0.5f);
    mPluck.SetDamp(0.85f);
    DSP.start(this, 2);
    Beat.start(this, 240);
}

void draw() {
    background(255);
    noStroke();
    fill(0);
    float mScale = 0.98f * height;
    circle(width * 0.5f, height * 0.5f, mScale);
    stroke(255);
    DSP.draw_buffers(g, width, height);
}

void mouseMoved() {
    mReverb.SetFeedback(map(mouseX, 0, width, 0, 1));
    mReverb.SetLpFreq(map(mouseY, 0, height, 0, 20000));
}

void beat(int beatCount) {
    mPluck.Trig();
    mPluck.SetFreq(DaisySP.mtof(mMIDINotes[mMIDINoteCounter]));
    mMIDINoteCounter++;
    mMIDINoteCounter %= mMIDINotes.length;
}

void audioblock(float[] output_signalLeft, float[] output_signalRight) {
    for (int i = 0; i < output_signalLeft.length; i++) {
        if (keyPressed) {
            output_signalLeft[i] = output_signalRight[i] = mPluck.Process();
        } else {
            mReverb.Process(mPluck.Process());
            output_signalLeft[i] = mReverb.GetLeft();
            output_signalRight[i] = mReverb.GetRight();
        }
    }
}
