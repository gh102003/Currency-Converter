package com.pyesmeadow.george.currencyconverter.main;

import com.pyesmeadow.george.currencyconverter.currency.CurrencyUpdater;
import com.pyesmeadow.george.currencyconverter.util.ResourceUtil;

import javax.swing.*;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.net.URISyntaxException;
import java.util.List;

public class CurrencyConverterMenuBar extends JMenuBar {

	private static final long serialVersionUID = 4423665550140769303L;

	private CurrencyConverterFrame frame;

	private JMenu menuFile;
	private JMenuItem menuItemAbout, menuItemOpenChangelog, menuItemOpenDataFolder;

	private JMenu menuOptions;
	private JMenuItem menuItemManage, menuItemUpdate;

	CurrencyConverterMenuBar(CurrencyConverterFrame frame)
	{
		this.frame = frame;

		// File menu
		menuFile = new JMenu("File");
		menuItemAbout = new JMenuItem("About");
		menuItemOpenChangelog = new JMenuItem("Open changelog");
		menuItemOpenDataFolder = new JMenuItem("Open data folder");

		// Options menu
		menuOptions = new JMenu("Options");
		menuItemManage = new JMenuItem("Manage currencies");
		menuItemUpdate = new JMenuItem("Update currencies");

		add(menuFile);
		menuFile.add(menuItemAbout);
		menuFile.addSeparator();
		menuFile.add(menuItemOpenChangelog);
		menuFile.add(menuItemOpenDataFolder);

		add(menuOptions);
		menuOptions.add(menuItemManage);
		menuOptions.add(menuItemUpdate);
		menuOptions.addSeparator();

		createListeners();
	}

	private void createListeners()
	{
		// ================================ FILE ===================================

		menuItemAbout.addActionListener(evt ->
		{
			if (!frame.isAboutDialogOpen())
			{
				frame.setAboutDialog(new AboutDialog());
			}
		});

		// ----------------------------------------------------------------------------

		menuItemOpenChangelog.addActionListener(evt ->
		{

			File fileChangelogAppdata = new File(ResourceUtil.getAppdataDirectory() + "/changelog.txt");
			File fileChangelogDefault = null;

			if (!fileChangelogAppdata.isFile())
			{
				try
				{
					fileChangelogDefault = new File(ResourceUtil.class.getClassLoader().getResource("assets/changelog.txt").toURI());
				}
				catch (URISyntaxException e1)
				{
					e1.printStackTrace();
				}

				try
				{
					ResourceUtil.copyFile(new FileReader(fileChangelogDefault), new FileWriter(fileChangelogAppdata));
				}
				catch (Exception e1)
				{
					e1.printStackTrace();
				}
			}

			ResourceUtil.openAbsoluteFile(fileChangelogAppdata.toURI());
		});

		menuItemOpenDataFolder.addActionListener(evt ->
		{
			File dataFolder = ResourceUtil.getAppdataDirectory();

			if (dataFolder.exists())
			{
				ResourceUtil.openAbsoluteFile(dataFolder.toURI());
			}
			else
			{
				dataFolder.mkdirs();
			}
		});

		// ================================ OPTIONS ===================================

		menuItemManage.addActionListener(evt ->
		{
			frame.currencyManager.openManagementDialog();
		});

		menuItemUpdate.addActionListener(evt ->
		{
			if (!frame.isCurrencyUpdaterOpen())
			{
				CurrencyUpdater currencyUpdater = new CurrencyUpdater(frame.currencyManager, true);

				frame.setCurrencyUpdater(currencyUpdater);
				Thread thread = new Thread(currencyUpdater, "Thread-CurrencyUpdater");
				thread.start();

				frame.refreshCurrencies();
			}
		});

		// ----------------------------------------------------------------------------

		// Toggle components should be inserted here
	}

	void addToggleableComponentMenuItems(List<IToggleableComponent> toggleableComponents)
	{
		toggleableComponents.forEach(this::addToggleableComponentMenuItem);
	}

	private void addToggleableComponentMenuItem(IToggleableComponent toggleableComponent)
	{
		String componentID = toggleableComponent.getID();
		JCheckBoxMenuItem menuItem = new JCheckBoxMenuItem("Show " + componentID);
		menuItem.setState(toggleableComponent.getComponent().isVisible());
		menuOptions.add(menuItem);

		// Add action listener to menu item
		menuItem.addActionListener(evt ->
		{
			// Get the state of the checkbox
			boolean willBeVisible = ((JCheckBoxMenuItem) evt.getSource()).getState();

			// Get all components with the correct id
			List<IToggleableComponent> componentsToToggle = frame.getToggleableComponentsByID(componentID);

			// Toggle components
			componentsToToggle.forEach(componentToToggle ->
			{
				componentToToggle.getComponent().setVisible(willBeVisible);
			});

			frame.updateHeight();
		});
	}
}
