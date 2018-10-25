package giselle.mdx.render;

import org.lwjgl.opengl.GL11;

import giselle.wc3data.mdx.LayerFilterMode;
import giselle.wc3data.mdx.Particle2FilterMode;
import giselle.wc3data.util.GLStateUtils;

public class MdxLayerFilterMode
{
	private static final float ALPHA_THRESHOLD = 0.95F;

	private MdxLayerFilterMode()
	{

	}

	public static void applyPartcile2FilterMode(Particle2FilterMode filterMode, boolean enable)
	{
		switch (filterMode)
		{
			case BLEND:
				setFilterModeBlendEnable(enable);
				break;
			case ADDITIVE:
				setFilterModeAdditiveEnable(enable);
				break;
			case ADDALPHA:
				setFilterModeAddalphaEnable(enable);
				break;
			case MODULATE:
				setFilterModeModulateEnable(enable);
				break;
			default:
				break;
		}

	}

	public static void applyLayerFilterMode(LayerFilterMode filterMode, boolean enable)
	{
		switch (filterMode)
		{
			case NONE:
				setFilterModeNoneEnable(enable);
				break;
			case TRANSPARENT:
				setFilterModeTransparentEnable(enable);
				break;
			case BLEND:
				setFilterModeBlendEnable(enable);
				break;
			case ADDITIVE:
				setFilterModeAdditiveEnable(enable);
				break;
			case ADDALPHA:
				setFilterModeAddalphaEnable(enable);
				break;
			case MODULATE:
				setFilterModeModulateEnable(enable);
				break;
			case MODULATE2X:
				setFilterModeModulate2XEnable(enable);
				break;
			default:
				break;
		}

	}

	private static void setFilterModeNoneEnable(boolean enable)
	{

	}

	private static void setFilterModeTransparentEnable(boolean enable)
	{
		GLStateUtils.set(GL11.GL_ALPHA_TEST, enable);

		if (enable == true)
		{
			GL11.glAlphaFunc(GL11.GL_GEQUAL, ALPHA_THRESHOLD);
		}
		else
		{
			GL11.glAlphaFunc(GL11.GL_GREATER, 0.0f);
		}

	}

	private static void setFilterModeBlendEnable(boolean enable)
	{
		GLStateUtils.set(GL11.GL_BLEND, enable);
		GL11.glDepthMask(!enable);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	}

	private static void setFilterModeAdditiveEnable(boolean enable)
	{
		GLStateUtils.set(GL11.GL_BLEND, enable);
		GL11.glDepthMask(!enable);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
	}

	private static void setFilterModeAddalphaEnable(boolean enable)
	{
		GLStateUtils.set(GL11.GL_BLEND, enable);
		GL11.glDepthMask(!enable);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
	}

	private static void setFilterModeModulateEnable(boolean enable)
	{

	}

	private static void setFilterModeModulate2XEnable(boolean enable)
	{

	}

}
