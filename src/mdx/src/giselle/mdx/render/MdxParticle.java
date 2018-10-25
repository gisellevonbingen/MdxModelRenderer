package giselle.mdx.render;

import javax.vecmath.Point3f;
import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;

import org.lwjgl.opengl.GL11;

import giselle.mdx.ModelWrapper;
import giselle.wc3data.mdx.MdxModel;
import giselle.wc3data.mdx.Particle2Matrix;
import giselle.wc3data.mdx.Particle2Segment;
import giselle.wc3data.mdx.ParticleEmitter2;
import giselle.wc3data.util.MaskUtils;

public class MdxParticle
{
	private final ModelWrapper modelWrapper;
	private final ParticleEmitter2 emitter;
	private TextureManager textureManager;

	public boolean isDead = false;
	public double totalLife = 0.0D;
	public double elapseLife = 0.0D;

	public final Point3f position;
	public final Vector3f vector;
	public final Vector2f rotation;

	public MdxParticle(ModelWrapper modelWrapper, ParticleEmitter2 emitter)
	{
		this.modelWrapper = modelWrapper;
		this.emitter = emitter;

		this.position = new Point3f();
		this.vector = new Vector3f();
		this.rotation = new Vector2f();
	}

	public boolean updateLife(float delta)
	{
		if (this.isDead == true)
		{
			return false;
		}

		float deltaMillise = delta * 1000;
		this.elapseLife += deltaMillise;

		if (this.elapseLife >= this.totalLife)
		{
			this.isDead = true;
			return false;
		}
		else
		{
			this.updatePosition(delta);
		}

		return true;
	}

	private void updatePosition(float delta)
	{
		Point3f position = this.position;
		Vector3f vector = this.vector;
		Particle2Matrix emitterMatrix = this.emitter.matrix;

		float dx = vector.x;
		float dy = vector.y;
		float dz = vector.z - emitterMatrix.gravity;

		Point3f newPosition = new Point3f();
		newPosition.x = position.x + (dx * delta);
		newPosition.y = position.y + (dy * delta);
		newPosition.z = position.z + (dz * delta);

		position.set(newPosition);
	}

	public void render(float delta, Vector3f rotation2)
	{
		ModelWrapper modelWrapper = this.modelWrapper;
		MdxModel model = modelWrapper.getModel();
		ParticleEmitter2 emitter = this.emitter;
		Particle2Matrix matrix = emitter.matrix;

		String fileName = model.textureChunk.textures[matrix.textureId].fileName;
		TextureObject texture = this.textureManager.get(modelWrapper, fileName);

		int flags = emitter.node.flags;
		boolean xyQuad = MaskUtils.testMask(flags, ParticleEmitter2.Flags_XYQuad);

		double halfTime = matrix.time;
		double elapseLife = this.elapseLife;
		double totalLife = this.totalLife;
		boolean isAppering = this.isAppering(halfTime, elapseLife);

		Particle2Segment segment0 = matrix.segments[0];
		Particle2Segment segment1 = matrix.segments[1];
		Particle2Segment segment2 = matrix.segments[2];
		float scaling = this.interpolation(segment0.scaling, segment1.scaling, segment2.scaling, halfTime, elapseLife, totalLife);
		float colorA = this.interpolation(segment0.alpha, segment1.alpha, segment2.alpha, halfTime, elapseLife, totalLife) / 255.0F;
		float colorR = this.interpolation(segment0.color[0], segment1.color[0], segment2.color[0], halfTime, elapseLife, totalLife);
		float colorG = this.interpolation(segment0.color[1], segment1.color[1], segment2.color[1], halfTime, elapseLife, totalLife);
		float colorB = this.interpolation(segment0.color[2], segment1.color[2], segment2.color[2], halfTime, elapseLife, totalLife);

		int cols = matrix.columns;
		int rows = matrix.rows;
		float uw = 1.0F / cols;
		float vw = 1.0F / rows;

		int index = 0;

		if (isAppering == true)
		{
			index = this.getTextureIndex(matrix.headIntervalStart, matrix.headIntervalEnd, matrix.headIntervalRepeat, elapseLife, halfTime);
		}
		else
		{
			index = this.getTextureIndex(matrix.headDecayIntervalStart, matrix.headDecayIntervalEnd, matrix.headDecayIntervalRepeat, elapseLife - halfTime, totalLife - halfTime);
		}

		int col = index % cols;
		int row = index / cols;
		float u1 = (col + 0) * uw;
		float v1 = (row + 0) * vw;
		float u2 = (col + 1) * uw;
		float v2 = (row + 1) * vw;

		float hw = scaling;
		float hh = scaling;
		Point3f position = this.position;

		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glTranslatef(position.x, position.y, position.z);
		MdxLayerFilterMode.applyPartcile2FilterMode(matrix.filterMode, true);
		texture.bind();

		VertexDrawer drawer = new VertexDrawer();
		drawer.layerid = -1;
		drawer.wireWidth = 2.0F;
		drawer.setColor(colorR, colorG, colorB, colorA);
		drawer.begin(GL11.GL_QUADS);
		this.addVertexWithUV(drawer, -hw, -hh, 0.0F, u1, v1, xyQuad);
		this.addVertexWithUV(drawer, -hw, +hh, 0.0F, u2, v1, xyQuad);
		this.addVertexWithUV(drawer, +hw, +hh, 0.0F, u2, v2, xyQuad);
		this.addVertexWithUV(drawer, +hw, -hh, 0.0F, u1, v2, xyQuad);
		drawer.end();
		MdxLayerFilterMode.applyPartcile2FilterMode(matrix.filterMode, false);
	}

	private void addVertexWithUV(VertexDrawer drawer, float x, float y, float z, float u, float v, boolean xyQuad)
	{
		if (xyQuad == true)
		{
			drawer.addVertexWithUV(x, y, z, u, v);
		}
		else
		{
			drawer.addVertexWithUV(x, y, z, u, v);
		}

	}

	private int getTextureIndex(int start, int end, int repeat, double t1, double t2)
	{
		double ratio = t1 / t2;

		int indexDelta = end - start;
		int index = start + (int) (ratio * indexDelta);

		return index;
	}

	private boolean isAppering(double halfTime, double elapseTime)
	{
		return elapseTime < halfTime;
	}

	private float interpolation(float value1, float value2, float value3, double halfTime, double elapseTime, double totalTime)
	{
		boolean isAppering = this.isAppering(halfTime, elapseTime);

		if (isAppering == true)
		{
			return this.interpolation(value1, value2, elapseTime, totalTime);
		}
		else
		{
			return this.interpolation(value2, value3, elapseTime, totalTime);
		}

	}

	private float interpolation(float value1, float value2, double elapseTime, double totalTime)
	{
		double ratio = elapseTime / totalTime;

		double delta = (value2 - value1) * ratio;
		double value = value1 + delta;

		return (float) value;
	}

	public ModelWrapper getModelWrapper()
	{
		return this.modelWrapper;
	}

	public ParticleEmitter2 getEmitter()
	{
		return this.emitter;
	}

	public TextureManager getTextureManager()
	{
		return textureManager;
	}

	public void setTextureManager(TextureManager textureManager)
	{
		this.textureManager = textureManager;
	}

}
