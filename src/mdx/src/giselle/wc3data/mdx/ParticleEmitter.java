package giselle.wc3data.mdx;

import java.io.IOException;

import giselle.wc3data.stream.BlizzardDataInputStream;
import giselle.wc3data.stream.StreamUtils;

public class ParticleEmitter
{
	public Node node = new Node();
	public float emissionRate;
	public float gravity;
	public float longitude;
	public float latitude;
	public String spawnModelFileName = "";
	public int unknownNull;
	public float lifeSpan;
	public float initialVelocity;
	public ParticleEmitterVisibility particleEmitterVisibility;

	public void load(BlizzardDataInputStream in) throws IOException
	{
		@SuppressWarnings("unused")
		int inclusiveSize = in.readInt();
		node = new Node();
		node.load(in);
		emissionRate = in.readFloat();
		gravity = in.readFloat();
		longitude = in.readFloat();
		latitude = in.readFloat();
		spawnModelFileName = in.readCharsAsString(256);
		unknownNull = in.readInt();
		lifeSpan = in.readFloat();
		initialVelocity = in.readFloat();
		
		if (StreamUtils.checkOptionalId(in, ParticleEmitterVisibility.key))
		{
			particleEmitterVisibility = new ParticleEmitterVisibility();
			particleEmitterVisibility.load(in);
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
		a += 4;
		a += 256;
		a += 4;
		a += 4;
		a += 4;
		if (particleEmitterVisibility != null)
		{
			a += particleEmitterVisibility.getSize();
		}

		return a;
	}
}
