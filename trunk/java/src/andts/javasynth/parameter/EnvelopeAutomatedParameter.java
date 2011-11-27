package andts.javasynth.parameter;

import andts.javasynth.generator.EnvelopeGenerator;

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
}
