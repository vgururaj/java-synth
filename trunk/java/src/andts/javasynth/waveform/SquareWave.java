package andts.javasynth.waveform;

public class SquareWave implements Waveform
{
    private final int impulseEndSample;
    private final int sampleCount;
    private final int sampleRate;

    /**
     *
     * @param sampleRate
     * @param impulseLength length of impulse in percents of period length [0; 1]
     */
    public SquareWave(int sampleRate, float impulseLength)
    {
        this.sampleRate = sampleRate;
        this.sampleCount = sampleRate;
        this.impulseEndSample = Math.round(sampleRate * impulseLength);
    }

    public SquareWave(int sampleRate)
    {
        this(sampleRate, 0.5F);
    }

    public int getSampleCount()
    {
        return sampleCount;
    }

    public float getSampleValue(int sampleNumber)
    {
        return sampleNumber < impulseEndSample ? 1F : -1F;
    }

    public int getSampleRate()
    {
        return sampleRate;
    }
}
