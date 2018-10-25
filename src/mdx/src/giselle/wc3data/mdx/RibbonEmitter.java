package giselle.wc3data.mdx;

import java.io.IOException;

import giselle.wc3data.stream.BlizzardDataInputStream;
import giselle.wc3data.stream.StreamUtils;

public class RibbonEmitter
{
	public Node node = new Node();
	public float heightAbove;
	public float heightBelow;
	public float alpha;
	public float[] color = new float[3];
	public float lifeSpan;
	public int unknownNull;
	public int emissionRate;
	public int rows;
	public int columns;
	public int materialId;
	public float gravity;
	public RibbonEmitterVisibility ribbonEmitterVisibility;
	public RibbonEmitterHeightAbove ribbonEmitterHeightAbove;
	public RibbonEmitterHeightBelow ribbonEmitterHeightBelow;

	public void load(BlizzardDataInputStream in) throws IOException
	{
		@SuppressWarnings("unused")
		int inclusiveSize = in.readInt();
		node = new Node();
		node.load(in);
		heightAbove = in.readFloat();
		heightBelow = in.readFloat();
		alpha = in.readFloat();
		color = StreamUtils.loadFloatArray(in, 3);
		lifeSpan = in.readFloat();
		unknownNull = in.readInt();
		emissionRate = in.readInt();
		rows = in.readInt();
		columns = in.readInt();
		materialId = in.readInt();
		gravity = in.readFloat();
		for (int i = 0; i < 3; i++)
		{
			if (StreamUtils.checkOptionalId(in, RibbonEmitterVisibility.key))
			{
				ribbonEmitterVisibility = new RibbonEmitterVisibility();
				ribbonEmitterVisibility.load(in);
			}
			else if (StreamUtils.checkOptionalId(in, RibbonEmitterHeightAbove.key))
			{
				ribbonEmitterHeightAbove = new RibbonEmitterHeightAbove();
				ribbonEmitterHeightAbove.load(in);
			}
			else if (StreamUtils.checkOptionalId(in, RibbonEmitterHeightBelow.key))
			{
				ribbonEmitterHeightBelow = new RibbonEmitterHeightBelow();
				ribbonEmitterHeightBelow.load(in);
			}

		}
	}

	public int getSize()
	{
		int a = 0;
		a += 4;
		a += node.getSize();
		a += 4;
		a += 4;
		a += 4;
		a += 12;
		a += 4;
		a += 4;
		a += 4;
		a += 4;
		a += 4;
		a += 4;
		a += 4;
		if (ribbonEmitterVisibility != null)
		{
			a += ribbonEmitterVisibility.getSize();
		}
		if (ribbonEmitterHeightAbove != null)
		{
			a += ribbonEmitterHeightAbove.getSize();
		}
		if (ribbonEmitterHeightBelow != null)
		{
			a += ribbonEmitterHeightBelow.getSize();
		}

		return a;
	}
}
