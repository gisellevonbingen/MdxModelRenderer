package giselle.mdx.settings;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.TextEvent;
import java.awt.event.TextListener;

public class SettingComponentColor extends Panel implements ISettingComponent
{
	private static final long serialVersionUID = -5100593206701281354L;

	private boolean textChanging = false;
	private TextField textField = null;

	public SettingComponentColor()
	{
		this.setLayout(null);

		this.addComponentListener(new ComponentAdapter()
		{
			@Override
			public void componentResized(ComponentEvent e)
			{
				super.componentResized(e);

				onResize();
			}

		});
		this.textField = new TextField();
		this.textField.addTextListener(new TextListener()
		{
			@Override
			public void textValueChanged(TextEvent e)
			{
				onTextChanged();
			}

		});
		this.add(this.textField);

		this.setColor(Color.white);
		this.onResize();

	}

	@Override
	public void bind(Object value)
	{
		this.setColor((Color) value);
	}

	@Override
	public Object parse()
	{
		return this.getColor();
	}

	private void onResize()
	{
		Dimension size = this.getSize();
		int width = size.width;
		int height = size.height;

		this.textField.setSize(width - this.textField.getX(), height);
		this.textField.setLocation(height, 0);
	}

	private void onTextChanged()
	{
		if (this.textChanging == true)
		{
			return;
		}

		try
		{
			this.textChanging = true;
			Color decode = Color.decode(this.textField.getText());
			this.setColor(decode);
		}
		catch (Exception e)
		{

		}
		finally
		{
			this.textChanging = false;
		}

	}

	@Override
	public void paint(Graphics g)
	{
		super.paint(g);

		g.setColor(Color.BLACK);
		g.drawRect(0, 0, this.textField.getX() - 1, this.textField.getHeight() - 1);
	}

	public Color getColor()
	{
		return this.getBackground();
	}

	public void setColor(Color c)
	{
		if (c == null)
		{
			c = Color.WHITE;
		}

		this.setBackground(c);

		if (this.textChanging == false)
		{
			this.updateText();
		}

	}

	private void updateText()
	{
		Color c = this.getColor();

		StringBuilder builder = new StringBuilder();
		builder.append("#");
		builder.append(String.format("%02X", c.getRed()));
		builder.append(String.format("%02X", c.getGreen()));
		builder.append(String.format("%02X", c.getBlue()));

		this.textField.setText(builder.toString());
	}

	@Override
	public Color getBackground()
	{
		return super.getBackground();
	}

	@Override
	public void setBackground(Color c)
	{
		super.setBackground(c);
	}

}
