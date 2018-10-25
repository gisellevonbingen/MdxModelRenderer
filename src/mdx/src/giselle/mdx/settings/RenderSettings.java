package giselle.mdx.settings;

import java.awt.Color;

public class RenderSettings
{
	public Color backgroundColor = new Color(0xFF9A9A9A);

	public boolean orientLineRender = true;
	public float orientLineWidth = 2.0F;
	public float orientLineLength = 100.0F;

	public boolean modelSolidRender = true;

	public boolean modelWireRender = false;
	public float modelWireWidth = 1.0F;

	public boolean modelBoneRender = false;
	public float modelBoneWidth = 1.0F;
	public Color modelBoneColor = Color.GREEN;
	public boolean modelBoneDepthTest = false;

	public RenderSettings()
	{

	}

}
