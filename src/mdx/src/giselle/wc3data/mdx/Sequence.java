package giselle.wc3data.mdx;

import java.io.IOException;

import javax.vecmath.Vector3f;

import giselle.wc3data.stream.BlizzardDataInputStream;
import giselle.wc3data.stream.BlizzardDataOutputStream;
import giselle.wc3data.stream.StreamUtils;

public class Sequence
{
	public String name = "";
	public int intervalStart;
	public int intervalEnd;
	public float moveSpeed;
	public int nonLooping;
	public float rarity;
	public int unknownNull;
	public float boundsRadius;
	public Vector3f minimumExtent = null;
	public Vector3f maximumExtent = null;

	public void load(BlizzardDataInputStream in) throws IOException
	{
		name = in.readCharsAsString(80);
		intervalStart = in.readInt();
		intervalEnd = in.readInt();
		moveSpeed = in.readFloat();
		nonLooping = in.readInt();
		rarity = in.readFloat();
		unknownNull = in.readInt();
		boundsRadius = in.readFloat();
		minimumExtent = StreamUtils.loadVector3f(in);
		maximumExtent = StreamUtils.loadVector3f(in);
	}

	public void save(BlizzardDataOutputStream out) throws IOException
	{
		out.writeNByteString(name, 80);
		out.writeInt(intervalStart);
		out.writeInt(intervalEnd);
		out.writeFloat(moveSpeed);
		out.writeInt(nonLooping);
		out.writeFloat(rarity);
		out.writeInt(unknownNull);
		out.writeFloat(boundsRadius);
		StreamUtils.saveVector3f(out, minimumExtent);
		StreamUtils.saveVector3f(out, maximumExtent);

	}

	public int getSize()
	{
		int a = 0;
		a += 80;
		a += 4;
		a += 4;
		a += 4;
		a += 4;
		a += 4;
		a += 4;
		a += 4;
		a += 12;
		a += 12;

		return a;
	}

}
