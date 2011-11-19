package andts.javasynth;

import andts.javasynth.generator.Gain;
import andts.javasynth.generator.LfoAmplifier;
import andts.javasynth.generator.LfoGenerator;
import andts.javasynth.generator.SoundGenerator;
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
    private static final int BUFFER_SIZE       = 64000;
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

//        outputLine.open();
//        outputLine.start();

        //first sound generator
        byte[] oscBuffer = new byte[BUFFER_SIZE];

        Waveform wave1 = new SawtoothWave(44100);
        Oscillator osc1 = new SimpleOscillator(wave1, 100.0F);
        Gain gain1 = new Gain(0.1f);
        //freq lfo
        Oscillator freqLfoOsc1 = new SimpleOscillator(wave1, 35F);
        LfoAmplifier freqLfoAmp1 = new LfoAmplifier(0.5f);
        LfoGenerator freqLfo1 = new LfoGenerator(freqLfoOsc1, freqLfoAmp1);
        //gain lfo
        Oscillator gainLfoOsc1 = new SimpleOscillator(wave1, 3F);
        LfoAmplifier gainLfoAmp1 = new LfoAmplifier(0.f);
        LfoGenerator gainLfo1 = new LfoGenerator(gainLfoOsc1, gainLfoAmp1);

        SoundGenerator gen1 = new SoundGenerator(16, osc1, gain1, freqLfo1, gainLfo1);

        //second sound generator
        byte[] oscBuffer2 = new byte[BUFFER_SIZE];

        Waveform wave2 = new SineWave(44100);
        Oscillator osc2 = new SimpleOscillator(wave2, 80.0F);
        Gain gain2 = new Gain(0.1f);
        //freq lfo
        Waveform oscWave = new SineWave(44100);
        Oscillator freqLfoOsc2 = new SimpleOscillator(oscWave, 52.3F);
        LfoAmplifier freqLfoAmp2 = new LfoAmplifier(1f);
        LfoGenerator freqLfo2 = new LfoGenerator(freqLfoOsc2, freqLfoAmp2);
        //gain lfo
        Oscillator gainLfoOsc2 = new SimpleOscillator(oscWave, .1F);
        LfoAmplifier gainLfoAmp2 = new LfoAmplifier(0.3f);
        LfoGenerator gainLfo2 = new LfoGenerator(gainLfoOsc2, gainLfoAmp2);

        SoundGenerator gen2 = new SoundGenerator(16, osc2, gain2, freqLfo2, gainLfo2);

        for (int i = 0; i < FRAME_BUFFER_SIZE; ++i)
        {
            byte[] monoFrame = Util.trimLong(gen1.getNextSample() + gen2.getNextSample());
            System.arraycopy(monoFrame, 0, oscBuffer, i * 4, 2); //left channel
            System.arraycopy(monoFrame, 0, oscBuffer, (i * 4) + 2, 2); //right channel
        }

        outputLine.write(oscBuffer, 0, BUFFER_SIZE);

        outputLine.open();
        outputLine.start();

        while (true)
        {
            outputLine.write(oscBuffer, 0, BUFFER_SIZE);

            for (int i = 0; i < FRAME_BUFFER_SIZE; ++i)
            {
                byte[] monoFrame = Util.trimLong(gen1.getNextSample() + gen2.getNextSample());
                System.arraycopy(monoFrame, 0, oscBuffer, i * 4, 2); //left channel
                System.arraycopy(monoFrame, 0, oscBuffer, (i * 4) + 2, 2); //right channel
            }

        }
    }
}
