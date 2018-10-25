package giselle.wc3data.mdx;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import giselle.wc3data.stream.BlizzardDataInputStream;
import giselle.wc3data.stream.StreamUtils;

public class SequenceChunk
{
	public Sequence[] sequences = new Sequence[0];

	public static final String key = "SEQS";

	public void load(BlizzardDataInputStream in) throws IOException
	{
		StreamUtils.checkId(in, key);

		int chunkSize = in.readInt();
		List<Sequence> sequenceList = new ArrayList<>();
		int sequenceCounter = chunkSize;

		while (sequenceCounter > 0)
		{
			Sequence tempsequence = new Sequence();
			sequenceList.add(tempsequence);
			tempsequence.load(in);
			sequenceCounter -= tempsequence.getSize();
		}

		sequences = sequenceList.toArray(new Sequence[sequenceList.size()]);
	}

	public int getSize()
	{
		int a = 0;
		a += 4;
		a += 4;
		for (int i = 0; i < sequences.length; i++)
		{
			a += sequences[i].getSize();
		}

		return a;
	}

}
