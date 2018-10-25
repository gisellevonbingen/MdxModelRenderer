package giselle.wc3data.mdx;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import giselle.wc3data.stream.BlizzardDataInputStream;
import giselle.wc3data.stream.StreamUtils;

public class ParticleEmitter2Chunk
{
	public ParticleEmitter2[] particles = new ParticleEmitter2[0];

	public static final String key = "PRE2";

	public void load(BlizzardDataInputStream in) throws IOException
	{
		StreamUtils.checkId(in, key);
		int chunkSize = in.readInt();
		List<ParticleEmitter2> particleEmitter2List = new ArrayList<>();
		int particleEmitter2Counter = chunkSize;

		while (particleEmitter2Counter > 0)
		{
			ParticleEmitter2 tempparticleEmitter2 = new ParticleEmitter2();
			particleEmitter2List.add(tempparticleEmitter2);
			tempparticleEmitter2.load(in);
			particleEmitter2Counter -= tempparticleEmitter2.getSize();
		}

		this.particles = particleEmitter2List.toArray(new ParticleEmitter2[particleEmitter2List.size()]);
	}

	public void setTime(MdxModel model, int time)
	{
		ParticleEmitter2[] particles = this.particles;

		for (int i = 0; i < particles.length; i++)
		{
			particles[i].setTime(model, time);
		}

	}

	public int getSize()
	{
		int a = 0;
		a += 4;
		a += 4;

		for (int i = 0; i < particles.length; i++)
		{
			a += particles[i].getSize();
		}

		return a;
	}

}
