package andts.javasynth.generator;

import andts.javasynth.JavaSynthException;

/**
 * Initial copyright: \n
 * Moog 24 dB/oct resonant lowpass VCF \n
 * References: CSound source code, Stilson/Smith CCRMA paper. \n
 * Modified by paul.kellett@maxim.abel.co.uk July 2000 \n
 */
public class MoogVcfFilter2
{

    // Moog 24 dB/oct resonant lowpass VCF
    // References: CSound source code, Stilson/Smith CCRMA paper.
    // Modified by paul.kellett@maxim.abel.co.uk July 2000

    private float f, p, q; //filter coefficients
    private float[] b; //filter buffers

    private float cutoffFrequency;
    private float baseCutoffFrequency;
    private LfoGenerator cutoffLfo;
    private float resonance;
    private FilterMode mode;

    public MoogVcfFilter2(float cutoffFrequency, float resonance, FilterMode mode, LfoGenerator lfo)
    {
        this.cutoffFrequency = cutoffFrequency;
        this.baseCutoffFrequency = cutoffFrequency;
        this.resonance = resonance;
        this.mode = mode;
        this.cutoffLfo = lfo;

        calculateCoefficients();

        b = new float[]{0F, 0F, 0F, 0F, 0F};
    }

    /**
     * Set coefficients by given frequency & resonance in range [0.0...1.0]
     */
    private void calculateCoefficients()
    {
        q = 1.0f - cutoffFrequency;
        p = cutoffFrequency + 0.8f * cutoffFrequency * q;
        f = p + p - 1.0f;
        q = resonance * (1.0f + 0.5f * q * (1.0f - q + 5.6f * q * q));
    }

    /**
     *
     * @param input in range [-1.0...+1.0]
     * @return filtered output
     */
    public float filter(float input)
    {
        float newCutoffFreq = baseCutoffFrequency * (1 + cutoffLfo.getNextValue());
        setCutoffFrequency(newCutoffFreq);

        input -= q * b[4];                          //feedback
        float t1 = b[1];
        b[1] = (input + b[0]) * p - b[1] * f;
        float t2 = b[2];
        b[2] = (b[1] + t1) * p - b[2] * f;
        t1 = b[3];
        b[3] = (b[2] + t2) * p - b[3] * f;
        b[4] = (b[3] + t1) * p - b[4] * f;
        b[4] = b[4] - b[4] * b[4] * b[4] * 0.166667f;    //clipping
        b[0] = input;

        float result = 0F;
        switch (mode)
        {
            case LOWPASS:
                result = b[4];
                break;
            case HIGHPASS:
                result = input - b[4];
                break;
            case BANDPASS:
                result = 3.0F * (b[3] - b[4]);
                break;
            default:
                throw new JavaSynthException("Unknown mode set for filter!");
        }

        return result;
    }

    public float getCutoffFrequency()
    {
        return cutoffFrequency;
    }

    public void setCutoffFrequency(float cutoffFrequency)
    {
        this.cutoffFrequency = cutoffFrequency;
        calculateCoefficients();
    }

    public float getResonance()
    {
        return resonance;
    }

    public void setResonance(float resonance)
    {
        this.resonance = resonance;
    }

    public FilterMode getMode()
    {
        return mode;
    }

    public void setMode(FilterMode mode)
    {
        this.mode = mode;
    }

    public static enum FilterMode
    {
        LOWPASS, HIGHPASS, BANDPASS
    }
}
