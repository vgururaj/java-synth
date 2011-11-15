package andts.javasynth;

import andts.javasynth.oscillator.OldOscillator;
import com.sun.media.sound.MixerSourceLine;

import javax.sound.sampled.*;
import java.io.IOException;
import java.util.Arrays;

public class Player
{
    private static final int     BUFFER_SIZE = 128000;
    private static       boolean DEBUG       = false;


    public static void main(String[] args) throws IOException, LineUnavailableException
    {
        byte[] abData;
        byte[] abData2;
        byte[] abData3;
        AudioFormat audioFormat;
        int nWaveformType = OldOscillator.WAVEFORM_TRIANGLE;
        float fSampleRate = 44100.0F;
        float fSignalFrequency = 80.0F;
        float fAmplitude = 0.1F;

        /*
           *	Parsing of command-line options takes place...
           */
        /*Getopt g = new Getopt("AudioPlayer", args, "ht:r:f:a:D");
        int c;
        while ((c = g.getopt()) != -1)
        {
            switch (c)
            {
                case 'h':
                    printUsageAndExit();

                case 't':
                    nWaveformType = getWaveformType(g.getOptarg());
                    break;

                case 'r':
                    fSampleRate = Float.parseFloat(g.getOptarg());
                    break;

                case 'f':
                    fSignalFrequency = Float.parseFloat(g.getOptarg());
                    break;

                case 'a':
                    fAmplitude = Float.parseFloat(g.getOptarg());
                    break;

                case 'D':
                    DEBUG = true;
                    break;

                case '?':
                    printUsageAndExit();

                default:
                    if (DEBUG) { out("getopt() returned " + c); }
                    break;
            }
        }*/

        Mixer.Info[] mixerInfos = AudioSystem.getMixerInfo();

        System.out.println("mixerInfos = " + Arrays.deepToString(mixerInfos));

        Mixer mainMixer = AudioSystem.getMixer(mixerInfos[0]);

        System.out.println("mainMixer = " + mainMixer);
        System.out.println("mainMixer.sourceLines = " + Arrays.deepToString(mainMixer.getTargetLines()));

        audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, fSampleRate, 16, 2, 4, fSampleRate, false);
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
        System.out.println("mainMixer.getMaxLines = " + mainMixer.getMaxLines(info));

        MixerSourceLine outputLine = (MixerSourceLine) mainMixer.getLine(info);
        MixerSourceLine outputLine2 = (MixerSourceLine) mainMixer.getLine(info);
        MixerSourceLine outputLine3 = (MixerSourceLine) mainMixer.getLine(info);

        System.out.println("outputLine = " + outputLine.toString());
        System.out.println("outputLine2 = " + outputLine2.toString());
        System.out.println("outputLine3 = " + outputLine3.toString());


        /*audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
                                      fSampleRate, 16, 2, 4, fSampleRate, false);*/
        AudioInputStream oscillator = new OldOscillator(
                nWaveformType,
                2000.0F,
                fAmplitude,
                audioFormat,
                AudioSystem.NOT_SPECIFIED);

        AudioInputStream oscillator2 = new OldOscillator(
                OldOscillator.WAVEFORM_SINE,
                700.0F,
                fAmplitude,
                audioFormat,
                AudioSystem.NOT_SPECIFIED);

        AudioInputStream oscillator3 = new OldOscillator(
                OldOscillator.WAVEFORM_SINE,
                200.0F,
                fAmplitude,
                audioFormat,
                AudioSystem.NOT_SPECIFIED);

        outputLine.open();
        outputLine2.open();
        outputLine3.open();

        outputLine.start();
        outputLine2.start();
        outputLine3.start();

        abData = new byte[BUFFER_SIZE];
        abData2 = new byte[BUFFER_SIZE];
        abData3 = new byte[BUFFER_SIZE];

        while (true)
        {
            if (DEBUG) { out("andts.javasynth.Player.main(): trying to read (bytes): " + abData.length); }
            int nRead = oscillator.read(abData);
            int nRead2 = oscillator2.read(abData2);
            int nRead3 = oscillator3.read(abData3);
            if (DEBUG) { out("andts.javasynth.Player.main(): in loop, read (bytes): " + nRead); }
            int nWritten = outputLine.write(abData, 0, nRead);
            int nWritten2 = outputLine2.write(abData2, 0, nRead2);
            int nWritten3 = outputLine3.write(abData3, 0, nRead3);
            if (DEBUG) { out("andts.javasynth.Player.main(): written: " + nWritten); }
        }
    }


    private static int getWaveformType(String strWaveformType)
    {
        int nWaveformType = OldOscillator.WAVEFORM_SINE;
        strWaveformType = strWaveformType.trim().toLowerCase();
        if (strWaveformType.equals("sine"))
        {
            nWaveformType = OldOscillator.WAVEFORM_SINE;
        }
        else if (strWaveformType.equals("square"))
        {
            nWaveformType = OldOscillator.WAVEFORM_SQUARE;
        }
        else if (strWaveformType.equals("triangle"))
        {
            nWaveformType = OldOscillator.WAVEFORM_TRIANGLE;
        }
        else if (strWaveformType.equals("sawtooth"))
        {
            nWaveformType = OldOscillator.WAVEFORM_SAWTOOTH;
        }
        return nWaveformType;
    }

    private static void out(String strMessage)
    {
        System.out.println(strMessage);
    }
}
