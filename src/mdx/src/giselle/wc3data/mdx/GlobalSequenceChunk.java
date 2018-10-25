package giselle.wc3data.mdx;

import java.io.IOException;

import giselle.wc3data.stream.BlizzardDataInputStream;
import giselle.wc3data.stream.StreamUtils;

public class GlobalSequenceChunk
{
	public int[] globalSequences = new int[0];

	public static final String key = "GLBS";

	public void load(BlizzardDataInputStream in) throws IOException
	{
		StreamUtils.checkId(in, key);
		int chunkSize = in.readInt();
		globalSequences = StreamUtils.loadIntArray(in, chunkSize / 4);
	}

	public int getSize()
	{
		int a = 0;
		a += 4;
		a += 4;
		a += 4 * globalSequences.length;

		return a;
	}
}
