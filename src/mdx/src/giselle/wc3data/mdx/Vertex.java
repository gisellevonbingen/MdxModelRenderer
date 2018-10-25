package giselle.wc3data.mdx;

import java.util.HashMap;

import javax.vecmath.Matrix4f;
import javax.vecmath.Point3f;
import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;

public class Vertex
{
	private Point3f position = null;
	private Vector2f uv = null;
	private Vector2f uv2 = null;
	private Vector3f normal = null;
	private byte group = 0;

	private Point3f matrixedPosition = null;
	private Vector3f matrixedNormal = null;
	private HashMap<Integer, Vector2f> matrixedUV = null;

	public Vertex()
	{
		this.position = new Point3f();
		this.uv = new Vector2f();
		this.uv2 = null;
		this.normal = new Vector3f();

		this.matrixedUV = new HashMap<>();
	}

	public void applyMatrix(VertexGroup vertexGroup)
	{
		Matrix4f matrix = vertexGroup.getMatrix();
		Point3f p = new Point3f(this.position);
		Vector3f n = new Vector3f(this.normal);
		matrix.transform(p);
		matrix.transform(n);

		this.matrixedPosition = p;
		this.matrixedNormal = n;
	}

	public void applyTextureMatrix(Material material)
	{
		this.matrixedUV.clear();

		LayerChunk layerChunk = material.layerChunk;

		if (layerChunk == null)
		{
			return;
		}

		Layer[] layers = layerChunk.layers;

		for (int i = 0; i < layers.length; i++)
		{
			Layer layer = layers[i];
			Vector2f textureTranslation = layer.textureTranslation;
			Vector2f uv = new Vector2f(this.uv);
			uv.add(textureTranslation);

			this.matrixedUV.put(i, uv);
		}

	}

	public Point3f getMatrixedPosition()
	{
		return this.matrixedPosition;
	}

	public Vector3f getMatrixedNormal()
	{
		return this.matrixedNormal;
	}

	public Vector2f getMatrixedUV(int layerId)
	{
		return this.matrixedUV.get(layerId);
	}

	public Point3f getPosition()
	{
		return this.position;
	}

	public void setPosition(Point3f position)
	{
		this.position = position;
	}

	public Vector2f getUV()
	{
		return this.uv;
	}

	public void setUV(Vector2f uv)
	{
		this.uv = uv;
	}

	public Vector2f getUV2()
	{
		return uv2;
	}

	public void setUV2(Vector2f uv2)
	{
		this.uv2 = uv2;
	}

	public Vector3f getNormal()
	{
		return normal;
	}

	public void setNormal(Vector3f normal)
	{
		this.normal = normal;
	}

	public byte getGroup()
	{
		return group;
	}

	public void setGroup(byte group)
	{
		this.group = group;
	}

}
