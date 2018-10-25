package giselle.mdx;

import java.awt.Button;
import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Label;
import java.awt.List;
import java.awt.Panel;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Scrollbar;
import java.awt.event.ActionEvent;
import java.awt.event.AdjustmentEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.util.Arrays;

import javax.swing.Timer;

import giselle.mdx.render.MdxModelRenderer;
import giselle.mdx.render.SequenceLoop;
import giselle.wc3data.mdx.MdxModel;
import giselle.wc3data.mdx.Sequence;
import giselle.wc3data.mdx.SequenceChunk;

public class SequencesFrame extends SimpleFrame<Panel>
{
	private static final String loopCheckboxSuffix = " Loop";
	private static final long serialVersionUID = -6605356173359434358L;

	private final RenderCanvas canvas;
	private final MdxModelRenderer renderer;

	private List list;

	private CheckboxGroup checkboxGroup;
	private Checkbox[] loopCheckboxs;

	private Button playButton;
	private Button pauseButton;
	private Scrollbar scrollbar;
	private Label durationLabel;

	private Timer timer;

	public SequencesFrame(RenderCanvas canvas)
	{
		this.canvas = canvas;
		this.renderer = canvas.getRenderer();

		Panel panel = this.getPanel();
		this.setTitle("Sequences");
		this.setResizable(false);

		this.list = new List();
		this.list.select(-1);
		this.list.addItemListener(this::onListSelected);
		panel.add(this.list);

		SequenceLoop[] loops = SequenceLoop.values();
		this.checkboxGroup = new CheckboxGroup();
		this.loopCheckboxs = new Checkbox[loops.length];

		for (int i = 0; i < loops.length; i++)
		{
			SequenceLoop loop = loops[i];
			Checkbox checkbox = this.loopCheckboxs[i] = new Checkbox();
			checkbox.setCheckboxGroup(this.checkboxGroup);
			checkbox.setLabel(loop.name() + loopCheckboxSuffix);
			checkbox.addItemListener(this::onListSelected);
			panel.add(checkbox);

			if (loop == SequenceLoop.Default)
			{
				checkbox.setState(true);
			}

		}

		this.playButton = new Button();
		this.playButton.setLabel("Play");
		this.playButton.addActionListener(this::onPlayButtonClick);
		panel.add(this.playButton);

		this.pauseButton = new Button();
		this.pauseButton.addActionListener(this::onPauseButtonClick);
		this.updatePauseButtonLabel();
		panel.add(this.pauseButton);

		this.scrollbar = new Scrollbar(Scrollbar.HORIZONTAL);
		this.scrollbar.setBackground(Color.black);
		this.scrollbar.addAdjustmentListener(this::onScrollbarAdjustment);
		panel.add(this.scrollbar);

		this.durationLabel = new Label();
		this.durationLabel.setAlignment(Label.CENTER);
		panel.add(this.durationLabel);

		this.timer = new Timer(50, this::onTimer);

		this.onResize(null);
		Rectangle bottomBounds = this.durationLabel.getBounds();
		panel.setPreferredSize(new Dimension(270, bottomBounds.y + bottomBounds.height + 10));
		panel.setSize(panel.getPreferredSize());
		this.pack();

		this.setLocationRelativeTo(null);
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

	private void onTimer(ActionEvent e)
	{
		this.updateScrollbar();
	}

	public void bind(MdxModel model)
	{
		SequenceChunk chunk = model.sequenceChunk;
		Sequence[] sequences = new Sequence[0];

		if (chunk != null)
		{
			sequences = chunk.sequences;
		}

		this.bind(sequences);
	}

	public void bind(Sequence[] sequences)
	{
		this.list.removeAll();
		this.list.add("<None>");

		for (int i = 0; i < sequences.length; i++)
		{
			this.list.add(sequences[i].name);
		}

		this.list.select(0);
		this.play();
	}

	private void onPauseButtonClick(ActionEvent e)
	{
		if (this.renderer.getSequenceIndex() != -1)
		{
			boolean pause = this.canvas.isPause();
			this.setPauseState(!pause);
		}

	}

	private void setPauseState(boolean pause)
	{
		this.canvas.setPause(pause);
		this.updatePauseButtonLabel();
	}

	private void updatePauseButtonLabel()
	{
		boolean pause = this.canvas.isPause();
		this.pauseButton.setLabel(pause ? "Resume" : "Pause");
		this.pauseButton.repaint();
	}

	private void onPlayButtonClick(ActionEvent e)
	{
		this.play();
	}

	private void onListSelected(ItemEvent e)
	{
		this.play();
	}

	private void play()
	{
		int index = Math.max(-1, this.list.getSelectedIndex() - 1);
		SequenceLoop loop = this.getSelectedLoop();
		this.renderer.setSequenceLoop(loop);
		this.renderer.setSequenceIndex(index);
		this.setPauseState(false);

		this.updatePauseButtonLabel();
		this.updateScrollbar();
	}

	private void updateScrollbar()
	{
		int sequenceIndex = this.renderer.getSequenceIndex();
		Sequence sequence = this.renderer.getSequence(sequenceIndex);

		int min = 0;
		int max = 0;
		int value = 0;

		if (sequence != null)
		{
			min = sequence.intervalStart;
			max = sequence.intervalEnd;
			value = min + (int) this.renderer.getSequenceDuration();
		}

		this.scrollbar.setMinimum(min);
		this.scrollbar.setMaximum(max);
		this.scrollbar.setValue(value);;

		this.durationLabel.setText(min + " <= " + value + " < " + max);
	}

	private void onScrollbarAdjustment(AdjustmentEvent e)
	{
		int min = this.scrollbar.getMinimum();
		int value = this.scrollbar.getValue();

		this.renderer.setSequenceDuration(value - min);
		this.setPauseState(true);

		this.updateScrollbar();
	}

	private SequenceLoop getSelectedLoop()
	{
		Checkbox checkbox = this.checkboxGroup.getSelectedCheckbox();
		String replace = checkbox.getLabel().replace(loopCheckboxSuffix, "");
		SequenceLoop loop = SequenceLoop.valueOf(replace);

		return loop;
	}

	@Override
	protected void onResize(ComponentEvent e)
	{
		Panel panel = this.getPanel();

		Rectangle layoutBounds = new Rectangle();
		layoutBounds.setLocation(10, 10);
		layoutBounds.setSize(panel.getWidth() - 20, panel.getHeight() - 20);

		Rectangle listBounds = new Rectangle(layoutBounds.x, layoutBounds.y, layoutBounds.width, 200);
		this.list.setBounds(listBounds);

		Checkbox[] checkboxs = Arrays.stream(panel.getComponents()).filter(c -> c instanceof Checkbox).toArray(Checkbox[]::new);
		Point checkboxLocation = new Point(listBounds.x, listBounds.y + listBounds.height + 10);
		Dimension checkboxSize = new Dimension(listBounds.width, 20);

		for (int i = 0; i < checkboxs.length; i++)
		{
			Checkbox checkbox = checkboxs[i];
			checkbox.setLocation(checkboxLocation);
			checkbox.setSize(checkboxSize);

			checkboxLocation.y = checkbox.getY() + checkbox.getHeight();
		}

		Dimension buttonSize = new Dimension((layoutBounds.width - 10) / 2, 40);
		int buttonY = checkboxLocation.y + 10;

		this.playButton.setSize(buttonSize);
		this.playButton.setLocation(checkboxLocation.x, buttonY);
		Rectangle playButtonBounds = this.playButton.getBounds();

		this.pauseButton.setSize(buttonSize);
		this.pauseButton.setLocation(layoutBounds.x + layoutBounds.width - this.pauseButton.getWidth(), buttonY);

		this.scrollbar.setSize(layoutBounds.width, 30);
		this.scrollbar.setLocation(layoutBounds.x, playButtonBounds.y + playButtonBounds.height + 10);
		Rectangle scrollbarBounds = this.scrollbar.getBounds();

		this.durationLabel.setSize(layoutBounds.width, 20);
		this.durationLabel.setLocation(layoutBounds.x, scrollbarBounds.y + scrollbarBounds.height);
	}

}
