package giselle.wc3data.mdx;

import javax.vecmath.Quat4f;

public class TransformationFloat extends Transformation
{
	public TransformationFloat(String key, int length)
	{
		super(key, length);
	}

	public float[] getInterpolationResult(MdxModel model, int time)
	{
		time = this.getGlobalSequencedTime(model, time);

		TransformTrack[] interval = this.getInterval(time);

		if (interval == null)
		{
			return null;
		}

		TransformTrack start = interval[0];
		TransformTrack end = interval[1];

		switch (this.interpolationType)
		{
			case NONE:
				return start.parameter;
			case LINEAR:
				return interpolateLinear(start, end, time);
			case HERMITE:
				return interpolateLinear(start, end, time); // TODO
			case BEZIER:
				return interpolateLinear(start, end, time); // TODO
		}

		return null;
	}

	public float[] getQuaternionInterpolationResult(MdxModel model, int time)
	{
		time = this.getGlobalSequencedTime(model, time);

		TransformTrack[] interval = this.getInterval(time);

		if (interval == null)
		{
			return null;
		}

		TransformTrack start = interval[0];
		TransformTrack end = interval[1];

		switch (this.interpolationType)
		{
			case NONE:
				return start.parameter;
			case LINEAR:
				return interpolateQuaternionLinear(start, end, time);
			case HERMITE:
				return interpolateQuaternionLinear(start, end, time); // TODO
			case BEZIER:
				return interpolateQuaternionLinear(start, end, time); // TODO
		}
		return null;
	}

	private static float[] interpolateLinear(TransformTrack start, TransformTrack end, float time)
	{
		if (end == null)
		{
			return start.parameter;
		}
		else
		{
			float k = (time - start.time) / (end.time - start.time);
			float[] result = start.parameter.clone();

			for (int i = 0; i < result.length; ++i)
			{
				result[i] += k * (end.parameter[i] - start.parameter[i]);
			}

			return result;
		}

	}

	private static float[] interpolateQuaternionLinear(TransformTrack start, TransformTrack end, float time)
	{
		if (end == null)
		{
			return start.parameter;
		}
		else
		{
			float k = (time - start.time) / (end.time - start.time);
			float[] result = start.parameter.clone();

			Quat4f q = new Quat4f(start.parameter);
			q.interpolate(new Quat4f(end.parameter), k);
			q.get(result);

			return result;
		}

	}

}
