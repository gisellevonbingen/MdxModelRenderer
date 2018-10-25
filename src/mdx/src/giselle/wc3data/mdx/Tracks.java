package giselle.wc3data.mdx;

import java.io.IOException;

import giselle.wc3data.stream.BlizzardDataInputStream;
import giselle.wc3data.stream.StreamUtils;

public class Tracks
{
	public int globalSequenceId;
	public int[] tracks = new int[0];

	public static final String key = "KEVT";

	public void load(BlizzardDataInputStream in) throws IOException
	{
		StreamUtils.checkId(in, "KEVT");
		int nrOfTracks = in.readInt();
		globalSequenceId = in.readInt();
		tracks = StreamUtils.loadIntArray(in, nrOfTracks);
	}

	public int getSize()
	{
		int a = 0;
		a += 4;
		a += 4;
		a += 4;
		a += 4 * tracks.length;

		return a;
	}
}
