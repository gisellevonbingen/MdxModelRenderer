package giselle.wc3data.mdx;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import giselle.wc3data.stream.BlizzardDataInputStream;
import giselle.wc3data.stream.StreamUtils;

public class AttachmentChunk
{
	public Attachment[] attachments = new Attachment[0];

	public static final String key = "ATCH";

	public void load(BlizzardDataInputStream in) throws IOException
	{
		StreamUtils.checkId(in, key);
		int chunkSize = in.readInt();
		List<Attachment> attachmentList = new ArrayList<>();
		int attachmentCounter = chunkSize;
		while (attachmentCounter > 0)
		{
			Attachment tempattachment = new Attachment();
			attachmentList.add(tempattachment);
			tempattachment.load(in);
			attachmentCounter -= tempattachment.getSize();
		}

		attachments = attachmentList.toArray(new Attachment[attachmentList.size()]);
	}

	public int getSize()
	{
		int a = 0;
		a += 4;
		a += 4;
		for (int i = 0; i < attachments.length; i++)
		{
			a += attachments[i].getSize();
		}

		return a;
	}

}
