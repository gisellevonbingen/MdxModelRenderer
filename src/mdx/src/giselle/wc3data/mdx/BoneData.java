package giselle.wc3data.mdx;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Matrix4f;
import javax.vecmath.Point3f;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import giselle.wc3data.util.MaskUtils;
import giselle.wc3data.util.TransformationUtils;

public class BoneData
{
	private final NodeType type;
	private final Node node;

	private List<BoneData> children = null;

	private Vector3f pivot = null;
	private Matrix4f matrix = null;
	private Point3f matrixedPivot = null;

	public BoneData(NodeType type, Node node)
	{
		this.type = type;
		this.node = node;
		this.children = new ArrayList<>();

		this.matrix = new Matrix4f();
		this.matrix.setIdentity();
	}

	public void setTime(MdxModel model, int time, Matrix4f parentMatrix)
	{
		this.matrix.setIdentity();
		TransformationUtils.applyTranslation(model, this.node.geosetTranslation, time, this.matrix);
		TransformationUtils.applyRotation(model, this.node.geosetRotation, time, this.matrix);
		TransformationUtils.applyScaling(model, this.node.geosetScaling, time, this.matrix);

		if (MaskUtils.testMask(this.node.flags, Node.BILLBOARDED) == false)
		{
			this.applyPivotPointAdjustment();
		}

		if (parentMatrix != null)
		{
			this.applyParentMatrix(parentMatrix);
		}

		for (BoneData child : this.children)
		{
			child.setTime(model, time, new Matrix4f(this.matrix));
		}

		Point3f pivot = new Point3f(this.pivot);
		this.matrix.transform(pivot);
		this.matrixedPivot = pivot;
	}

	private final void applyParentMatrix(Matrix4f parentMatrix)
	{
		Matrix4f mTmp = new Matrix4f(this.matrix);
		this.matrix.mul(parentMatrix, this.matrix);

		int flags = this.node.flags;

		if (MaskUtils.testMask(flags, Node.DONT_INHERIT_ROTATION))
		{
			Quat4f quat = new Quat4f();
			mTmp.get(quat);
			this.matrix.setRotation(quat);
		}

		if (MaskUtils.testMask(flags, Node.DONT_INHERIT_TRANSLATION))
		{
			Vector3f vTmp = new Vector3f();
			mTmp.get(vTmp);
			this.matrix.setTranslation(vTmp);
		}

		if (MaskUtils.testMask(flags, Node.DONT_INHERIT_SCALING))
		{
			this.matrix.setScale(mTmp.getScale());
		}

	}

	private void applyPivotPointAdjustment()
	{
		Vector3f pivot = new Vector3f(this.pivot);
		Matrix4f m = new Matrix4f();
		m.setIdentity();
		m.setTranslation(pivot);

		this.matrix.mul(m, this.matrix);
		pivot.negate();
		m.setTranslation(pivot);

		this.matrix.mul(this.matrix, m);
	}

	public NodeType getType()
	{
		return this.type;
	}

	public List<BoneData> getChildren()
	{
		return this.children;
	}

	public Node getNode()
	{
		return node;
	}

	public Vector3f getPivot()
	{
		return pivot;
	}

	public void setPivot(Vector3f pivot)
	{
		this.pivot = pivot;
	}

	public Matrix4f getMatrix()
	{
		return this.matrix;
	}

	public void setMatrix(Matrix4f matrix)
	{
		this.matrix = matrix;
	}

	public Point3f getMatrixedPivot()
	{
		return matrixedPivot;
	}

}
