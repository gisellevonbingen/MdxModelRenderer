package giselle.wc3data.mdx;

import java.io.IOException;

import giselle.wc3data.stream.BlizzardDataInputStream;
import giselle.wc3data.stream.StreamUtils;

public class LightAmbientIntensity
{
	public int interpolationType;
	public int globalSequenceId;
	public ScalingTrack[] scalingTrack = new ScalingTrack[0];

	public static final String key = "KLBI";

	public void load(BlizzardDataInputStream in) throws IOException
	{
		StreamUtils.checkId(in, "KLBI");
		int nrOfTracks = in.readInt();
		interpolationType = in.readInt();
		globalSequenceId = in.readInt();
		scalingTrack = new ScalingTrack[nrOfTracks];
		for (int i = 0; i < nrOfTracks; i++)
		{
			scalingTrack[i] = new ScalingTrack();
			scalingTrack[i].load(in);
		}
	}

	public int getSize()
	{
		int a = 0;
		a += 4;
		a += 4;
		a += 4;
		a += 4;
		for (int i = 0; i < scalingTrack.length; i++)
		{
			a += scalingTrack[i].getSize();
		}

		return a;
	}

	public class ScalingTrack
	{
		public int time;
		public float ambientIntensity;
		public float inTan;
		public float outTan;

		public void load(BlizzardDataInputStream in) throws IOException
		{
			time = in.readInt();
			ambientIntensity = in.readFloat();
			if (interpolationType > 1)
			{
				inTan = in.readFloat();
				outTan = in.readFloat();
			}
		}

		public int getSize()
		{
			int a = 0;
			a += 4;
			a += 4;
			if (interpolationType > 1)
			{
				a += 4;
				a += 4;
			}

			return a;
		}
	}
}
