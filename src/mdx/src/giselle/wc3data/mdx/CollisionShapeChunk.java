package giselle.wc3data.mdx;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import giselle.wc3data.stream.BlizzardDataInputStream;
import giselle.wc3data.stream.StreamUtils;

public class CollisionShapeChunk
{
	public CollisionShape[] collisionShapes = new CollisionShape[0];

	public static final String key = "CLID";

	public void load(BlizzardDataInputStream in) throws IOException
	{
		StreamUtils.checkId(in, key);
		int chunkSize = in.readInt();
		List<CollisionShape> collisionShapeList = new ArrayList<>();
		int collisionShapeCounter = chunkSize;

		while (collisionShapeCounter > 0)
		{
			CollisionShape tempcollisionShape = new CollisionShape();
			collisionShapeList.add(tempcollisionShape);
			tempcollisionShape.load(in);
			collisionShapeCounter -= tempcollisionShape.getSize();
		}

		collisionShapes = collisionShapeList.toArray(new CollisionShape[collisionShapeList.size()]);
	}

	public int getSize()
	{
		int a = 0;
		a += 4;
		a += 4;
		for (int i = 0; i < collisionShapes.length; i++)
		{
			a += collisionShapes[i].getSize();
		}

		return a;
	}

}
