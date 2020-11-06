package de.hfkbremen.ton.examples_ext;

import de.hfkbremen.ton.BeatMIDI;
import de.hfkbremen.ton.Ton;
import processing.core.PApplet;

public class SketchExampleEventBeatMIDIClock extends PApplet {

    private int mColor;

    private BeatMIDI mBeatMIDI;

    public void settings() {
        size(640, 480);
    }

    public void setup() {
        Ton.dumpMidiInputDevices();
        mBeatMIDI = BeatMIDI.start(this, "Bus 1");
    }

    public void draw() {
        background(mBeatMIDI.running() ? mColor : 0);
    }

    public void beat(int pBeat) {
        /* MIDI clock runs at 24 pulses per quarter note (PPQ). `pBeat % 12` is there for 0 every eigth note. */
        if (pBeat % 12 == 0) {
            mColor = color(random(127, 255),
                    random(127, 255),
                    random(127, 255));
            int mOffset = 4 * ((pBeat / 24) % 8);
            Ton.noteOn(36 + mOffset, 90);
            System.out.println(mBeatMIDI.bpm());
        } else {
            Ton.noteOff();
        }
    }

    public static void main(String[] args) {
        PApplet.main(SketchExampleEventBeatMIDIClock.class.getName());
    }
}