package giselle.wc3data.mdx;

import java.io.IOException;

import giselle.wc3data.stream.BlizzardDataInputStream;

public class Helper implements INodedObject
{
	public Node node = new Node();

	public void load(BlizzardDataInputStream in) throws IOException
	{
		node = new Node();
		node.load(in);
	}

	public int getSize()
	{
		int a = 0;
		a += node.getSize();

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
		return NodeType.Helper;
	}

}
