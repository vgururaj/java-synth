package andts.javasynth.parameter;

import andts.javasynth.generator.EnvelopeGenerator;

import java.util.Observable;

/**
 * A parameter that is automated by an envelope generator.
 */
public class EnvelopeAutomatedParameter extends AutomatedParameter<Float>
{
    EnvelopeGenerator env;

    public EnvelopeAutomatedParameter(Parameter<Float> baseParameter, EnvelopeGenerator envelope)
    {
        super(baseParameter);
        this.env = envelope;
    }

    @Override
    public Float getValue()
    {
        return super.getValue() * env.getNextValue();
    }

    @Override
    public void update(Observable o, Object arg)
    {
        super.update(o, arg);
        env.update(o, arg);
    }
}
