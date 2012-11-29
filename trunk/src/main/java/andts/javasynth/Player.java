package andts.javasynth;

import andts.javasynth.effects.Gain;
import andts.javasynth.generator.EnvelopeGenerator;
import andts.javasynth.generator.LfoGenerator;
import andts.javasynth.generator.SoundGenerator;
import andts.javasynth.oscillator.Oscillator;
import andts.javasynth.oscillator.SimpleOscillator;
import andts.javasynth.parameter.ConstantParameter;
import andts.javasynth.parameter.LfoAutomatedParameter;
import andts.javasynth.waveform.SineWave;
import andts.javasynth.waveform.SquareWave;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sound.sampled.*;
import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Player
{
    private static Logger log = LoggerFactory.getLogger(Player.class);

    private static final int SAMPLE_RATE = 44100;
    private static final int BUFFER_SIZE = SAMPLE_RATE;
    private static final int FRAME_BUFFER_SIZE = BUFFER_SIZE / 4;
    private static final int CHANNELS = 2;
    private static final int SAMPLE_SIZE = 16;
    private static final int BITS_IN_BYTE = 8;

    public static void main(String[] args) throws IOException, LineUnavailableException, InterruptedException
    {
        AudioFormat audioFormat;
        float sampleRate = SAMPLE_RATE;

        Mixer.Info[] mixerInfos = AudioSystem.getMixerInfo();
        audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, sampleRate, SAMPLE_SIZE, CHANNELS,
                                      (CHANNELS * SAMPLE_SIZE) / BITS_IN_BYTE, sampleRate, false);
        SourceDataLine outputLine = AudioSystem.getSourceDataLine(audioFormat, mixerInfos[0]);
        outputLine.open();
        outputLine.start();



        EnvelopeGenerator envelope = new EnvelopeGenerator(SAMPLE_RATE, 100, 500, 0.2F, 10);

        SimpleOscillator lfoOsc = new SimpleOscillator(new SineWave(), new ConstantParameter<>(3f), SAMPLE_RATE);
        Gain lfoAmplitude = new Gain(new ConstantParameter<>(0.2f));
        LfoGenerator lfoGenerator = new LfoGenerator(lfoOsc, lfoAmplitude);

        LfoAutomatedParameter oscFreq = new LfoAutomatedParameter(new ConstantParameter<>(0F), lfoGenerator);

        Oscillator osc1 = new SimpleOscillator(new SquareWave(), oscFreq, sampleRate);
        Gain gain1 = new Gain(new ConstantParameter<>(0.1F));

        BlockingQueue<Integer> soundData = new ArrayBlockingQueue<>(BUFFER_SIZE);

        final SoundGenerator sg1 = new SoundGenerator(SAMPLE_SIZE, osc1, gain1, envelope);
        sg1.setOutputQueue(soundData);

        new Thread(sg1).start();

        byte[] oscBuffer = new byte[BUFFER_SIZE];

        new Thread(new Runnable()
        {
            char lastChar = ' ';
            boolean running = true;

            @Override
            public void run()
            {
                while (!Thread.interrupted())
                {
                    try
                    {
                        char input = (char) System.in.read();
                        log.debug("got key: {}", input);
                        if (input == '\n')
                        {
                            continue;
                        }

                        if (lastChar == input && running)
                        {
                            sg1.stop();
                            lastChar = input;
                            log.debug("stop!!!");
                            running = false;
                        }
                        else
                        {
                            sg1.start(input*2);
                            lastChar = input;
                            log.debug("start!!!");
                            running = true;
                        }
                    }
                    catch (IOException e)
                    {
                        log.debug("got exception while reading keys:", e);
                    }
                }
            }
        }).start();

        //noinspection InfiniteLoopStatement
        while (true)
        {
            for (int i = 0; i < FRAME_BUFFER_SIZE; ++i)
            {
                Integer nextValue = soundData.take();
                byte[] monoFrame = Util.chop(nextValue);
                System.arraycopy(monoFrame, 0, oscBuffer, i * 4, 2); //left channel
                System.arraycopy(monoFrame, 0, oscBuffer, (i * 4) + 2, 2); //right channel
            }

            outputLine.write(oscBuffer, 0, BUFFER_SIZE);
        }
    }
}
