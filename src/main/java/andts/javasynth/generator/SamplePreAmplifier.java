package andts.javasynth.generator;

/**
 * Pre-amplifies signal from oscillator: <br />
 * translates float value from oscillator into a real sample value in int <br />
 * Amplification factor depends on sample size.
 */
public class SamplePreAmplifier
{
    private final int ampFactor;

    public SamplePreAmplifier(int sampleSize)
    {
        ampFactor = (int) Math.round(Math.pow(2, sampleSize - 1));
    }

    public int getAmplifiedValue(float value)
    {
        return Math.round(value * ampFactor);
    }
}
