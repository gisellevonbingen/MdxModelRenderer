package giselle.wc3data.util;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class ExceptionUtils
{
	public static String toString(Exception e)
	{
		try
		{
			try (ByteArrayOutputStream baos = new ByteArrayOutputStream())
			{
				try (PrintStream ps = new PrintStream(baos))
				{
					e.printStackTrace(ps);
				}

				String string = baos.toString();
				return string;
			}

		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}

		return e.toString();
	}

	private ExceptionUtils()
	{

	}

}
