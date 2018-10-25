package giselle.mdx.render;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.vecmath.Matrix3f;
import javax.vecmath.Point3f;
import javax.vecmath.Tuple3f;

import org.lwjgl.opengl.GL11;

import giselle.mdx.ModelWrapper;
import giselle.mdx.settings.RenderSettings;
import giselle.wc3data.mdx.BoneData;
import giselle.wc3data.mdx.BoneManager;
import giselle.wc3data.mdx.Geoset;
import giselle.wc3data.mdx.GeosetChunk;
import giselle.wc3data.mdx.GeosetRenderProperty;
import giselle.wc3data.mdx.Layer;
import giselle.wc3data.mdx.Material;
import giselle.wc3data.mdx.MdxModel;
import giselle.wc3data.mdx.Node;
import giselle.wc3data.mdx.Particle2Matrix;
import giselle.wc3data.mdx.ParticleEmitter2;
import giselle.wc3data.mdx.ParticleEmitter2Chunk;
import giselle.wc3data.mdx.Sequence;
import giselle.wc3data.mdx.SequenceChunk;
import giselle.wc3data.mdx.Texture;
import giselle.wc3data.mdx.Vertex;
import giselle.wc3data.util.GLStateUtils;

public class MdxModelRenderer
{
	private RenderSettings settings;

	private TextureManager textureManager;

	private ModelWrapper modelWrapper;

	private SequenceLoop sequenceLoop = SequenceLoop.Default;
	private int sequenceIndex = -1;
	private double sequenceDuration = 0.0D;
	private double globaSequenceTime = 0.0D;

	private float[] remainEmissions = null;

	public MdxModelRenderer()
	{

	}

	public void setSettings(RenderSettings settings)
	{
		this.settings = settings;
	}

	private void updateAnimation(MdxModel model, float delta)
	{
		float deltaMillis = delta * 1000;
		this.sequenceDuration += deltaMillis;
		this.globaSequenceTime += deltaMillis;

		int sequenceIndex = this.sequenceIndex;
		Sequence sequence = this.getSequence(model, sequenceIndex);

		if (sequence == null)
		{
			this.setSequenceIndex(-1);
		}
		else
		{
			if (sequence.intervalStart + this.sequenceDuration >= sequence.intervalEnd)
			{
				int newId = -1;

				if (this.sequenceLoop == SequenceLoop.Default)
				{
					if (sequence.nonLooping == 0)
					{
						newId = sequenceIndex;
					}

				}
				else if (this.sequenceLoop == SequenceLoop.Always)
				{
					newId = sequenceIndex;
				}

				this.setSequenceIndex(newId);
			}

		}

	}

	public Sequence getSequence(int sequenceIndex)
	{
		ModelWrapper modelWrapper = this.modelWrapper;

		if (modelWrapper != null)
		{
			MdxModel model = modelWrapper.getModel();
			return this.getSequence(model, sequenceIndex);
		}

		return null;
	}

	private Sequence getSequence(MdxModel model, int sequenceIndex)
	{
		Sequence sequence = null;
		SequenceChunk chunk = model.sequenceChunk;

		if (chunk != null)
		{
			Sequence[] sequences = chunk.sequences;

			if (-1 < sequenceIndex && sequenceIndex < sequences.length)
			{
				sequence = sequences[sequenceIndex];
			}

		}

		return sequence;
	}

	public void update(float delta)
	{
		ModelWrapper modelWrapper = this.modelWrapper;

		if (modelWrapper == null)
		{
			return;
		}

		MdxModel model = modelWrapper.getModel();
		this.updateAnimation(model, delta);
	}

	public void render(float delta)
	{
		ModelWrapper modelWrapper = this.modelWrapper;

		if (modelWrapper == null)
		{
			return;
		}

		this.applyAnimation(modelWrapper);

		if (this.settings.modelSolidRender == true)
		{
			this.renderGeosets(modelWrapper, GeosetRenderMode.Solid);
		}

		if (this.settings.modelWireRender == true)
		{
			this.renderGeosets(modelWrapper, GeosetRenderMode.Wire);
		}

		if (this.settings.modelBoneRender == true)
		{
			this.renderBone(modelWrapper);
		}

	}

	private void applyAnimation(ModelWrapper modelWrapper)
	{
		MdxModel model = modelWrapper.getModel();
		model.updateGlobalAnimation((int) this.globaSequenceTime);
		Sequence sequence = this.getSequence(this.sequenceIndex);

		if (sequence != null)
		{
			model.updateMatrix((int) (sequence.intervalStart + this.sequenceDuration));
		}
		else
		{
			model.updateMatrix(-1);
		}

	}

