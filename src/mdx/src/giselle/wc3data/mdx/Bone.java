package giselle.wc3data.mdx;

import java.io.IOException;

import giselle.wc3data.stream.BlizzardDataInputStream;

public class Bone implements INodedObject
{
	public static final int multipleId = -1;

	public Node node = new Node();
	public int geosetId;
	public int geosetAnimationId;

	public void load(BlizzardDataInputStream in) throws IOException
	{
		node = new Node();
		node.load(in);
		geosetId = in.readInt();
		geosetAnimationId = in.readInt();
	}

	public int getSize()
	{
		int a = 0;
		a += node.getSize();
		a += 4;
		a += 4;

		return a;
	}

	@Override
	public Node getNode()
	{
		return this.node;
	}

	@Override
	public NodeType getNodeType()
	{
		return NodeType.Bone;
	}

}
