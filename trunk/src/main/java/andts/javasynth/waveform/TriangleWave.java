package andts.javasynth.waveform;

public class TriangleWave implements Waveform
{
    @Override
    public float getValue(float periodPosition)
    {
        float triangleValue;

        if (periodPosition < 0.25F)
        {
            triangleValue = 4.0F * periodPosition;
        }
        else if (periodPosition < 0.75F)
        {
            triangleValue = -4.0F * (periodPosition - 0.5F);
        }
        else
        {
            triangleValue = 4.0F * (periodPosition - 1.0F);
        }

        return triangleValue;
    }
}
