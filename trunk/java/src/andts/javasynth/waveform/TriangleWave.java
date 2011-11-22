package andts.javasynth.waveform;

public class TriangleWave implements Waveform
{
    private final int sampleRate;
    private final int sampleCount;
    private final float[] TRIANGLE_VALUES;

    public TriangleWave(int sampleRate)
    {
        this.sampleRate = sampleRate;
        this.sampleCount = Math.round(sampleRate / MIN_FREQUENCY);

        TRIANGLE_VALUES = new float[sampleCount];

        for (int currentSample = 0; currentSample < sampleCount; currentSample++)
        {
            /**
             * The relative position inside the period
             * of the waveform. 0.0 = beginning, 1.0 = end
             */
            float periodPosition = (float) currentSample / (float) sampleCount;
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

            TRIANGLE_VALUES[currentSample] = triangleValue;
        }
    }

    public int getSampleCount()
    {
        return sampleCount;
    }

    public float getSampleValue(int sampleNumber)
    {
        return TRIANGLE_VALUES[sampleNumber];
    }

    public int getSampleRate()
    {
        return sampleRate;
    }
}
