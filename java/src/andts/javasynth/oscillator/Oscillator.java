package andts.javasynth.oscillator;

/**
 * Base interface for Signal Generators and Low-Frequency Oscillators
 * Has knowledge about frequency and amplitude of signal and means for manipulating them.
 */
public interface Oscillator
{

    public float getFrequency();

    public void setFrequency(float freq);
}
