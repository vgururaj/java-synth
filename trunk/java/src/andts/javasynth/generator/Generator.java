package andts.javasynth.generator;

import andts.javasynth.oscillator.Oscillator;

/**
 * Creates audio samples. Gets wave of some form and frequency from oscillator, applies lfo(+adsr later) and amplifies it.
 */
public abstract class Generator
{
    protected Oscillator osc;
    protected Amplifier amp;

    public Oscillator getOsc()
    {
        return osc;
    }

    public Amplifier getAmp()
    {
        return amp;
    }
}
