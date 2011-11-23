package andts.javasynth.generator;

import andts.javasynth.oscillator.Oscillator;

public class SoundGenerator extends Generator<Integer>
{
    private SamplePreAmplifier amp;

    /**
     * Creates new SoundGenerator
     * @param sampleSize size of generated sample in bits
     * @param osc oscillator used to generate wave of some frequency
     * @param gain used to amplify generated sample to some volume
     */
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
