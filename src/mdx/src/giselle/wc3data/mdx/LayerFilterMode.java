package giselle.wc3data.mdx;

public enum LayerFilterMode
{
	NONE(0), TRANSPARENT(1), BLEND(2), ADDITIVE(3), ADDALPHA(4), MODULATE(5), MODULATE2X(6),;

	private int id;

	private LayerFilterMode(int id)
	{
		this.id = id;
	}

	public int getId()
	{
		return this.id;
	}

	public static LayerFilterMode find(int id)
	{
		for (LayerFilterMode layerFilterMode : LayerFilterMode.values())
		{
			if (layerFilterMode.getId() == id)
			{
				return layerFilterMode;
			}

		}

		return null;
	}

}
