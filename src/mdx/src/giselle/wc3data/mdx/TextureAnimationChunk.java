package giselle.wc3data.mdx;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import giselle.wc3data.stream.BlizzardDataInputStream;
import giselle.wc3data.stream.StreamUtils;

public class TextureAnimationChunk
{
	public TextureAnimation[] textureAnimations = new TextureAnimation[0];

	public static final String key = "TXAN";

	public void load(BlizzardDataInputStream in) throws IOException
	{
		StreamUtils.checkId(in, key);
		int chunkSize = in.readInt();
		List<TextureAnimation> textureAnimationList = new ArrayList<>();
		int textureAnimationCounter = chunkSize;

		while (textureAnimationCounter > 0)
		{
			TextureAnimation temptextureAnimation = new TextureAnimation();
			textureAnimationList.add(temptextureAnimation);
			temptextureAnimation.load(in);
			textureAnimationCounter -= temptextureAnimation.getSize();
		}

		textureAnimations = textureAnimationList.toArray(new TextureAnimation[textureAnimationList.size()]);
	}

	public int getSize()
	{
		int a = 0;
		a += 4;
		a += 4;

		for (int i = 0; i < textureAnimations.length; i++)
		{
			a += textureAnimations[i].getSize();
		}

		return a;
	}

}
