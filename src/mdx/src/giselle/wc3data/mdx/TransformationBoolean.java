package giselle.wc3data.mdx;

public class TransformationBoolean extends Transformation
{
	public TransformationBoolean(String key, int length)
	{
		super(key, length);
	}

	public boolean getInterpolationResult(MdxModel model, int time)
	{
		time = this.getGlobalSequencedTime(model, time);

		TransformTrack[] interval = this.getInterval(time);

		if (interval == null)
		{
			return false;
		}

		TransformTrack start = interval[0];
		return start.parameter[0] > 0.0F;
	}

}
