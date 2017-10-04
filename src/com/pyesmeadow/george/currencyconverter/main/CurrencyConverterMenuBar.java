package com.pyesmeadow.george.currencyconverter.main;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.net.URISyntaxException;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import com.pyesmeadow.george.currencyconverter.currency.CurrencyUpdater;
import com.pyesmeadow.george.currencyconverter.util.ResourceUtil;

public class CurrencyConverterMenuBar extends JMenuBar {

	private static final long serialVersionUID = 4423665550140769303L;

	private CurrencyConverterFrame frame;

	public JMenu menuFile;
	public JMenuItem menuItemAbout, menuItemOpenChangelog, menuItemOpenDataFolder;

	public JMenu menuOptions;
	public JMenuItem menuItemOptions, menuItemManage, menuItemUpdate;

	public CurrencyConverterMenuBar(CurrencyConverterFrame frame)
	{
		this.frame = frame;

		// File menu
		this.menuFile = new JMenu("File");
		this.menuItemAbout = new JMenuItem("About");
		this.menuItemOpenChangelog = new JMenuItem("Open changelog");
		this.menuItemOpenDataFolder = new JMenuItem("Open data folder");

		// Options menu
		this.menuOptions = new JMenu("Options");
		this.menuItemOptions = new JMenuItem("Options");
		this.menuItemManage = new JMenuItem("Manage currencies");
		this.menuItemUpdate = new JMenuItem("Update currencies");

		this.add(menuFile);
		menuFile.add(menuItemAbout);
		menuFile.addSeparator();
		menuFile.add(menuItemOpenChangelog);
		menuFile.add(menuItemOpenDataFolder);

		this.add(menuOptions);
		menuOptions.add(menuItemOptions);
		menuOptions.addSeparator();
		menuOptions.add(menuItemManage);
		menuOptions.add(menuItemUpdate);

		this.createListeners();
	}

	private void createListeners()
	{
		// ----------------------------------- FILE -----------------------------------

		menuItemAbout.addActionListener(l -> new AboutDialog(12));

		menuItemOpenChangelog.addActionListener(l -> {

			File fileChangelogAppdata = new File(ResourceUtil.getAppdataDirectory() + "/changelog.txt");
			File fileChangelogDefault = null;

			if(!fileChangelogAppdata.isFile())
			{
				try
				{
					fileChangelogDefault = new File(
							ResourceUtil.class.getClassLoader().getResource("assets/changelog.txt").toURI());
				} catch(URISyntaxException e1)
				{
					e1.printStackTrace();
				}

				try
				{
					ResourceUtil.copyFile(new FileReader(fileChangelogDefault), new FileWriter(fileChangelogAppdata));
				} catch(Exception e1)
				{
					e1.printStackTrace();
				}
			}

			ResourceUtil.openAbsoluteFile(fileChangelogAppdata.toURI());
		});

		menuItemOpenDataFolder.addActionListener(l -> {
			File dataFolder = ResourceUtil.getAppdataDirectory();

			if(dataFolder.exists())
			{
				ResourceUtil.openAbsoluteFile(dataFolder.toURI());
			} else
			{
				dataFolder.mkdirs();
			}
		});

		// ----------------------------------- OPTIONS -----------------------------------

		menuItemOptions.addActionListener(l -> new OptionsDialog());

		menuItemManage.addActionListener(l -> {
			frame.currencyManager.openManagementDialog();
		});

		menuItemUpdate.addActionListener(l -> {

			frame.currencyUpdater = new CurrencyUpdater(frame.currencyManager, true);
			Thread thread = new Thread(frame.currencyUpdater, "Thread-CurrencyUpdater");
			thread.start();

			CurrencyConverter.frame.refreshCurrencies();

		});
	}
}
