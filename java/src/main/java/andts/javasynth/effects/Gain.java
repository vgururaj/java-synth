package andts.javasynth.effects;

import andts.javasynth.parameter.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Observable;
import java.util.Observer;

public class Gain implements Observer
{
    private static final Logger log = LoggerFactory.getLogger(Gain.class);
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

    @Override
    public void update(Observable o, Object arg)
    {
        log.debug("Got {}, send it to ampFactor", arg);
        ampFactor.update(o, arg);
    }
}
