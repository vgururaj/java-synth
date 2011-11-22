package andts.javasynth.generator;

import andts.javasynth.oscillator.Oscillator;

/**
 * Creates audio samples. Gets wave of some form and frequency from oscillator, applies lfo(+adsr later) and amplifies it.
 */
public abstract class Generator<T>
{
    private Oscillator osc;
    private Gain gain;

    public Generator(Oscillator osc, Gain gain)
    {
        this.osc = osc;
        this.gain = gain;
    }

    public Oscillator getOsc()
    {
        return osc;
    }

    public Gain getGain()
    {
        return gain;
    }

    public abstract T getNextValue();
}
