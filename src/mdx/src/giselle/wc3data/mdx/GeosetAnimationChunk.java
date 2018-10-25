package giselle.wc3data.mdx;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import giselle.wc3data.stream.BlizzardDataInputStream;
import giselle.wc3data.stream.StreamUtils;

public class GeosetAnimationChunk
{
	public GeosetAnimation[] geosetAnimations = new GeosetAnimation[0];

	public static final String key = "GEOA";

	public void load(BlizzardDataInputStream in) throws IOException
	{
		StreamUtils.checkId(in, key);
		int chunkSize = in.readInt();
		List<GeosetAnimation> geosetAnimationList = new ArrayList<>();
		int geosetAnimationCounter = chunkSize;

		while (geosetAnimationCounter > 0)
		{
			GeosetAnimation tempgeosetAnimation = new GeosetAnimation();
			geosetAnimationList.add(tempgeosetAnimation);
			tempgeosetAnimation.load(in);
			geosetAnimationCounter -= tempgeosetAnimation.getSize();
		}

		this.geosetAnimations = geosetAnimationList.toArray(new GeosetAnimation[geosetAnimationList.size()]);
	}

	public void setTime(MdxModel model, int time)
	{
		for (int i = 0; i < this.geosetAnimations.length; i++)
		{
			this.geosetAnimations[i].setTime(model, time);
		}

	}

	public int getSize()
	{
		int a = 0;
		a += 4;
		a += 4;

		for (int i = 0; i < this.geosetAnimations.length; i++)
		{
			a += this.geosetAnimations[i].getSize();
		}

		return a;
	}

}
