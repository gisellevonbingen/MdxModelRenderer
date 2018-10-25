package giselle.wc3data.mdx;

import java.io.IOException;

import giselle.wc3data.stream.BlizzardDataInputStream;
import giselle.wc3data.stream.StreamUtils;

public class Node
{
	public static final int DONT_INHERIT_TRANSLATION = 0x0001;
	public static final int DONT_INHERIT_SCALING = 0x0002;
	public static final int DONT_INHERIT_ROTATION = 0x0004;
	public static final int BILLBOARDED = 0x0008;
	public static final int BILLBOARDED_LOCK_X = 0x0010;
	public static final int BILLBOARDED_LOCK_Y = 0x0020;
	public static final int BILLBOARDED_LOCK_Z = 0x0040;
	public static final int CAMERA_ANCHORED = 0x0080;

	public static final String TranslationId = "KGTR";
	public static final String RotationId = "KGRT";
	public static final String ScalingId = "KGSC";

	public String name = "";
	public int objectId;
	public int parentId;
	public int flags;
	public TransformationFloat geosetTranslation;
	public TransformationFloat geosetRotation;
	public TransformationFloat geosetScaling;

	public void load(BlizzardDataInputStream in) throws IOException
	{
		@SuppressWarnings("unused")
		int inclusiveSize = in.readInt();
		name = in.readCharsAsString(80);
		objectId = in.readInt();
		parentId = in.readInt();
		flags = in.readInt();

		for (int i = 0; i < 3; i++)
		{
			String readOptionalId = StreamUtils.readOptionalId(in);

			if (readOptionalId.equals(TranslationId))
			{
				geosetTranslation = new TransformationFloat(readOptionalId, 3);
				geosetTranslation.load(in);
			}
			else if (readOptionalId.equals(RotationId))
			{
				geosetRotation = new TransformationFloat(readOptionalId, 4);
				geosetRotation.load(in);
			}
			else if (readOptionalId.equals(ScalingId))
			{
				geosetScaling = new TransformationFloat(readOptionalId, 3);
				geosetScaling.load(in);
			}

		}

	}

	public int getSize()
	{
		int a = 0;
		a += 4;
		a += 80;
		a += 4;
		a += 4;
		a += 4;

		if (geosetTranslation != null)
		{
			a += geosetTranslation.getSize();
		}
		if (geosetRotation != null)
		{
			a += geosetRotation.getSize();
		}
		if (geosetScaling != null)
		{
			a += geosetScaling.getSize();
		}

		return a;
	}
}
