package andts.javasynth.generator;

import andts.javasynth.oscillator.Oscillator;
import andts.javasynth.oscillator.SimpleOscillator;
import andts.javasynth.waveform.SineWave;

public class SoundGenerator extends Generator<Integer>
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
        this.filter = new MoogVcfFilter2(0.101F, .3F, MoogVcfFilter2.FilterMode.LOWPASS,
                                        new LfoGenerator(
                                                new SimpleOscillator(new SineWave(44100), 2.5F),
                                                new Gain(0.95F)));
    }

    public Integer getNextValue()
    {
        float nextValue = (float) filter.filter(
                getGain().getAmplifiedValue(getOsc().getNextValue()));
        //        float nextValue = getGain().getAmplifiedValue(getOsc().getNextValue());
        return amp.getAmplifiedValue(nextValue);
    }
}
