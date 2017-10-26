package com.pyesmeadow.george.currencyconverter.main;

import com.pyesmeadow.george.currencyconverter.currency.CurrencyUpdater;
import com.pyesmeadow.george.currencyconverter.util.ResourceUtil;

import javax.swing.*;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.net.URISyntaxException;

public class CurrencyConverterMenuBar extends JMenuBar {

	private static final long serialVersionUID = 4423665550140769303L;

	private CurrencyConverterFrame frame;

	private JMenu menuFile;
	private JMenuItem menuItemAbout, menuItemOpenChangelog, menuItemOpenDataFolder;

	private JMenu menuOptions;
	private JMenuItem menuItemOptions, menuItemManage, menuItemUpdate;
	private JCheckBoxMenuItem menuItemShowCurrencyDetails, menuItemShowSaves;

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
		menuItemOptions = new JMenuItem("Options");
		menuItemManage = new JMenuItem("Manage currencies");
		menuItemUpdate = new JMenuItem("Update currencies");
		menuItemShowCurrencyDetails = new JCheckBoxMenuItem("Show currency details");
		menuItemShowSaves = new JCheckBoxMenuItem("Show saves");

		System.out.println(CurrencyConverter.frame);
		menuItemShowCurrencyDetails.setState(frame.getCurrencyDetailsVisibility());
		menuItemShowSaves.setState(frame.getSavesVisibility());

		add(menuFile);
		menuFile.add(menuItemAbout);
		menuFile.addSeparator();
		menuFile.add(menuItemOpenChangelog);
		menuFile.add(menuItemOpenDataFolder);

		add(menuOptions);
		menuOptions.add(menuItemOptions);
		menuOptions.addSeparator();
		menuOptions.add(menuItemManage);
		menuOptions.add(menuItemUpdate);
		menuOptions.addSeparator();
		menuOptions.add(menuItemShowCurrencyDetails);
		menuOptions.add(menuItemShowSaves);

		this.createListeners();
	}

	private void createListeners()
	{
		// ================================ FILE ===================================

		menuItemAbout.addActionListener(evt -> new AboutDialog());

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

		menuItemOptions.addActionListener(evt -> new OptionsDialog());

		// ----------------------------------------------------------------------------

		menuItemManage.addActionListener(evt ->
		{
			frame.currencyManager.openManagementDialog();
		});

		menuItemUpdate.addActionListener(evt ->
		{

			frame.currencyUpdater = new CurrencyUpdater(frame.currencyManager, true);
			Thread thread = new Thread(frame.currencyUpdater, "Thread-CurrencyUpdater");
			thread.start();

			CurrencyConverter.frame.refreshCurrencies();

		});

		// ----------------------------------------------------------------------------

		menuItemShowCurrencyDetails.addItemListener(evt ->
		{
			CurrencyConverter.frame.setCurrencyDetailsVisibility(((JCheckBoxMenuItem) evt.getSource()).getState());
		});

		menuItemShowSaves.addItemListener(evt ->
		{
			CurrencyConverter.frame.setSavesVisibility(((JCheckBoxMenuItem) evt.getSource()).getState());
		});
	}
}
