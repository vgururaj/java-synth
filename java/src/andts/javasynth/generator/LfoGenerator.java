package andts.javasynth.generator;

import andts.javasynth.JavaSynthException;
import andts.javasynth.effects.Gain;
import andts.javasynth.oscillator.Oscillator;

/**
 * Generates low-frequency waveforms, used to automatically change values of sound generators
 */
public class LfoGenerator extends OscillatedGenerator<Float>
{
    public LfoGenerator(Oscillator osc, Gain gain)
    {
        super(osc, gain);

        if (osc.getFrequency() >= 1000)
        {
            throw new JavaSynthException("Frequency of the Oscillator is too high for LFO!");
        }
    }

    public Float getNextValue()
    {
        return getGain().getAmplifiedValue(getOsc().getNextValue());
    }
}
