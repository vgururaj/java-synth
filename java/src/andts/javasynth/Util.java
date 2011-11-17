package andts.javasynth;

public class Util
{
    public static byte[] trimInt(int value)
    {
        return new byte[]{(byte) (value & 0xFF), (byte) ((value >>> 8) & 0xFF)};
    }
}
