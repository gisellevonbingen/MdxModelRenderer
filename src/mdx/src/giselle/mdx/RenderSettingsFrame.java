package giselle.mdx;

import java.awt.Button;
import java.awt.Checkbox;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Label;
import java.awt.Panel;
import java.awt.Rectangle;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import giselle.mdx.event.EventArgs;
import giselle.mdx.event.EventManager;
import giselle.mdx.event.EventManager.EventHandlerList;
import giselle.mdx.settings.ISettingComponent;
import giselle.mdx.settings.RenderSettings;
import giselle.mdx.settings.SettingComponentBoolean;
import giselle.mdx.settings.SettingComponentColor;
import giselle.mdx.settings.SettingComponentFloat;
import giselle.mdx.settings.SettingParameter;
import giselle.mdx.settings.SettingParameter.DescendantInfo;

public class RenderSettingsFrame extends SimpleFrame<Panel>
{
	private static final long serialVersionUID = -5654180183581570699L;

	private EventManager<EventArgs> doneEvent;

	private SettingParameter<Object> root;
	private HashMap<SettingParameter<?>, ISettingComponent> components = new HashMap<>();

	private Button doneButton;
	private Button cancelButton;
	private Button defaultButton;

	public RenderSettingsFrame()
	{
		this.root = new SettingParameter<>(null, null, "root");

		try
		{
			RenderSettings d = new RenderSettings();
			Field[] fields = d.getClass().getDeclaredFields();

			for (Field field : fields)
			{
				field.setAccessible(true);
				new SettingParameter<>(this.root, field);
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		this.doneEvent = new EventManager<>();

		Panel panel = this.getPanel();
		panel.setPreferredSize(new Dimension(270, 1000));
		panel.setSize(panel.getPreferredSize());
		this.pack();

		this.setTitle("Render Settings");
		this.setResizable(false);

		List<DescendantInfo> descendants = this.root.getDescendants();
		int top = 0;

		for (DescendantInfo descendant : descendants)
		{
			top = this.addComponent(descendant);
		}

		List<Button> buttons = new ArrayList<>();

		this.doneButton = new Button();
		this.doneButton.setLabel("Done");
		this.doneButton.addActionListener((e) -> this.onDoneButtonClick());
		buttons.add(this.doneButton);

		this.cancelButton = new Button();
		this.cancelButton.setLabel("Cancel");
		this.cancelButton.addActionListener((e) -> this.onCancelButtonClick());
		buttons.add(this.cancelButton);

		this.defaultButton = new Button();
		this.defaultButton.setLabel("Default");
		this.defaultButton.addActionListener((e) -> this.onDefaultButtonClick());
		buttons.add(this.defaultButton);

		Rectangle layoutBounds = this.getLayoutBounds();
		int buttonOffset = 10;
		Dimension buttonSize = new Dimension((layoutBounds.width - (buttonOffset * (buttons.size() - 1))) / buttons.size(), 30);
		int buttonTop = top + 10;

		for (int i = 0; i < buttons.size(); i++)
		{
			Button button = buttons.get(i);
			button.setSize(buttonSize);
			button.setLocation(layoutBounds.x + (buttonOffset + buttonSize.width) * i, buttonTop);

			panel.add(button);
		}

		Insets insets = this.getLayoutInsets();
		panel.setPreferredSize(new Dimension(insets.left + insets.right + layoutBounds.width, buttonTop + buttonSize.height + 10));
		panel.setSize(panel.getPreferredSize());
		this.pack();

		this.setLocationRelativeTo(null);
	}

	private void onDefaultButtonClick()
	{
		this.bind(new RenderSettings());
	}

	private void onDoneButtonClick()
	{
		this.setVisible(false);

		this.doneEvent.post(this, new EventArgs());
	}

	private void onCancelButtonClick()
	{
		this.setVisible(false);
	}

	private int addComponent(DescendantInfo descendant)
	{
		SettingParameter<?> parameter = descendant.parameter;

		Panel panel = this.getPanel();
		Rectangle layoutBounds = this.getLayoutBounds();

		ISettingComponent icomponent = this.createValueComponent(parameter.getField().getType());
		Component valueComponent = (Component) icomponent;
		Dimension componentSize = new Dimension(120, 20);

		if (valueComponent != null)
		{
			panel.add(valueComponent);
		}

		int x = layoutBounds.x + 10 * descendant.level;
		int y = layoutBounds.y + (10 + componentSize.height) * descendant.totalIndex;

		if (valueComponent instanceof Checkbox)
		{
			Checkbox checkbox = (Checkbox) valueComponent;
			checkbox.setLabel(parameter.getName());

			valueComponent.setSize(componentSize.width * 2 + 10, componentSize.height);
			valueComponent.setLocation(x, y);
		}
		else
		{
			Label label = new Label();
			label.setText(parameter.getName());
			label.setSize(120, 20);
			label.setLocation(x, y);
			Rectangle labelBounds = label.getBounds();
			panel.add(label);

			if (valueComponent != null)
			{
				valueComponent.setSize(componentSize);
				valueComponent.setLocation(labelBounds.x + labelBounds.width + 10, labelBounds.y);
			}

		}

		this.components.put(parameter, icomponent);

		return y + componentSize.height;
	}

	private Rectangle getLayoutBounds()
	{
		Panel panel = this.getPanel();
		Dimension panelSize = panel.getSize();
		Insets insets = this.getLayoutInsets();
		Rectangle layoutBounds = new Rectangle(insets.left, insets.top, panelSize.width - insets.left - insets.right, panelSize.height - insets.top - insets.bottom);

		return layoutBounds;
	}

	private Insets getLayoutInsets()
	{
		Insets insets = new Insets(10, 10, 10, 10);
		return insets;
	}

	private ISettingComponent createValueComponent(Class<?> valueType)
	{
		if (valueType == null)
		{
			return null;
		}

		if (valueType.equals(Color.class) == true)
		{
			SettingComponentColor component = new SettingComponentColor();
			return component;
		}
		else if (valueType.equals(boolean.class) == true)
		{
			SettingComponentBoolean checkbox = new SettingComponentBoolean();
			return checkbox;
		}
		else if (valueType.equals(float.class) == true)
		{
			SettingComponentFloat textField = new SettingComponentFloat();
			return textField;
		}

		return null;
	}

	protected void onDone(EventArgs e)
	{
		this.doneEvent.post(this, e);
	}

	public EventHandlerList<EventArgs> getDoneEvent()
	{
		return doneEvent.getHandlers();
	}

	public void bind(RenderSettings renderSettings)
	{
		try
		{
			List<DescendantInfo> descendants = this.root.getDescendants();

			for (DescendantInfo descendant : descendants)
			{
				SettingParameter<?> parameter = descendant.parameter;
				Field field = parameter.getField();
				Object value = field.get(renderSettings);

				ISettingComponent component = this.components.get(parameter);
				component.bind(value);
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}

	public RenderSettings parse()
	{
		RenderSettings renderSettings = new RenderSettings();

		try
		{
			List<DescendantInfo> descendants = this.root.getDescendants();

			for (DescendantInfo descendant : descendants)
			{
				SettingParameter<?> parameter = descendant.parameter;

				ISettingComponent component = this.components.get(parameter);
				Object value = component.parse();

				Field field = parameter.getField();
				field.set(renderSettings, value);
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return renderSettings;
	}

}
