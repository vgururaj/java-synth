package andts.javasynth.waveform;

public class TriangleWave implements Waveform
{
    private final int     frameRate;
    private final int     frameCount;
    private final float[] TRIANGLE_VALUES;

    public TriangleWave(int frameRate)
    {
        this.frameRate = frameRate;
        this.frameCount = Math.round(frameRate / MIN_FREQUENCY);

        TRIANGLE_VALUES = new float[frameCount];

        for (int currentFrame = 0; currentFrame < frameCount; currentFrame++)
        {
            /**
             * The relative position inside the period
             * of the waveform. 0.0 = beginning, 1.0 = end
             */
            float periodPosition = (float) currentFrame / (float) frameCount;
            float triangleValue;

            if (periodPosition < 0.25F)
            {
                triangleValue = 4.0F * periodPosition;
            }
            else if (periodPosition < 0.75F)
            {
                triangleValue = -4.0F * (periodPosition - 0.5F);
            }
            else
            {
                triangleValue = 4.0F * (periodPosition - 1.0F);
            }

            TRIANGLE_VALUES[currentFrame] = triangleValue;
        }
    }

    public int getFrameCount()
    {
        return frameCount;
    }

    public double getFrameValue(int frameNumber)
    {
        return TRIANGLE_VALUES[frameNumber];
    }

    public int getFrameRate()
    {
        return frameRate;
    }
}
