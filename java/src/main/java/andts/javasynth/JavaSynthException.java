package andts.javasynth;

public class JavaSynthException extends RuntimeException
{
    public JavaSynthException(String message)
    {
        super(message);
    }

    public JavaSynthException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
