package giselle.wc3data.mdx;

import java.io.IOException;

import giselle.wc3data.stream.BlizzardDataInputStream;
import giselle.wc3data.stream.StreamUtils;

public class TransformTrack
{
	private final Transformation parent;
	private final int length;

	public int time;
	public float[] parameter = null;
	public float[] inTan = null;
	public float[] outTan = null;

	public TransformTrack(Transformation parent, int length)
	{
		this.parent = parent;
		this.length = length;
	}

	public void load(BlizzardDataInputStream in) throws IOException
	{
		int length = this.length;

		time = in.readInt();
		parameter = StreamUtils.loadFloatArray(in, length);

		if (parent.interpolationType.isUseTan())
		{
			inTan = StreamUtils.loadFloatArray(in, length);
			outTan = StreamUtils.loadFloatArray(in, length);
		}

	}

	public int getSize()
	{
		int length = this.length;

		int a = 0;
		a += 4;
		a += 4 * length;

		if (parent.interpolationType.isUseTan())
		{
			a += 4 * length;
			a += 4 * length;
		}

		return a;
	}

	public int getLength()
	{
		return this.length;
	}

}
