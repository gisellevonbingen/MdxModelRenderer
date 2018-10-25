package giselle.wc3data.mdx;

public enum FilterMode
{
	NONE(0), TRANSPARENT(1), BLEND(2), ADDITIVE(3), ADDALPHA(4), MODULATE(5), MODULATE2X(6),;

	private int id;

	private FilterMode(int id)
	{
		this.id = id;
	}

	public int getId()
	{
		return this.id;
	}

}
