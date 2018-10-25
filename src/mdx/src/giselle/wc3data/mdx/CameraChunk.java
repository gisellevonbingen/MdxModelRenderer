package giselle.wc3data.mdx;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import giselle.wc3data.stream.BlizzardDataInputStream;
import giselle.wc3data.stream.StreamUtils;

public class CameraChunk
{
	public Camera[] cameras = new Camera[0];

	public static final String key = "CAMS";

	public void load(BlizzardDataInputStream in) throws IOException
	{
		StreamUtils.checkId(in, key);
		int chunkSize = in.readInt();
		List<Camera> cameraList = new ArrayList<>();
		int cameraCounter = chunkSize;
		while (cameraCounter > 0)
		{
			Camera tempcamera = new Camera();
			cameraList.add(tempcamera);
			tempcamera.load(in);
			cameraCounter -= tempcamera.getSize();
		}
		cameras = cameraList.toArray(new Camera[cameraList.size()]);
	}

	public int getSize()
	{
		int a = 0;
		a += 4;
		a += 4;
		for (int i = 0; i < cameras.length; i++)
		{
			a += cameras[i].getSize();
		}

		return a;
	}

}
