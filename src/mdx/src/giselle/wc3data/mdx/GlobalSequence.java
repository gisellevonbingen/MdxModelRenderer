package giselle.wc3data.mdx;

public class GlobalSequence
{
	private SequenceTimer sequenceTimer;

	public GlobalSequence(int duration)
	{
		this.sequenceTimer = new SequenceTimer(0, duration);
	}

	public void setTime(int time)
	{
		this.sequenceTimer.setTime(time);
	}

	public void setTimeRepeat(int time)
	{
		int end = this.sequenceTimer.getEnd();

		if (end == 0)
		{
			this.setTime(0);

		}
		else
		{
			time = time % end;
			this.setTime(time);
		}

	}

	public int getTime()
	{
		return this.sequenceTimer.getTime();
	}

}
