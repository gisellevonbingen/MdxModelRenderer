package giselle.mdx;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Vector3f;

import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import giselle.mdx.render.MdxModelRenderer;
import giselle.mdx.render.MdxParticle;
import giselle.mdx.render.TextureManager;
import giselle.mdx.settings.RenderSettings;
import giselle.wc3data.mdx.Geoset;
import giselle.wc3data.mdx.GeosetChunk;
import giselle.wc3data.util.Vector3fUtils;

public class RenderCanvas extends Canvas
{
	private static final long serialVersionUID = 8704751294355457866L;

	private boolean dirty = false;

	private long lastRenderTicks = 0L;
	private float fps = 0.0F;
	private float delta = 0.0F;

	private boolean mousePrevDown = false;
	private float scale = 0.0F;
	private Vector3f rotation = new Vector3f();

	private Vector3f cameraOffset = new Vector3f();
	private Vector3f modelOffset = new Vector3f();
	private Vector3f renderOffset = new Vector3f();

	private RenderSettings settings;
	private TextureManager textureManager;
	private MdxModelRenderer renderer;
	private List<MdxParticle> particles;
	private boolean clearRequested = false;
	private boolean isPause = false;

	public RenderCanvas()
	{
		super();

		this.textureManager = new TextureManager();
		this.renderer = new MdxModelRenderer();
		this.renderer.setTextureManager(this.textureManager);
		this.particles = new ArrayList<>();

		this.addComponentListener(new ComponentAdapter()
		{
			@Override
			public void componentResized(ComponentEvent e)
			{
				super.componentResized(e);

				onResize(e);
			}

		});

		this.selectModelWrapper(null);
	}

	public void setSettings(RenderSettings settings)
	{
		this.settings = settings;
		this.renderer.setSettings(settings);

		this.dirty = true;

	}

	public void selectModelWrapper(ModelWrapper modelWrapper)
	{
		this.clearRequested = true;
		this.renderer.setModelWrapper(modelWrapper);

		this.resetTransform();
	}

	public ModelWrapper getModelWrapper()
	{
		return this.renderer.getModelWrapper();
	}

	public MdxModelRenderer getRenderer()
	{
		return this.renderer;
	}

	public void resetTransform()
	{
		ModelWrapper modelWrapper = this.renderer.getModelWrapper();

		this.scale = 1.0F;
		this.rotation.set(0.0F, 0.0F, -45.0F);
		this.cameraOffset.set(-500.0F, 0.0F, 0.0F);
		this.renderOffset.set(0.0F, 0.0F, 0.0F);

		if (modelWrapper != null)
		{
			GeosetChunk geosetChunk = modelWrapper.getModel().geosetChunk;
			Geoset[] geosets = geosetChunk.geosets;
			Vector3f min = null;
			Vector3f max = null;

			for (int i = 0; i < geosets.length; i++)
			{
				Geoset geoset = geosets[i];

				if (min == null)
				{
					min = geoset.minimumExtent;
				}
				else
				{
					min = Vector3fUtils.min(min, geoset.minimumExtent);
				}

				if (max == null)
				{
					max = geoset.maximumExtent;
				}
				else
				{
					max = Vector3fUtils.max(max, geoset.maximumExtent);
				}

			}

			if (min == null)
			{
				min = new Vector3f();
			}

			if (max == null)
			{
				max = new Vector3f();
			}

			Vector3f modelSize = new Vector3f();
			modelSize.x = max.x - min.x;
			modelSize.y = max.y - min.y;
			modelSize.z = max.z - min.z;
			this.renderOffset.set(0.0F, 0.0F, (-modelSize.z / 2 - min.z));

			Dimension controlSize = this.getSize();
			float fy = controlSize.width / (modelSize.y - this.renderOffset.y * 2);
			float fz = controlSize.height / (modelSize.z - this.renderOffset.z * 2);

			this.scale = Math.min(fy, fz);
		}

		this.modelOffset.set(0.0F, 0.0F, 0.0F);
	}

