package giselle.wc3data.mdx;

public enum Interpolation
{
	NONE(false), LINEAR(false), HERMITE(true), BEZIER(true);
	
	private boolean useTan;

	private Interpolation(boolean useTan)
	{
		this.useTan = useTan;
	}
	
	public boolean isUseTan()
	{
		return useTan;
	}
	
}
