package andts.javasynth.generator;

public enum SoundGeneratorState
{
    /**
     * When generator receives a play event.
     */
    RUNNING,
    /**
     * When generator receives a stop event.
     */
    STOPPING,
    /**
     * When generator produces no sound. Before first play event and after all sounds ended after stop event.
     */
    SILENT,

    IDLE
}
