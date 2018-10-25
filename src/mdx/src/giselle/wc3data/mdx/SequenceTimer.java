package giselle.wc3data.mdx;

public class SequenceTimer
{
	private int start;
	private int end;
	private int time;

	public SequenceTimer(int start, int end)
	{
		this.start = start;
		this.end = end;
		this.time = start;
	}

	public void update(int dt)
	{
		this.setTime(this.time + dt);
	}

	public int getDuration()
	{
		return Math.max(1, this.end - this.start);
	}

	public int getTime()
	{
		return time;
	}

	public void setTime(int time)
	{
		this.time = time;

		if (this.time > this.end)
		{
			this.time = this.end;
		}

	}

	public int getStart()
	{
		return start;
	}

	public void setStart(int start)
	{
		this.start = start;
	}

	public int getEnd()
	{
		return end;
	}

	public void setEnd(int end)
	{
		this.end = end;
	}

}
