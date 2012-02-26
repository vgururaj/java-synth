package andts.javasynth.waveform;

public class SineWave implements Waveform
{
    @Override
    public float getValue(float periodPosition)
    {
        return (float) Math.sin(periodPosition * 2.0 * Math.PI);
    }
}
