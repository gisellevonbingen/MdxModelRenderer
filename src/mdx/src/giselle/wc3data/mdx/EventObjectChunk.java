package giselle.wc3data.mdx;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import giselle.wc3data.stream.BlizzardDataInputStream;
import giselle.wc3data.stream.StreamUtils;

public class EventObjectChunk
{
	public EventObject[] eventObjects = new EventObject[0];

	public static final String key = "EVTS";

	public void load(BlizzardDataInputStream in) throws IOException
	{
		StreamUtils.checkId(in, key);
		int chunkSize = in.readInt();
		List<EventObject> eventObjectList = new ArrayList<>();
		int eventObjectCounter = chunkSize;
		while (eventObjectCounter > 0)
		{
			EventObject tempeventObject = new EventObject();
			eventObjectList.add(tempeventObject);
			tempeventObject.load(in);
			eventObjectCounter -= tempeventObject.getSize();
		}
		eventObjects = eventObjectList.toArray(new EventObject[eventObjectList.size()]);
	}

	public int getSize()
	{
		int a = 0;
		a += 4;
		a += 4;
		for (int i = 0; i < eventObjects.length; i++)
		{
			a += eventObjects[i].getSize();
		}

		return a;
	}

}
