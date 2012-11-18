package andts.javasynth.parameter;

import java.util.Observer;

/**
 * An interface specifying Parameter. Extends Observer, so that actual Parameter could
 * change its state according to state of the sound generator.
 * @param <T> Type of value
 */
public interface Parameter<T extends Number> extends Observer
{
    T getValue();
    void setValue(T value);
}
