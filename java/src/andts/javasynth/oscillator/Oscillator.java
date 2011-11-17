package andts.javasynth.oscillator;

/**
 * Base interface for Signal Generators and Low-Frequency Oscillators
 * Allow to set frequency of the signal.
 */
public interface Oscillator
{
    public double getFrequency();

    public void setFrequency(double freq);

    public double getNextValue();
}
