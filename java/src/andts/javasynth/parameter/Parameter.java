package andts.javasynth.parameter;

public interface Parameter<T>
{
    T getValue();
    void setValue(T value);
}
