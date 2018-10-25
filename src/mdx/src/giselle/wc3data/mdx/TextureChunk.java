package giselle.wc3data.mdx;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import giselle.wc3data.stream.BlizzardDataInputStream;
import giselle.wc3data.stream.StreamUtils;

public class TextureChunk
{
	public Texture[] textures = new Texture[0];

	public static final String key = "TEXS";

	public void load(BlizzardDataInputStream in) throws IOException
	{
		StreamUtils.checkId(in, key);
		int chunkSize = in.readInt();
		List<Texture> textureList = new ArrayList<>();
		int textureCounter = chunkSize;

		while (textureCounter > 0)
		{
			Texture temptexture = new Texture();
			textureList.add(temptexture);
			temptexture.load(in);
			textureCounter -= temptexture.getSize();
		}

		textures = textureList.toArray(new Texture[textureList.size()]);
	}

	public int getSize()
	{
		int a = 0;
		a += 4;
		a += 4;
		for (int i = 0; i < textures.length; i++)
		{
			a += textures[i].getSize();
		}

		return a;
	}

}
