package giselle.mdx;

import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Panel;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.util.prefs.Preferences;

import javax.swing.JOptionPane;

import com.google.gson.Gson;

import giselle.mdx.event.EventArgs;
import giselle.mdx.settings.RenderSettings;
import giselle.wc3data.mdx.MdxModel;
import giselle.wc3data.stream.BlizzardDataInputStream;
import giselle.wc3data.util.ExceptionUtils;

public class MainFrame extends SimpleFrame<MainPanel>
{
	private static final long serialVersionUID = 3088704751195078027L;

	private Preferences preferences;
	private MenuManager menuManager;

	private SequencesFrame sequencesFrame;
	private RenderSettingsFrame renderSettingsFrame;

	private boolean closeRequested = false;

	private RenderSettings renderSettings;

	public MainFrame()
	{
		this.setTitle("Mdx Renderer");

		this.preferences = Preferences.userNodeForPackage(MainFrame.class);
		this.menuManager = new MenuManager(this);

		RenderCanvas renderCanvas = this.getRenderCanvas();
		this.sequencesFrame = new SequencesFrame(renderCanvas);
		this.renderSettingsFrame = new RenderSettingsFrame();
		this.renderSettingsFrame.getDoneEvent().add((sender, e) -> this.onSettingFrameDone(e));

		Panel panel = this.getPanel();
		panel.setPreferredSize(new Dimension(960, 540));
		panel.setSize(panel.getPreferredSize());
		this.pack();
		this.setLayout(null);

		this.setLocationRelativeTo(null);

		RenderSettings renderSettings = this.loadRenderSettings();
		this.setSettings(renderSettings);
	}

	private void onSettingFrameDone(EventArgs e)
	{
		RenderSettings settings = this.renderSettingsFrame.parse();
		this.setSettings(settings);

		this.saveRenderSettings(settings);
	}

	public void setSettings(RenderSettings renderSettings)
	{
		this.renderSettings = renderSettings;

		RenderCanvas renderCanvas = this.getRenderCanvas();
		renderCanvas.setSettings(renderSettings);
	}

	private RenderSettings loadRenderSettings()
	{
		String json = this.preferences.node("rendersettings").get("value", null);
		RenderSettings settings = new Gson().fromJson(json, RenderSettings.class);

		if (settings == null)
		{
			settings = new RenderSettings();
		}

		return settings;
	}

	private void saveRenderSettings(RenderSettings settings)
	{
		String json = new Gson().toJson(settings);
		this.preferences.node("rendersettings").put("value", json);
	}

	@Override
	protected MainPanel createPanel()
	{
		return new MainPanel();
	}

	@Override
	public void removeNotify()
	{
		super.removeNotify();

		this.renderSettingsFrame.dispose();
		this.sequencesFrame.dispose();

		this.closeRequested = true;
	}

	public void onViewSettingsMenuAction()
	{
		this.renderSettingsFrame.bind(this.renderSettings);
		this.renderSettingsFrame.setVisible(true);
	}

	public void onViewSequencesMenuAction()
	{
		this.sequencesFrame.setVisible(true);
	}

	public void onViewResetTransformMenuAction()
	{
		this.getPanel().getRenderCanvas().resetTransform();
	}

	public void onFileOpenMenuAction(String file)
	{
		MdxModel model = new MdxModel();

		try
		{
			try (FileInputStream fis = new FileInputStream(file))
			{
				try (BlizzardDataInputStream bdis = new BlizzardDataInputStream(fis))
				{
					model.load(bdis);
				}

			}

		}
		catch (Exception ex)
		{
			ex.printStackTrace();

			String string = ExceptionUtils.toString(ex);
			JOptionPane.showMessageDialog(this, string);

			return;
		}

		String directory = new File(file).getParent() + "\\";

		ModelWrapper modelWrapper = new ModelWrapper(directory, model);
		this.getPanel().bind(modelWrapper);

		this.sequencesFrame.bind(model);

		this.menuManager.addRecent(file);
	}

	public void onFileOpenMenuAction()
	{
		FileDialog d = new FileDialog(this);
		d.setFilenameFilter(new FilenameFilter()
		{
			@Override
			public boolean accept(File dir, String name)
			{
				boolean b = name.toLowerCase().endsWith(".mdx");
				return b;
			}
		});

		d.setVisible(true);
		d.dispose();

		String directory = d.getDirectory();
		String file = directory + d.getFile();

		if (file == null || file.toLowerCase().endsWith(".mdx") == false)
		{
			return;
		}

		this.onFileOpenMenuAction(file);
	}

	public Preferences getPreferences()
	{
		return this.preferences;
	}

	public RenderCanvas getRenderCanvas()
	{
		return this.getPanel().getRenderCanvas();
	}

	public boolean isCloseRequested()
	{
		return this.closeRequested;
	}

}
