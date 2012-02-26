package andts.javasynth.waveform;

public class SawtoothWave implements Waveform
{
    @Override
    public float getValue(float periodPosition)
    {
        float sawtoothValue;

        if (periodPosition < 0.5F)
        {
            sawtoothValue = 2.0F * periodPosition;
        }
        else
        {
            sawtoothValue = 2.0F * (periodPosition - 1.0F);
        }

        return sawtoothValue;
    }
}
