package andts.javasynth;

public class Util
{
    public static byte[] chop(int value)
    {
        return new byte[]{(byte) (value & 0xFF), (byte) ((value >>> 8) & 0xFF)};
    }
}
