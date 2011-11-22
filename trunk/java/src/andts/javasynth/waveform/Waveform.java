package andts.javasynth.waveform;

/**
 * Objects implementing this interface will hold information about some type of a wave.
 * They provide no means using this wave, only compute needed data, and provide interface to get it.
 */
public interface Waveform
{
    public float MIN_FREQUENCY = 0.1F; //lowest frequency -> longest period

    /**
     * Get resolution of this waveform in frames
     * @return max number of frames computed for one period of this wave
     */
    public int getSampleCount();

    /**
     * Get the value of this waveform for specific frame
     * @param sampleNumber number of the frame in range [0; frameCount)
     * @return value of the waveform in specified position
     */
    public float getSampleValue(int sampleNumber);

    /**
     * Get frame rate of this waveform
     * @return frame rate [frames/sec]
     */
    int getSampleRate();
}
