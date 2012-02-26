package andts.javasynth.generator;

import andts.javasynth.effects.Gain;
import andts.javasynth.effects.MoogVcfFilter2;
import andts.javasynth.oscillator.Oscillator;
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
    private BlockingQueue<GeneratorState> eventQueue = new LinkedBlockingQueue<>();
    private GeneratorState currentState = GeneratorState.IDLE;

    /**
     * Creates new SoundGenerator
     *
     * @param sampleSize size of generated sample in bits
     * @param osc        oscillator used to generate wave of some frequency
     * @param gain       used to amplify generated sample to some volume
     */
    public SoundGenerator(int sampleSize, Oscillator osc, Gain gain)
    {
        super(osc, gain);
        this.amp = new SamplePreAmplifier(sampleSize);
//        addObserver(osc);
        addObserver(gain);
    }

    public Integer getNextValue()
    {
        float nextValue = getGain().getAmplifiedValue(getOsc().getNextValue());
        return amp.getAmplifiedValue(nextValue);
    }

    public GeneratorState getCurrentState()
    {
        return currentState;
    }

    public void setOutputQueue(BlockingQueue<Integer> outputQueue)
    {
        this.outputQueue = outputQueue;
    }

    public void start(float frequency)
    {
        if (currentState.equals(GeneratorState.RUNNING)
            && getOsc().getFrequency() == frequency)
        {
            return;
        }

        setChanged();

        getOsc().reset();
        getOsc().setFrequency(frequency);

        notifyObservers(GeneratorState.RUNNING);
        eventQueue.add(GeneratorState.RUNNING);
    }

    public void stop()
    {
        if (currentState.equals(GeneratorState.STOPPING))
        {
            return;
        }

        log.debug("Send STOPPING");
        setChanged();
        notifyObservers(GeneratorState.STOPPING);
        eventQueue.add(GeneratorState.STOPPING);
    }

    @Override
    public void run()
    {
        while (!Thread.currentThread().isInterrupted())
        {
            GeneratorState newState = eventQueue.poll();

            if (newState != null && newState != currentState)
            {
                currentState = newState;
                log.debug("currentState = {}", currentState);
            }
            try
            {
                if (currentState.equals(GeneratorState.RUNNING) || currentState.equals(GeneratorState.STOPPING))
                {
                    outputQueue.put(getNextValue());
                }
                else if (currentState.equals(GeneratorState.SILENT))
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
        if (arg.equals(GeneratorState.SILENT))
        {
            currentState = GeneratorState.SILENT;
        }
    }
}
