package giselle.wc3data.mdx;

import java.io.IOException;

import giselle.wc3data.stream.BlizzardDataInputStream;
import giselle.wc3data.stream.StreamUtils;

public class CollisionShape
{
	public Node node = new Node();
	public int type;
	public float[] vertexs = new float[0];
	public float boundsRadius;

	public void load(BlizzardDataInputStream in) throws IOException
	{
		node = new Node();
		node.load(in);
		type = in.readInt();
		vertexs = StreamUtils.loadFloatArray(in, (2 - type / 2) * 3);
		if (type == 2)
		{
			boundsRadius = in.readFloat();
		}
	}

	public int getSize()
	{
		int a = 0;
		a += node.getSize();
		a += 4;
		a += (2 - type / 2) * 12;
		if (type == 2)
		{
			a += 4;
		}

		return a;
	}
}
