package andts.javasynth.generator;

public interface Amplifier<T>
{
    public T getAmplifiedValue(float value);

    public T getAmpfactor();

    public void setAmpFactor(T ampFactor);
}
