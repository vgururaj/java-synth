package andts.javasynth.generator;

/**
 * User: Andrew
 * Date: 23.11.11
 * Time: 22:39
 */
public class MoogVcfFilter
{
    private float cutoffFrequency;
    private float baseCutoffFrequency;
    private float resonance;
    private double f;
    private double fb;
    private double[] out;
    private double[] in;

    private LfoGenerator cutoffLfo;

    public MoogVcfFilter(float cutoffFrequency, float resonance, LfoGenerator cutoffLfo)
    {
        this.cutoffFrequency = cutoffFrequency;
        this.baseCutoffFrequency = cutoffFrequency;
        this.resonance = resonance;
        f = cutoffFrequency * 1.16;
        fb = resonance * (1.0 - 0.15 * f * f);
        out = new double[]{0, 0, 0, 0};
        in = new double[]{0, 0, 0, 0};

        this.cutoffLfo = cutoffLfo;
    }

    public double filter(float input)
    {
        float newCutoffFreq = baseCutoffFrequency * (1 + cutoffLfo.getNextValue());
        setCutoffFrequency(newCutoffFreq);
        input -= out[3] * fb;
        input *= 0.35013 * (f * f) * (f * f);
        out[0] = input + 0.3 * in[0] + (1 - f) * out[0]; // Pole 1
        in[0] = input;
        out[1] = out[0] + 0.3 * in[1] + (1 - f) * out[1];  // Pole 2
        in[1] = out[0];
        out[2] = out[1] + 0.3 * in[2] + (1 - f) * out[2];  // Pole 3
        in[2] = out[1];
        out[3] = out[2] + 0.3 * in[3] + (1 - f) * out[3];  // Pole 4
        in[3] = out[2];
        return out[3];
    }

    public float getCutoffFrequency()
    {
        return cutoffFrequency;
    }

    public void setCutoffFrequency(float cutoffFrequency)
    {
        this.cutoffFrequency = cutoffFrequency;
        f = cutoffFrequency * 1.16;
    }

    public float getResonance()
    {
        return resonance;
    }

    public void setResonance(float resonance)
    {
        this.resonance = resonance;
        fb = resonance * (1.0 - 0.15 * f * f);
    }
}
