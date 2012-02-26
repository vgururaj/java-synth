package andts.javasynth.generator;

import java.util.Observable;
import java.util.Observer;

public class EnvelopeGenerator extends Observable implements Generator<Float>, Observer
{
    private final int sampleRate;

    public EnvelopeGenerator(int sampleRate)
    {
        this.sampleRate = sampleRate;
    }

    public Float getNextValue()
    {
        return 1F;
    }

    @Override
    public void update(Observable o, Object arg)
    {
        if (arg instanceof GeneratorState)
        {

        }
    }


}
