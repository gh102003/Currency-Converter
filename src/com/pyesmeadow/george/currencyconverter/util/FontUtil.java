package com.pyesmeadow.george.currencyconverter.util;

import java.awt.Font;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JComponent;

public class FontUtil {
	
	private static final String FONT_PLAIN_NAME = "Segoe UI";
	private static final String FONT_BOLD_NAME = "Segoe UI Bold";
	private static final String FONT_SYMBOL_NAME = "Default";

	/**
	 * Used to 'register' component font variations.
	 */
	private static HashMap<JComponent, FontVariation> mapComponentFontSize = new HashMap<JComponent, FontVariation>();
	
	
	// Remove the changeFontSize method and register all components in constructor
	/**
	 * Use to 'register' component font variations. This will make the component update when the font size is changed.
	 * Returns component for convenience.
	 * 
	 * @see updateComponentFontVariations
	 */
	public static JComponent registerComponentFontVariation(JComponent component, FontVariation fontVariation) {
		mapComponentFontSize.put(component, fontVariation);
		return component;
	}
	
	// Call this when the font combo box is changed
	
	/**
	 * Sets the font of all registered components using a specified FontProfile.
	 * 
	 * @param profile the profile to use
	 * @see registerComponentFontVariation
	 */
	public static void updateComponentFontVariations(FontProfile profile, boolean platformIndependent) {
		if(!Util.isRunningOnMac() || platformIndependent) {
			for(Map.Entry<JComponent, FontVariation> entry : mapComponentFontSize.entrySet()) {
				Font font = profile.getFont(entry.getValue());
				entry.getKey().setFont(font);
			}
		}
	}
	
	public enum FontVariation {
		SMALL_PLAIN,
		MEDIUM_PLAIN,
		LARGE_PLAIN,
		SMALL_BOLD,
		MEDIUM_BOLD,
		LARGE_BOLD,
		SMALL_SYMBOL,
		MEDIUM_SYMBOL,
		LARGE_SYMBOL;
	}
	
	public enum FontProfile {
		SMALL(13, 15, 18),
		MEDIUM(15, 20, 26),
		LARGE(20, 28, 34);
		
		// Font variations
		private Font smallPlain;
		private Font mediumPlain;
		private Font largePlain;
		
		private Font smallSemibold;
		private Font mediumSemibold;
		private Font largeSemibold;

		private Font smallSymbol;
		private Font mediumSymbol;
		private Font largeSymbol;
		
		private FontProfile(int smallSize, int mediumSize, int largeSize) {
			smallPlain = new Font(FONT_PLAIN_NAME, Font.PLAIN, smallSize);
			mediumPlain = new Font(FONT_PLAIN_NAME, Font.PLAIN, mediumSize);
			largePlain = new Font(FONT_PLAIN_NAME, Font.PLAIN, largeSize);
			
			smallSemibold = new Font(FONT_BOLD_NAME, Font.BOLD, smallSize);
			mediumSemibold = new Font(FONT_BOLD_NAME, Font.BOLD, mediumSize);
			largeSemibold = new Font(FONT_BOLD_NAME, Font.BOLD, largeSize);

			smallSymbol = new Font(FONT_SYMBOL_NAME, Font.PLAIN, smallSize);
			mediumSymbol = new Font(FONT_SYMBOL_NAME, Font.PLAIN, mediumSize);
			largeSymbol = new Font(FONT_SYMBOL_NAME, Font.PLAIN, largeSize);
		}
		
		private Font getSmallPlain() {
			return smallPlain;
		}
		
		private Font getMediumPlain() {
			return mediumPlain;
		}
		
		private Font getLargePlain() {
			return largePlain;
		}
		
		private Font getSmallSemibold() {
			return smallSemibold;
		}
		
		private Font getMediumSemibold() {
			return mediumSemibold;
		}
		
		private Font getLargeSemibold() {
			return largeSemibold;
		}
		
		public Font getSmallSymbol() {
			return smallSymbol;
		}

		public Font getMediumSymbol() {
			return mediumSymbol;
		}

		public Font getLargeSymbol() {
			return largeSymbol;
		}

		public Font getFont(FontVariation variation) {
			switch(variation) {
			case SMALL_PLAIN:
				return getSmallPlain();
			case MEDIUM_PLAIN:
				return getMediumPlain();
			case LARGE_PLAIN:
				return getLargePlain();
				
			case SMALL_BOLD:
				return getSmallSemibold();
			case MEDIUM_BOLD:
				return getMediumSemibold();
			case LARGE_BOLD:
				return getLargeSemibold();
				
			case SMALL_SYMBOL:
				return getSmallSymbol();
			case MEDIUM_SYMBOL:
				return getMediumSymbol();
			case LARGE_SYMBOL:
				return getLargeSymbol();
			default:
				return null;
			}
		}
		
		@Override
		public String toString() {
			return Util.toTitleCase(super.toString());
		}
	}
}
