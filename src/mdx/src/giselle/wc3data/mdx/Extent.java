package giselle.wc3data.mdx;

import java.io.IOException;

import javax.vecmath.Vector3f;

import giselle.wc3data.stream.BlizzardDataInputStream;
import giselle.wc3data.stream.BlizzardDataOutputStream;
import giselle.wc3data.stream.StreamUtils;

public class Extent
{
	public Vector3f minimumExtent = null;
	public Vector3f maximumExtent = null;
	public float bounds;

	public Extent()
	{

	}

	public void load(BlizzardDataInputStream in) throws IOException
	{
		bounds = in.readFloat();
		minimumExtent = StreamUtils.loadVector3f(in);
		maximumExtent = StreamUtils.loadVector3f(in);
	}

	public void save(BlizzardDataOutputStream out) throws IOException
	{
		StreamUtils.saveVector3f(out, minimumExtent);
		StreamUtils.saveVector3f(out, maximumExtent);
		out.writeFloat(bounds);

	}

	public int getSize()
	{
		int a = 0;
		a += 12;
		a += 12;
		a += 4;

		return a;
	}

}
