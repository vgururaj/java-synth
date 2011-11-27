package andts.javasynth.parameter;

public abstract class AutomatedParameter<T> implements Parameter<T>
{
    private Parameter<T> baseParam;

    protected AutomatedParameter(Parameter<T> baseParameter)
    {
        this.baseParam = baseParameter;
    }

    public T getValue()
    {
        return baseParam.getValue();
    }

    public void setValue(T value)
    {
        baseParam.setValue(value);
    }
}
