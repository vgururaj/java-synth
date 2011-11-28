package andts.javasynth.generator;

import andts.javasynth.effects.Gain;
import andts.javasynth.effects.MoogVcfFilter2;
import andts.javasynth.oscillator.Oscillator;

public class SoundGenerator extends OscillatedGenerator<Integer>
{
    private SamplePreAmplifier amp;
    private MoogVcfFilter2 filter;

    /**
     * Creates new SoundGenerator
     *
     * @param sampleSize size of generated sample in bits
     * @param osc        oscillator used to generate wave of some frequency
     * @param gain       used to amplify generated sample to some volume
     */
    public SoundGenerator(int sampleSize, Oscillator osc, Gain gain)
    {
        super(osc, gain);
        this.amp = new SamplePreAmplifier(sampleSize);
        /*this.filter = new MoogVcfFilter2(0.05F, .3F, MoogVcfFilter2.FilterMode.LOWPASS,
                                         new LfoGenerator(
                                                 new SimpleOscillator(
                                                         new SineWave(44100),
                                                         new ConstantParameter<Float>(2F)),
                                                 new Gain(0.7F)));*/
    }

    public Integer getNextValue()
    {
        //        float nextValue = (float) filter.filter(
        //                getGain().getAmplifiedValue(getOsc().getNextValue()));
        float nextValue = getGain().getAmplifiedValue(getOsc().getNextValue());
        return amp.getAmplifiedValue(nextValue);
    }
}
