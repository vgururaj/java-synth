package andts.javasynth.generator;

import andts.javasynth.oscillator.Oscillator;

public class SoundGenerator
{
    private Oscillator osc;
    private SamplePreAmplifier amp;
    private LfoGenerator freqLfo;
    private LfoGenerator gainLfo;
    private float initialFreq;
    private Gain gain;
    private float initialGain;

    public SoundGenerator(int sampleSize, Oscillator osc, Gain gain, LfoGenerator freqLfo, LfoGenerator gainLfo)
    {
        this.osc = osc;
        this.amp = new SamplePreAmplifier(sampleSize);
        this.freqLfo = freqLfo;
        this.gainLfo = gainLfo;
        this.initialFreq = osc.getFrequency();
        this.gain = gain;
        initialGain = gain.getAmpFactor();
    }

    public long getNextSample()
    {
        osc.setFrequency(initialFreq + initialFreq * freqLfo.getNextValue());
        gain.setAmpFactor(initialGain + initialGain * gainLfo.getNextValue());
        double nextValue = gain.getAmplifiedValue((float) osc.getNextValue());
        return amp.getAmplifiedValue(nextValue);
    }
}
