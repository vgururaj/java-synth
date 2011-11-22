package andts.javasynth.generator;

public class Gain
{
    private float ampFactor = 0.0F;
    private LfoGenerator lfo;

    public Gain(float ampFactor)
    {
        this.ampFactor = ampFactor;
    }

    public Gain(float ampFactor, LfoGenerator lfo)
    {
        this.ampFactor = ampFactor;
        this.lfo = lfo;
    }

    public float getAmplifiedValue(float value)
    {
        float lfoValue = 0F;
        if (lfo != null)
        {
            lfoValue = lfo.getNextValue();
        }
        float amp = ampFactor * (1 + lfoValue);
        return value * amp;
    }

    public float getAmpFactor()
    {
        return ampFactor;
    }

    public void setAmpFactor(float ampFactor)
    {
        this.ampFactor = ampFactor;
    }

    public LfoGenerator getLfo()
    {
        return lfo;
    }

    public void setLfo(LfoGenerator lfo)
    {
        this.lfo = lfo;
    }
}
