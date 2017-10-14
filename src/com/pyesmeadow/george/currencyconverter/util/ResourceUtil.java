package com.pyesmeadow.george.currencyconverter.util;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

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
	 * Opens a file (in explorer) from a absolute path. Use '.toURI()' on a file or URL to get
	 * a URI. Does not check whether the file exists.
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

	public static URL getResource(String path)
	{
		return ResourceUtil.class.getClassLoader().getResource(path);
	}

	public static Image getImage(String path)
	{
		return Toolkit.getDefaultToolkit().getImage(getResource(path));
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

	public static File getCurrencyList() throws FileNotFoundException
	{
		File appdataDirectory = ResourceUtil.getAppdataDirectory();

		File fileCurrencyList = new File(appdataDirectory.getAbsolutePath() + "/currencies.json");

		// Check that the folder exists
		if (!appdataDirectory.exists())
		{
			appdataDirectory.mkdirs();
		}

		// If the file doesn't exist, throw an exception
		if (!fileCurrencyList.exists())
		{
			throw new FileNotFoundException();
		}

		return fileCurrencyList;

	}

	public static File getSavesList()
	{
		File appdataDirectory = ResourceUtil.getAppdataDirectory();

		File fileSavesList = new File(appdataDirectory.getAbsolutePath() + "/saves.json");

		// Check that the folder exists
		if (!appdataDirectory.exists())
		{
			appdataDirectory.mkdirs();
		}

		// If the file doesn't exist, create it
		if (!fileSavesList.exists())
		{
			try
			{
				fileSavesList.createNewFile();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}

		return fileSavesList;

	}
}
