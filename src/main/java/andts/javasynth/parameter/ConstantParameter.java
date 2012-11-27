package andts.javasynth.parameter;

import andts.javasynth.generator.SoundGeneratorState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Observable;

/**
 * A parameter that always returns the same value. Has two states - active and passive, each may have a distinct value.
 * Passive value is used when the sound generator is not running.
 * @param <T> Type of value
 */
public class ConstantParameter<T extends Number> implements Parameter<T>
{
    private static final Logger log = LoggerFactory.getLogger(ConstantParameter.class);
    private T value = null;
    private T activeValue = null;
    private T passiveValue = null;

    public ConstantParameter(T activeValue, T passiveValue)
    {
        this.value = activeValue;
        this.activeValue = activeValue;
        this.passiveValue = passiveValue;
    }

    public ConstantParameter(T value)
    {
        this(value, value);
    }

    public T getValue()
    {
        return value;
    }

    public void setValue(T value)
    {
        this.value = value;
        this.activeValue = value;
        this.passiveValue = value;
    }

    @Override
    public void update(Observable o, Object arg)
    {
        if (arg.equals(SoundGeneratorState.RUNNING))
        {
            log.debug("Activating to {}", activeValue);
            value = activeValue;
        }
        else if (arg.equals(SoundGeneratorState.STOPPING))
        {
            log.debug("Passivating to {}", passiveValue);
            value = passiveValue;
        }
    }
}
