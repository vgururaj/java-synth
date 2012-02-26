package andts.javasynth.oscillator;

import andts.javasynth.waveform.Waveform;

import java.util.Observer;

/**
 * Base interface for Oscillators.
 * Produces signal with specified waveform and frequency.
 */
public interface Oscillator extends Observer
{
    public float getFrequency();

    public void setFrequency(float freq);

    public Waveform getWaveform();

    public void setWaveform(Waveform waveform);

    public float getNextValue();

    public void reset();
}
