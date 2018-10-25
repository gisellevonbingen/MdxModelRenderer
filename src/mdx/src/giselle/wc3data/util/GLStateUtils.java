package giselle.wc3data.util;

import org.lwjgl.opengl.GL11;

public class GLStateUtils
{
	public static void set(int cap, boolean enable)
	{
		if (enable == true)
		{
			GL11.glEnable(cap);
		}
		else
		{
			GL11.glDisable(cap);
		}

	}

	private GLStateUtils()
	{

	}

}
