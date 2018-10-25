package giselle.mdx.render;

import javax.vecmath.Point3f;
import javax.vecmath.Vector2f;

import org.lwjgl.opengl.GL11;

import giselle.wc3data.mdx.Vertex;

public class VertexDrawer
{
	public int layerid = -1;;
	public float wireWidth = 1.0F;

	public float colorR;
	public float colorG;
	public float colorB;
	public float colorA;

	public VertexDrawer()
	{

	}

	public void setColor(int rgba)
	{
		int a = ((rgba >> 0x18) & 0xFF);
		int r = ((rgba >> 0x10) & 0xFF);
		int g = ((rgba >> 0x08) & 0xFF);
		int b = ((rgba >> 0x00) & 0xFF);

		this.setColor(r, g, b, a);
	}

	public void setColor(int r, int g, int b, int a)
	{
		this.colorR = r / 255.F;
		this.colorG = g / 255.F;
		this.colorB = b / 255.F;
		this.colorA = a / 255.F;
	}

	public void setColor(float r, float g, float b, float a)
	{
		this.colorR = r;
		this.colorG = g;
		this.colorB = b;
		this.colorA = a;
	}

	public void begin(int mode)
	{
		GL11.glLineWidth(this.wireWidth);
		GL11.glBegin(mode);
	}

	public void addVertexWithUV(float x, float y, float z, float u, float v)
	{
		GL11.glColor4f(this.colorR, this.colorG, this.colorB, this.colorA);
		GL11.glTexCoord2f(u, v);
		GL11.glVertex3f(x, y, z);
	}

	public void addVertexWithUV(Vertex vertex)
	{
		Point3f p = vertex.getMatrixedPosition();
		Vector2f t = vertex.getMatrixedUV(this.layerid);

		if (p == null)
		{
			p = vertex.getPosition();
		}

		if (t == null)
		{
			t = vertex.getUV();
		}

		this.addVertexWithUV(p.x, p.y, p.z, t.x, t.y);
	}

	public void end()
	{
		GL11.glEnd();
	}

	public int getLayerid()
	{
		return this.layerid;
	}

}
