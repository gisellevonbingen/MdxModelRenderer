package giselle.wc3data.mdx;

import java.io.IOException;

import javax.vecmath.Vector2f;

import giselle.wc3data.stream.BlizzardDataInputStream;
import giselle.wc3data.stream.StreamUtils;

public class Layer
{
	public static final int ShadingFlags_UNSHADED = 0x01;
	public static final int ShadingFlags_SPHERE_ENVIRONMENT_MAP = 0x02;
	public static final int ShadingFlags_UNKNOWN_1 = 0x04;
	public static final int ShadingFlags_UNKNOWN_2 = 0x08;
	public static final int ShadingFlags_TWO_SIDED = 0x10;
	public static final int ShadingFlags_UNFOGGED = 0x20;
	public static final int ShadingFlags_NO_DEPTH_TEST = 0x40;
	public static final int ShadingFlags_NO_DEPTH_SET = 0x80;

	public LayerFilterMode filterMode;
	public int shadingFlags;
	public int textureId;
	public int textureAnimationId;
	public int unknownNull;
	public float alpha;
	public TransformationFloat materialAlpha;
	public static final String MaterialAlphaKey = "KMTA";
	public TransformationFloat materialTextureId;
	public static final String MaterialTextureIdKey = "KMTF";

	public Vector2f textureTranslation = null;
	public float alphaTranslration = 0.0F;

	public void load(BlizzardDataInputStream in) throws IOException
	{
		@SuppressWarnings("unused")
		int inclusiveSize = in.readInt();
		filterMode = LayerFilterMode.find(in.readInt());
		shadingFlags = in.readInt();
		textureId = in.readInt();
		textureAnimationId = in.readInt();
		unknownNull = in.readInt();
		alpha = in.readFloat();

		for (int i = 0; i < 2; i++)
		{
			if (StreamUtils.checkOptionalId(in, MaterialAlphaKey))
			{
				materialAlpha = new TransformationFloat(MaterialAlphaKey, 1);
				materialAlpha.load(in);
			}
			else if (StreamUtils.checkOptionalId(in, MaterialTextureIdKey))
			{
				materialTextureId = new TransformationFloat(MaterialTextureIdKey, 1);
				materialTextureId.load(in);
			}

		}

		this.textureTranslation = new Vector2f(0.0F, 0.0F);
	}

	public void setTime(MdxModel model, int time)
	{
		this.applyTextureAnimation(model, time);

		this.applyMaterialAnimation(model, time);

	}

	private void applyMaterialAnimation(MdxModel model, int time)
	{
		this.alphaTranslration = this.alpha;

		if (this.materialAlpha != null)
		{
			float[] interpolationResult = this.materialAlpha.getInterpolationResult(model, time);

			if (interpolationResult != null)
			{
				this.alphaTranslration = interpolationResult[0];
			}

		}

	}

	private void applyTextureAnimation(MdxModel model, int time)
	{
		TextureAnimationChunk textureAnimationChunk = model.textureAnimationChunk;

		if (textureAnimationChunk != null)
		{
			TextureAnimation[] textureAnimations = textureAnimationChunk.textureAnimations;

			if (-1 < this.textureAnimationId && this.textureAnimationId < textureAnimations.length)
			{
				TextureAnimation textureAnimation = textureAnimations[this.textureAnimationId];
				this.applyTextureAnimation(model, textureAnimation, time);
			}

		}

	}

	private void applyTextureAnimation(MdxModel model, TextureAnimation textureAnimation, int time)
	{
		this.textureTranslation.set(0.0F, 0.0F);
		TransformationFloat textureTranslation = textureAnimation.textureTranslation;

		if (textureTranslation != null)
		{
			float[] interpolationResult = textureTranslation.getInterpolationResult(model, time);

			if (interpolationResult != null)
			{
				this.textureTranslation.set(interpolationResult);
			}

		}

	}

	public int getSize()
	{
		int a = 0;
		a += 4;
		a += 4;
		a += 4;
		a += 4;
		a += 4;
		a += 4;
		a += 4;
		if (materialAlpha != null)
		{
			a += materialAlpha.getSize();
		}
		if (materialTextureId != null)
		{
			a += materialTextureId.getSize();
		}

		return a;
	}

}
