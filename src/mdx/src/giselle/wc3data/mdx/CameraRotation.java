package giselle.wc3data.mdx;

import java.io.IOException;

import giselle.wc3data.stream.BlizzardDataInputStream;
import giselle.wc3data.stream.StreamUtils;

public class CameraRotation
{
	public int interpolationType;
	public int globalSequenceId;
	public TranslationTrack[] translationTrack = new TranslationTrack[0];

	public static final String key = "KCRL";

	public void load(BlizzardDataInputStream in) throws IOException
	{
		StreamUtils.checkId(in, "KCRL");
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
		public float rotation;
		public float inTan;
		public float outTan;

		public void load(BlizzardDataInputStream in) throws IOException
		{
			time = in.readInt();
			rotation = in.readFloat();
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
