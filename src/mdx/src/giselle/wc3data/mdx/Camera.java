package giselle.wc3data.mdx;

import java.io.IOException;

import giselle.wc3data.stream.BlizzardDataInputStream;
import giselle.wc3data.stream.StreamUtils;

public class Camera
{
	public String name = "";
	public float[] position = new float[3];
	public float fieldOfView;
	public float farClippingPlane;
	public float nearClippingPlane;
	public float[] targetPosition = new float[3];
	public CameraPositionTranslation cameraPositionTranslation;
	public CameraRotation cameraRotation;
	public CameraTargetTranslation cameraTargetTranslation;

	public void load(BlizzardDataInputStream in) throws IOException
	{
		@SuppressWarnings("unused")
		int inclusiveSize = in.readInt();
		name = in.readCharsAsString(80);
		position = StreamUtils.loadFloatArray(in, 3);
		fieldOfView = in.readFloat();
		farClippingPlane = in.readFloat();
		nearClippingPlane = in.readFloat();
		targetPosition = StreamUtils.loadFloatArray(in, 3);
		for (int i = 0; i < 3; i++)
		{
			if (StreamUtils.checkOptionalId(in, CameraPositionTranslation.key))
			{
				cameraPositionTranslation = new CameraPositionTranslation();
				cameraPositionTranslation.load(in);
			}
			else if (StreamUtils.checkOptionalId(in, CameraRotation.key))
			{
				cameraRotation = new CameraRotation();
				cameraRotation.load(in);
			}
			else if (StreamUtils.checkOptionalId(in, CameraTargetTranslation.key))
			{
				cameraTargetTranslation = new CameraTargetTranslation();
				cameraTargetTranslation.load(in);
			}

		}
	}

	public int getSize()
	{
		int a = 0;
		a += 4;
		a += 80;
		a += 12;
		a += 4;
		a += 4;
		a += 4;
		a += 12;
		if (cameraPositionTranslation != null)
		{
			a += cameraPositionTranslation.getSize();
		}
		if (cameraRotation != null)
		{
			a += cameraRotation.getSize();
		}
		if (cameraTargetTranslation != null)
		{
			a += cameraTargetTranslation.getSize();
		}

		return a;
	}
}
