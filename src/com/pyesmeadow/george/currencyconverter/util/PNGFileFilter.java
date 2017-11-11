package com.pyesmeadow.george.currencyconverter.util;

import javax.swing.filechooser.FileFilter;
import java.io.File;

public class PNGFileFilter extends FileFilter {

	@Override
	public boolean accept(File file)
	{
		if(file.isDirectory())
		{
			return true;
		}

		String extension = Util.getFileExtension(file);
		if(extension != null)
		{
			if(extension.equals("png"))
			{
				return true;
			}
		}

		return false;
	}

	@Override
	public String getDescription()
	{
		return "Portable Network Graphics (*.png)";
	}
}
