package giselle.wc3data.mdx;

public enum Particle2FilterMode
{
	BLEND(0), ADDITIVE(1), MODULATE(2), MODULATE2X(3), ADDALPHA(4),;

	private int id;

	private Particle2FilterMode(int id)
	{
		this.id = id;
	}

	public int getId()
	{
		return this.id;
	}

	public static Particle2FilterMode find(int id)
	{
		for (Particle2FilterMode particle2FilterMode : Particle2FilterMode.values())
		{
			if (particle2FilterMode.getId() == id)
			{
				return particle2FilterMode;
			}

		}

		return null;
	}

}
