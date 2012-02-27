package andts.javasynth.oscillator;

import andts.javasynth.parameter.Parameter;
import andts.javasynth.waveform.Waveform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Observable;

/**
 * Simple Oscillator that gets samples from passed waveform
 * to create wave of some frequency
 */
public class SimpleOscillator implements Oscillator
{
    private static final Logger log = LoggerFactory.getLogger(SimpleOscillator.class);

    private Waveform wave;
    private Parameter<Float> freq;
    private final float sampleRate;
    private float currentFreq;
    private float currentPeriodPosition;

    public SimpleOscillator(Waveform wave, Parameter<Float> freq, float sampleRate)
    {
        this.wave = wave;
        this.freq = freq;
        this.sampleRate = sampleRate;
    }

    @Override
    public float getFrequency()
    {
        return currentFreq;
    }

    @Override
    public void setFrequency(float freq)
    {
        this.freq.setValue(freq);
    }

    @Override
    public Waveform getWaveform()
    {
        return wave;
    }

    @Override
    public void setWaveform(Waveform waveform)
    {
        wave = waveform;
    }

    @Override
    public void reset()
    {
        currentPeriodPosition = 0;
    }

    @Override
    public float getNextValue()
    {
        if (currentPeriodPosition > 1f)
        {
            currentPeriodPosition = 0;
        }
        float result = wave.getValue(currentPeriodPosition);
        currentPeriodPosition = calculateNextPeriodPosition();
        return result;
    }

    private float calculateNextPeriodPosition()
    {
        currentFreq = freq.getValue();
        return currentPeriodPosition + currentFreq / sampleRate;
    }

    @Override
    public void update(Observable o, Object arg)
    {
        log.debug("Got {}, send it to freq", arg);
        freq.update(o, arg);
    }
}
