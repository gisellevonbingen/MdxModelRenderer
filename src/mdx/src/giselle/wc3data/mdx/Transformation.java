package giselle.wc3data.mdx;

import java.io.IOException;

import giselle.wc3data.stream.BlizzardDataInputStream;
import giselle.wc3data.stream.StreamUtils;

public abstract class Transformation
{
	protected final String key;
	protected final int length;

	public Interpolation interpolationType;
	public int globalSequenceId;
	public TransformTrack[] tracks = null;

	public Transformation(String key, int length)
	{
		this.length = length;
		this.key = key;
	}

	public int getGlobalSequencedTime(MdxModel model, int time)
	{
		GlobalSequence globalSequence = model.getGlobalSequenceManager().get(this.globalSequenceId);

		if (globalSequence != null)
		{
			time = globalSequence.getTime();
		}
		return time;
	}

	public TransformTrack[] getInterval(int time)
	{
		TransformTrack[] tracks = this.tracks;
		int length = tracks.length;

		if (length == 1)
		{
			TransformTrack track = tracks[0];

			if (track.time == time)
			{
				return new TransformTrack[]{track, null};
			}

		}

		int i = 0;
		boolean found = false;
		TransformTrack start = null;
		TransformTrack end = null;

		while (i < length - 1 && !found)
		{
			start = tracks[i + 0];
			end = tracks[i + 1];
			++i;
			found = start.time <= time && time < end.time;
		}

		if (found)
		{
			return new TransformTrack[]{start, end};
		}
		else
		{
			return null;
		}

	}

	public void load(BlizzardDataInputStream in) throws IOException
	{
		StreamUtils.checkId(in, this.key);

		int nrOfTracks = in.readInt();
		interpolationType = Interpolation.values()[in.readInt()];
		globalSequenceId = in.readInt();
		this.tracks = new TransformTrack[nrOfTracks];

		for (int i = 0; i < nrOfTracks; i++)
		{
			this.tracks[i] = new TransformTrack(this, this.length);
			this.tracks[i].load(in);
		}

	}

	public int getSize()
	{
		int a = 0;
		a += 4;
		a += 4;
		a += 4;
		a += 4;

		for (int i = 0; i < this.tracks.length; i++)
		{
			a += this.tracks[i].getSize();
		}

		return a;
	}

	public int getLength()
	{
		return this.length;
	}

}
