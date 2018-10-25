package giselle.wc3data.mdx;

import java.io.IOException;

import giselle.wc3data.stream.BlizzardDataInputStream;
import giselle.wc3data.stream.StreamUtils;

public class ParticleEmitter2 implements INodedObject
{
	public static final int Flags_XYQuad = 1 << 20;

	public Node node = new Node();

	public TransformationBoolean visibility;
	public static final String VisibilityKey = "KP2V";

	public float defaultEmissionRate;
	public TransformationFloat emissionRate;
	public static final String EmissionRateKey = "KP2E";

	public float defaultSpeed;
	public TransformationFloat speed;
	public static final String SpeedKey = "KP2S";

	public float defaultLatitude;
	public TransformationFloat latitude;
	public static final String LatitudeKey = "KP2L";

	public float defaultWidth;
	public TransformationFloat width;
	public static final String WidthKey = "KP2W";

	public float defaultLength;
	public TransformationFloat length;
	public static final String LengthKey = "KP2N";

	public Particle2Matrix matrix = null;

	public void load(BlizzardDataInputStream in) throws IOException
	{
		this.matrix = new Particle2Matrix();

		@SuppressWarnings("unused")
		int inclusiveSize = in.readInt();
		this.node = new Node();
		this.node.load(in);

		this.defaultSpeed = in.readFloat();
		this.matrix.variation = in.readFloat();
		this.defaultLatitude = in.readFloat();
		this.matrix.gravity = in.readFloat();
		this.matrix.lifespan = in.readFloat();
		this.defaultEmissionRate = in.readFloat();
		this.defaultLength = in.readFloat();
		this.defaultWidth = in.readFloat();
		this.matrix.filterMode = Particle2FilterMode.find(in.readInt());
		this.matrix.rows = in.readInt();
		this.matrix.columns = in.readInt();
		this.matrix.headOrTail = in.readInt();
		this.matrix.tailLength = in.readFloat();
		this.matrix.time = in.readFloat();

		float[] segmentColor = StreamUtils.loadFloatArray(in, 9);
		byte[] segmentAlpha = StreamUtils.loadByteArray(in, 3);
		float[] segmentScaling = StreamUtils.loadFloatArray(in, 3);

		for (int i = 0; i < this.matrix.segments.length; i++)
		{
			Particle2Segment segment = this.matrix.segments[i] = new Particle2Segment();
			float[] color = segment.color;
			System.arraycopy(segmentColor, color.length * i, color, 0, color.length);
			segment.alpha = Byte.toUnsignedInt(segmentAlpha[i]);
			segment.scaling = segmentScaling[i];
		}

		this.matrix.headIntervalStart = in.readInt();
		this.matrix.headIntervalEnd = in.readInt();
		this.matrix.headIntervalRepeat = in.readInt();
		this.matrix.headDecayIntervalStart = in.readInt();
		this.matrix.headDecayIntervalEnd = in.readInt();
		this.matrix.headDecayIntervalRepeat = in.readInt();
		this.matrix.tailIntervalStart = in.readInt();
		this.matrix.tailIntervalEnd = in.readInt();
		this.matrix.tailIntervalRepeat = in.readInt();
		this.matrix.tailDecayIntervalStart = in.readInt();
		this.matrix.tailDecayIntervalEnd = in.readInt();
		this.matrix.tailDecayIntervalRepeat = in.readInt();
		this.matrix.textureId = in.readInt();
		this.matrix.squirt = in.readInt();
		this.matrix.priorityPlane = in.readInt();
		this.matrix.replaceableId = in.readInt();

		for (int i = 0; i < 6; i++)
		{
			if (StreamUtils.checkOptionalId(in, VisibilityKey))
			{
				this.visibility = new TransformationBoolean(VisibilityKey, 1);
				this.visibility.load(in);
			}
			else if (StreamUtils.checkOptionalId(in, EmissionRateKey))
			{
				this.emissionRate = new TransformationFloat(EmissionRateKey, 1);
				this.emissionRate.load(in);
			}
			else if (StreamUtils.checkOptionalId(in, SpeedKey))
			{
				this.speed = new TransformationFloat(SpeedKey, 1);
				this.speed.load(in);
			}
			else if (StreamUtils.checkOptionalId(in, LatitudeKey))
			{
				this.latitude = new TransformationFloat(LatitudeKey, 1);
				this.latitude.load(in);
			}
			else if (StreamUtils.checkOptionalId(in, WidthKey))
			{
				this.width = new TransformationFloat(WidthKey, 1);
				this.width.load(in);
			}
			else if (StreamUtils.checkOptionalId(in, LengthKey))
			{
				this.length = new TransformationFloat(LengthKey, 1);
				this.length.load(in);
			}

		}

	}

	public void setTime(MdxModel model, int time)
	{
		Particle2Matrix matrix = this.matrix;
		matrix.visible = this.interpolationBoolean(model, time, this.visibility, true);
		matrix.emissionRate = this.interpolationFloat(model, time, this.emissionRate, this.defaultEmissionRate);
		matrix.speed = this.interpolationFloat(model, time, this.speed, this.defaultSpeed);
		matrix.latitude = this.interpolationFloat(model, time, this.latitude, this.defaultLatitude);
		matrix.width = this.interpolationFloat(model, time, this.width, this.defaultWidth);
		matrix.length = this.interpolationFloat(model, time, this.length, this.defaultLength);
	}

	public boolean interpolationBoolean(MdxModel model, int time, TransformationBoolean transformation, boolean defaultValue)
	{
		if (transformation != null)
		{
			return transformation.getInterpolationResult(model, time);
		}
		else
		{
			return defaultValue;
		}

	}

	public float interpolationFloat(MdxModel model, int time, TransformationFloat transformation, float defaultValue)
	{
		if (transformation != null)
		{
			float[] interpolationResult = transformation.getInterpolationResult(model, time);

			if (interpolationResult != null)
			{
				return interpolationResult[0];
			}

		}

		return defaultValue;
	}

	public int getSize()
	{
		int a = 0;
		a += 4;
		a += this.node.getSize();
		a += 4;
		a += 4;
		a += 4;
		a += 4;
		a += 4;
		a += 4;
		a += 4;
		a += 4;
		a += 4;
		a += 4;
		a += 4;
		a += 4;
		a += 4;
		a += 4;
		a += 36;
		a += 3;
		a += 12;
		a += 4;
		a += 4;
		a += 4;
		a += 4;
		a += 4;
		a += 4;
		a += 4;
		a += 4;
		a += 4;
		a += 4;
		a += 4;
		a += 4;
		a += 4;
		a += 4;
		a += 4;
		a += 4;

		if (this.visibility != null)
		{
			a += this.visibility.getSize();
		}
		if (this.emissionRate != null)
		{
			a += this.emissionRate.getSize();
		}
		if (this.width != null)
		{
			a += this.width.getSize();
		}
		if (this.length != null)
		{
			a += this.length.getSize();
		}
		if (this.speed != null)
		{
			a += this.speed.getSize();
		}
		if (this.latitude != null)
		{
			a += this.latitude.getSize();
		}

		return a;
	}

	@Override
	public Node getNode()
	{
		return this.node;
	}

	@Override
	public NodeType getNodeType()
	{
		return NodeType.Particle2;
	}

}
