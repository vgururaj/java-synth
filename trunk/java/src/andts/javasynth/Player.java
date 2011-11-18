package andts.javasynth;

import andts.javasynth.generator.LfoAmplifier;
import andts.javasynth.generator.LfoGenerator;
import andts.javasynth.generator.SamplePreAmplifier;
import andts.javasynth.generator.SoundGenerator;
import andts.javasynth.oscillator.Oscillator;
import andts.javasynth.oscillator.SimpleOscillator;
import andts.javasynth.waveform.SineWave;
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
        float fSampleRate = 44100.0F;

        Mixer.Info[] mixerInfos = AudioSystem.getMixerInfo();
        System.out.println("mixerInfos = " + Arrays.deepToString(mixerInfos));

        Mixer mainMixer = AudioSystem.getMixer(mixerInfos[0]);
        System.out.println("mainMixer = " + mainMixer);
        System.out.println("mainMixer.sourceLines = " + Arrays.deepToString(mainMixer.getTargetLines()));

        audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, fSampleRate, 16, 2, 4, fSampleRate, false);
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
        System.out.println("mainMixer.getMaxLines = " + mainMixer.getMaxLines(info));

        SourceDataLine outputLine = (SourceDataLine) mainMixer.getLine(info);

        System.out.println("outputLine = " + outputLine.toString());

        outputLine.open();
        outputLine.start();

        byte[] oscBuffer = new byte[BUFFER_SIZE];

        Waveform wave = new SineWave(44100);
        Oscillator osc = new SimpleOscillator(wave, 100.0F);
        SamplePreAmplifier amp = new SamplePreAmplifier(16);
        //freq lfo
        Oscillator freqLfoOsc = new SimpleOscillator(wave, 3F);
        LfoAmplifier freqLfoAmp = new LfoAmplifier(0.5f);
        LfoGenerator freqLfo = new LfoGenerator(freqLfoOsc, freqLfoAmp);
        //gain lfo
        Oscillator gainLfoOsc = new SimpleOscillator(wave, 1.7F);
        LfoAmplifier gainLfoAmp = new LfoAmplifier(1f);
        LfoGenerator gainLfo = new LfoGenerator(gainLfoOsc, gainLfoAmp);

        SoundGenerator gen = new SoundGenerator(osc, amp, freqLfo, gainLfo);

        while (true)
        {
            for (int i = 0; i < FRAME_BUFFER_SIZE; ++i)
            {
                byte[] monoFrame = Util.trimLong(gen.getNextSample());
                System.arraycopy(monoFrame, 0, oscBuffer, i * 4, 2); //left channel
                System.arraycopy(monoFrame, 0, oscBuffer, (i * 4) + 2, 2); //right channel
            }

            outputLine.write(oscBuffer, 0, BUFFER_SIZE);
        }
    }
}
