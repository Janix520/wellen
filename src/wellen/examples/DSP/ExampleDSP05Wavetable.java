package wellen.examples.DSP;

import processing.core.PApplet;
import wellen.Wellen;
import wellen.dsp.DSP;
import wellen.dsp.Wavetable;

public class ExampleDSP05Wavetable extends PApplet {

    /*
     * this example demonstrates how to use a *wavetable* ( a chunk of memory ) and play it back at different
     * frequencies and amplitudes. in this example a wavetable is used to emulate an oscillator (VCO) with different
     * wave shapes. use keys `1`–`8` to select waveshapes.
     *
     * this example also demonstrates the effect of different interpolation methods for sample data. use keys
     * `+` and `-` to change the table size of a wave shape. use keys `q`, `w` and `e` to select an interpolation
     * method. note that the effect of interpolation can be quite significant at small table sizes.
     *
     * and lastly this example also demonstrates how to modify amplitude and frequency of an oscillator with
     * interpolation. use `SPACE` to toggle interpolation. see explantion inline.
     */
    private int mWavetableSize = 6;
    private Wavetable mWavetable;
    private boolean mInterpolateAmplitudeAndFrequency = false;

    public void settings() {
        size(640, 480);
    }

    public void setup() {
        init_wavetable();
        DSP.start(this);
    }

    public void draw() {
        background(255);
        DSP.draw_buffers(g, width, height);
    }

    public void mouseDragged() {
        mWavetable.set_frequency(2.0f * Wellen.DEFAULT_SAMPLING_RATE / Wellen.DEFAULT_AUDIOBLOCK_SIZE);
        mWavetable.set_amplitude(0.25f);
    }

    public void mouseMoved() {
        final float mNewFrequency = map(mouseX, 0, width, 55, 880);
        final float mNewAmplitude = map(mouseY, 0, height, 0.0f, 0.9f);
        if (mInterpolateAmplitudeAndFrequency) {
            /* these versions of `set_frequency` + `set_amplitude` take a second paramter which define the duration in
             * samples from current to new value. on the one hands this prevents unwanted artifacts ( e.g crackling
             * noise when changing amplitude quickly especially on smoother wave shape like sine waves ) and on the
             * other hand can be used to create glissando or portamento effects.
             */
            mWavetable.set_frequency(mNewFrequency, Wellen.seconds_to_samples(0.5f));
            mWavetable.set_amplitude(mNewAmplitude, Wellen.millis_to_samples(10));
        } else {
            mWavetable.set_frequency(mNewFrequency);
            mWavetable.set_amplitude(mNewAmplitude);
        }
    }

    public void keyPressed() {
        switch (key) {
            case '1':
                Wavetable.sine(mWavetable.get_wavetable());
                break;
            case '2':
                Wavetable.triangle(mWavetable.get_wavetable());
                break;
            case '3':
                Wavetable.sawtooth(mWavetable.get_wavetable());
                break;
            case '4':
                Wavetable.square(mWavetable.get_wavetable());
                break;
            case '5':
                Wavetable.triangle(mWavetable.get_wavetable(), 2);
                break;
            case '6':
                Wavetable.sawtooth(mWavetable.get_wavetable(), 8);
                break;
            case '7':
                Wavetable.square(mWavetable.get_wavetable(), 16);
                break;
            case '8':
                randomize(mWavetable.get_wavetable());
                break;
            case '+':
                mWavetableSize++;
                init_wavetable();
                break;
            case '-':
                mWavetableSize--;
                if (mWavetableSize < 1) {
                    mWavetableSize = 1;
                }
                init_wavetable();
                break;
            case 'q':
                mWavetable.set_interpolation(Wellen.WAVESHAPE_INTERPOLATE_NONE);
                break;
            case 'w':
                mWavetable.set_interpolation(Wellen.WAVESHAPE_INTERPOLATE_LINEAR);
                break;
            case 'e':
                mWavetable.set_interpolation(Wellen.WAVESHAPE_INTERPOLATE_CUBIC);
                break;
            case ' ':
                mInterpolateAmplitudeAndFrequency = !mInterpolateAmplitudeAndFrequency;
                break;
        }
    }

    public void audioblock(float[] output_signal) {
        for (int i = 0; i < output_signal.length; i++) {
            output_signal[i] = mWavetable.output();
        }
    }

    private void init_wavetable() {
        mWavetable = new Wavetable(1 << mWavetableSize);
        Wavetable.sine(mWavetable.get_wavetable());
    }

    private void randomize(float[] pWavetable) {
        for (int i = 0; i < pWavetable.length; i++) {
            pWavetable[i] = random(-1, 1);
        }
    }

    public static void main(String[] args) {
        PApplet.main(ExampleDSP05Wavetable.class.getName());
    }
}