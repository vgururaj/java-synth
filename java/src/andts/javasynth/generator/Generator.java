package andts.javasynth.generator;

import andts.javasynth.oscillator.Oscillator;

/**
 * Creates audio samples. Gets wave of some form and frequency from oscillator, applies lfo(+adsr later) and amplifies it.
 */
public class Generator
{
    Oscillator osc;
    Amplifier amp;
}
