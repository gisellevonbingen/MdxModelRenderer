package giselle.mdx;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.Panel;
import java.awt.Point;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class SimpleFrame<T extends Panel> extends Frame
{
	private static final long serialVersionUID = -5654180183581570699L;

	private final T panel;

	public SimpleFrame()
	{
		this.addComponentListener(new ComponentAdapter()
		{
			@Override
			public void componentResized(ComponentEvent e)
			{
				super.componentResized(e);

				onResize(e);
			}

		});

		this.addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				super.windowClosing(e);

				dispose();
			}

		});

		this.panel = this.createPanel();
		this.panel.setLayout(null);
		this.panel.setLocation(new Point(0, 0));
		this.add(this.panel);
	}

	protected void onResize(ComponentEvent e)
	{
		Dimension size = this.getSize();
		Insets insets = this.getInsets();
		this.panel.setBounds(insets.left, insets.top, size.width - insets.left - insets.right, size.height - insets.top - insets.bottom);
	}

	public T getPanel()
	{
		return this.panel;
	}

	@SuppressWarnings("unchecked")
	protected T createPanel()
	{
		return (T) new Panel();
	}

}
