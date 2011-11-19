package andts.javasynth.waveform;

public class SquareWave implements Waveform
{
    private final int halfFrame;
    private final int frameCount;
    private final int frameRate;

    public SquareWave(int frameRate)
    {
        this.frameRate = frameRate;
        this.frameCount = frameRate;
        this.halfFrame = frameRate / 2;
    }

    public int getFrameCount()
    {
        return frameCount;
    }

    public double getFrameValue(int frameNumber)
    {
        return frameNumber < halfFrame ? 1F : -1F;
    }

    public int getFrameRate()
    {
        return frameRate;
    }
}
