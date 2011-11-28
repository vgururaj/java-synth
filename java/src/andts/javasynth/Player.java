package andts.javasynth;

import andts.javasynth.effects.Gain;
import andts.javasynth.generator.LfoGenerator;
import andts.javasynth.generator.SoundGenerator;
import andts.javasynth.oscillator.Oscillator;
import andts.javasynth.oscillator.SimpleOscillator;
import andts.javasynth.parameter.ConstantParameter;
import andts.javasynth.parameter.LfoAutomatedParameter;
import andts.javasynth.waveform.SawtoothWave;
import andts.javasynth.waveform.SineWave;
import andts.javasynth.waveform.Waveform;

import javax.sound.sampled.*;
import java.io.IOException;
import java.util.Arrays;

public class Player
{
    public static final int SAMPLE_RATE = 44100;
    private static final int BUFFER_SIZE = SAMPLE_RATE;
    private static final int FRAME_BUFFER_SIZE = 11025;

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

        Waveform osc1Wave = new SawtoothWave(SAMPLE_RATE);
        LfoGenerator osc1Lfo = new LfoGenerator(
                new SimpleOscillator(new SineWave(SAMPLE_RATE), new ConstantParameter<Float>(6F)),
                new Gain(new ConstantParameter<Float>(.5F)));
        Oscillator osc1 = new SimpleOscillator(osc1Wave,
                                               new LfoAutomatedParameter(
                                                       new ConstantParameter<Float>(500F), osc1Lfo));

        //        LfoGenerator gain1Lfo = new LfoGenerator(
        //                new SimpleOscillator(new TriangleWave(SAMPLE_RATE), new ConstantParameter<Float>(6F)),
        //                new Gain(0.F));
        Gain gain1 = new Gain(new ConstantParameter<Float>(0.1F));

        SoundGenerator sg1 = new SoundGenerator(16, osc1, gain1);

        /*Waveform osc2Wave = new SawtoothWave(SAMPLE_RATE);
        LfoGenerator osc2Lfo = new LfoGenerator(
                new SimpleOscillator(
                        new SawtoothWave(SAMPLE_RATE),
                        new ConstantParameter<Float>(3F)),
                new Gain(0.0F));
        Oscillator osc2 = new AutomatedOscillator(osc2Wave, new ConstantParameter<Float>(200F), osc2Lfo);

        LfoGenerator gain2Lfo = new LfoGenerator(
                new SimpleOscillator(new TriangleWave(SAMPLE_RATE), new ConstantParameter<Float>(.3F)),
                new Gain(1F));
        Gain gain2 = new Gain(0.1F, gain2Lfo);

        SoundGenerator sg2 = new SoundGenerator(16, osc2, gain2);*/

        byte[] oscBuffer = new byte[BUFFER_SIZE];
        long iteration = 0;
        int noteLen = 7;
        while (true)
        {
            outputLine.write(oscBuffer, 0, BUFFER_SIZE);

            for (int i = 0; i < FRAME_BUFFER_SIZE; ++i)
            {
                byte[] monoFrame = Util.trimLong(sg1.getNextValue()/* + sg2.getNextValue()*/);
                System.arraycopy(monoFrame, 0, oscBuffer, i * 4, 2); //left channel
                System.arraycopy(monoFrame, 0, oscBuffer, (i * 4) + 2, 2); //right channel
            }

            /*//sequencer :)
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
                sg1.getOsc().setFrequency(200F);
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

            iteration++;*/
        }

        //        AudioSystem.w
    }
}
