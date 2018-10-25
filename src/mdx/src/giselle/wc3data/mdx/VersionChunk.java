package giselle.wc3data.mdx;

import java.io.IOException;

import giselle.wc3data.stream.BlizzardDataInputStream;
import giselle.wc3data.stream.StreamUtils;

public class VersionChunk
{
	private int version;

	public static final String key = "VERS";

	public void load(BlizzardDataInputStream in) throws IOException
	{
		StreamUtils.checkId(in, key);
		@SuppressWarnings("unused")
		int chunkSize = in.readInt();
		this.setVersion(in.readInt());
	}

	public int getSize()
	{
		int a = 0;
		a += 4;
		a += 4;
		a += 4;

		return a;
	}

	public int getVersion()
	{
		return this.version;
	}

	public void setVersion(int version)
	{
		this.version = version;
	}

}
