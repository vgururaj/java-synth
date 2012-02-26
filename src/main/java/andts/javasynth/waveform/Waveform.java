package andts.javasynth.waveform;

/**
 * Objects implementing this interface will hold information about some type of a wave.
 * They provide no means using this wave, only compute needed data.
 */
public interface Waveform
{
    /**
     * Get the value of this waveform in specific position of whole period
     * @param periodPosition specific position in period in range [0..1]
     * @return value of the waveform in specified position
     */
    public float getValue(float periodPosition);
}
