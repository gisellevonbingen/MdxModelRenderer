package giselle.wc3data.mdx;

import java.io.IOException;

import giselle.wc3data.stream.BlizzardDataInputStream;
import giselle.wc3data.stream.StreamUtils;

public class EventObject
{
	public Node node = new Node();
	public Tracks tracks;

	public void load(BlizzardDataInputStream in) throws IOException
	{
		node = new Node();
		node.load(in);
		if (StreamUtils.checkOptionalId(in, Tracks.key))
		{
			tracks = new Tracks();
			tracks.load(in);
		}

	}

	public int getSize()
	{
		int a = 0;
		a += node.getSize();
		if (tracks != null)
		{
			a += tracks.getSize();
		}

		return a;
	}
}
