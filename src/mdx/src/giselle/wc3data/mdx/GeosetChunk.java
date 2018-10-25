package giselle.wc3data.mdx;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import giselle.wc3data.stream.BlizzardDataInputStream;
import giselle.wc3data.stream.StreamUtils;

public class GeosetChunk
{
	public Geoset[] geosets = new Geoset[0];

	public static final String key = "GEOS";

	public void load(BlizzardDataInputStream in) throws IOException
	{
		StreamUtils.checkId(in, key);
		int chunkSize = in.readInt();
		List<Geoset> geosetList = new ArrayList<>();
		int geosetCounter = chunkSize;

		while (geosetCounter > 0)
		{
			Geoset tempgeoset = new Geoset();
			geosetList.add(tempgeoset);
			tempgeoset.load(in);
			int size = tempgeoset.getSize();
			geosetCounter -= size;
		}

		geosets = geosetList.toArray(new Geoset[geosetList.size()]);
	}

	public int getSize()
	{
		int a = 0;
		a += 4;
		a += 4;
		for (int i = 0; i < geosets.length; i++)
		{
			a += geosets[i].getSize();
		}

		return a;
	}

}
