package giselle.wc3data.mdx;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import giselle.wc3data.stream.BlizzardDataInputStream;
import giselle.wc3data.stream.StreamUtils;

public class ParticleEmitterChunk
{
	public ParticleEmitter[] particleEmitters = new ParticleEmitter[0];

	public static final String key = "PREM";

	public void load(BlizzardDataInputStream in) throws IOException
	{
		StreamUtils.checkId(in, key);
		int chunkSize = in.readInt();
		List<ParticleEmitter> particleEmitterList = new ArrayList<ParticleEmitter>();
		int particleEmitterCounter = chunkSize;
		
		while (particleEmitterCounter > 0)
		{
			ParticleEmitter tempparticleEmitter = new ParticleEmitter();
			particleEmitterList.add(tempparticleEmitter);
			tempparticleEmitter.load(in);
			particleEmitterCounter -= tempparticleEmitter.getSize();
		}
		
		particleEmitters = particleEmitterList.toArray(new ParticleEmitter[particleEmitterList.size()]);
	}

	public int getSize()
	{
		int a = 0;
		a += 4;
		a += 4;
		for (int i = 0; i < particleEmitters.length; i++)
		{
			a += particleEmitters[i].getSize();
		}

		return a;
	}

}
