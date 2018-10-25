package giselle.wc3data.mdx;

public class GlobalSequenceManager
{
	private GlobalSequence[] globalSequences = null;

	public GlobalSequenceManager()
	{
		this.globalSequences = new GlobalSequence[0];
	}

	public void build(GlobalSequenceChunk chunk)
	{
		int[] durations = null;

		if (chunk == null)
		{
			durations = new int[0];
		}
		else
		{
			durations = chunk.globalSequences;
		}

		this.globalSequences = new GlobalSequence[durations.length];

		for (int i = 0; i < this.globalSequences.length; i++)
		{
			int duration = durations[i];
			this.globalSequences[i] = new GlobalSequence(duration);
		}

	}

	public GlobalSequence[] getGlobalSequences()
	{
		return globalSequences;
	}

	public GlobalSequence get(int id)
	{
		if (-1 < id && id < this.globalSequences.length)
		{
			return this.globalSequences[id];
		}
		else
		{
			return null;
		}

	}

}
