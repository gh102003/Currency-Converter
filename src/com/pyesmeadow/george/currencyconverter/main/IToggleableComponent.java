package com.pyesmeadow.george.currencyconverter.main;

import javax.swing.*;
import java.awt.*;

public interface IToggleableComponent {

	int getMinimumHeight();

	int getDefaultHeight();

	String getID();

	default JComponent getComponent()
	{
		if (this instanceof JComponent)
		{
			return (JComponent) this;
		}
		else
		{
			return null;
		}
	}

	default GridBagConstraints getGridBagConstraints()
	{
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.1;
		return c;
	}
}
