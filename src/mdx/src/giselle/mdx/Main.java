package giselle.mdx;

import java.util.prefs.BackingStoreException;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;

public class Main
{
	public static void main(String[] args) throws BackingStoreException
	{
		MainFrame frame = null;

		try
		{
			frame = new MainFrame();
			frame.setVisible(true);

			RenderCanvas renderCanvas = frame.getRenderCanvas();
			Display.setParent(renderCanvas);
			Display.setVSyncEnabled(false);

			Display.create();
			renderCanvas.initGL();

			while (Display.isCloseRequested() == false && frame.isCloseRequested() == false)
			{
				renderCanvas.renderGL();
				Display.update();
				Display.sync(60);
			}

			Display.destroy();
		}
		catch (LWJGLException e)
		{
			e.printStackTrace();
		}

		if (frame != null)
		{
			frame.dispose();
		}

	}

}
