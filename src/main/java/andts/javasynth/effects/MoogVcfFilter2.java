package andts.javasynth.effects;

import andts.javasynth.JavaSynthException;
import andts.javasynth.parameter.Parameter;

/**
 * Initial &copy;:<br />
 * Moog 24 dB/oct resonant lowpass VCF <br />
 * References: CSound source code, Stilson/Smith CCRMA paper. <br />
 * Modified by paul.kellett@maxim.abel.co.uk July 2000 <br />
 */
public class MoogVcfFilter2
{
    private float f, p, q; //filter coefficients
    private float[] b; //filter buffers

    private Parameter<Float> cutoffFrequency;
    private float currentCutoffFreq;

    private Parameter<Float> resonance;
    private float currentResonance;

    private FilterMode mode;

    public MoogVcfFilter2(Parameter<Float> cutoffFrequency, Parameter<Float> resonance, FilterMode mode)
    {
        this.cutoffFrequency = cutoffFrequency;
        this.resonance = resonance;
        this.mode = mode;
        currentCutoffFreq = cutoffFrequency.getValue();
        currentResonance = resonance.getValue();

        calculateCoefficients();

        b = new float[]{0F, 0F, 0F, 0F, 0F};
    }

    /**
     * Set coefficients by given frequency & resonance in range [0.0...1.0]
     */
    private void calculateCoefficients()
    {
        //It's a kind of magiic... (c) Queen
        q = 1.0f - currentCutoffFreq;
        p = currentCutoffFreq + 0.8f * currentCutoffFreq * q;
        f = p + p - 1.0f;
        q = currentResonance * (1.0f + 0.5f * q * (1.0f - q + 5.6f * q * q));
    }

    /**
     *
     * @param input in range [-1.0...+1.0]
     * @return filtered output
     */
    public float filter(float input)
    {
        currentCutoffFreq = cutoffFrequency.getValue();
        currentResonance = resonance.getValue();
        calculateCoefficients();

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

        float result;
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
        return currentCutoffFreq;
    }

    public void setCutoffFrequency(float cutoffFrequency)
    {
        this.cutoffFrequency.setValue(cutoffFrequency);
        currentCutoffFreq = this.cutoffFrequency.getValue();
        calculateCoefficients();
    }

    public float getResonance()
    {
        return currentResonance;
    }

    public void setResonance(float resonance)
    {
        this.resonance.setValue(resonance);
        currentResonance = this.resonance.getValue();
        calculateCoefficients();
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
