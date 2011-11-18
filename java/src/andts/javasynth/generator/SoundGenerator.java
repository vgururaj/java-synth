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

    public SoundGenerator(Oscillator osc, SamplePreAmplifier amp, LfoGenerator freqLfo, LfoGenerator gainLfo)
    {
        this.osc = osc;
        this.amp = amp;
        this.freqLfo = freqLfo;
        this.gainLfo = gainLfo;
        this.initialFreq = osc.getFrequency();
        gain = new Gain(0.2f);
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
