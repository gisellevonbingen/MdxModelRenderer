package giselle.wc3data.mdx;

import java.io.IOException;

import giselle.wc3data.stream.BlizzardDataInputStream;
import giselle.wc3data.stream.StreamUtils;

public class Material
{
	public int priorityPlane;
	public int flags;
	public LayerChunk layerChunk;

	public void load(BlizzardDataInputStream in) throws IOException
	{
		@SuppressWarnings("unused")
		int inclusiveSize = in.readInt();
		priorityPlane = in.readInt();
		flags = in.readInt();

		if (StreamUtils.checkOptionalId(in, LayerChunk.key))
		{
			layerChunk = new LayerChunk();
			layerChunk.load(in);
		}

	}

	public void setTime(MdxModel model, int time)
	{
		if (this.layerChunk != null)
		{
			this.layerChunk.setTime(model, time);
		}

	}

	public int getSize()
	{
		int a = 0;
		a += 4;
		a += 4;
		a += 4;

		if (layerChunk != null)
		{
			a += layerChunk.getSize();
		}

		return a;
	}

}
