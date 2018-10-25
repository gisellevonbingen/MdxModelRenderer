package giselle.wc3data.stream;

import java.io.IOException;

import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;

import giselle.wc3data.mdx.MdxModel;

public class StreamUtils
{
	public static MdxModel loadModel(BlizzardDataInputStream in) throws IOException
	{
		MdxModel model = new MdxModel();
		model.load(in);

		return model;
	}

	public static boolean checkOptionalId(BlizzardDataInputStream in, String name) throws IOException
	{
		in.mark(8);

		if (name.equals(in.readCharsAsString(4)))
		{
			in.reset();

			return true;
		}

		in.reset();

		return false;
	}

	public static String readOptionalId(BlizzardDataInputStream in) throws IOException
	{
		in.mark(8);
		char[] c = in.readChars(4);
		in.reset();

		int allEmpty = 0;

		for (int i = 0; i < 4; i++)
		{
			if ((short) c[0] == -1)
			{
				allEmpty++;
			}

		}

		if (allEmpty == 4)
		{
			return null;
		}

		return "" + c[0] + c[1] + c[2] + c[3];
	}

	public static void checkId(BlizzardDataInputStream in, String name) throws IOException
	{
		String found = in.readCharsAsString(4);

		if (!found.equals(name))
		{
			throw new IOException("Error loading model: CheckID failed, required " + name + " found " + found);
		}

	}

	/*
	 * public static boolean checkOptionalId(BlizzardDataInputStream in, String name) throws IOException { if(lastCheck == null){ lastCheck = in.readCharsAsString(4); }
	 * 
	 * return lastCheck.equals(name); }
	 * 
	 * public static void checkId(BlizzardDataInputStream in, String name) throws IOException {
	 * 
	 * if(lastCheck != null){
	 * 
	 * if(!name.equals(lastCheck)){ throw new IOException( "Error loading model: CheckID failed after optinal check, required " + name + " found " + lastCheck); }
	 * 
	 * lastCheck=null;
	 * 
	 * }else{
	 * 
	 * String found = in.readCharsAsString(4); if (!found.equals(name)) { throw new IOException("Error loading model: CheckID failed, required " + name + " found " + found); } } }
	 */

	public static byte[] loadByteArray(BlizzardDataInputStream in, int size) throws IOException
	{
		byte array[] = new byte[size];

		for (int i = 0; i < size; i++)
		{
			array[i] = in.readByte();
		}

		return array;
	}

	public static void saveByteArray(BlizzardDataOutputStream out, byte[] array) throws IOException
	{
		for (int i = 0; i < array.length; i++)
		{
			out.writeByte(array[i]);
		}

	}

	public static short[] loadShortArray(BlizzardDataInputStream in, int size) throws IOException
	{
		short array[] = new short[size];

		for (int i = 0; i < size; i++)
		{
			array[i] = (short) (in.readShort() & 0xFFFF);
		}

		return array;
	}

	public static void saveShortArray(BlizzardDataOutputStream out, short[] array) throws IOException
	{
		for (int i = 0; i < array.length; i++)
		{
			out.writeNByteInt(array[i], 2);
		}

	}

	public static char[] loadCharArray(BlizzardDataInputStream in, int size) throws IOException
	{
		char array[] = new char[size];

		for (int i = 0; i < size; i++)
		{
			array[i] = in.readChar();
		}

		return array;
	}

	public static int[] loadIntArray(BlizzardDataInputStream in, int size) throws IOException
	{
		int array[] = new int[size];

		for (int i = 0; i < size; i++)
		{
			array[i] = in.readInt();
		}

		return array;
	}

	public static void saveIntArray(BlizzardDataOutputStream out, int[] array) throws IOException
	{
		for (int i = 0; i < array.length; i++)
		{
			out.writeInt(array[i]);
		}

	}

	public static float[] loadFloatArray(BlizzardDataInputStream in, int size) throws IOException
	{
		float array[] = new float[size];

		for (int i = 0; i < size; i++)
		{
			array[i] = in.readFloat();
		}

		return array;
	}

	public static void saveFloatArray(BlizzardDataOutputStream out, float[] array) throws IOException
	{
		for (int i = 0; i < array.length; i++)
		{
			out.writeFloat(array[i]);
		}

	}

	public static void saveCharArray(BlizzardDataOutputStream out, char[] array) throws IOException
	{
		out.writeChars(array);
	}

	public static Vector2f loadVector2f(BlizzardDataInputStream in) throws IOException
	{
		Vector2f vector = new Vector2f();
		vector.x = in.readFloat();
		vector.y = in.readFloat();

		return vector;
	}

	public static Vector2f[] loadVector2fArray(BlizzardDataInputStream in, int length) throws IOException
	{
		Vector2f[] array = new Vector2f[length];

		for (int i = 0; i < length; i++)
		{
			array[i] = loadVector2f(in);
		}

		return array;
	}

	public static void saveVector2f(BlizzardDataOutputStream out, Vector2f value) throws IOException
	{
		out.writeFloat(value.x);
		out.writeFloat(value.y);
	}

	public static void saveVector2fArray(BlizzardDataOutputStream out, Vector2f[] array) throws IOException
	{
		for (int i = 0; i < array.length; i++)
		{
			saveVector2f(out, array[i]);
		}

	}

	public static Vector3f loadVector3f(BlizzardDataInputStream in) throws IOException
	{
		Vector3f vector = new Vector3f();
		vector.x = in.readFloat();
		vector.y = in.readFloat();
		vector.z = in.readFloat();

		return vector;
	}

	public static Vector3f[] loadVector3fArray(BlizzardDataInputStream in, int length) throws IOException
	{
		Vector3f[] array = new Vector3f[length];

		for (int i = 0; i < length; i++)
		{
			array[i] = loadVector3f(in);
		}

		return array;
	}

	public static void saveVector3f(BlizzardDataOutputStream out, Vector3f value) throws IOException
	{
		out.writeFloat(value.x);
		out.writeFloat(value.y);
		out.writeFloat(value.z);
	}

	public static void saveVector3fArray(BlizzardDataOutputStream out, Vector3f[] array) throws IOException
	{
		for (int i = 0; i < array.length; i++)
		{
			saveVector3f(out, array[i]);
		}

	}

}
