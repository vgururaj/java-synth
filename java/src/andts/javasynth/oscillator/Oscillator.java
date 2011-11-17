package andts.javasynth.oscillator;

import andts.javasynth.waveform.Waveform;

/**
 * Base interface for Oscillators.
 * Produces signal with specified waveform and frequency.
 */
public interface Oscillator
{
    public float getFrequency();

    public void setFrequency(float freq);

    public Waveform getWaveform();
    
    public void setWaveform(Waveform waveform);

    public double getNextValue();
}
