package giselle.wc3data.mdx;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.vecmath.Vector3f;

public class BoneManager
{
	public static final int RootId = -1;
	
	private Map<Integer, BoneData> map = null;
	private BoneData root = null;

	public BoneManager()
	{
		this.map = new HashMap<>();

		BoneData root = new BoneData(NodeType.None, new Node());
		root.setPivot(new Vector3f(0, 0, 0));
		this.map.put(RootId, root);
		this.root = root;
	}
	
	public BoneData getRoot()
	{
		return this.root;
	}

	public int size()
	{
		return this.map.size();
	}

	public void setTime(MdxModel model, int time)
	{
		this.root.setTime(model, time, null);
	}

	public BoneData get(int id)
	{
		return this.map.get(id);
	}

	public void build(SkeletonBuilder builder)
	{
		PivotPointChunk pivotChunk = builder.getPivotChunk();
		Vector3f[] pivotPoints = pivotChunk != null ? pivotChunk.pivotPoints : null;

		Map<Integer, INodedObject> build = builder.build();
		Collection<INodedObject> nodeWrappers = build.values();

		for (INodedObject nodeWrapper : nodeWrappers)
		{
			Node node = nodeWrapper.getNode();
			NodeType nodeType = nodeWrapper.getNodeType();
			int objectId = node.objectId;

			BoneData boneData = new BoneData(nodeType, node);

			if (pivotPoints != null)
			{
				boneData.setPivot(pivotPoints[objectId]);
			}

			this.map.put(objectId, boneData);
		}

		for (INodedObject nodeWrapper : nodeWrappers)
		{
			Node node = nodeWrapper.getNode();
			int objectId = node.objectId;
			int parentId = node.parentId;
			BoneData boneData = this.map.get(objectId);
			BoneData parent = this.map.get(parentId);

			if (parent != null)
			{
				parent.getChildren().add(boneData);
			}

		}

	}

}
