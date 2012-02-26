package andts.javasynth;

import andts.javasynth.effects.Gain;
import andts.javasynth.generator.SoundGenerator;
import andts.javasynth.oscillator.Oscillator;
import andts.javasynth.oscillator.SimpleOscillator;
import andts.javasynth.parameter.ConstantParameter;
import andts.javasynth.waveform.SineWave;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sound.sampled.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Player
{
    private static Logger log = LoggerFactory.getLogger(Player.class);

    private static final int SAMPLE_RATE = 44100;
    private static final int BUFFER_SIZE = SAMPLE_RATE;
    private static final int FRAME_BUFFER_SIZE = SAMPLE_RATE / 4;
    private static final int CHANNELS = 2;
    private static final int SAMPLE_SIZE = 16;
    private static final int BITS_IN_BYTE = 8;

    public static void main(String[] args) throws IOException, LineUnavailableException, InterruptedException
    {
        AudioFormat audioFormat;
        float sampleRate = SAMPLE_RATE;

        log.debug("Testing");

        Mixer.Info[] mixerInfos = AudioSystem.getMixerInfo();
        log.debug("mixerInfos = " + Arrays.deepToString(mixerInfos));
//        System.out.println("mixerInfos[0] = " + mixerInfos[0]);

//        Mixer mainMixer = AudioSystem.getMixer(mixerInfos[0]);
//        System.out.println("mainMixer = " + mainMixer);
//        System.out.println("mainMixer.sourceLines = " + Arrays.deepToString(mainMixer.getTargetLines()));

        audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, sampleRate, SAMPLE_SIZE, CHANNELS, (CHANNELS * SAMPLE_SIZE) / BITS_IN_BYTE, sampleRate, false);
//        DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
//        System.out.println("mainMixer.getMaxLines = " + mainMixer.getMaxLines(info));

        SourceDataLine outputLine = AudioSystem.getSourceDataLine(audioFormat, mixerInfos[0]);

        log.debug("outputLine = " + outputLine.toString());

        outputLine.open();
        outputLine.start();

        Oscillator osc1 = new SimpleOscillator(new SineWave(), new ConstantParameter<>(500F, 0F), sampleRate);
        Gain gain1 = new Gain(new ConstantParameter<>(0.1F, 0F));

        BlockingQueue<Integer> soundData = new ArrayBlockingQueue<>(BUFFER_SIZE);

        SoundGenerator sg1 = new SoundGenerator(SAMPLE_SIZE, osc1, gain1);
        sg1.setOutputQueue(soundData);
        new Thread(sg1).start();

        byte[] oscBuffer = new byte[BUFFER_SIZE];
        long iteration = 0;
        int noteLen = 7;
        //noinspection InfiniteLoopStatement
        while (true)
        {
            //sequencer :)
            if (iteration >= noteLen && iteration < 2 * noteLen)
            {
                sg1.start(180F);
                log.debug("start note 2");
            }
            else if (iteration >= 2 * noteLen && iteration < 3 * noteLen)
            {
                sg1.start(220F);
                log.debug("start note 3");
            }
            else if (iteration >= 3 * noteLen && iteration < 4 * noteLen)
            {
                sg1.start(210F);
//                sg1.stop();
                log.debug("start note 4");
//                log.debug("stop");
            }
            else if (iteration == 4 * noteLen)
            {
                sg1.start(200F);
                log.debug("start note 1 again");
                iteration = 0;
            }
            else /*if (iteration == 4 * noteLen)*/
            {
                sg1.start(200F);
                log.debug("start note 1");
//                iteration = 0;
            }

            for (int i = 0; i < FRAME_BUFFER_SIZE; ++i)
            {
//                Integer nextValue = sg1.getNextValue();
                Integer nextValue = soundData.take();
                byte[] monoFrame = Util.chop(nextValue);
                System.arraycopy(monoFrame, 0, oscBuffer, i * 4, 2); //left channel
                System.arraycopy(monoFrame, 0, oscBuffer, (i * 4) + 2, 2); //right channel
            }

            outputLine.write(oscBuffer, 0, BUFFER_SIZE);

            iteration++;
        }

    }
}
