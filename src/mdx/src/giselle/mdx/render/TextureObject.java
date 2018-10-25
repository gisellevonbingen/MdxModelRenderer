package giselle.mdx.render;

import java.awt.image.BufferedImage;

import org.lwjgl.opengl.GL11;

public class TextureObject
{
	public static final int NoneId = -1;

	private final int id;
	private final BufferedImage image;

	public TextureObject()
	{
		this.id = NoneId;
		this.image = null;
	}

	public TextureObject(int id, BufferedImage image)
	{
		this.id = id;
		this.image = image;
	}

	public void bind()
	{
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.id);
	}

	public int getId()
	{
		return this.id;
	}

	public BufferedImage getImage()
	{
		return this.image;
	}

}
