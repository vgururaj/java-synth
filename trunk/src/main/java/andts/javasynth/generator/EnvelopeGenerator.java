package andts.javasynth.generator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Observable;
import java.util.Observer;

public class EnvelopeGenerator extends Observable implements Generator<Float>, Observer
{
    private static final Logger log = LoggerFactory.getLogger(EnvelopeGenerator.class);
    public static final int MILLISECONDS_IN_SEC = 1000;

    private int attackSamples;
    private double attackRisePerSample;

    private int decaySamples;
    private double decayDecreasePerSample;

    private float sustain;

    private int releaseSamples;
    private double releaseDecreasePerSample;

    private int currentSample;
    private double currentLevel;

    private EnvelopeState currentState = EnvelopeState.IDLE;

    /**
     * Create new EnvelopeGenerator with specified ADSR parameters
     *
     * @param sampleRate sample rate of audio signal
     * @param attack     time to rise to 100%, in ms
     * @param decay      time to lower to sustain level, in ms
     * @param sustain    sustain level of output, [0..1]
     * @param release    time to lower from sustain level to 0
     */
    public EnvelopeGenerator(int sampleRate, int attack, int decay, float sustain, int release)
    {
        calcAttackParams(sampleRate, attack);
        calcDecayParams(sampleRate, decay, sustain);
        this.sustain = sustain;
        calcReleaseParams(sampleRate, sustain, release);
    }

    private void calcReleaseParams(int sampleRate, float sustain, int release)
    {
        releaseSamples = release * sampleRate / MILLISECONDS_IN_SEC;
        log.debug("releaseSamples = {}", releaseSamples);
        releaseDecreasePerSample = sustain / releaseSamples;
        log.debug("releaseDecreasePerSample = {}", releaseDecreasePerSample);
    }

    private void calcDecayParams(int sampleRate, int decay, float sustain)
    {
        decaySamples = decay * sampleRate / MILLISECONDS_IN_SEC;
        log.debug("decaySamples = {}", decaySamples);
        decayDecreasePerSample = (1F - sustain) / decaySamples;
        log.debug("decayDecreasePerSample = {}", decayDecreasePerSample);
    }

    private void calcAttackParams(int sampleRate, int attack)
    {
        attackSamples = attack * sampleRate / MILLISECONDS_IN_SEC;
        log.debug("attackSamples = {}", attackSamples);
        attackRisePerSample = 1F / attackSamples;
        log.debug("attackRisePerSample = {}", attackRisePerSample);
    }

    //TODO refactor this shit! extract states to separate classes
    public Float getNextValue()
    {
        if (currentState.equals(EnvelopeState.ATTACK))
        {
            currentLevel += attackRisePerSample;
            currentSample++;

            if (currentSample == attackSamples)
            {
                log.debug("DECAY!");
                currentState = EnvelopeState.DECAY;
                currentSample = 0;
            }

//            log.debug("currentLevel = {}", currentLevel);
            return (float) currentLevel;
        }
        else if (currentState.equals(EnvelopeState.DECAY))
        {
            currentLevel -= decayDecreasePerSample;
            currentSample++;

            if (currentSample == decaySamples)
            {
                log.debug("SUSTAIN!");
                currentState = EnvelopeState.SUSTAIN;
                currentSample = 0;
            }

//            log.debug("currentLevel = {}", currentLevel);
            return (float) currentLevel;
        }
        else if (currentState.equals(EnvelopeState.SUSTAIN))
        {
            currentLevel = sustain;

//            log.debug("currentLevel = {}", currentLevel);
            return (float) currentLevel;
        }
        else if (currentState.equals(EnvelopeState.RELEASE))
        {
            currentLevel -= releaseDecreasePerSample;
            currentSample++;

            if (currentSample == releaseSamples)
            {
                log.debug("IDLE!");
                currentState = EnvelopeState.IDLE;
                currentSample = 0;
            }

//            log.debug("currentLevel = {}", currentLevel);
            return (float) currentLevel;
        }
        else
        {
            return 0F;
        }
    }

    @Override
    public void update(Observable o, Object arg)
    {
        if (arg instanceof SoundGeneratorState)
        {
            if (arg.equals(SoundGeneratorState.RUNNING))
            {
                log.debug("ATTACK!");
                reset();
            }
            else if (arg.equals(SoundGeneratorState.STOPPING))
            {
                log.debug("RELEASE");
                currentState = EnvelopeState.RELEASE;
            }
        }
    }

    private void reset()
    {
        currentState = EnvelopeState.ATTACK;
        currentSample = 0;
        currentLevel = 0F;
    }

    private static enum EnvelopeState
    {
        ATTACK, DECAY, SUSTAIN, RELEASE, IDLE
    }
}
