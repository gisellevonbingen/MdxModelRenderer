package giselle.wc3data.mdx;

import java.util.HashMap;
import java.util.Map;

public class SkeletonBuilder
{
	private Map<Integer, INodedObject> map = null;
	private PivotPointChunk pivotChunk;

	public SkeletonBuilder()
	{
		this.map = new HashMap<Integer, INodedObject>();
	}

	public void add(INodedObject nodeWrapper)
	{
		Node node = nodeWrapper.getNode();
		this.map.put(node.objectId, nodeWrapper);
	}

	public void addAll(INodedObject[] nodeWrappers)
	{
		for (int i = 0; i < nodeWrappers.length; i++)
		{
			this.add(nodeWrappers[i]);
		}

	}

	public Map<Integer, INodedObject> build()
	{
		return this.map;
	}

	public void setPivotChunk(PivotPointChunk pivotChunk)
	{
		this.pivotChunk = pivotChunk;
	}

	public PivotPointChunk getPivotChunk()
	{
		return pivotChunk;
	}

}
