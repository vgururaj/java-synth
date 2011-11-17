package andts.javasynth.generator;

import andts.javasynth.oscillator.Oscillator;

/**
 * Creates audio samples. Gets wave of some form and frequency from oscillator, applies lfo(+adsr later) and amplifies it.
 */
public class Generator
{
    protected Oscillator osc;
    protected SamplePreAmplifier amp;

    public Generator(Oscillator osc, SamplePreAmplifier amp)
    {
        this.osc = osc;
        this.amp = amp;
    }

    public Oscillator getOsc()
    {
        return osc;
    }

    public Amplifier getAmp()
    {
        return amp;
    }

    /*public long getNextSample()
    {
        return amp.getAmplifiedValue(osc.getNextValue());
    }*/
}
