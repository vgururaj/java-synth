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

        SourceDataLine outputLine1 = (SourceDataLine) mainMixer.getLine(info);
        SourceDataLine outputLine2 = (SourceDataLine) mainMixer.getLine(info);

        System.out.println("outputLine = " + outputLine1.toString());
        System.out.println("outputLine = " + outputLine2.toString());

        outputLine1.open();
        outputLine2.open();
        outputLine1.start();
        outputLine2.start();

        //first sound generator
        byte[] oscBuffer1 = new byte[BUFFER_SIZE];

        Waveform wave1 = new SineWave(44100);
        Oscillator osc1 = new SimpleOscillator(wave1, 300.0F);
        SamplePreAmplifier amp1 = new SamplePreAmplifier(16);
        //freq lfo
        Oscillator freqLfoOsc1 = new SimpleOscillator(wave1, 3F);
        LfoAmplifier freqLfoAmp1 = new LfoAmplifier(0.5f);
        LfoGenerator freqLfo1 = new LfoGenerator(freqLfoOsc1, freqLfoAmp1);
        //gain lfo
        Oscillator gainLfoOsc1 = new SimpleOscillator(wave1, 1.7F);
        LfoAmplifier gainLfoAmp1 = new LfoAmplifier(1f);
        LfoGenerator gainLfo1 = new LfoGenerator(gainLfoOsc1, gainLfoAmp1);

        SoundGenerator gen1 = new SoundGenerator(osc1, amp1, freqLfo1, gainLfo1);
        
        //second sound generator
        byte[] oscBuffer2 = new byte[BUFFER_SIZE];
        
        Waveform wave2 = new SineWave(44100);
        Oscillator osc2 = new SimpleOscillator(wave2, 150.0F);
        SamplePreAmplifier amp2 = new SamplePreAmplifier(16);
        //freq lfo
        Oscillator freqLfoOsc2 = new SimpleOscillator(wave2, 5F);
        LfoAmplifier freqLfoAmp2 = new LfoAmplifier(0.5f);
        LfoGenerator freqLfo2 = new LfoGenerator(freqLfoOsc2, freqLfoAmp2);
        //gain lfo
        Oscillator gainLfoOsc2 = new SimpleOscillator(wave2, 1F);
        LfoAmplifier gainLfoAmp2 = new LfoAmplifier(0f);
        LfoGenerator gainLfo2 = new LfoGenerator(gainLfoOsc2, gainLfoAmp2);

        SoundGenerator gen2 = new SoundGenerator(osc2, amp2, freqLfo2, gainLfo2);
        

        while (true)
        {
            for (int i = 0; i < FRAME_BUFFER_SIZE; ++i)
            {
                byte[] monoFrame1 = Util.trimLong(gen1.getNextSample());
                System.arraycopy(monoFrame1, 0, oscBuffer1, i * 4, 2); //left channel
                System.arraycopy(monoFrame1, 0, oscBuffer1, (i * 4) + 2, 2); //right channel

                byte[] monoFrame2 = Util.trimLong(gen2.getNextSample());
                System.arraycopy(monoFrame2, 0, oscBuffer2, i * 4, 2); //left channel
                System.arraycopy(monoFrame2, 0, oscBuffer2, (i * 4) + 2, 2); //right channel
            }

            outputLine1.write(oscBuffer1, 0, BUFFER_SIZE);
            outputLine2.write(oscBuffer2, 0, BUFFER_SIZE);
        }
    }
}
