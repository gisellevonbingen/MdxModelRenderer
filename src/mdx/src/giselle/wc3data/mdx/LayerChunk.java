package giselle.wc3data.mdx;

import java.io.IOException;

import giselle.wc3data.stream.BlizzardDataInputStream;
import giselle.wc3data.stream.StreamUtils;

public class LayerChunk
{
	public Layer[] layers = new Layer[0];

	public static final String key = "LAYS";

	public void load(BlizzardDataInputStream in) throws IOException
	{
		StreamUtils.checkId(in, key);
		int nrOfLayers = in.readInt();
		layers = new Layer[nrOfLayers];

		for (int i = 0; i < nrOfLayers; i++)
		{
			layers[i] = new Layer();
			layers[i].load(in);
		}

	}

	public void setTime(MdxModel model, int time)
	{
		for (int i = 0; i < this.layers.length; i ++)
		{
			this.layers[i].setTime(model, time);
		}
		
	}

	public int getSize()
	{
		int a = 0;
		a += 4;
		a += 4;
		for (int i = 0; i < layers.length; i++)
		{
			a += layers[i].getSize();
		}

		return a;
	}

}
