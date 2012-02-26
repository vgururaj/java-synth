package andts.javasynth.parameter;

import andts.javasynth.generator.LfoGenerator;

public class LfoAutomatedParameter extends AutomatedParameter<Float>
{
    private LfoGenerator lfo;

    public LfoAutomatedParameter(Parameter<Float> baseParameter, LfoGenerator lfo)
    {
        super(baseParameter);
        this.lfo = lfo;
    }

    @Override
    public Float getValue()
    {
        return super.getValue() * (1 + lfo.getNextValue());
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
