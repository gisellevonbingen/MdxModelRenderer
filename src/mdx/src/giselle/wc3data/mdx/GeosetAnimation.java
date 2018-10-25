package giselle.wc3data.mdx;

import java.io.IOException;

import javax.vecmath.Vector3f;

import giselle.wc3data.stream.BlizzardDataInputStream;
import giselle.wc3data.stream.StreamUtils;

public class GeosetAnimation
{
	public int flags;
	public static final int Flags_DropShawdow = 0x01;
	public static final int Flags_UseColor = 0x02;

	public float alpha = 0.0F;
	public Vector3f color = null;

	public int geosetId;
	public TransformationFloat geosetAlpha;
	public static final String AlpahKey = "KGAO";
	public TransformationFloat geosetColor;
	public static final String ColorKey = "KGAC";

	public float alphaMatrix = 0.0F;
	public Vector3f colorMatrix = null;

	public void load(BlizzardDataInputStream in) throws IOException
	{
		@SuppressWarnings("unused")
		int inclusiveSize = in.readInt();

		this.alpha = in.readFloat();
		this.flags = in.readInt();
		this.color = StreamUtils.loadVector3f(in);
		this.geosetId = in.readInt();

		for (int i = 0; i < 2; i++)
		{
			if (StreamUtils.checkOptionalId(in, AlpahKey))
			{
				this.geosetAlpha = new TransformationFloat(AlpahKey, 1);
				this.geosetAlpha.load(in);
			}
			else if (StreamUtils.checkOptionalId(in, ColorKey))
			{
				this.geosetColor = new TransformationFloat(ColorKey, 3);
				this.geosetColor.load(in);
			}

		}

	}

	public void setTime(MdxModel model, int time)
	{
		float[] alphaInterpolation = null;
		float[] colorInterpolation = null;

		if (this.geosetAlpha != null)
		{
			alphaInterpolation = this.geosetAlpha.getInterpolationResult(model, time);
		}

		if (this.geosetColor != null)
		{
			colorInterpolation = this.geosetColor.getInterpolationResult(model, time);
		}

		if (alphaInterpolation != null)
		{
			this.alphaMatrix = alphaInterpolation[0];
		}
		else
		{
			this.alphaMatrix = this.alpha;
		}

		if (colorInterpolation != null)
		{
			this.colorMatrix = new Vector3f(colorInterpolation);
		}
		else
		{
			this.colorMatrix = new Vector3f(this.color);
		}

	}

	public int getSize()
	{
		int a = 0;
		a += 4;
		a += 4;
		a += 4;
		a += 12;
		a += 4;

		if (this.geosetAlpha != null)
		{
			a += geosetAlpha.getSize();
		}

		if (this.geosetColor != null)
		{
			a += this.geosetColor.getSize();
		}

		return a;
	}

}
