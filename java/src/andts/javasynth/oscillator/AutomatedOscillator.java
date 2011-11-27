package andts.javasynth.oscillator;

import andts.javasynth.generator.LfoGenerator;
import andts.javasynth.parameter.ConstantParameter;
import andts.javasynth.parameter.Parameter;
import andts.javasynth.waveform.Waveform;

/**
 * Extends SimpleOscillator, can change frequency with help of LFO
 */
public class AutomatedOscillator extends SimpleOscillator
{
    private LfoGenerator lfo;
    private Parameter<Float> baseFreq;

    public AutomatedOscillator(Waveform wave, Parameter<Float> freq, LfoGenerator lfo)
    {
        super(wave, new ConstantParameter<Float>(freq.getValue()));
        this.lfo = lfo;
        baseFreq = freq;
    }

    /**
     * Set base frequency from which LFO may vary
     * @param freq base frequency of oscillator
     */
    @Override
    public void setFrequency(float freq)
    {
        baseFreq.setValue(freq);
    }

    @Override
    public float getNextValue()
    {
        float value = super.getNextValue();
        float newFreq = baseFreq.getValue() * (1 + lfo.getNextValue());
        super.setFrequency(newFreq);
        return value;
    }
}
