package giselle.wc3data.mdx;

import java.io.IOException;

import giselle.wc3data.stream.BlizzardDataInputStream;
import giselle.wc3data.stream.StreamUtils;

public class Light
{
	public Node node = new Node();
	public int type;
	public int attenuationStart;
	public int attenuationEnd;
	public float[] color = new float[3];
	public float intensity;
	public float[] ambientColor = new float[3];
	public float ambientIntensity;
	public LightVisibility lightVisibility;
	public LightColor lightColor;
	public LightIntensity lightIntensity;
	public LightAmbientColor lightAmbientColor;
	public LightAmbientIntensity lightAmbientIntensity;

	public void load(BlizzardDataInputStream in) throws IOException
	{
		@SuppressWarnings("unused")
		int inclusiveSize = in.readInt();
		node = new Node();
		node.load(in);
		type = in.readInt();
		attenuationStart = in.readInt();
		attenuationEnd = in.readInt();
		color = StreamUtils.loadFloatArray(in, 3);
		intensity = in.readFloat();
		ambientColor = StreamUtils.loadFloatArray(in, 3);
		ambientIntensity = in.readFloat();
		for (int i = 0; i < 5; i++)
		{
			if (StreamUtils.checkOptionalId(in, LightVisibility.key))
			{
				lightVisibility = new LightVisibility();
				lightVisibility.load(in);
			}
			else if (StreamUtils.checkOptionalId(in, LightColor.key))
			{
				lightColor = new LightColor();
				lightColor.load(in);
			}
			else if (StreamUtils.checkOptionalId(in, LightIntensity.key))
			{
				lightIntensity = new LightIntensity();
				lightIntensity.load(in);
			}
			else if (StreamUtils.checkOptionalId(in, LightAmbientColor.key))
			{
				lightAmbientColor = new LightAmbientColor();
				lightAmbientColor.load(in);
			}
			else if (StreamUtils.checkOptionalId(in, LightAmbientIntensity.key))
			{
				lightAmbientIntensity = new LightAmbientIntensity();
				lightAmbientIntensity.load(in);
			}

		}
	}

	public int getSize()
	{
		int a = 0;
		a += 4;
		a += node.getSize();
		a += 4;
		a += 4;
		a += 4;
		a += 12;
		a += 4;
		a += 12;
		a += 4;
		if (lightVisibility != null)
		{
			a += lightVisibility.getSize();
		}
		if (lightColor != null)
		{
			a += lightColor.getSize();
		}
		if (lightIntensity != null)
		{
			a += lightIntensity.getSize();
		}
		if (lightAmbientColor != null)
		{
			a += lightAmbientColor.getSize();
		}
		if (lightAmbientIntensity != null)
		{
			a += lightAmbientIntensity.getSize();
		}

		return a;
	}
}
