package andts.javasynth.generator;

import andts.javasynth.effects.Gain;
import andts.javasynth.effects.MoogVcfFilter2;
import andts.javasynth.oscillator.Oscillator;
import andts.javasynth.parameter.ConstantParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class SoundGenerator extends OscillatedGenerator<Integer> implements Runnable, Observer
{
    private static Logger log = LoggerFactory.getLogger(SoundGenerator.class);

    private SamplePreAmplifier amp;
    private MoogVcfFilter2 filter;
    private BlockingQueue<Integer> outputQueue;
    private BlockingQueue<SoundGeneratorState> eventQueue = new LinkedBlockingQueue<>();
    private SoundGeneratorState currentState = SoundGeneratorState.IDLE;
    private EnvelopeGenerator volumeEnvelope;

    /**
     * Creates new SoundGenerator
     *
     * @param sampleSize     size of generated sample in bits
     * @param osc            oscillator used to generate wave of some frequency
     * @param gain           used to amplify generated sample to some volume
     * @param volumeEnvelope asd
     */
    public SoundGenerator(int sampleSize, Oscillator osc, Gain gain, EnvelopeGenerator volumeEnvelope)
    {
        super(osc, gain);
        this.amp = new SamplePreAmplifier(sampleSize);
        this.volumeEnvelope = volumeEnvelope;
        if (volumeEnvelope != null)
        {
            volumeEnvelope.addObserver(this);
        }
        addObserver(osc);
        addObserver(gain);
        addObserver(volumeEnvelope);
        this.filter = new MoogVcfFilter2(
            new ConstantParameter<>(0.05F),
            new ConstantParameter<>(.3F),
            MoogVcfFilter2.FilterType.LOWPASS);
    }

    public Integer getNextValue()
    {
        float nextValue = filter.filter(getGain().getAmplifiedValue(getOsc().getNextValue() * volumeEnvelope.getNextValue()));
//        float nextValue = getGain().getAmplifiedValue(getOsc().getNextValue() * volumeEnvelope.getNextValue());
        return amp.getAmplifiedValue(nextValue);
    }

    public SoundGeneratorState getCurrentState()
    {
        return currentState;
    }

    public void setOutputQueue(BlockingQueue<Integer> outputQueue)
    {
        this.outputQueue = outputQueue;
    }

    public void start(float frequency)
    {
        if (currentState.equals(SoundGeneratorState.RUNNING))
        {
            return;
        }

        setChanged();

        getOsc().reset();
        getOsc().setFrequency(frequency);

        notifyObservers(SoundGeneratorState.RUNNING);
        eventQueue.add(SoundGeneratorState.RUNNING);
        outputQueue.clear();
    }

    public void stop()
    {
        if (currentState.equals(SoundGeneratorState.STOPPING))
        {
            return;
        }

        outputQueue.clear();
        log.debug("Send STOPPING");
        setChanged();
        notifyObservers(SoundGeneratorState.STOPPING);
        eventQueue.add(SoundGeneratorState.STOPPING);
    }

    @Override
    public void run()
    {
        while (!Thread.currentThread().isInterrupted())
        {
            SoundGeneratorState newState = eventQueue.poll();

            if (newState != null && newState != currentState)
            {
                currentState = newState;
                log.debug("currentState = {}", currentState);
            }
            try
            {
                if (currentState.equals(SoundGeneratorState.RUNNING) || currentState.equals(SoundGeneratorState.STOPPING))
                {
                    outputQueue.put(getNextValue());
                }
                else if (currentState.equals(SoundGeneratorState.SILENT))
                {
                    outputQueue.put(0);
                }
            }
            catch (InterruptedException e)
            {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * To set state to SILENT when all Envelope Generators stop.
     *
     * @param o
     * @param arg
     */
    @Override
    public void update(Observable o, Object arg)
    {
        if (arg.equals(EnvelopeGenerator.EnvelopeState.IDLE))
        {
            currentState = SoundGeneratorState.SILENT;
            log.debug("currentState = {}", currentState);
        }
    }
}
