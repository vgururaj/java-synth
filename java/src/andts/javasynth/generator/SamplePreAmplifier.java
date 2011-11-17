package andts.javasynth.generator;

/**
 * Pre-amplifies signal from oscillator:
 * translates double value from oscillator into a real sample value in long
 * amp factor depends on sample resolution
 */
public class SamplePreAmplifier implements Amplifier<Long>
{
    private final long ampFactor;

    public SamplePreAmplifier(int sampleSize)
    {
        ampFactor = Math.round(Math.pow(2, sampleSize - 1));
    }

    public Long getAmplifiedValue(double value)
    {
        return Math.round(value * ampFactor);
    }
}
