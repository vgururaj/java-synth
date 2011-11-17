package andts.javasynth.waveform;

public class SineWave implements Waveform
{
    private static final float MIN_FREQUENCY = 0.1F; //lowest frequency -> longest period
    private final float[] SINE_VALUES;
    private int frameCount;

    private SineWave(int frameRate)
    {
        this.frameCount = Math.round(frameRate / MIN_FREQUENCY);

        SINE_VALUES = new float[frameCount];

        for (int currentFrame = 0; currentFrame < frameCount; currentFrame++)
        {
            /**    The relative position inside the period
             of the waveform. 0.0 = beginning, 1.0 = end
             */
            float periodPosition = (float) currentFrame / (float) frameCount;
            float sineValue = (float) Math.sin(periodPosition * 2.0 * Math.PI);

            // this is for 16 bit stereo, little-endian
            SINE_VALUES[currentFrame] = sineValue;
        }
    }

    public SineWave getInstance(int frameRate)
    {
        return new SineWave(frameRate);
    }

    public int getFrameCount()
    {
        return frameCount;
    }

    public double getFrameValue(int frameNumber)
    {
        return SINE_VALUES[frameNumber];
    }
}
