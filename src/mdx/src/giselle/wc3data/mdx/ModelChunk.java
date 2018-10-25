package giselle.wc3data.mdx;

import java.io.IOException;

import javax.vecmath.Vector3f;

import giselle.wc3data.stream.BlizzardDataInputStream;
import giselle.wc3data.stream.StreamUtils;

public class ModelChunk
{
	public String name = "";
	public int unknownNull;
	public float boundsRadius;
	public Vector3f minimumExtent = null;
	public Vector3f maximumExtent = null;
	public int blendTime;

	public static final String key = "MODL";

	public void load(BlizzardDataInputStream in) throws IOException
	{
		StreamUtils.checkId(in, key);
		@SuppressWarnings("unused")
		int chunkSize = in.readInt();
		name = in.readCharsAsString(336);
		unknownNull = in.readInt();
		boundsRadius = in.readFloat();

		minimumExtent = StreamUtils.loadVector3f(in);
		maximumExtent = StreamUtils.loadVector3f(in);

		blendTime = in.readInt();
	}

	public int getSize()
	{
		int a = 0;
		a += 4;
		a += 4;
		a += 336;
		a += 4;
		a += 4;
		a += 12;
		a += 12;
		a += 4;

		return a;
	}

}
