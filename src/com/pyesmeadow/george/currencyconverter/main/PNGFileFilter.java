package com.pyesmeadow.george.currencyconverter.main;

import java.io.File;

import javax.swing.filechooser.FileFilter;

import com.pyesmeadow.george.currencyconverter.util.Util;

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
