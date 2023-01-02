package wellen.examples.technique;

import processing.core.PApplet;
import wellen.Beat;
import wellen.Note;
import wellen.Tone;
import wellen.Wellen;

public class TechniqueAlgorithmicComposition00Modulo extends PApplet {

    private boolean mPlaying = false;

    public void settings() {
        size(640, 480);
    }

    public void setup() {
        Tone.instrument().set_oscillator_type(Wellen.WAVEFORM_TRIANGLE);
        Beat.start(this, 120 * 4);
    }

    public void draw() {
        background(255);
        fill(0);
        float mScale;
        if (mPlaying) {
            mScale = width * 0.1f;
        } else {
            mScale = width * 0.25f;
        }
        ellipse(width * 0.5f, height * 0.5f, mScale, mScale);
    }

    public void beat(int beat) {
        mPlaying = true;
        if (beat % 32 == 0) {
            Tone.note_on(Note.NOTE_A4, 80);
        } else if (beat % 8 == 0) {
            Tone.note_on(Note.NOTE_A3, 110);
        } else if (beat % 2 == 0) {
            Tone.note_on(Note.NOTE_A2 + (beat % 4) * 3, 120);
        } else if (beat % 11 == 0) {
            Tone.note_on(Note.NOTE_C4, 90, 0.05f);
        } else if (beat % 13 == 0) {
            Tone.note_on(Note.NOTE_C5, 100, 0.1f);
        } else {
            mPlaying = false;
        }
    }

    public static void main(String[] args) {
        PApplet.main(TechniqueAlgorithmicComposition00Modulo.class.getName());
    }
}
