package giselle.wc3data.mdx;

public class Particle2Matrix
{
	public boolean visible;
	public float emissionRate;
	public float speed;
	public float variation;
	public float latitude;
	public float width;
	public float length;
	public float gravity;

	public float lifespan;
	public Particle2FilterMode filterMode;
	public int rows;
	public int columns;
	public int headOrTail;
	public float tailLength;
	public float time;
	public Particle2Segment[] segments = new Particle2Segment[3];
	public int headIntervalStart;
	public int headIntervalEnd;
	public int headIntervalRepeat;
	public int headDecayIntervalStart;
	public int headDecayIntervalEnd;
	public int headDecayIntervalRepeat;
	public int tailIntervalStart;
	public int tailIntervalEnd;
	public int tailIntervalRepeat;
	public int tailDecayIntervalStart;
	public int tailDecayIntervalEnd;
	public int tailDecayIntervalRepeat;
	public int textureId;
	public int squirt;
	public int priorityPlane;
	public int replaceableId;

	public Particle2Matrix()
	{

	}

}