	private void renderBone(ModelWrapper modelWrapper)
	{
		MdxModel model = modelWrapper.getModel();
		BoneManager boneManager = model.getBoneManager();
		BoneData root = boneManager.getRoot();

		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GLStateUtils.set(GL11.GL_DEPTH_TEST, this.settings.modelBoneDepthTest);

		VertexDrawer drawer = new VertexDrawer();
		drawer.layerid = -1;
		drawer.wireWidth = this.settings.modelBoneWidth;
		drawer.setColor(0xFF000000 | this.settings.modelBoneColor.getRGB());
		drawer.begin(GL11.GL_LINES);

		this.renderBone(drawer, root);

		drawer.end();
	}

	private void renderBone(VertexDrawer drawer, BoneData bone)
	{
		Point3f pivot1 = bone.getMatrixedPivot();
		List<BoneData> children = bone.getChildren();

		for (int i = 0; i < children.size(); ++i)
		{
			BoneData child = children.get(i);
			Point3f pivot2 = child.getMatrixedPivot();

			drawer.addVertexWithUV(pivot1.x, pivot1.y, pivot1.z, 0.0F, 0.0F);
			drawer.addVertexWithUV(pivot2.x, pivot2.y, pivot2.z, 0.0F, 0.0F);

			this.renderBone(drawer, child);

		}

	}

	private void renderGeosets(ModelWrapper modelWrapper, GeosetRenderMode mode)
	{
		MdxModel model = modelWrapper.getModel();
		GeosetChunk geosetChunk = model.geosetChunk;

		if (geosetChunk == null)
		{
			return;
		}

		Geoset[] geosets = geosetChunk.geosets;

		for (int i = 0; i < geosets.length; ++i)
		{
			Geoset geoset = geosets[i];

			GL11.glPushMatrix();
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glEnable(GL11.GL_DEPTH_TEST);

			this.renderGeoset(modelWrapper, geoset, mode);

			GL11.glPopMatrix();
		}

	}

	private void renderGeoset(ModelWrapper modelWrapper, Geoset geoset, GeosetRenderMode mode)
	{
		MdxModel model = modelWrapper.getModel();
		Material[] materials = model.materialChunk.materials;
		GeosetRenderProperty renderProperty = geoset.renderProperty;

		short[] faces = geoset.faces;
		Vertex[] vertexs = geoset.vertexs;
		Material material = materials[geoset.materialId];
		Layer[] layers = material.layerChunk.layers;

		for (int j = 0; j < layers.length; j++)
		{
			Layer layer = layers[j];

			Tuple3f color = renderProperty.color;
			float alpha = renderProperty.alpha * layer.alphaTranslration;

			if (alpha > 0.0F)
			{
				this.enableLayer(modelWrapper, renderProperty, layer);
				VertexDrawer drawer = new VertexDrawer();
				drawer.layerid = j;
				drawer.wireWidth = this.settings.modelWireWidth;
				drawer.setColor(color.x, color.y, color.z, alpha);
				drawer.begin(mode.getId());

				for (int k = 0; k < faces.length; k += 3)
				{
					Vertex v0 = vertexs[faces[k + 0]];
					Vertex v1 = vertexs[faces[k + 1]];
					Vertex v2 = vertexs[faces[k + 2]];
					mode.add(drawer, v0, v1, v2);
				}

				drawer.end();
				this.disableLayer(layer);
			}

		}

	}

	public void enableLayer(ModelWrapper modelWrapper, GeosetRenderProperty renderProperty, Layer layer)
	{
		MdxModel model = modelWrapper.getModel();
		int textureId = layer.textureId;
		Texture texture = model.textureChunk.textures[textureId];

		boolean useTwoSided = (layer.shadingFlags & Layer.ShadingFlags_TWO_SIDED) == Layer.ShadingFlags_TWO_SIDED;
		GLStateUtils.set(GL11.GL_CULL_FACE, !useTwoSided);

		GLStateUtils.set(GL11.GL_ALPHA_TEST, true);
		GL11.glAlphaFunc(GL11.GL_GREATER, 0.0f);
		GL11.glDepthMask(true);
		MdxLayerFilterMode.applyLayerFilterMode(layer.filterMode, true);

		TextureObject texture2 = this.textureManager.get(modelWrapper, texture.fileName.replace("\\", "/"));
		texture2.bind();
	}

	public void disableLayer(Layer layer)
	{
		MdxLayerFilterMode.applyLayerFilterMode(layer.filterMode, false);

		GLStateUtils.set(GL11.GL_CULL_FACE, false);
	}

