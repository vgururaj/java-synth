package andts.javasynth.oscillator;

import andts.javasynth.waveform.Waveform;

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
        this.frameCount = wave.getFrameCount();
        this.frameSkip = calcFrameSkip(wave.getFrameRate(), this.freq);
    }

    public float getFrequency()
    {
        return freq;
    }

    public void setFrequency(float freq)
    {
        this.freq = freq;
        this.frameSkip = calcFrameSkip(wave.getFrameRate(), freq);
    }

    private int calcFrameSkip(int frameRate, float freq)
    {
        int oneWavePeriodInFrames = Math.round(frameRate / freq);
        return frameCount / oneWavePeriodInFrames;
    }

    public Waveform getWaveform()
    {
        return wave;
    }

    public void setWaveform(Waveform waveform)
    {
        this.wave = waveform;
        this.frameCount = waveform.getFrameCount();
        this.frameSkip = calcFrameSkip(wave.getFrameRate(), freq);
    }

    public double getNextValue()
    {
        if (currentFrame >= frameCount)
        {
            currentFrame = 0;
        }

        double result =  wave.getFrameValue(currentFrame);
        currentFrame = currentFrame + frameSkip;

        return result;
    }
}
