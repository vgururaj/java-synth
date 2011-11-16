package andts.javasynth.generator;

/**
 * Amplifies signal from oscillator:
 * translates float value from oscillator into a real sample value in int/double (generify?)
 * sample resolution must be configurable (16/32/64 bits?)
 */
public class Amplifier
{
    private final double ampFactor;

    public Amplifier (int sampleSize)
    {
        ampFactor = Math.pow(2, sampleSize - 1);
    }

    public double getAmplifiedValue(float value)
    {
        return (double) (value * ampFactor);
    }
}
