package andts.javasynth.effects;

import andts.javasynth.parameter.Parameter;

public class Gain
{
    private Parameter<Float> ampFactor;
    private float currentAmpFactor;

    public Gain(Parameter<Float> ampFactor)
    {
        this.ampFactor = ampFactor;
    }

    public float getAmplifiedValue(float value)
    {
        currentAmpFactor = ampFactor.getValue();
        return value * currentAmpFactor;
    }

    public float getAmpFactor()
    {
        return currentAmpFactor;
    }

    public void setAmpFactor(float ampFactor)
    {
        this.ampFactor.setValue(ampFactor);
    }
}
