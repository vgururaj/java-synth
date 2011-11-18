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
    public int getFrameCount();

    /**
     * Get the value of this waveform for specific frame
     * @param frameNumber number of the frame to get the value for
     * @return value of the waveform in specified position
     */
    public double getFrameValue(int frameNumber);

    int getFrameRate();
}
