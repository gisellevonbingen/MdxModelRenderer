package giselle.mdx.render;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map.Entry;

import org.lwjgl.opengl.GL11;

import giselle.mdx.ModelWrapper;
import giselle.wc3data.util.TextureUtils;

public class TextureManager
{
	private HashMap<String, TextureObject> map;

	public TextureManager()
	{
		this.map = new HashMap<>();
	}

	public void clear()
	{
		for (Entry<String, TextureObject> entry : this.map.entrySet())
		{
			int id = entry.getValue().getId();

			if (id != -1)
			{
				GL11.glDeleteTextures(id);
			}

		}

		this.map.clear();
	}

	public TextureObject get(ModelWrapper modelWrapper, String fileName)
	{
		TextureObject texture = this.map.get(fileName);

		if (texture == null)
		{
			texture = this.load(modelWrapper, fileName);
		}

		return texture;
	}

	private TextureObject load(ModelWrapper modelWrapper, String fileName)
	{
		TextureObject texture = null;

		try
		{
			try (InputStream is = modelWrapper.openInputStream(fileName))
			{
				texture = TextureUtils.loadTexture(fileName, is);
			}

		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		if (texture == null)
		{
			texture = new TextureObject();
		}

		this.map.put(fileName, texture);
		return texture;
	}

}
