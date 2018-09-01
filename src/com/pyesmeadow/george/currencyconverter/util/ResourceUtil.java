package com.pyesmeadow.george.currencyconverter.util;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Utility class for resources
 */
public class ResourceUtil {

	private static final Toolkit tk = Toolkit.getDefaultToolkit();
	private static final ClassLoader cl = ResourceUtil.class.getClassLoader();

	/**
	 * Resizes an image to [width] x [height] pixels.
	 *
	 * @param imgToResize the image before the operation
	 * @param width       the target width of the image
	 * @param height      the target height of the image
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
	 * @param path starts from src, use 'assets/******.***'
	 */
	public static void openFile(String path) throws FileNotFoundException
	{
		URL url = getResource(path);

		try
		{
			openAbsoluteFile(url.toURI());
		}
		catch (URISyntaxException e)
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
		if (Desktop.isDesktopSupported())
		{
			try
			{
				Desktop.getDesktop().browse(uri);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			throw new UnsupportedOperationException("Desktop is not supported, could not open URI \"" + uri.toString() + "\"");
		}
	}

	/**
	 * @param path the path of the resource
	 * @return the URL of the resource
	 * @throws FileNotFoundException if the file cannot be found or accessed
	 */
	public static URL getResource(String path) throws FileNotFoundException
	{
		URL resource = cl.getResource(path);

		if (resource == null)
		{
			throw new FileNotFoundException("The file at the path specified cannot be found");
		}

		return resource;
	}

	public static Image getImage(String path) throws FileNotFoundException
	{
		return tk.getImage(getResource(path));
	}

	/**
	 * A system-dependent method to get a directory to save data in. Does not
	 * check whether the file exists.
	 *
	 * @return a File that corresponds to:
	 * Users/$USER/AppData/Roaming/CurrencyConverter (Windows)
	 * $USER/Library/Application Support/CurrencyConverter (Mac)
	 * home/.Launcher (*nux)
	 * <p>
	 * If none of these environments are detected, returns null.
	 */
	public static File getAppdataDirectory()
	{
		String fileFolder;

		String os = System.getProperty("os.name").toUpperCase();

		// Get a string of the directory
		if (os.contains("WIN"))
		{

			fileFolder = System.getenv("APPDATA") + "\\" + "CurrencyConverter";

		}
		else if (os.contains("MAC"))
		{

			fileFolder = System.getProperty("user.home") + "/Library/Application " + "Support" + "/CurrencyConverter";

		}
		else if (os.contains("NUX"))
		{

			fileFolder = System.getProperty("user.dir") + "/.CurrencyConverter";

		}
		else
		{
			return null;
		}

		// Convert the string to a file and return it
		return new File(fileFolder);
	}

	/**
	 * This will copy the contents of a file to another. Specify the files in
	 * the constructor of the FileReader and Writer.
	 *
	 * @param reader a FileReader, with the file of the original file
	 * @param writer a FileWriter, with the file of the copied file
	 * @throws IOException If the file cannot be copied
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
