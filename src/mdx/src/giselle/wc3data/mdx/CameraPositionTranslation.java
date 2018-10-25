package giselle.wc3data.mdx;

import java.io.IOException;

import giselle.wc3data.stream.BlizzardDataInputStream;
import giselle.wc3data.stream.StreamUtils;

public class CameraPositionTranslation
{
	public int interpolationType;
	public int globalSequenceId;
	public TranslationTrack[] translationTrack = new TranslationTrack[0];

	public static final String key = "KCTR";

	public void load(BlizzardDataInputStream in) throws IOException
	{
		StreamUtils.checkId(in, "KCTR");
		int nrOfTracks = in.readInt();
		interpolationType = in.readInt();
		globalSequenceId = in.readInt();
		translationTrack = new TranslationTrack[nrOfTracks];
		for (int i = 0; i < nrOfTracks; i++)
		{
			translationTrack[i] = new TranslationTrack();
			translationTrack[i].load(in);
		}
	}

	public int getSize()
	{
		int a = 0;
		a += 4;
		a += 4;
		a += 4;
		a += 4;
		for (int i = 0; i < translationTrack.length; i++)
		{
			a += translationTrack[i].getSize();
		}

		return a;
	}

	public class TranslationTrack
	{
		public int time;
		public float[] translation = new float[3];
		public float[] inTan = new float[3];
		public float[] outTan = new float[3];

		public void load(BlizzardDataInputStream in) throws IOException
		{
			time = in.readInt();
			translation = StreamUtils.loadFloatArray(in, 3);
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
