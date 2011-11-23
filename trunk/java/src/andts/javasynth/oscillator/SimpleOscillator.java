package andts.javasynth.oscillator;

import andts.javasynth.waveform.Waveform;

/**
 * Simple Oscillator that can't change frequency by itself
 */
public class SimpleOscillator implements Oscillator
{
    private Waveform wave;
    private float freq = Waveform.MIN_FREQUENCY;
    private int frameSkip = 0;
    private int currentFrame = 0;
    private int frameCount = 0;

    public SimpleOscillator(Waveform wave, float freq)
    {
        this.wave = wave;
        this.freq = freq;
        this.frameCount = wave.getSampleCount();
        this.frameSkip = calcFrameSkip(wave.getSampleRate(), this.freq);
    }

    public float getFrequency()
    {
        return freq;
    }

    public void setFrequency(float freq)
    {
        this.freq = freq;
        this.frameSkip = calcFrameSkip(wave.getSampleRate(), freq);
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

    public float getNextValue()
    {
        if (currentFrame >= frameCount)
        {
            currentFrame = 0;
        }

        float result =  wave.getSampleValue(currentFrame);
        currentFrame = currentFrame + frameSkip;

        return result;
    }
}
