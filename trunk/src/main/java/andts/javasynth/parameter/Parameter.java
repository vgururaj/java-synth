package andts.javasynth.parameter;

import java.util.Observer;

public interface Parameter<T> extends Observer
{
    T getValue();
    void setValue(T value);
}