	public List<MdxParticle> createParticles(float delta)
	{
		List<MdxParticle> list = new ArrayList<MdxParticle>();
		ParticleEmitter2[] emitters = this.getParticle2Emitters(modelWrapper);

		if (emitters != null)
		{
			for (int i = 0; i < emitters.length; i++)
			{
				ParticleEmitter2 emitter = emitters[i];
				list.addAll(this.createParticles(emitter, i, delta));
			}

		}

		return list;
	}

	private List<MdxParticle> createParticles(ParticleEmitter2 emitter, int index, float delta)
	{
		Particle2Matrix matrix = emitter.matrix;
		List<MdxParticle> list = new ArrayList<MdxParticle>();
		float[] remainEmissions = this.remainEmissions;

		if (matrix.visible == true)
		{
			remainEmissions[index] += delta;

			float emissionRate = matrix.emissionRate;

			if (emissionRate > 0)
			{
				float e = 1.0F / emissionRate;

				while (remainEmissions[index] >= e)
				{
					remainEmissions[index] -= e;

					MdxParticle particle = this.createParticle(emitter);
					list.add(particle);
				}

			}

		}
		else
		{
			remainEmissions[index] = 0.0F;
		}

		return list;
	}

	private MdxParticle createParticle(ParticleEmitter2 emitter)
	{
		ModelWrapper modelWrapper = this.modelWrapper;
		MdxModel model = modelWrapper.getModel();

		Particle2Matrix matrix = emitter.matrix;
		BoneManager boneManager = model.getBoneManager();

		Node node = emitter.node;
		BoneData boneData = boneManager.get(node.objectId);
		Point3f pivot = boneData.getMatrixedPivot();

		Matrix3f r = new Matrix3f();
		boneData.getMatrix().getRotationScale(r);

		double xo = (Math.random() * 2 - 1.0D) * matrix.width;
		double yo = (Math.random() * 2 - 1.0D) * matrix.length;
		double zo = 0.0D;

		double dy = Math.random() * 360.0D;
		double dp = (Math.random() * matrix.latitude) - 90;
		double ry = Math.toRadians(dy);
		double rp = Math.toRadians(dp);

		float variation = matrix.variation;
		double variationedYawSpeed = matrix.speed + (Math.random() * 2 - 1.0D) * variation;
		double variationedPitchSpeed = matrix.speed + Math.random() * variation;
		double motionX = -Math.sin(ry) * Math.cos(rp) * variationedYawSpeed;
		double motionY = Math.cos(ry) * Math.cos(rp) * variationedYawSpeed;
		double motionZ = -Math.sin(rp) * variationedPitchSpeed;

		MdxParticle particle = new MdxParticle(modelWrapper, emitter);
		particle.totalLife = matrix.lifespan * 1000;
		particle.position.x = (float) (pivot.x + xo);
		particle.position.y = (float) (pivot.y + yo);
		particle.position.z = (float) (pivot.z + zo);
		particle.vector.x = (float) (motionX);
		particle.vector.y = (float) (motionY);
		particle.vector.z = (float) (motionZ);
		particle.rotation.x = (float) dy;
		particle.rotation.y = (float) dp;
		particle.setTextureManager(this.textureManager);

		return particle;
	}

	public void setModelWrapper(ModelWrapper modelWrapper)
	{
		this.modelWrapper = modelWrapper;
		this.setSequenceIndex(-1);

		ParticleEmitter2[] emitters = this.getParticle2Emitters(modelWrapper);

		if (emitters != null)
		{
			this.remainEmissions = new float[emitters.length];
			Arrays.fill(this.remainEmissions, 0.0F);
		}
		else
		{
			this.remainEmissions = null;
		}

	}

	private ParticleEmitter2[] getParticle2Emitters(ModelWrapper modelWrapper)
	{
		if (modelWrapper != null)
		{
			MdxModel model = modelWrapper.getModel();
			ParticleEmitter2Chunk particleEmitter2Chunk = model.particleEmitter2Chunk;

			if (particleEmitter2Chunk != null)
			{
				ParticleEmitter2[] emitters = particleEmitter2Chunk.particles;
				return emitters;
			}

		}

		return null;
	}

	public ModelWrapper getModelWrapper()
	{
		return this.modelWrapper;
	}

	public void setSequenceIndex(int sequenceIndex)
	{
		this.sequenceIndex = sequenceIndex;
		this.sequenceDuration = 0;
	}

	public int getSequenceIndex()
	{
		return this.sequenceIndex;
	}

	public void setSequenceDuration(double sequenceDuration)
	{
		this.sequenceDuration = sequenceDuration;
	}

	public double getSequenceDuration()
	{
		return this.sequenceDuration;
	}

	public void setSequenceLoop(SequenceLoop sequenceLoop)
	{
		this.sequenceLoop = sequenceLoop;
	}

	public SequenceLoop getSequenceLoop()
	{
		return sequenceLoop;
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
