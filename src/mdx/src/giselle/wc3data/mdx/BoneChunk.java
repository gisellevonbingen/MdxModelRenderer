package giselle.wc3data.mdx;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import giselle.wc3data.stream.BlizzardDataInputStream;
import giselle.wc3data.stream.StreamUtils;

public class BoneChunk
{
	public Bone[] bones = new Bone[0];

	public static final String key = "BONE";

	public void load(BlizzardDataInputStream in) throws IOException
	{
		StreamUtils.checkId(in, key);
		int chunkSize = in.readInt();
		List<Bone> boneList = new ArrayList<>();
		int boneCounter = chunkSize;
		while (boneCounter > 0)
		{
			Bone tempbone = new Bone();
			boneList.add(tempbone);
			tempbone.load(in);
			boneCounter -= tempbone.getSize();
		}
		bones = boneList.toArray(new Bone[boneList.size()]);
	}

	public int getSize()
	{
		int a = 0;
		a += 4;
		a += 4;
		for (int i = 0; i < bones.length; i++)
		{
			a += bones[i].getSize();
		}

		return a;
	}

}
