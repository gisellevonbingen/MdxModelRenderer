package giselle.mdx.render;

import org.lwjgl.opengl.GL11;

import giselle.wc3data.mdx.Vertex;

public enum GeosetRenderMode
{
	Solid(GL11.GL_TRIANGLES)
	{
		@Override
		public void add(VertexDrawer drawer, Vertex v0, Vertex v1, Vertex v2)
		{
			super.add(drawer, v0, v1, v2);

			drawer.addVertexWithUV(v0);
			drawer.addVertexWithUV(v1);
			drawer.addVertexWithUV(v2);
		}
	},
	Wire(GL11.GL_LINES)
	{
		@Override
		public void add(VertexDrawer drawer, Vertex v0, Vertex v1, Vertex v2)
		{
			super.add(drawer, v0, v1, v2);

			drawer.addVertexWithUV(v0);
			drawer.addVertexWithUV(v1);

			drawer.addVertexWithUV(v1);
			drawer.addVertexWithUV(v2);

			drawer.addVertexWithUV(v2);
			drawer.addVertexWithUV(v0);
		}
	};

	private int id;

	private GeosetRenderMode(int id)
	{
		this.id = id;
	}

	public void add(VertexDrawer drawer, Vertex v0, Vertex v1, Vertex v2)
	{

	}

	public int getId()
	{
		return this.id;
	}

}
