package giselle.wc3data.mdx;

import java.io.IOException;

import javax.vecmath.Vector3f;

import giselle.wc3data.stream.BlizzardDataInputStream;
import giselle.wc3data.stream.StreamUtils;

public class PivotPointChunk
{
	public Vector3f[] pivotPoints = new Vector3f[0];

	public static final String key = "PIVT";

	public void load(BlizzardDataInputStream in) throws IOException
	{
		StreamUtils.checkId(in, key);
		int chunkSize = in.readInt();
		pivotPoints = StreamUtils.loadVector3fArray(in, chunkSize / 4 / 3);
	}

	public int getSize()
	{
		int a = 0;
		a += 4;
		a += 4;
		a += 4 * pivotPoints.length * 3;

		return a;
	}
}
