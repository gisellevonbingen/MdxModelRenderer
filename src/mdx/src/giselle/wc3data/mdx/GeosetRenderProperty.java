package giselle.wc3data.mdx;

import java.util.List;

import javax.vecmath.Vector3f;

public class GeosetRenderProperty
{
	public float alpha = 1.0F;
	public Vector3f color = new Vector3f(1.0F, 1.0F, 1.0F);

	public GeosetRenderProperty()
	{

	}

	public void updateAnimation(List<GeosetAnimation> animations)
	{
		int size = animations.size();

		if (size == 0)
		{
			this.alpha = 1.0F;
			this.color = new Vector3f(1.0F, 1.0F, 1.0F);
		}
		else
		{
			float alpha = 0.0F;
			Vector3f color = new Vector3f();

			for (int i = 0; i < size; i++)
			{
				GeosetAnimation animation = animations.get(i);
				alpha += animation.alphaMatrix;
				color.add(animation.colorMatrix);
			}

			this.alpha = alpha / size;
			color.scale(1.0F / size);
			this.color = color;
		}

	}

}
