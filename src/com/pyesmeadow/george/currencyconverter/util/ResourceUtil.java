package com.pyesmeadow.george.currencyconverter.util;

import java.awt.Desktop;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import com.pyesmeadow.george.currencyconverter.currency.Currency;
import com.pyesmeadow.george.currencyconverter.currency.manager.CurrencyManager;
import com.pyesmeadow.george.currencyconverter.main.CurrencyConverter;

/** Utility class for resources */
public class ResourceUtil {

	/**
	 * Resizes an image to [width] x [height] pixels.
	 * 
	 * @param imgToResize
	 *            the image before the operation
	 * @param width
	 *            the target width of the image
	 * @param height
	 *            the target height of the image
	 * @return
	 */
	public static Image resizeImage(Image imgToResize, int width, int height)
	{
		BufferedImage resizedImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = resizedImg.createGraphics();

		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2d.drawImage(imgToResize, 0, 0, width, height, null);
		g2d.dispose();

		return resizedImg;
	}

	/**
	 * 
	 * @param path
	 *            starts from src, use 'assets/******.***'
	 */
	public static void openFile(String path)
	{
		ClassLoader cl = ResourceUtil.class.getClassLoader();

		URL url = cl.getResource(path);

		try
		{
			openAbsoluteFile(url.toURI());
		} catch (URISyntaxException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Opens a file from a absolute path. Use '.toURI()' on a file or URL to get
	 * a URI. Does not check whether the file exists.
	 * 
	 * @param path
	 *            the path of the file to open
	 */
	public static void openAbsoluteFile(URI uri)
	{
		Desktop desktop = Desktop.getDesktop();

		try
		{
			desktop.browse(uri);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * A system-dependent method to get a directory to save data in. Does not
	 * check whether the file exists.
	 * 
	 * @return a File that corresponds to:
	 *         Users/$USER/AppData/Roaming/CurrencyConverter (Windows)
	 *         $USER/Library/Application Support/CurrencyConverter (Mac)
	 *         home/.Launcher (*nux)
	 * 
	 *         If none of these environments are detected, returns null.
	 * 
	 */
	public static File getAppdataDirectory()
	{
		String FileFolder;

		String os = System.getProperty("os.name").toUpperCase();

		// Get a string of the directory
		if (os.contains("WIN"))
		{

			FileFolder = System.getenv("APPDATA") + "\\" + "CurrencyConverter";

		} else if (os.contains("MAC"))
		{

			FileFolder = System.getProperty("user.home") + "/Library/Application " + "Support" + "/CurrencyConverter";

		} else if (os.contains("NUX"))
		{

			FileFolder = System.getProperty("user.dir") + "/.CurrencyConverter";

		} else
		{
			return null;
		}

		// Convert the string to a file
		File directory = new File(FileFolder);

		return directory;
	}

	/**
	 * This will copy the contents of a file to another. Specify the files in
	 * the constructor of the FileReader and Writer.
	 * 
	 * @param reader
	 * @param writer
	 * @throws IOException
	 */
	public static void copyFile(Reader reader, Writer writer) throws IOException
	{
		while (reader.ready())
		{
			writer.write(reader.read());
		}

		reader.close();
		writer.close();
	}

	public static File getCurrencyList(boolean createIfNecessary) throws Exception
	{
		File appdataDirectory = ResourceUtil.getAppdataDirectory();

		File fileAppdataCurrencyList = new File(appdataDirectory.getAbsolutePath() + "/currencies.json");

		// Check that the file exists
		if (!appdataDirectory.exists() && createIfNecessary)
		{
			appdataDirectory.mkdirs();
		}

		// If the file doesn't exist, create it and set it to have the
		// default currency list
		if (!fileAppdataCurrencyList.exists() && createIfNecessary)
		{
			fileAppdataCurrencyList.createNewFile();
			
			CurrencyManager currencyManager = CurrencyConverter.currencyConverterFrame.currencyManager;
			
			currencyManager.addCurrency(new Currency("USD", "US Dollar", Currency.getLocaleFromLocaleString("en_US"), 1, "currency_icon/USD.png"));
			currencyManager.addCurrency(new Currency("GBP", "Great British Pound", Currency.getLocaleFromLocaleString("en_GB"), 1.23927, "currency_icon/GBP.png"));
			currencyManager.addCurrency(new Currency("EUR", "Euro", Currency.getLocaleFromLocaleString("de_DE"), 1.07083, "currency_icon/EUR.png"));
			currencyManager.addCurrency(new Currency("AUD", "Australian Dollar", Currency.getLocaleFromLocaleString("en_AU"), 0.75469, "currency_icon/AUD.png"));
			currencyManager.addCurrency(new Currency("CAD", "Canadian Dollar", Currency.getLocaleFromLocaleString("en_CA"), 0.76617, "currency_icon/CAD.png"));
			currencyManager.addCurrency(new Currency("JPY", "Japanese Yen", Currency.getLocaleFromLocaleString("ja_JP"), 0.00886, "currency_icon/JPY.png"));
			currencyManager.addCurrency(new Currency("CNY", "Chinese Yuan", Currency.getLocaleFromLocaleString("zh_CN"), 0.14585, "currency_icon/CNY.png"));
			currencyManager.addCurrency(new Currency("KRW", "South Korean Won", Currency.getLocaleFromLocaleString("ko_KR"), 0.00086, "currency_icon/KRW.png"));
			currencyManager.addCurrency(new Currency("XBT", "Bitcoin", Currency.getLocaleFromLocaleString("en_US"), 1001.76, "currency_icon/XBT.png"));
			
			currencyManager.getCurrencyList().writeCurrencies();
			
			/*fileAppdataCurrencyList.createNewFile();

			File fileDefaultCurrencyList = new File(
					ResourceUtil.class.getClassLoader().getResource("assets/currencies.json").toURI().getPath());

			System.out.println("Copying: " + fileDefaultCurrencyList + "\n To: " + fileAppdataCurrencyList);

			FileReader reader = new FileReader(fileDefaultCurrencyList);
			FileWriter writer = new FileWriter(fileAppdataCurrencyList);

			ResourceUtil.copyFile(reader, writer);*/
		}

		return fileAppdataCurrencyList;

	}
}
