package andts.javasynth.parameter;

import java.util.Observable;

/**
 * If one automated parameter will be used by several generators,
 * they will call getValue several times per one sample calculation,
 * and the frequency or length of the parameter will be wrong
 * This class caches a value from the parameter and returns it to several users.
 * Only when all users ask for their values, next value of parameter will be calculated.
 */
public class ParameterBarrier<T extends Number> implements Parameter<T>
{
    private T cachedValue;
    private Parameter<T> baseParam;
    private int users = 1;
    private int currentUser = 0;

    public ParameterBarrier(AutomatedParameter<T> baseParam, int users)
    {
        this.baseParam = baseParam;
        this.cachedValue = baseParam.getValue();
        this.users = users;
    }

    public T getValue()
    {
        if (currentUser == users)
        {
            cachedValue = baseParam.getValue();
            currentUser = 0;
        }

        currentUser++;

        return cachedValue;
    }

    public void setValue(T value)
    {
        baseParam.setValue(value);
    }

    @Override
    public void update(Observable o, Object arg)
    {
        baseParam.update(o, arg);
    }
}
