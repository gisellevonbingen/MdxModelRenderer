package giselle.wc3data.mdx;

import java.io.IOException;

import giselle.wc3data.stream.BlizzardDataInputStream;
import giselle.wc3data.stream.BlizzardDataOutputStream;

public class Texture
{
	public int replaceableId;
	public String fileName = "";
	public int unknownNull;
	public int flags;

	public void load(BlizzardDataInputStream in) throws IOException
	{
		replaceableId = in.readInt();
		fileName = in.readCharsAsString(256);
		unknownNull = in.readInt();
		flags = in.readInt();
	}

	public void save(BlizzardDataOutputStream out) throws IOException
	{
		out.writeInt(replaceableId);
		out.writeNByteString(fileName, 256);
		out.writeInt(unknownNull);
		out.writeInt(flags);
	}

	public int getSize()
	{
		int a = 0;
		a += 4;
		a += 256;
		a += 4;
		a += 4;

		return a;
	}
}
