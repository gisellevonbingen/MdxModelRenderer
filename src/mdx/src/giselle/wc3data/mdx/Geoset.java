package giselle.wc3data.mdx;

import java.io.IOException;
import java.util.List;

import javax.vecmath.Point3f;
import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;

import giselle.wc3data.stream.BlizzardDataInputStream;
import giselle.wc3data.stream.StreamUtils;

public class Geoset
{
	public Vertex[] vertexs = null;
	public VertexGroup[] vertexGroups = null;

	public int[] faceTypeGroups = new int[0];
	public int[] faceGroups = new int[0];
	public short[] faces = new short[0];
	public int materialId;
	public int selectionGroup;
	public int selectionType;
	public float boundsRadius;
	public Vector3f minimumExtent = null;
	public Vector3f maximumExtent = null;
	public Extent[] extent = new Extent[0];

	public GeosetRenderProperty renderProperty = null;

	public static final String key = "VRTX";

	public void load(BlizzardDataInputStream in) throws IOException
	{
		@SuppressWarnings("unused")
		int inclusiveSize = in.readInt();
		StreamUtils.checkId(in, key);
		int nrOfVertexPositions = in.readInt();
		Vector3f[] vertexPositions = StreamUtils.loadVector3fArray(in, nrOfVertexPositions);
		StreamUtils.checkId(in, "NRMS");
		int nrOfVertexNormals = in.readInt();
		Vector3f[] vertexNormals = StreamUtils.loadVector3fArray(in, nrOfVertexNormals);
		StreamUtils.checkId(in, "PTYP");
		int nrOfFaceTypeGroups = in.readInt();
		faceTypeGroups = StreamUtils.loadIntArray(in, nrOfFaceTypeGroups);
		StreamUtils.checkId(in, "PCNT");
		int nrOfFaceGroups = in.readInt();
		faceGroups = StreamUtils.loadIntArray(in, nrOfFaceGroups);
		StreamUtils.checkId(in, "PVTX");
		int nrOfIndexes = in.readInt();
		faces = StreamUtils.loadShortArray(in, nrOfIndexes);
		StreamUtils.checkId(in, "GNDX");
		int nrOfVertexGroups = in.readInt();
		byte[] vertexGroups = StreamUtils.loadByteArray(in, nrOfVertexGroups);
		StreamUtils.checkId(in, "MTGC");
		int nrOfMatrixGroups = in.readInt();
		int[] matrixGroups = StreamUtils.loadIntArray(in, nrOfMatrixGroups);
		StreamUtils.checkId(in, "MATS");
		int nrOfMatrixIndexes = in.readInt();
		int[] matrixIndexs = StreamUtils.loadIntArray(in, nrOfMatrixIndexes);
		materialId = in.readInt();
		selectionGroup = in.readInt();
		selectionType = in.readInt();
		boundsRadius = in.readFloat();
		minimumExtent = StreamUtils.loadVector3f(in);
		maximumExtent = StreamUtils.loadVector3f(in);
		int nrOfExtents = in.readInt();
		extent = new Extent[nrOfExtents];

		for (int i = 0; i < nrOfExtents; i++)
		{
			extent[i] = new Extent();
			extent[i].load(in);
		}

		StreamUtils.checkId(in, "UVAS");
		@SuppressWarnings("unused")
		int nrOfTextureVertexGroups = in.readInt();
		StreamUtils.checkId(in, "UVBS");
		int nrOfVertexTexturePositions = in.readInt();
		Vector2f[] vertexTexturePositions = StreamUtils.loadVector2fArray(in, nrOfVertexTexturePositions);
		Vector2f[] vertexTexturePositions2 = null;

		if (StreamUtils.checkOptionalId(in, "UVBS"))
		{
			StreamUtils.checkId(in, "UVBS");
			int nrOfVertexTexturePositions2 = in.readInt();
			vertexTexturePositions2 = StreamUtils.loadVector2fArray(in, nrOfVertexTexturePositions2);
		}

		this.vertexs = new Vertex[nrOfVertexPositions];

		for (int i = 0; i < this.vertexs.length; i++)
		{
			Vertex vertex = this.vertexs[i] = new Vertex();
			vertex.setPosition(new Point3f(vertexPositions[i]));
			vertex.setNormal(vertexNormals[i]);
			vertex.setUV(vertexTexturePositions[i]);
			vertex.setGroup(vertexGroups[i]);

			if (vertexTexturePositions2 != null)
			{
				vertex.setUV2(vertexTexturePositions2[i]);
			}

		}

		this.vertexGroups = this.buildVertexGroup(matrixGroups, matrixIndexs);
		this.renderProperty = new GeosetRenderProperty();
	}

