package andts.javasynth;

import andts.javasynth.effects.Gain;
import andts.javasynth.generator.EnvelopeGenerator;
import andts.javasynth.generator.LfoGenerator;
import andts.javasynth.generator.SoundGenerator;
import andts.javasynth.oscillator.Oscillator;
import andts.javasynth.oscillator.SimpleOscillator;
import andts.javasynth.parameter.ConstantParameter;
import andts.javasynth.parameter.EnvelopeAutomatedParameter;
import andts.javasynth.parameter.LfoAutomatedParameter;
import andts.javasynth.waveform.SawtoothWave;
import andts.javasynth.waveform.SineWave;
import andts.javasynth.waveform.SquareWave;
import andts.javasynth.waveform.TriangleWave;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sound.sampled.*;
import java.io.IOException;
import java.util.Arrays;
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

        EnvelopeGenerator envelope = new EnvelopeGenerator(SAMPLE_RATE, 5000, 5000, 0.5F, 5000);

        LfoGenerator lfo = new LfoGenerator(
            new SimpleOscillator(
                new SawtoothWave(),
                new ConstantParameter<>(5F),
                sampleRate),
            new Gain(new ConstantParameter<>(-0.3F)));

        LfoAutomatedParameter lfoFreq = new LfoAutomatedParameter(new ConstantParameter<>(500F, 0F), lfo);

        Oscillator osc1 =
            new SimpleOscillator(
                new TriangleWave(),
                new EnvelopeAutomatedParameter(lfoFreq, envelope), sampleRate);

        Gain gain1 = new Gain(new ConstantParameter<>(0.7F));

        BlockingQueue<Integer> soundData = new ArrayBlockingQueue<>(BUFFER_SIZE);

        SoundGenerator sg1 = new SoundGenerator(SAMPLE_SIZE, osc1, gain1, null);
        sg1.setOutputQueue(soundData);
        new Thread(sg1).start();

        byte[] oscBuffer = new byte[BUFFER_SIZE];
        sg1.start(540F);

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
