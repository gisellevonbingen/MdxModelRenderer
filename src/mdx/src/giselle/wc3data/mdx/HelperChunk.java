package giselle.wc3data.mdx;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import giselle.wc3data.stream.BlizzardDataInputStream;
import giselle.wc3data.stream.StreamUtils;

public class HelperChunk
{
	public Helper[] helpers = new Helper[0];

	public static final String key = "HELP";

	public void load(BlizzardDataInputStream in) throws IOException
	{
		StreamUtils.checkId(in, key);
		int chunkSize = in.readInt();
		List<Helper> helperList = new ArrayList<>();
		int helperCounter = chunkSize;
		while (helperCounter > 0)
		{
			Helper temphelper = new Helper();
			helperList.add(temphelper);
			temphelper.load(in);
			helperCounter -= temphelper.getSize();
		}
		helpers = helperList.toArray(new Helper[helperList.size()]);
	}

	public int getSize()
	{
		int a = 0;
		a += 4;
		a += 4;
		for (int i = 0; i < helpers.length; i++)
		{
			a += helpers[i].getSize();
		}

		return a;
	}

}
