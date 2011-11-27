package andts.javasynth.parameter;

public class ConstantParameter<T> implements Parameter<T>
{
    private T value = null;

    public ConstantParameter(T value)
    {
        this.value = value;
    }

    public T getValue()
    {
        return value;
    }

    public void setValue(T value)
    {
        this.value = value;
    }
}
