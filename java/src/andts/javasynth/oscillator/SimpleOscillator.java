package andts.javasynth.oscillator;

import andts.javasynth.waveform.Waveform;

public class SimpleOscillator implements Oscillator
{
    private Waveform wave;
    private double freq = 1;
    private long frameSkip = 0;
    private int currentFrame = 0;
    private int frameCount = 0;

    public SimpleOscillator(Waveform wave, double freq)
    {
        this.wave = wave;
        this.freq = freq;
        this.frameCount = wave.getFrameCount();
        this.frameSkip = calcFrameSkip(frameCount, this.freq);
    }

    public double getFrequency()
    {
        return freq;
    }

    public void setFrequency(double freq)
    {
        this.freq = freq;
        this.frameSkip = calcFrameSkip(frameCount, freq);
    }

    private long calcFrameSkip(int frameCount, double freq)
    {
        long periodInFrames = Math.round(frameCount / freq);
        return frameCount / periodInFrames;
    }

    public Waveform getWaveform()
    {
        return wave;
    }

    public void setWaveform(Waveform waveform)
    {
        this.wave = waveform;
        this.frameCount = waveform.getFrameCount();
        this.frameSkip = calcFrameSkip(frameCount, freq);
    }

    public double getNextValue()
    {
        currentFrame = (int) (currentFrame + frameSkip);
        if (currentFrame > frameCount)
        {
            currentFrame = 0;
        }

        return wave.getFrameValue(currentFrame);
    }
}
