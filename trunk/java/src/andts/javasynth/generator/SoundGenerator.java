package andts.javasynth.generator;

import andts.javasynth.oscillator.Oscillator;

public class SoundGenerator extends Generator<Integer>
{
    private SamplePreAmplifier amp;

    public SoundGenerator(int sampleSize, Oscillator osc, Gain gain)
    {
        super(osc, gain);
        this.amp = new SamplePreAmplifier(sampleSize);
    }

    public Integer getNextValue()
    {
        float nextValue = getGain().getAmplifiedValue(getOsc().getNextValue());
        return amp.getAmplifiedValue(nextValue);
    }
}
