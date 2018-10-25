package giselle.mdx;

import java.awt.Frame;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class MenuManager
{
	public static final int RecentFileCount = 5;

	private MainFrame frame;
	private Preferences preferences;

	private Menu fileMenu;
	private MenuItem fileOpenMenu;
	private List<String> files;

	private Menu viewMenu;
	private MenuItem viewResetTransformMenu;
	private MenuItem viewSequencesMenu;
	private MenuItem viewSettingsMenu;

	public MenuManager(MainFrame frame)
	{
		this.frame = frame;
		this.preferences = frame.getPreferences().node("menu");

		MenuBar menuBar = new MenuBar();
		frame.setMenuBar(menuBar);

		this.fileMenu = new Menu("File");
		menuBar.add(this.fileMenu);

		this.fileOpenMenu = new MenuItem("Open");
		this.fileOpenMenu.addActionListener(e -> this.onFileOpenMenuAction());
		this.files = this.loadRecentFiles();

		this.viewMenu = new Menu("View");
		menuBar.add(this.viewMenu);

		this.viewResetTransformMenu = new MenuItem("Reset Transform");
		this.viewResetTransformMenu.addActionListener(e -> this.onViewResetTransformMenuAction());
		this.viewMenu.add(this.viewResetTransformMenu);

		this.viewSequencesMenu = new MenuItem("Sequences");
		this.viewSequencesMenu.addActionListener(e -> this.onViewSequencesMenuAction());
		this.viewMenu.add(this.viewSequencesMenu);

		this.viewSettingsMenu = new MenuItem("Settings");
		this.viewSettingsMenu.addActionListener(e -> this.onViewSettingsMenuAction());
		this.viewMenu.add(this.viewSettingsMenu);

		this.buildFileMenu();
	}

	private void onViewSettingsMenuAction()
	{
		this.frame.onViewSettingsMenuAction();
	}

	private void onViewSequencesMenuAction()
	{
		this.frame.onViewSequencesMenuAction();
	}

	private void onViewResetTransformMenuAction()
	{
		this.frame.onViewResetTransformMenuAction();
	}

	private void onFileOpenMenuAction()
	{
		this.frame.onFileOpenMenuAction();
	}

	private void onFileOpenMenuAction(String file)
	{
		this.frame.onFileOpenMenuAction(file);
	}

	public void addRecent(String file)
	{
		int index = this.indexOf(this.files, file);

		if (index != -1)
		{
			this.files.remove(index);
		}

		this.files.add(0, file);

		while (this.files.size() > RecentFileCount)
		{
			this.files.remove(this.files.size() - 1);
		}

		this.saveRecentFiles(this.files);

		this.buildFileMenu();
	}

	private int indexOf(List<String> files, String file)
	{
		for (int i = 0; i < files.size(); i++)
		{
			String f = files.get(i);

			if (f.equalsIgnoreCase(file) == true)
			{
				return i;
			}

		}

		return -1;
	}

	private void buildFileMenu()
	{
		this.fileMenu.removeAll();

		this.fileMenu.add(this.fileOpenMenu);
		this.fileMenu.addSeparator();

		for (int i = 0; i < this.files.size(); i++)
		{
			String file = this.files.get(i);

			if (file != null)
			{
				MenuItem mi = new MenuItem(file);
				mi.addActionListener(e -> this.onFileOpenMenuAction(e.getActionCommand()));
				this.fileMenu.add(mi);
			}

		}

	}

	private void saveRecentFiles(List<String> files)
	{
		JsonArray values = new JsonArray();

		for (int i = 0; i < files.size(); i++)
		{
			String file = files.get(i);
			values.add(file);
		}

		JsonObject jobject = new JsonObject();
		jobject.add("values", values);

		this.preferences.put("recents", jobject.toString());
	}

	private List<String> loadRecentFiles()
	{
		String json = this.preferences.get("recents", null);
		JsonObject jobject = new Gson().fromJson(json, JsonObject.class);

		List<String> files = new ArrayList<String>();

		if (jobject != null)
		{
			JsonArray values = jobject.get("values").getAsJsonArray();

			for (int i = 0; i < values.size(); i++)
			{
				String file = values.get(i).getAsString();

				if (file != null)
				{
					files.add(file);
				}

			}

		}

		return files;
	}

	public Frame getFrame()
	{
		return this.frame;
	}

}
