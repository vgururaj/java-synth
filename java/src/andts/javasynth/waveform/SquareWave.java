package andts.javasynth.waveform;

public class SquareWave implements Waveform
{
    private final int impulseEndFrame;
    private final int frameCount;
    private final int frameRate;

    /**
     *
     * @param frameRate
     * @param impulseLength length of impulse in percents of period length [0; 1]
     */
    public SquareWave(int frameRate, float impulseLength)
    {
        this.frameRate = frameRate;
        this.frameCount = frameRate;
        this.impulseEndFrame = Math.round(frameRate * impulseLength);
    }

    public int getFrameCount()
    {
        return frameCount;
    }

    public double getFrameValue(int frameNumber)
    {
        return frameNumber < impulseEndFrame ? 1F : -1F;
    }

    public int getFrameRate()
    {
        return frameRate;
    }
}
