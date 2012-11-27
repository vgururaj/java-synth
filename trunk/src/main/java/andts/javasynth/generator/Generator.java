package andts.javasynth.generator;

import java.util.Observer;

public interface Generator<T> extends Observer
{
    public T getNextValue();
}
