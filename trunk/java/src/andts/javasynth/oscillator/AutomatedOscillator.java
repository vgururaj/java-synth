package andts.javasynth.oscillator;

import andts.javasynth.generator.LfoGenerator;
import andts.javasynth.waveform.Waveform;

/**
 * Extends SimpleOscillator, can change frequency with help of LFO
 */
public class AutomatedOscillator extends SimpleOscillator
{
    private LfoGenerator lfo;
    private float baseFreq;

    public AutomatedOscillator(Waveform wave, float freq, LfoGenerator lfo)
    {
        super(wave, freq);
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
        baseFreq = freq;
    }

    @Override
    public float getNextValue()
    {
        float value = super.getNextValue();
        float newFreq = baseFreq * (1 + lfo.getNextValue());
        super.setFrequency(newFreq);
        return value;
    }
}
