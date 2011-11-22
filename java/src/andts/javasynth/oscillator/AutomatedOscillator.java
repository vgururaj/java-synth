package andts.javasynth.oscillator;

import andts.javasynth.generator.LfoGenerator;
import andts.javasynth.waveform.Waveform;

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

    @Override
    public void setFrequency(float freq)
    {
        baseFreq = freq;
        super.setFrequency(freq);
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
