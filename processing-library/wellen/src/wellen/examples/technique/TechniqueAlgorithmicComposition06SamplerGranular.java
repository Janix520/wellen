package wellen.examples.technique;

import processing.core.PApplet;
import wellen.Wellen;
import wellen.dsp.DSP;
import wellen.dsp.Sampler;

import java.util.ArrayList;

public class TechniqueAlgorithmicComposition06SamplerGranular extends PApplet {

    private Sampler fSampler;
    private final ArrayList<Sampler> fSamplers = new ArrayList<Sampler>();

    public void settings() {
        size(640, 480, P3D);
    }

    public void setup() {
        System.out.println(sketchPath());
        byte[] mData = loadBytes("../../../resources/a_portrait_in_reverse.raw");
        fSampler = new Sampler();
        fSampler.load(mData);
        fSampler.set_loop_all();

        DSP.start(this);
    }

    public void draw() {
        background(255);
        noFill();
        stroke(0);
        DSP.draw_buffers(g, width, height);
        stroke(0, 31);
        Wellen.draw_buffer(g, width, height, fSampler.get_data());

        stroke(0);
        drawPosition(fSampler.get_in(), height / 8);
        drawPosition(fSampler.get_out(), height / 8);
        drawPosition(fSampler.get_position(), height / 16);
        for (Sampler s : fSamplers) {
            drawPosition(s.get_position(), height / 16);
        }
    }

    public void mousePressed() {
        if (mouseButton == LEFT) {
            fSampler.set_in((int) map(mouseX, 0, width, 0, fSampler.get_data().length));
            for (Sampler s : fSamplers) {
                s.set_in(fSampler.get_in());
            }
        } else {
            fSampler.set_out((int) map(mouseX, 0, width, 0, fSampler.get_data().length));
            for (Sampler s : fSamplers) {
                s.set_out(fSampler.get_out());
            }
        }
    }

    public void keyPressed() {
        switch (key) {
            case '+':
                fSampler.set_speed(fSampler.get_speed() + 0.1f);
                for (Sampler s : fSamplers) {
                    s.set_speed(fSampler.get_speed());
                }
                break;
            case '-':
                fSampler.set_speed(fSampler.get_speed() - 0.1f);
                for (Sampler s : fSamplers) {
                    s.set_speed(fSampler.get_speed());
                }
                break;
            case ' ':
                Sampler s = new Sampler(fSampler.get_data());
                s.set_in(fSampler.get_in());
                s.set_out(fSampler.get_out());
                s.enable_loop(true);
                s.set_loop_all();
                s.start();
                fSamplers.add(s);
                break;
            case 'c':
                fSamplers.clear();
                break;
        }
    }

    public void audioblock(float[] output_signal) {
        for (int i = 0; i < output_signal.length; i++) {
            output_signal[i] = fSampler.output();
            for (Sampler s : fSamplers) {
                output_signal[i] += s.output();
            }
            output_signal[i] /= 1 + fSamplers.size() * 0.1f;
        }
    }

    private void drawPosition(int pPosition, int pPadding) {
        final float x = map(pPosition, 0, fSampler.get_data().length, 0, width);
        line(x, 0 + pPadding, x, height - pPadding);
    }

    public static void main(String[] args) {
        Wellen.run_sketch_with_resources(TechniqueAlgorithmicComposition06SamplerGranular.class);
    }
}