package andts.javasynth.waveform;

public class SquareWave implements Waveform
{
    private float impulseLength;

    /**
     * @param impulseLength length of impulse in percents of period length [0; 1]
     */
    public SquareWave(float impulseLength)
    {
        this.impulseLength = impulseLength;
    }

    public SquareWave()
    {
        this(0.5F);
    }

    @Override
    public float getValue(float periodPosition)
    {
        return periodPosition < impulseLength ? 1F : -1F;
    }
}
