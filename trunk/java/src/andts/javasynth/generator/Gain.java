package andts.javasynth.generator;

/**
 * This class serves as a 'Gain' knob for LFO generator
 */
public class Gain
{
    private float ampFactor = 0.0F;

    public Gain(float ampFactor)
    {
        this.ampFactor = ampFactor;
    }

    public float getAmplifiedValue(float value)
    {
        return value * ampFactor;
    }

    public float getAmpFactor()
    {
        return ampFactor;
    }

    public void setAmpFactor(float ampFactor)
    {
        this.ampFactor = ampFactor;
    }
}
