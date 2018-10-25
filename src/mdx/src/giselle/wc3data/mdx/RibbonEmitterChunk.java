package giselle.wc3data.mdx;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import giselle.wc3data.stream.BlizzardDataInputStream;
import giselle.wc3data.stream.StreamUtils;

public class RibbonEmitterChunk
{
	public RibbonEmitter[] ribbonEmitters = new RibbonEmitter[0];

	public static final String key = "RIBB";

	public void load(BlizzardDataInputStream in) throws IOException
	{
		StreamUtils.checkId(in, key);
		int chunkSize = in.readInt();
		List<RibbonEmitter> ribbonEmitterList = new ArrayList<>();
		int ribbonEmitterCounter = chunkSize;
		while (ribbonEmitterCounter > 0)
		{
			RibbonEmitter tempribbonEmitter = new RibbonEmitter();
			ribbonEmitterList.add(tempribbonEmitter);
			tempribbonEmitter.load(in);
			ribbonEmitterCounter -= tempribbonEmitter.getSize();
		}
		ribbonEmitters = ribbonEmitterList.toArray(new RibbonEmitter[ribbonEmitterList.size()]);
	}

	public int getSize()
	{
		int a = 0;
		a += 4;
		a += 4;
		for (int i = 0; i < ribbonEmitters.length; i++)
		{
			a += ribbonEmitters[i].getSize();
		}

		return a;
	}

}
