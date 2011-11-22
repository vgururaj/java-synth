package andts.javasynth.waveform;

public class SineWave implements Waveform
{
    private final float[] SINE_VALUES;
    private final int sampleCount;
    private final int sampleRate;

    public SineWave(int sampleRate)
    {
        this.sampleRate = sampleRate;
        this.sampleCount = Math.round(sampleRate / MIN_FREQUENCY);

        SINE_VALUES = new float[sampleCount];

        for (int currentSample = 0; currentSample < sampleCount; currentSample++)
        {
            /**
             * The relative position inside the period
             * of the waveform. 0.0 = beginning, 1.0 = end
             */
            float periodPosition = (float) currentSample / (float) sampleCount;
            float sineValue = (float) Math.sin(periodPosition * 2.0 * Math.PI);

            SINE_VALUES[currentSample] = sineValue;
        }
    }

    public int getSampleCount()
    {
        return sampleCount;
    }

    public int getSampleRate()
    {
        return sampleRate;
    }

    public float getSampleValue(int sampleNumber)
    {
        return SINE_VALUES[sampleNumber];
    }
}
