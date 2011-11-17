package andts.javasynth.generator;

import andts.javasynth.oscillator.Oscillator;

public class SoundGenerator
{
    private Oscillator osc;
    private SamplePreAmplifier amp;

    public SoundGenerator(Oscillator osc, SamplePreAmplifier amp)
    {
        this.osc = osc;
        this.amp = amp;
    }

    public int getNextSample()
    {
        return amp.getAmplifiedValue(osc.getNextValue());
    }
}
