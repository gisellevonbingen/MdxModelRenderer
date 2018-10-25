package giselle.wc3data.util;

import javax.vecmath.Matrix4f;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import giselle.wc3data.mdx.MdxModel;
import giselle.wc3data.mdx.TransformationFloat;

public class TransformationUtils
{
	private TransformationUtils()
	{

	}
	
	public static void applyTranslation(MdxModel model, TransformationFloat translation, int time, Matrix4f matrix)
	{
		if (translation != null)
		{
			float[] translationValue = translation.getInterpolationResult(model, time);

			if (translationValue != null)
			{
				matrix.setTranslation(new Vector3f(translationValue));
			}

		}

	}

	public static void applyRotation(MdxModel model, TransformationFloat rotation, int time, Matrix4f matrix)
	{
		if (rotation != null)
		{
			float[] rotationValue = rotation.getQuaternionInterpolationResult(model, time);

			if (rotationValue != null)
			{
				matrix.setRotation(new Quat4f(rotationValue));
			}

		}

	}

	public static void applyScaling(MdxModel model, TransformationFloat scaling, int time, Matrix4f matrix)
	{
		if (scaling != null)
		{
			float[] scalingValue = scaling.getInterpolationResult(model, time);

			if (scalingValue != null)
			{
				// XXX I don't know if the scaling is supposed to come first or last...
				// To change the order, exchange the arguments of mul() in the following expression
				matrix.mul(matrix, new Matrix4f(scalingValue[0], 0, 0, 0, 0, scalingValue[1], 0, 0, 0, 0, scalingValue[2], 0, 0, 0, 0, 1));
			}

		}

	}

}
