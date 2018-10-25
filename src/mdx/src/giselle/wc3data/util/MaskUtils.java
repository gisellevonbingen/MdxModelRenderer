package giselle.wc3data.util;

public class MaskUtils
{
	public static boolean testMask(int value, int mask)
	{
		return (value == mask) || (mask != 0 && (value & mask) == mask);
	}
	
}
