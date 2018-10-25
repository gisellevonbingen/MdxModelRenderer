package giselle.wc3data.mdx;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import giselle.wc3data.stream.BlizzardDataInputStream;
import giselle.wc3data.stream.StreamUtils;

public class MdxModel
{
	public static final String key = "MDLX";

	public VersionChunk versionChunk;
	public ModelChunk modelChunk;
	public SequenceChunk sequenceChunk;
	public MaterialChunk materialChunk;
	public TextureChunk textureChunk;
	public TextureAnimationChunk textureAnimationChunk;
	public GeosetChunk geosetChunk;
	public GeosetAnimationChunk geosetAnimationChunk;
	public LightChunk lightChunk;
	public AttachmentChunk attachmentChunk;
	public ParticleEmitterChunk particleEmitterChunk;
	public ParticleEmitter2Chunk particleEmitter2Chunk;
	public RibbonEmitterChunk ribbonEmitterChunk;
	public EventObjectChunk eventObjectChunk;
	public CameraChunk cameraChunk;
	public CollisionShapeChunk collisionShapeChunk;

	private GlobalSequenceManager globalSequenceManager = null;
	private BoneManager boneManager = null;

	public MdxModel()
	{
		this.globalSequenceManager = new GlobalSequenceManager();
		this.boneManager = new BoneManager();
	}

	public void updateGlobalAnimation(int time)
	{
		GlobalSequence[] globalSequences = this.globalSequenceManager.getGlobalSequences();

		for (GlobalSequence globalSequence : globalSequences)
		{
			globalSequence.setTimeRepeat(time);
		}

	}

	public void updateMatrix(int time)
	{
		this.boneManager.setTime(this, time);

		ParticleEmitter2Chunk particleEmitter2Chunk = this.particleEmitter2Chunk;

		if (particleEmitter2Chunk != null)
		{
			particleEmitter2Chunk.setTime(this, time);
		}

		GeosetAnimationChunk geosetAnimationChunk = this.geosetAnimationChunk;

		if (geosetAnimationChunk != null)
		{
			geosetAnimationChunk.setTime(this, time);
		}

		MaterialChunk materialChunk = this.materialChunk;

		if (materialChunk != null)
		{
			materialChunk.setTime(this, time);
		}

		GeosetChunk geosetChunk = this.geosetChunk;

		if (geosetChunk != null)
		{
			Geoset[] geosets = geosetChunk.geosets;

			for (int i = 0; i < geosets.length; i++)
			{
				Geoset geoset = geosets[i];
				geoset.updateMatrices(this);

				if (geosetAnimationChunk != null)
				{
					List<GeosetAnimation> animations = this.getGeosetAnimations(geosetAnimationChunk, i);
					geoset.updateAnimations(animations);
				}

			}

		}

	}

	private List<GeosetAnimation> getGeosetAnimations(GeosetAnimationChunk geosetAnimationChunk, int geosetId)
	{
		List<GeosetAnimation> list = new ArrayList<>();

		for (GeosetAnimation animation : geosetAnimationChunk.geosetAnimations)
		{
			if (animation.geosetId == geosetId)
			{
				list.add(animation);
			}

		}

		return list;
	}

	public void load(BlizzardDataInputStream in) throws IOException
	{
		BoneChunk boneChunk = null;
		HelperChunk helperChunk = null;
		PivotPointChunk pivotPointChunk = null;
		GlobalSequenceChunk globalSequenceChunk = null;

		StreamUtils.checkId(in, key);

		String chunkId;;
		while ((chunkId = StreamUtils.readOptionalId(in)) != null)
		{

			switch (chunkId)
			{
				case VersionChunk.key:
					versionChunk = new VersionChunk();
					versionChunk.load(in);
					break;
				case ModelChunk.key:
					modelChunk = new ModelChunk();
					modelChunk.load(in);
					break;
				case SequenceChunk.key:
					sequenceChunk = new SequenceChunk();
					sequenceChunk.load(in);
					break;
				case GlobalSequenceChunk.key:
					globalSequenceChunk = new GlobalSequenceChunk();
					globalSequenceChunk.load(in);
					break;
				case MaterialChunk.key:
					materialChunk = new MaterialChunk();
					materialChunk.load(in);
					break;
				case TextureChunk.key:
					textureChunk = new TextureChunk();
					textureChunk.load(in);
					break;
				case TextureAnimationChunk.key:
					textureAnimationChunk = new TextureAnimationChunk();
					textureAnimationChunk.load(in);
					break;
				case GeosetChunk.key:
					geosetChunk = new GeosetChunk();
					geosetChunk.load(in);
					break;
				case GeosetAnimationChunk.key:
					geosetAnimationChunk = new GeosetAnimationChunk();
					geosetAnimationChunk.load(in);
					break;
				case BoneChunk.key:
					boneChunk = new BoneChunk();
					boneChunk.load(in);
					break;
				case LightChunk.key:
					lightChunk = new LightChunk();
					lightChunk.load(in);
					break;
				case HelperChunk.key:
					helperChunk = new HelperChunk();
					helperChunk.load(in);
					break;
				case AttachmentChunk.key:
					attachmentChunk = new AttachmentChunk();
					attachmentChunk.load(in);
					break;
				case PivotPointChunk.key:
					pivotPointChunk = new PivotPointChunk();
					pivotPointChunk.load(in);
					break;
				case ParticleEmitterChunk.key:
					particleEmitterChunk = new ParticleEmitterChunk();
					particleEmitterChunk.load(in);
					break;
				case ParticleEmitter2Chunk.key:
					particleEmitter2Chunk = new ParticleEmitter2Chunk();
					particleEmitter2Chunk.load(in);
					break;
				case RibbonEmitterChunk.key:
					ribbonEmitterChunk = new RibbonEmitterChunk();
					ribbonEmitterChunk.load(in);
					break;
				case EventObjectChunk.key:
					eventObjectChunk = new EventObjectChunk();
					eventObjectChunk.load(in);
					break;
				case CameraChunk.key:
					cameraChunk = new CameraChunk();
					cameraChunk.load(in);
					break;
				case CollisionShapeChunk.key:
					collisionShapeChunk = new CollisionShapeChunk();
					collisionShapeChunk.load(in);
					break;
				default:
					StreamUtils.checkId(in, chunkId);
					int chunkSize = in.readInt();
					in.read(new byte[chunkSize]);
			}

		}

		this.globalSequenceManager.build(globalSequenceChunk);

		SkeletonBuilder builder = new SkeletonBuilder();

		if (boneChunk != null)
		{
			builder.addAll(boneChunk.bones);
		}

		if (helperChunk != null)
		{
			builder.addAll(helperChunk.helpers);
		}

		if (this.particleEmitter2Chunk != null)
		{
			builder.addAll(this.particleEmitter2Chunk.particles);
		}

		builder.setPivotChunk(pivotPointChunk);
		this.boneManager.build(builder);

		this.updateMatrix(-1);
	}

	public GlobalSequenceManager getGlobalSequenceManager()
	{
		return this.globalSequenceManager;
	}

	public BoneManager getBoneManager()
	{
		return this.boneManager;
	}

}
