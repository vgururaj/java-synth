package andts.javasynth.generator;

import andts.javasynth.oscillator.Oscillator;
import andts.javasynth.waveform.Waveform;

/**
 * Generates low-frequency waveforms, used to automatically change values of sound generators
 */
public class LfoGenerator extends Generator<Float>
{
    public LfoGenerator(Oscillator osc, Gain gain)
    {
        super(osc, gain);
    }

    public Float getNextValue()
    {
        return getGain().getAmplifiedValue(getOsc().getNextValue());
    }
}
