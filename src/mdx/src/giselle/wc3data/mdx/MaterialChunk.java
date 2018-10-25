package giselle.wc3data.mdx;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import giselle.wc3data.stream.BlizzardDataInputStream;
import giselle.wc3data.stream.StreamUtils;

public class MaterialChunk
{
	public Material[] materials = new Material[0];

	public static final String key = "MTLS";

	public void load(BlizzardDataInputStream in) throws IOException
	{
		StreamUtils.checkId(in, key);
		int chunkSize = in.readInt();
		List<Material> materialList = new ArrayList<>();
		int materialCounter = chunkSize;

		while (materialCounter > 0)
		{
			Material tempmaterial = new Material();
			materialList.add(tempmaterial);
			tempmaterial.load(in);
			materialCounter -= tempmaterial.getSize();
		}

		materials = materialList.toArray(new Material[materialList.size()]);
	}

	public int getSize()
	{
		int a = 0;
		a += 4;
		a += 4;
		for (int i = 0; i < materials.length; i++)
		{
			a += materials[i].getSize();
		}

		return a;
	}

	public void setTime(MdxModel model, int time)
	{
		for (int i = 0; i < this.materials.length; i++)
		{
			this.materials[i].setTime(model, time);
		}

	}

}
