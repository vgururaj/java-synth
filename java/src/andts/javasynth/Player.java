package andts.javasynth;

import andts.javasynth.generator.SamplePreAmplifier;
import andts.javasynth.generator.SoundGenerator;
import andts.javasynth.oscillator.OldOscillator;
import andts.javasynth.oscillator.Oscillator;
import andts.javasynth.oscillator.SimpleOscillator;
import andts.javasynth.waveform.SineWave;
import andts.javasynth.waveform.Waveform;

import javax.sound.sampled.*;
import java.io.IOException;
import java.util.Arrays;

public class Player
{
    private static final int     BUFFER_SIZE       = 64000;
    private static final int     FRAME_BUFFER_SIZE = 16000;
    private static       boolean DEBUG             = false;


    public static void main(String[] args) throws IOException, LineUnavailableException
    {
        byte[] abData;
        //        byte[] abData2;
        //        byte[] abData3;
        AudioFormat audioFormat;
        int nWaveformType = OldOscillator.WAVEFORM_TRIANGLE;
        float fSampleRate = 44100.0F;
        //        float fSignalFrequency = 80.0F;
        float fAmplitude = 0.1F;


        Mixer.Info[] mixerInfos = AudioSystem.getMixerInfo();

        System.out.println("mixerInfos = " + Arrays.deepToString(mixerInfos));

        Mixer mainMixer = AudioSystem.getMixer(mixerInfos[0]);

        System.out.println("mainMixer = " + mainMixer);
        System.out.println("mainMixer.sourceLines = " + Arrays.deepToString(mainMixer.getTargetLines()));

        audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, fSampleRate, 16, 2, 4, fSampleRate, false);
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
        System.out.println("mainMixer.getMaxLines = " + mainMixer.getMaxLines(info));

        SourceDataLine outputLine = (SourceDataLine) mainMixer.getLine(info);
        SourceDataLine outputLine2 = (SourceDataLine) mainMixer.getLine(info);
        //        SourceDataLine outputLine3 = (SourceDataLine) mainMixer.getLine(info);

        System.out.println("outputLine = " + outputLine.toString());
        //        System.out.println("outputLine2 = " + outputLine2.toString());
        //        System.out.println("outputLine3 = " + outputLine3.toString());

        /*audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
                                      fSampleRate, 16, 2, 4, fSampleRate, false);*/
        AudioInputStream oscillator = new OldOscillator(
                OldOscillator.WAVEFORM_SINE,
                1500.0F,
                fAmplitude,
                audioFormat,
                AudioSystem.NOT_SPECIFIED);

        /*AudioInputStream oscillator2 = new OldOscillator(
                OldOscillator.WAVEFORM_SINE,
                700.0F,
                fAmplitude,
                audioFormat,
                AudioSystem.NOT_SPECIFIED);

        AudioInputStream oscillator3 = new OldOscillator(
                OldOscillator.WAVEFORM_SINE,
                50.0F,
                0.7f,
                audioFormat,
                AudioSystem.NOT_SPECIFIED);*/

        outputLine.open();
        outputLine2.open();
        //        outputLine3.open();

        outputLine.start();
        outputLine2.start();
        //        outputLine3.start();

        abData = new byte[BUFFER_SIZE];
        //        abData2 = new byte[BUFFER_SIZE];
        //        abData3 = new byte[BUFFER_SIZE];

        byte[] oscBuffer = new byte[BUFFER_SIZE];

        Waveform wave = new SineWave(44100);

        Oscillator osc = new SimpleOscillator(wave, 1500.0F);

        SamplePreAmplifier amp = new SamplePreAmplifier(16);

        SoundGenerator gen = new SoundGenerator(osc, amp);

                while (true)
                {
        //            if (DEBUG) { out("andts.javasynth.Player.main(): trying to read (bytes): " + abData.length); }
        int nRead = oscillator.read(abData);
        //            int nRead2 = oscillator2.read(abData2);
        //            int nRead3 = oscillator3.read(abData3);

   /*     System.out.print("array1 = ");
        for (int j = 0; j < abData.length; j += 4)
        {
            int a = (abData[j+1] << 8) + abData[j];
            System.out.print(a + ",\n ");
        }*/

//        System.out.println("\n");

//        int[] waveBuffer = new int[FRAME_BUFFER_SIZE];
        for (int i = 0; i < FRAME_BUFFER_SIZE; ++i)
        {
//            waveBuffer[i] = gen.getNextSample();
            byte[] monoFrame = Util.trimInt(gen.getNextSample());
//            System.arraycopy(monoFrame, 0, oscBuffer, i * 4, 2);
            System.arraycopy(monoFrame, 0, oscBuffer, (i * 4) + 2, 2);
        }

        /*System.out.print("array2 = ");
        for (int j = 0; j < waveBuffer.length; ++j)
        {
            System.out.print(waveBuffer[j] + ",\n ");
        }*/

        //            if (DEBUG) { out("andts.javasynth.Player.main(): in loop, read (bytes): " + nRead); }
                    int nWritten = outputLine2.write(abData, 0, nRead);
        //            int nWritten2 = outputLine2.write(abData2, 0, nRead2);
        //            int nWritten3 = outputLine3.write(abData3, 0, nRead3);
                    outputLine.write(oscBuffer, 0, BUFFER_SIZE);
        //            if (DEBUG) { out("andts.javasynth.Player.main(): written: " + nWritten); }
                }
    }
}
