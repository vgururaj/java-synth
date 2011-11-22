package andts.javasynth;

import andts.javasynth.generator.Gain;
import andts.javasynth.generator.LfoGenerator;
import andts.javasynth.generator.SoundGenerator;
import andts.javasynth.oscillator.AutomatedOscillator;
import andts.javasynth.oscillator.Oscillator;
import andts.javasynth.oscillator.SimpleOscillator;
import andts.javasynth.waveform.SawtoothWave;
import andts.javasynth.waveform.SineWave;
import andts.javasynth.waveform.TriangleWave;
import andts.javasynth.waveform.Waveform;

import javax.sound.sampled.*;
import java.io.IOException;
import java.util.Arrays;

public class Player
{
    private static final int BUFFER_SIZE = 64000;
    private static final int FRAME_BUFFER_SIZE = 16000;

    public static void main(String[] args) throws IOException, LineUnavailableException
    {
        AudioFormat audioFormat;
        float sampleRate = 44100.0F;

        Mixer.Info[] mixerInfos = AudioSystem.getMixerInfo();
        System.out.println("mixerInfos = " + Arrays.deepToString(mixerInfos));

        Mixer mainMixer = AudioSystem.getMixer(mixerInfos[0]);
        System.out.println("mainMixer = " + mainMixer);
        System.out.println("mainMixer.sourceLines = " + Arrays.deepToString(mainMixer.getTargetLines()));

        audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, sampleRate, 16, 2, 4, sampleRate, false);
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
        System.out.println("mainMixer.getMaxLines = " + mainMixer.getMaxLines(info));

        SourceDataLine outputLine = (SourceDataLine) mainMixer.getLine(info);

        System.out.println("outputLine = " + outputLine.toString());

        outputLine.open();
        outputLine.start();

        Waveform osc1Wave = new TriangleWave(44100);
        LfoGenerator osc1Lfo = new LfoGenerator(
                new SimpleOscillator(new SineWave(44100), 6F),
                new Gain(0.0F));
        Oscillator osc1 = new AutomatedOscillator(osc1Wave, 100F, osc1Lfo);

        LfoGenerator gain1Lfo = new LfoGenerator(
                new SimpleOscillator(new TriangleWave(44100), 7F),
                new Gain(0.5F));
        Gain gain1 = new Gain(0.1F, gain1Lfo);

        SoundGenerator sg1 = new SoundGenerator(16, osc1, gain1);

        Waveform osc2Wave = new TriangleWave(44100);
        LfoGenerator osc2Lfo = new LfoGenerator(
                new SimpleOscillator(new SawtoothWave(44100), 3F),
                new Gain(0.0F));
        Oscillator osc2 = new AutomatedOscillator(osc2Wave, 200F, osc2Lfo);

        LfoGenerator gain2Lfo = new LfoGenerator(
                new SimpleOscillator(new TriangleWave(44100), 7F),
                new Gain(0.5F));
        Gain gain2 = new Gain(0.1F, gain2Lfo);

        SoundGenerator sg2 = new SoundGenerator(16, osc2, gain2);

        byte[] oscBuffer = new byte[BUFFER_SIZE];
        long iteration = 0;
        int noteLen = 6;
        while (true)
        {
            outputLine.write(oscBuffer, 0, BUFFER_SIZE);

            for (int i = 0; i < FRAME_BUFFER_SIZE; ++i)
            {
                byte[] monoFrame = Util.trimLong(sg1.getNextValue() + sg2.getNextValue());
                System.arraycopy(monoFrame, 0, oscBuffer, i * 4, 2); //left channel
                System.arraycopy(monoFrame, 0, oscBuffer, (i * 4) + 2, 2); //right channel
            }

            //sequencer :)
            if (iteration >= noteLen && iteration < 2 * noteLen)
            {
                sg1.getOsc().setFrequency(80F);
                sg2.getOsc().setFrequency(160F);
            }
            else if (iteration >= 2 * noteLen && iteration < 3 * noteLen)
            {
                sg1.getOsc().setFrequency(120F);
                sg2.getOsc().setFrequency(240F);
            }
            else if (iteration >= 3 * noteLen && iteration < 4 * noteLen)
            {
                sg1.getOsc().setFrequency(100F);
                sg2.getOsc().setFrequency(300F);
                osc1Lfo.getGain().setAmpFactor(0.2F);
                osc2Lfo.getGain().setAmpFactor(0.4F);
            }
            else if (iteration == 4 * noteLen)
            {
                sg1.getOsc().setFrequency(100F);
                sg2.getOsc().setFrequency(200F);
                osc1Lfo.getGain().setAmpFactor(0.0F);
                osc2Lfo.getGain().setAmpFactor(0.0F);
                iteration = 0;
            }

            iteration++;
        }
    }
}
