package giselle.mdx.settings;

import java.awt.TextField;

public class SettingComponentFloat extends TextField implements ISettingComponent
{
	private static final long serialVersionUID = 8487655249866963157L;

	public SettingComponentFloat()
	{

	}

	@Override
	public void bind(Object value)
	{
		Float state = (Float) value;
		this.setText(state.toString());
	}

	@Override
	public Object parse()
	{
		float value = Float.parseFloat(this.getText());
		return value;
	}

}
