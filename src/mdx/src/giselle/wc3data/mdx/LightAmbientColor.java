package giselle.wc3data.mdx;

import java.io.IOException;

import giselle.wc3data.stream.BlizzardDataInputStream;
import giselle.wc3data.stream.StreamUtils;

public class LightAmbientColor
{
	public int interpolationType;
	public int globalSequenceId;
	public ScalingTrack[] scalingTrack = new ScalingTrack[0];

	public static final String key = "KLBC";

	public void load(BlizzardDataInputStream in) throws IOException
	{
		StreamUtils.checkId(in, "KLBC");
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
		public float[] ambientColor = new float[3];
		public float[] inTan = new float[3];
		public float[] outTan = new float[3];

		public void load(BlizzardDataInputStream in) throws IOException
		{
			time = in.readInt();
			ambientColor = StreamUtils.loadFloatArray(in, 3);
			if (interpolationType > 1)
			{
				inTan = StreamUtils.loadFloatArray(in, 3);
				outTan = StreamUtils.loadFloatArray(in, 3);
			}
		}

		public int getSize()
		{
			int a = 0;
			a += 4;
			a += 12;
			if (interpolationType > 1)
			{
				a += 12;
				a += 12;
			}

			return a;
		}
	}
}
