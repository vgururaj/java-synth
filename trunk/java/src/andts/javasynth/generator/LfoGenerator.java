package andts.javasynth.generator;

import andts.javasynth.oscillator.Oscillator;
import andts.javasynth.waveform.Waveform;

/**
 * Generates low-frequency waveforms, used to automatically change values of sound generators
 */
public class LfoGenerator extends Generator
{

    public LfoGenerator(Oscillator osc, SamplePreAmplifier amp)
    {
        super(osc, amp);
    }
}