	public void updateMatrices(MdxModel model)
	{
		VertexGroup[] vertexGroups = this.vertexGroups;
		int vertexGroupsLength = vertexGroups.length;

		for (int i = 0; i < vertexGroupsLength; i++)
		{
			VertexGroup group = vertexGroups[i];
			group.updateMatrix(model);
		}

		MaterialChunk materialChunk = model.materialChunk;
		Material material = null;

		if (materialChunk != null)
		{
			int materialId = this.materialId;
			Material[] materials = materialChunk.materials;

			if (-1 < materialId && materialId < materials.length)
			{
				material = materials[materialId];
			}

		}

		for (Vertex vertex : this.vertexs)
		{
			byte groupId = vertex.getGroup();

			if (-1 < groupId && groupId < vertexGroupsLength)
			{
				VertexGroup vertexGroup = vertexGroups[groupId];
				vertex.applyMatrix(vertexGroup);
			}

			if (material != null)
			{
				vertex.applyTextureMatrix(material);
			}

		}

	}

	public void updateAnimations(List<GeosetAnimation> animations)
	{
		this.renderProperty.updateAnimation(animations);
	}

	protected VertexGroup[] buildVertexGroup(int[] matrixGroups, int[] matrixIndexs)
	{
		VertexGroup[] groups = new VertexGroup[matrixGroups.length];
		int index = 0;

		for (int i = 0; i < groups.length; ++i)
		{
			VertexGroup group = groups[i] = new VertexGroup();
			int c = matrixGroups[i];
			group.nodes = new int[c];

			for (int j = 0; j < c; j++)
			{
				group.nodes[j] = matrixIndexs[index + j];
			}

			index += c;
		}

		return groups;
	}

	protected int getVertexGroupsSize()
	{
		int size = 0;

		for (int i = 0; i < this.vertexGroups.length; i++)
		{
			size += 4;
			size += this.vertexGroups[i].nodes.length * 4;
		}

		return size;
	}

	public boolean isUseUV2()
	{
		if (this.vertexs != null && this.vertexs.length > 0)
		{
			return this.vertexs[0].getUV2() != null;
		}

		return false;
	}

	public int getSize()
	{
		int a = 0;
		a += 4;
		a += 4;
		a += 4;
		a += 4 * this.vertexs.length * 3;
		a += 4;
		a += 4;
		a += 4 * this.vertexs.length * 3;
		a += 4;
		a += 4;
		a += 4 * faceTypeGroups.length;
		a += 4;
		a += 4;
		a += 4 * faceGroups.length;
		a += 4;
		a += 4;
		a += 2 * faces.length;
		a += 4;
		a += 4;
		a += 1 * this.vertexs.length;
		a += 4;
		a += 4;
		a += this.getVertexGroupsSize();
		a += 4;
		a += 4;
		a += 4;
		a += 4;
		a += 4;
		a += 4;
		a += 12;
		a += 12;
		a += 4;
		for (int i = 0; i < extent.length; i++)
		{
			a += extent[i].getSize();
		}
		a += 4;
		a += 4;
		a += 4;
		a += 4;
		a += 4 * this.vertexs.length * 2;

		if (this.isUseUV2() == true)
		{
			a += 4;
			a += 4;
			a += 4 * this.vertexs.length * 2;
		}

		return a;
	}

}
