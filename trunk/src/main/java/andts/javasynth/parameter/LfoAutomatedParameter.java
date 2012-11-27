package andts.javasynth.parameter;

import andts.javasynth.generator.LfoGenerator;

import java.util.Observable;

/**
 * A parameter that is automated by an LFO generator.
 */
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

    @Override
    public void update(Observable o, Object arg)
    {
        super.update(o, arg);
        lfo.update(o, arg);
    }
}
