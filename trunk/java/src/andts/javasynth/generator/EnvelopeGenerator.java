package andts.javasynth.generator;

import andts.javasynth.oscillator.Oscillator;

public class EnvelopeGenerator extends Generator<Float>
{
    public EnvelopeGenerator(Oscillator osc, Gain gain)
    {
        super(osc, gain);
    }

    @Override
    public Float getNextValue()
    {
        return null;
    }
}
