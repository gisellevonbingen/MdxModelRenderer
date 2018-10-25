package giselle.wc3data.mdx;

import java.io.IOException;

import giselle.wc3data.stream.BlizzardDataInputStream;
import giselle.wc3data.stream.StreamUtils;

public class TextureAnimation
{
	public TransformationFloat textureTranslation;
	public static final String TranslationKey = "KTAT";
	public TransformationFloat textureRotation;
	public static final String TextureRotationKey = "KTAR";
	public TransformationFloat textureScaling;
	public static final String TextureScalingKey = "KTAS";

	public void load(BlizzardDataInputStream in) throws IOException
	{
		@SuppressWarnings("unused")
		int inclusiveSize = in.readInt();

		for (int i = 0; i < 3; i++)
		{
			if (StreamUtils.checkOptionalId(in, TranslationKey))
			{
				textureTranslation = new TransformationFloat(TranslationKey, 3);
				textureTranslation.load(in);
			}
			else if (StreamUtils.checkOptionalId(in, TextureRotationKey))
			{
				textureRotation = new TransformationFloat(TextureRotationKey, 4);
				textureRotation.load(in);
			}
			else if (StreamUtils.checkOptionalId(in, TextureScalingKey))
			{
				textureScaling = new TransformationFloat(TextureScalingKey, 3);
				textureScaling.load(in);
			}

		}
	}

	public int getSize()
	{
		int a = 0;
		a += 4;

		if (textureTranslation != null)
		{
			a += textureTranslation.getSize();
		}

		if (textureRotation != null)
		{
			a += textureRotation.getSize();
		}

		if (textureScaling != null)
		{
			a += textureScaling.getSize();
		}

		return a;
	}

}
