package giselle.mdx.settings;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class SettingParameter<T>
{
	private final List<SettingParameter<?>> children;

	private final SettingParameter<?> parent;
	private final Field field;
	private final String name;

	public SettingParameter(SettingParameter<?> parent, Field field)
	{
		this(parent, field, field.getName());
	}

	public SettingParameter(SettingParameter<?> parent, Field field, String name)
	{
		this.children = new ArrayList<>();

		this.parent = parent;
		this.field = field;
		this.name = name;

		if (parent != null)
		{
			parent.children.add(this);
		}

	}

	public SettingParameter<?>[] getChildren()
	{
		SettingParameter<?>[] array = this.children.toArray(new SettingParameter<?>[0]);
		return array;
	}

	public List<DescendantInfo> getDescendants()
	{
		List<DescendantInfo> list = new ArrayList<DescendantInfo>();
		this.getDescendants(list, 0, 0);

		return list;
	}

	private List<DescendantInfo> getDescendants(List<DescendantInfo> list, int index, int level)
	{
		SettingParameter<?>[] children = this.getChildren();

		for (int i = 0; i < children.length; i++)
		{
			SettingParameter<?> child = children[i];
			list.add(new DescendantInfo(child, level, index, list.size()));

			child.getDescendants(list, i, level + 1);
		}

		return list;
	}

	public SettingParameter<?> getParent()
	{
		return this.parent;
	}

	public Field getField()
	{
		return this.field;
	}

	public String getName()
	{
		return this.name;
	}

	public static class DescendantInfo
	{
		public final SettingParameter<?> parameter;
		public final int level;
		public final int index;
		public final int totalIndex;

		public DescendantInfo(SettingParameter<?> parameter, int level, int index, int totalIndex)
		{
			this.parameter = parameter;
			this.level = level;
			this.index = index;
			this.totalIndex = totalIndex;
		}

	}

}
