package giselle.wc3data.mdx;

import java.io.IOException;

import giselle.wc3data.stream.BlizzardDataInputStream;
import giselle.wc3data.stream.StreamUtils;

public class Attachment
{
	public Node node = new Node();
	public String unknownName = "";
	public int unknownNull;
	public int attachmentId;
	public AttachmentVisibility attachmentVisibility;

	public void load(BlizzardDataInputStream in) throws IOException
	{
		@SuppressWarnings("unused")
		int inclusiveSize = in.readInt();
		node = new Node();
		node.load(in);
		unknownName = in.readCharsAsString(256);
		unknownNull = in.readInt();
		attachmentId = in.readInt();
		if (StreamUtils.checkOptionalId(in, AttachmentVisibility.key))
		{
			attachmentVisibility = new AttachmentVisibility();
			attachmentVisibility.load(in);
		}

	}

	public int getSize()
	{
		int a = 0;
		a += 4;
		a += node.getSize();
		a += 256;
		a += 4;
		a += 4;
		if (attachmentVisibility != null)
		{
			a += attachmentVisibility.getSize();
		}

		return a;
	}
}
