package andts.javasynth.generator;

import andts.javasynth.effects.Gain;
import andts.javasynth.oscillator.Oscillator;

import java.util.Observable;

/**
 * Creates samples based on some oscillator. Gets wave of some form and frequency from oscillator, and amplifies it.
 */
public abstract class OscillatedGenerator<T> extends Observable implements Generator<T>
{
    private Oscillator osc;
    private Gain gain;

    public OscillatedGenerator(Oscillator osc, Gain gain)
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
