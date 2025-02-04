package wellen.examples.technique;

import processing.core.PApplet;
import processing.core.PVector;
import wellen.SampleDataSNARE;
import wellen.Wellen;
import wellen.dsp.DSP;
import wellen.dsp.Sampler;

import java.util.ArrayList;

public class TechniqueAlgorithmicComposition02VisualModel extends PApplet {

    private final int NUM_OF_CONTROLLERS = 1;
    private final ArrayList<CircleController> mControllers = new ArrayList<CircleController>();

    public void settings() {
        size(640, 480);
    }

    public void setup() {
        for (int i = 0; i < NUM_OF_CONTROLLERS; i++) {
            CircleController c = new CircleController();
            c.position.set(random(width), random(height));
            c.radius = random(20, 120);
            c.speed = random(0, 5);
            mControllers.add(c);
        }

        Wellen.dumpAudioInputAndOutputDevices();
        DSP.start(this);
    }

    public void draw() {
        background(255);
        final float mDelta = 1.0f / frameRate;

        noFill();
        stroke(0);
        DSP.draw_buffers(g, width, height);

        for (CircleController c : mControllers) {
            c.draw();
            c.update(mDelta);
        }
    }

    public void mouseDragged() {
        CircleController c = getCircleController();
        if (c != null) {
            c.position.set(mouseX, mouseY);
        }
    }

    public void keyPressed() {
        CircleController c = getCircleController();
        if (c != null) {
            switch (key) {
                case '+':
                    c.radius += 10;
                    break;
                case '-':
                    c.radius -= 10;
                    c.radius = c.radius < 10 ? 10 : c.radius;
                    break;
                case '.':
                    c.speed += 0.5f;
                    break;
                case ',':
                    c.speed -= 0.5f;
                    break;
            }
        }
    }

    public CircleController getCircleController() {
        for (CircleController c : mControllers) {
            if (PVector.dist(c.position, new PVector().set(mouseX, mouseY)) - 10 < c.radius) {
                return c;
            }
        }
        return null;
    }

    public void audioblock(float[] output_signal) {
        for (int i = 0; i < output_signal.length; i++) {
            for (CircleController c : mControllers) {
                output_signal[i] += c.process();
            }
            output_signal[i] /= mControllers.size();
            output_signal[i] = Wellen.clamp(output_signal[i], -1.0f, 1.0f);
        }
    }

    private class CircleController {

        float counter = 0.0f;
        final PVector pointer = new PVector();
        final PVector position = new PVector();
        float radius = 100.0f;
        float speed = 3.0f;
        private final Sampler mSampler;
        CircleController() {
            byte[] mData = SampleDataSNARE.data;
            mSampler = new Sampler();
            mSampler.load(mData);
            mSampler.set_loop_all();
            mSampler.set_speed(1);
        }

        void draw() {
            noFill();
            stroke(0);
            ellipse(position.x, position.y, radius * 2, radius * 2);
            noStroke();
            fill(0);
            ellipse(pointer.x, pointer.y, 10, 10);
        }

        float process() {
            return mSampler.output();
        }

        void update(float pDelta) {
            counter += pDelta * speed;
            pointer.x = sin(counter) * radius + position.x;
            pointer.y = cos(counter) * radius + position.y;

            mSampler.set_speed(map(pointer.x, 0, width, 0, 32));
            mSampler.set_amplitude(map(pointer.y, 0, height, 0.0f, 0.9f));
        }
    }

    public static void main(String[] args) {
        PApplet.main(TechniqueAlgorithmicComposition02VisualModel.class.getName());
    }
}