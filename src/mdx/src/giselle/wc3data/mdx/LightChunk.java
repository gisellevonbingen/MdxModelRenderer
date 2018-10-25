package giselle.wc3data.mdx;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import giselle.wc3data.stream.BlizzardDataInputStream;
import giselle.wc3data.stream.StreamUtils;

public class LightChunk
{
	public Light[] lights = new Light[0];

	public static final String key = "LITE";

	public void load(BlizzardDataInputStream in) throws IOException
	{
		StreamUtils.checkId(in, key);
		int chunkSize = in.readInt();
		List<Light> lightList = new ArrayList<>();
		int lightCounter = chunkSize;
		while (lightCounter > 0)
		{
			Light templight = new Light();
			lightList.add(templight);
			templight.load(in);
			lightCounter -= templight.getSize();
		}
		lights = lightList.toArray(new Light[lightList.size()]);
	}

	public int getSize()
	{
		int a = 0;
		a += 4;
		a += 4;
		for (int i = 0; i < lights.length; i++)
		{
			a += lights[i].getSize();
		}

		return a;
	}

}
