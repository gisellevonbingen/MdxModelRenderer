package giselle.wc3data.util;

import javax.vecmath.Tuple3f;
import javax.vecmath.Vector3f;

public class Vector3fUtils
{
	public static Vector3f rotateYaw(Tuple3f vector, double yaw)
	{
		double cos = Math.cos(yaw);
		double sin = Math.sin(yaw);

		float d0 = (float) (vector.x * cos + vector.z * sin);
		float d1 = vector.y;
		float d2 = (float) (vector.z * cos - vector.x * sin);

		return new Vector3f(d0, d1, d2);
	}

	public static void clampMax(Vector3f vector, Tuple3f tuple)
	{
		vector.x = Math.max(vector.x, tuple.x);
		vector.y = Math.max(vector.y, tuple.y);
		vector.z = Math.max(vector.z, tuple.z);
	}

	public static void clampMin(Vector3f vector, Tuple3f tuple)
	{
		vector.x = Math.min(vector.x, tuple.x);
		vector.y = Math.min(vector.y, tuple.y);
		vector.z = Math.min(vector.z, tuple.z);
	}

	public static Vector3f max(Tuple3f v1, Tuple3f v2)
	{
		Vector3f v = new Vector3f();
		v.x = Math.max(v1.x, v2.x);
		v.y = Math.max(v1.y, v2.y);
		v.z = Math.max(v1.z, v2.z);

		return v;
	}

	public static Vector3f min(Tuple3f v1, Tuple3f v2)
	{
		Vector3f v = new Vector3f();
		v.x = Math.min(v1.x, v2.x);
		v.y = Math.min(v1.y, v2.y);
		v.z = Math.min(v1.z, v2.z);

		return v;
	}

}
