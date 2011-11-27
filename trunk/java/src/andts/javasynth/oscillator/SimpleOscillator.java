package andts.javasynth.oscillator;

import andts.javasynth.parameter.Parameter;
import andts.javasynth.waveform.Waveform;

/**
 * Simple Oscillator that can't change frequency by itself
 */
public class SimpleOscillator implements Oscillator
{
    private Waveform wave;
    private Parameter<Float> freq;
    private int currentFrame = 0;
    private int frameCount = 0;

    public SimpleOscillator(Waveform wave, Parameter<Float> freq)
    {
        this.wave = wave;
        this.freq = freq;
        this.frameCount = wave.getSampleCount();
    }

    public float getFrequency()
    {
        return freq.getValue();
    }

    public void setFrequency(float freq)
    {
        this.freq.setValue(freq);
    }

    private int calcFrameSkip(int sampleRate, float freq)
    {
        int oneWavePeriodInSamples = Math.round(sampleRate / freq);
        return frameCount / oneWavePeriodInSamples;
    }

    public Waveform getWaveform()
    {
        return wave;
    }

    public void reset()
    {
        currentFrame = 0;
    }

    public float getNextValue()
    {
        if (currentFrame >= frameCount)
        {
            currentFrame = 0;
        }
        float result =  wave.getSampleValue(currentFrame);
        currentFrame = currentFrame + calcFrameSkip(wave.getSampleRate(), freq.getValue());
        return result;
    }
}
