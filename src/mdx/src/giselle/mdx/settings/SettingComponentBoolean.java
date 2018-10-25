package giselle.mdx.settings;

import java.awt.Checkbox;

public class SettingComponentBoolean extends Checkbox implements ISettingComponent
{
	private static final long serialVersionUID = 8487655249866963157L;

	public SettingComponentBoolean()
	{

	}

	@Override
	public void bind(Object value)
	{
		boolean state = (boolean) value;
		this.setState(state);
	}

	@Override
	public Object parse()
	{
		boolean state = this.getState();
		return state;
	}

}
