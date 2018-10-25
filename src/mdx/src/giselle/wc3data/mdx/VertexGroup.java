package giselle.wc3data.mdx;

import javax.vecmath.Matrix4f;

public class VertexGroup
{
	public int[] nodes = null;

	private Matrix4f matrix = null;

	public VertexGroup()
	{
		this.matrix = new Matrix4f();
		this.matrix.setIdentity();
	}

	public Matrix4f getMatrix()
	{
		return this.matrix;
	}

	public void updateMatrix(MdxModel model)
	{
		BoneManager boneManager = model.getBoneManager();
		int nodesLength = this.nodes.length;

		if (nodesLength > 0)
		{
			this.matrix.mul(0);

			for (int nodeId : this.nodes)
			{
				BoneData boneData = boneManager.get(nodeId);

				if (boneData != null)
				{
					Matrix4f matrix2 = boneData.getMatrix();
					this.matrix.add(matrix2);
				}

			}

			this.matrix.mul(1.0F / nodesLength);
		}

	}

}
