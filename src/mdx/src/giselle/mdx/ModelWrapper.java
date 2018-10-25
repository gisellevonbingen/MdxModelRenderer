package giselle.mdx;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import giselle.wc3data.mdx.MdxModel;

public class ModelWrapper
{
	private String directory;
	private MdxModel model;

	public ModelWrapper(String directory, MdxModel model)
	{
		this.directory = directory;
		this.model = model;
	}

	public InputStream openInputStream(String fileName) throws FileNotFoundException
	{
		return new FileInputStream(this.directory + fileName);
	}

	public String getDirectory()
	{
		return directory;
	}

	public MdxModel getModel()
	{
		return model;
	}

}
