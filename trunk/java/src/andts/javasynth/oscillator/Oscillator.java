package andts.javasynth.oscillator;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.ByteArrayInputStream;
import java.io.IOException;


public class Oscillator extends AudioInputStream
{
    private static final boolean DEBUG = false;

    public static final int WAVEFORM_SINE = 0;
    public static final int WAVEFORM_SQUARE = 1;
    public static final int WAVEFORM_TRIANGLE = 2;
    public static final int WAVEFORM_SAWTOOTH = 3;

    private byte[] m_abData;
    private int    m_nBufferPosition;
    private long   m_lRemainingFrames;
    private final int waveformType;


    public Oscillator(int nWaveformType, float fSignalFrequency,
                      float fAmplitude, AudioFormat audioFormat, long lLength)
    {
        super(new ByteArrayInputStream(new byte[0]),
              new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
                              audioFormat.getSampleRate(),
                              16,
                              2,
                              4,
                              audioFormat.getFrameRate(),
                              audioFormat.isBigEndian()),
              lLength);
        waveformType = nWaveformType;
        if (DEBUG) { out("andts.javasynth.oscillator.Oscillator.<init>(): begin"); }
        m_lRemainingFrames = lLength;
        fAmplitude = (float) (fAmplitude * Math.pow(2, getFormat().getSampleSizeInBits() - 1));
        // length of one period in frames
        int nPeriodLengthInFrames = Math.round(getFormat().getFrameRate() / fSignalFrequency);
        int nBufferLength = nPeriodLengthInFrames * getFormat().getFrameSize();
        m_abData = new byte[nBufferLength];
        for (int nFrame = 0; nFrame < nPeriodLengthInFrames; nFrame++)
        {
            /**    The relative position inside the period
             of the waveform. 0.0 = beginning, 1.0 = end
             */
            float fPeriodPosition = (float) nFrame / (float) nPeriodLengthInFrames;
            float fValue = 0;
            switch (waveformType)
            {
                case WAVEFORM_SINE:
                    fValue = (float) Math.sin(fPeriodPosition * 2.0 * Math.PI);
                    break;

                case WAVEFORM_SQUARE:
                    fValue = (fPeriodPosition < 0.5F) ? 1.0F : -1.0F;
                    break;

                case WAVEFORM_TRIANGLE:
                    if (fPeriodPosition < 0.25F)
                    {
                        fValue = 4.0F * fPeriodPosition;
                    }
                    else if (fPeriodPosition < 0.75F)
                    {
                        fValue = -4.0F * (fPeriodPosition - 0.5F);
                    }
                    else
                    {
                        fValue = 4.0F * (fPeriodPosition - 1.0F);
                    }
                    break;

                case WAVEFORM_SAWTOOTH:
                    if (fPeriodPosition < 0.5F)
                    {
                        fValue = 2.0F * fPeriodPosition;
                    }
                    else
                    {
                        fValue = 2.0F * (fPeriodPosition - 1.0F);
                    }
                    break;
            }
            int nValue = Math.round(fValue * fAmplitude);
            int nBaseAddr = (nFrame) * getFormat().getFrameSize();
            // this is for 16 bit stereo, little endian
            m_abData[nBaseAddr] = 0;//(byte) (nValue & 0xFF);
            m_abData[nBaseAddr + 1] = (byte) ((nValue >>> 8) & 0xFF);
            m_abData[nBaseAddr + 2] = 0;//(byte) (nValue & 0xFF);
            m_abData[nBaseAddr + 3] = (byte) ((nValue >>> 8) & 0xFF);
        }
        m_nBufferPosition = 0;
        if (DEBUG) { out("andts.javasynth.oscillator.Oscillator.<init>(): end"); }
    }


    /**
     * Returns the number of bytes that can be read without blocking.
     * Since there is no blocking possible here, we simply try to
     * return the number of bytes available at all. In case the
     * length of the stream is indefinite, we return the highest
     * number that can be represented in an integer. If the length
     * if finite, this length is returned, clipped by the maximum
     * that can be represented.
     */
    public int available()
    {
        int nAvailable;
        if (m_lRemainingFrames == AudioSystem.NOT_SPECIFIED)
        {
            nAvailable = Integer.MAX_VALUE;
        }
        else
        {
            long lBytesAvailable = m_lRemainingFrames * getFormat().getFrameSize();
            nAvailable = (int) Math.min(lBytesAvailable, (long) Integer.MAX_VALUE);
        }
        return nAvailable;
    }


    /*
       this method should throw an IOException if the frame size is not 1.
       Since we currently always use 16 bit samples, the frame size is
       always greater than 1. So we always throw an exception.
     */
    public int read()
            throws IOException
    {
        if (DEBUG) { out("andts.javasynth.oscillator.Oscillator.read(): begin"); }
        throw new IOException("cannot use this method currently");
    }


    public int read(byte[] abData, int nOffset, int nLength)
            throws IOException
    {
        if (DEBUG) { out("andts.javasynth.oscillator.Oscillator.read(): begin"); }
        if (nLength % getFormat().getFrameSize() != 0)
        {
            throw new IOException("length must be an integer multiple of frame size");
        }
        int nConstrainedLength = Math.min(available(), nLength);
        int nRemainingLength = nConstrainedLength;
        while (nRemainingLength > 0)
        {
            int nNumBytesToCopyNow = m_abData.length - m_nBufferPosition;
            nNumBytesToCopyNow = Math.min(nNumBytesToCopyNow, nRemainingLength);
            System.arraycopy(m_abData, m_nBufferPosition, abData, nOffset, nNumBytesToCopyNow);
            nRemainingLength -= nNumBytesToCopyNow;
            nOffset += nNumBytesToCopyNow;
            m_nBufferPosition = (m_nBufferPosition + nNumBytesToCopyNow) % m_abData.length;
        }
        int nFramesRead = nConstrainedLength / getFormat().getFrameSize();
        if (m_lRemainingFrames != AudioSystem.NOT_SPECIFIED)
        {
            m_lRemainingFrames -= nFramesRead;
        }
        int nReturn = nConstrainedLength;
        if (m_lRemainingFrames == 0)
        {
            nReturn = -1;
        }
        if (DEBUG) { out("andts.javasynth.oscillator.Oscillator.read(): end"); }
        return nReturn;
    }


    private static void out(String strMessage)
    {
        System.out.println(strMessage);
    }
}


/*** andts.javasynth.oscillator.Oscillator.java ***/