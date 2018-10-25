package giselle.mdx;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.Timer;

public class MainPanel extends Panel
{
	private static final long serialVersionUID = 7541305836570187124L;

	private Label statusLabel;

	private RenderCanvas canvas;

	private Timer timer;

	public MainPanel()
	{
		this.setLayout(null);

		this.statusLabel = new Label();
		this.statusLabel.setBackground(new Color(0xF0F0F0));
		this.add(this.statusLabel);

		this.canvas = new RenderCanvas();
		this.add(this.canvas);

		this.addComponentListener(new ComponentAdapter()
		{
			@Override
			public void componentResized(ComponentEvent e)
			{
				super.componentResized(e);

				onResize(e);
			}

		});

		this.timer = new Timer(50, this::onTimer);
	}

	private void onTimer(ActionEvent e)
	{
		this.statusLabel.setText(" " + this.canvas.getRenderToString());
	}

	@Override
	public void addNotify()
	{
		super.addNotify();

		this.timer.start();
	}

	@Override
	public void removeNotify()
	{
		super.removeNotify();

		this.timer.stop();
	}

	public RenderCanvas getRenderCanvas()
	{
		return this.canvas;
	}

	private void onResize(ComponentEvent e)
	{
		Dimension size = this.getSize();

		this.statusLabel.setLocation(0, 0);
		this.statusLabel.setSize(size.width, 30);

		this.canvas.setLocation(0, this.statusLabel.getY() + this.statusLabel.getHeight());
		this.canvas.setSize(size.width, size.height - this.canvas.getY());
	}

	public void bind(ModelWrapper modelWrapper)
	{
		this.canvas.selectModelWrapper(modelWrapper);
	}

}
