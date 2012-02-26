package andts.javasynth.synth;

import andts.javasynth.generator.SoundGenerator;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Synthesizer
{
    private final BlockingQueue<Integer> outputQueue;
    private final SoundGenerator soundGenerator;

    public Synthesizer(SoundGenerator soundGenerator, BlockingQueue<Integer> outputQueue)
    {
        this.outputQueue = outputQueue;
        this.soundGenerator = soundGenerator;
    }

    /**
     * Start playing sound with some frequency. Key pressed.
     * @param freq
     */
    public void playFrequency(float freq)
    {
//        soundGenerator.

    }

    /**
     * Start playing sound with some note. Key pressed.
     * @param noteNumber
     */
    public void playNote(int noteNumber)
    {

    }

    /**
     * Stop playing. Key released.
     */
    public void stop()
    {

    }
}