	public void initGL()
	{
		Dimension size = this.getSize();
		float w = size.width;
		float h = size.height;

		GL11.glViewport(0, 0, size.width, size.height);

		// GL11.glShadeModel(GL11.GL_SMOOTH);

		Color color = this.settings.backgroundColor;
		float r = color.getRed() / 255.0F;
		float g = color.getGreen() / 255.0F;
		float b = color.getBlue() / 255.0F;
		GL11.glClearColor(r, g, b, 0);
		GL11.glClearDepth(1.0f);

		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthFunc(GL11.GL_LEQUAL);

		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();

		float f = w / h;
		GLU.gluPerspective(45.0f, f, 1.0f, 1000.0f);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST);

	}

	public void renderGL()
	{
		if (this.dirty == true)
		{
			this.dirty = false;

			this.initGL();
		}

		if (this.clearRequested == true)
		{
			this.clearRequested = false;

			this.particles.clear();
			this.textureManager.clear();
		}

		this.calcFPS();
		float delta = this.delta;
		boolean isPause = this.isPause;

		this.handleInput(delta);

		this.setupRendering();
		this.renderOrientLine();

		if (isPause == false)
		{
			this.renderer.update(delta);
		}

		this.renderModel(delta);
		this.updateParticles(delta);

		this.renderParticles(delta);
	}

	private void renderModel(float delta)
	{
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColor3f(1.0F, 1.0F, 1.0F);

		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glTranslatef(this.modelOffset.x, this.modelOffset.y, this.modelOffset.z);

		this.renderer.render(delta);
	}

	private void renderParticles(float delta)
	{
		for (MdxParticle particle : this.particles)
		{
			GL11.glPushMatrix();
			particle.render(delta, this.rotation);
			GL11.glPopMatrix();
		}

	}

	private void updateParticles(float delta)
	{
		List<MdxParticle> particles = this.particles;
		List<MdxParticle> removes = new ArrayList<>();

		for (MdxParticle particle : particles)
		{
			if (particle.updateLife(delta) == false)
			{
				removes.add(particle);
			}

		}

		particles.removeAll(removes);
		particles.addAll(this.renderer.createParticles(delta));

	}

	private void setupRendering()
	{
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glLoadIdentity();

		GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
		GL11.glRotatef(-90.0F, 0.0F, 0.0F, 1.0F);
		GL11.glScaled(0.01F, 0.01F, 0.01F);
		GL11.glTranslatef(this.cameraOffset.x, this.cameraOffset.y, this.cameraOffset.z);

		GL11.glScaled(this.scale, this.scale, this.scale);
		GL11.glRotatef(this.rotation.x, 1.0F, 0.0F, 0.0F);
		GL11.glRotatef(this.rotation.y, 0.0F, -1.0F, 0.0F);
		GL11.glRotatef(this.rotation.z, 0.0F, 0.0F, 1.0F);
		GL11.glTranslatef(this.renderOffset.x, this.renderOffset.y, this.renderOffset.z);
	}

	private void renderOrientLine()
	{
		if (this.settings.orientLineRender == false)
		{
			return;
		}

		float width = this.settings.orientLineWidth;
		float length = this.settings.orientLineLength;

		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glLineWidth(width);

		GL11.glColor4f(1.0F, 0.0F, 0.0F, 1.0F);
		this.renderLineFromOrient(length, 0.0F, 0.0F);

		GL11.glColor4f(0.0F, 1.0F, 0.0F, 1.0F);
		this.renderLineFromOrient(0.0F, length, 0.0F);

		GL11.glColor4f(0.0F, 0.0F, 1.0F, 1.0F);
		this.renderLineFromOrient(0.0F, 0.0F, length);
	}

	private void renderLineFromOrient(float x, float y, float z)
	{
		GL11.glBegin(GL11.GL_LINES);
		GL11.glVertex3d(0.0F, 0.0F, 0.0F);
		GL11.glVertex3d(x, y, z);
		GL11.glEnd();
	}

	private void handleInput(float delta)
	{
		int dw = Mouse.getDWheel();

		if (dw != 0)
		{
			if (dw > 0)
			{
				this.scale *= 1.1F;
			}
			else
			{
				this.scale /= 1.1F;
			}

		}

		boolean mouseDown = Mouse.isButtonDown(0);
		int mouseDX = Mouse.getDX();
		int mouseDY = Mouse.getDY();

		if (mouseDown == true)
		{
			if (this.mousePrevDown == false)
			{
				Display.update();
			}
			else
			{
				this.rotation.z += mouseDX;
				this.rotation.y += mouseDY;
			}

		}

		float keyboardSpeed = 180.0F * delta;

		if (Keyboard.isKeyDown(Keyboard.KEY_A) == true)
		{
			this.rotation.z -= keyboardSpeed;
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_D) == true)
		{
			this.rotation.z += keyboardSpeed;
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_W) == true)
		{
			this.rotation.y += keyboardSpeed;
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_S) == true)
		{
			this.rotation.y -= keyboardSpeed;
		}

		float keyboardZoom = 1 + (2.00F * delta);

		if (Keyboard.isKeyDown(Keyboard.KEY_Q) == true)
		{
			this.scale *= keyboardZoom;
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_E) == true)
		{
			this.scale /= keyboardZoom;
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_R) == true)
		{
			this.resetTransform();
		}

		this.rotation.x = this.rotation.x % 360.0F;
		this.rotation.y = Math.min(Math.max(-90.0F, this.rotation.y), +90.0F);
		this.rotation.z = this.rotation.z % 360.0F;

		this.mousePrevDown = mouseDown;
	}

	private void calcFPS()
	{
		long cu = Sys.getTime();
		float f = 1000.0F;

		if (this.lastRenderTicks == 0)
		{
			this.lastRenderTicks = cu;
		}

		long diff = cu - this.lastRenderTicks;

		this.fps = f / diff;
		this.delta = 1.0F / this.fps;
		this.lastRenderTicks = cu;
	}

	private void onResize(ComponentEvent e)
	{
		this.dirty = true;
	}

	public String getRenderToString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append(String.format("FPS=%.2f, ", this.fps));
		builder.append(String.format("Roation={X=%.2f, Y=%.2f, Z=%.2f}, ", this.rotation.x, this.rotation.y, this.rotation.z));
		builder.append(String.format("Zoom=%.4f, ", this.scale));

		return builder.toString();
	}

	public void setPause(boolean isPause)
	{
		this.isPause = isPause;
	}

	public boolean isPause()
	{
		return this.isPause;
	}

}
