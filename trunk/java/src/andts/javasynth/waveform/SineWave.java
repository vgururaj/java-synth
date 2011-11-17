package andts.javasynth.waveform;

public class SineWave implements Waveform
{
    private final float[] SINE_VALUES;
    private final int frameCount;

    public SineWave(int frameRate)
    {
        this.frameCount = Math.round(frameRate / 1);

        SINE_VALUES = new float[frameCount];

        for (int currentFrame = 0; currentFrame < frameCount; currentFrame++)
        {
            /**    The relative position inside the period
             of the waveform. 0.0 = beginning, 1.0 = end
             */
            float periodPosition = (float) currentFrame / (float) frameCount;
            float sineValue = (float) Math.sin(periodPosition * 2.0 * Math.PI);

            SINE_VALUES[currentFrame] = sineValue;
        }

        /*System.out.print("SINE_VALUES = ");
        for (int j = 0; j < SINE_VALUES.length; ++j)
        {
            System.out.print(SINE_VALUES[j] + ", ");
        }*/
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
