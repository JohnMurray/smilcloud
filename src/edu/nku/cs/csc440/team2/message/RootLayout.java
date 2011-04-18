/*
 * RootLayout.java
 */
package edu.nku.cs.csc440.team2.message;

/**
 * Java binding for root-layout element contained within a SMIL message; the
 * superclass for Region objects; "Determines the value of the layout properties
 * of the root element, which in turn determines the size of the viewport, e.g.
 * the window in which the SMIL presentation is rendered." - w3.org
 * 
 * @author Shane Crandall
 * @version 1.0, 04/18/11
 */
public class RootLayout {

	/**
	 * "to make the underlying colors shine through" - w3.org
	 */
	public final static String TRANSPARENT = "transparent";

	/**
	 * "Uniquely identifies an element within a document." - w3.org
	 */
	String id = null;

	/**
	 * "Sets the background color of an element, either a <color> value or the
	 * keyword 'transparent', to make the underlying colors shine through." -
	 * w3.org; default value is "transparent"
	 */
	private String backgroundColor = TRANSPARENT;

	/**
	 * width and height; default values of 480, 320 respectively
	 */
	private SmilDimension dimensions = new SmilDimension(480, 320);

	/**
	 * default constructor that constructs a new RootLayout with all defaults
	 */
	RootLayout() {
	}

	/**
	 * constructs new RootLayout from a dimension with default background
	 * color
	 * 
	 * @param dimensions
	 *            dimensions to use
	 */
	RootLayout(SmilDimension dimensions) {
		setDimensions(dimensions);
	}

	/**
	 * constructs new RootLayout from a dimension and background color; if the
	 * background color is invalid, it maintains its current value
	 * 
	 * @param dimensions
	 *            dimensions to use
	 * @param backgroundColor
	 *            background color to use
	 */
	RootLayout(SmilDimension dimensions, String backgroundColor) {
		setDimensions(dimensions);
		if (new CdataValidator().validate(backgroundColor, "#") != null) {
			this.backgroundColor = backgroundColor;
		}
	}

	/**
	 * @return background color
	 */
	public String getBackgroundColor() {
		return backgroundColor;
	}

	/**
	 * @return dimensions
	 */
	public SmilDimension getDimensions() {
		return dimensions;
	}

	/**
	 * sets dimensions unless the argument is null in which case they
	 * maintain their current value
	 * 
	 * @param dimensions
	 *            dimensions to use
	 */
	private void setDimensions(SmilDimension dimensions) {
		if (dimensions != null) {
			this.dimensions = dimensions;
		}
	}

	/**
	 * formats tags and their associated values
	 * 
	 * @param keyword
	 *            literal XML tag, left operand
	 * @param insideQuotes
	 *            XML tag value, right operand
	 * @return correctly formatted, concatenated tag & value
	 */
	String format(String keyword, Object insideQuotes) {
		return " " + keyword + "=\"" + String.valueOf(insideQuotes) + "\"";
	}

	/**
	 * generates SMIL message members in XML format
	 * 
	 * @return XML representation of element objects
	 */
	String toXml() {
		// <root-layout width="480" height="320" background-color="#98FB98"/>
		// <region id="r1" width="225" height="300" background-color="#ADFF2F"
		// left="10" top="10" z-index="0" fit="hidden"/>
		StringBuilder xml = new StringBuilder();
		if (id == null) {
			xml.append("<root-layout");
		}
		xml.append(format("width", dimensions.getWidth()));
		xml.append(format("height", dimensions.getHeight()));
		xml.append(format("background-color", backgroundColor));
		if (id == null) {
			xml.append("/>\n");
		}
		return xml.toString();
	}
}
