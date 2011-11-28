package andts.javasynth.oscillator;

import andts.javasynth.parameter.Parameter;
import andts.javasynth.waveform.Waveform;

/**
 * Simple Oscillator that gets samples from passed waveform
 * to create wave of some frequency
 */
public class SimpleOscillator implements Oscillator
{
    private Waveform wave;
    private int sampleRate;
    private Parameter<Float> freq;
    private float currentFreq;
    private int currentFrame = 0;
    private int frameCount = 0;

    public SimpleOscillator(Waveform wave, Parameter<Float> freq)
    {
        this.wave = wave;
        this.freq = freq;
        this.frameCount = wave.getSampleCount();
        this.sampleRate = wave.getSampleRate();
    }

    public float getFrequency()
    {
        return currentFreq;
    }

    public void setFrequency(float freq)
    {
        this.freq.setValue(freq);
    }

    private int calcFrameSkip()
    {
        int oneWavePeriodInSamples = Math.round(sampleRate / currentFreq);
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

        currentFreq = freq.getValue();
        currentFrame = currentFrame + calcFrameSkip();

        return result;
    }
}
