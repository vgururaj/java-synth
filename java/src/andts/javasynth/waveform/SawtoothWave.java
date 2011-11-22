package andts.javasynth.waveform;

public class SawtoothWave implements Waveform
{
    private final int sampleRate;
    private final int sampleCount;
    private final float[] SAWTOOTH_VALUES;

    public SawtoothWave(int sampleRate)
    {
        this.sampleRate = sampleRate;
        this.sampleCount = Math.round(sampleRate / MIN_FREQUENCY);

        SAWTOOTH_VALUES = new float[sampleCount];

        for (int currentSample = 0; currentSample < sampleCount; currentSample++)
        {
            /**
             * The relative position inside the period
             * of the waveform. 0.0 = beginning, 1.0 = end
             */
            float periodPosition = (float) currentSample / (float) sampleCount;
            float sawtoothValue;

            if (periodPosition < 0.5F)
            {
                sawtoothValue = 2.0F * periodPosition;
            }
            else
            {
                sawtoothValue = 2.0F * (periodPosition - 1.0F);
            }

            SAWTOOTH_VALUES[currentSample] = sawtoothValue;
        }
    }

    public int getSampleCount()
    {
        return sampleCount;
    }

    public float getSampleValue(int sampleNumber)
    {
        return SAWTOOTH_VALUES[sampleNumber];
    }

    public int getSampleRate()
    {
        return sampleRate;
    }
}
