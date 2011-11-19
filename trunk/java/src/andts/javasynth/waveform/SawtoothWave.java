package andts.javasynth.waveform;

public class SawtoothWave implements Waveform
{
    private final int     frameRate;
    private final int     frameCount;
    private final float[] SAWTOOTH_VALUES;

    public SawtoothWave(int frameRate)
    {
        this.frameRate = frameRate;
        this.frameCount = Math.round(frameRate / MIN_FREQUENCY);

        SAWTOOTH_VALUES = new float[frameCount];

        for (int currentFrame = 0; currentFrame < frameCount; currentFrame++)
        {
            /**
             * The relative position inside the period
             * of the waveform. 0.0 = beginning, 1.0 = end
             */
            float periodPosition = (float) currentFrame / (float) frameCount;
            float sawtoothValue;

            if (periodPosition < 0.5F)
            {
                sawtoothValue = 2.0F * periodPosition;
            }
            else
            {
                sawtoothValue = 2.0F * (periodPosition - 1.0F);
            }

            SAWTOOTH_VALUES[currentFrame] = sawtoothValue;
        }
    }

    public int getFrameCount()
    {
        return frameCount;
    }

    public double getFrameValue(int frameNumber)
    {
        return SAWTOOTH_VALUES[frameNumber];
    }

    public int getFrameRate()
    {
        return frameRate;
    }
}
