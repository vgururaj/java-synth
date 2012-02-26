package andts.javasynth.parameter;

import andts.javasynth.generator.GeneratorState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Observable;

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

    public T getValue()
    {
        return value;
    }

    public void setValue(T value)
    {
        this.value = value;
    }

    @Override
    public void update(Observable o, Object arg)
    {
        if (arg.equals(GeneratorState.RUNNING))
        {
            log.debug("Activating to {}", activeValue);
            value = activeValue;
        }
        else if (arg.equals(GeneratorState.STOPPING))
        {
            log.debug("Passivating to {}", passiveValue);
            value = passiveValue;
        }
    }
}
