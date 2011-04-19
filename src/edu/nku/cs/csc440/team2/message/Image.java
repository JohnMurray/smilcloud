/*
 * Image.java
 */
package edu.nku.cs.csc440.team2.message;

/**
 * Java binding for image media elements within a SMIL message; each image
 * object has an automatically generated unique id
 * 
 * @author Shane Crandall
 * @version 1.0, 04/18/11
 */
public class Image extends TextImage {

	/**
	 * image id prefix
	 */
	private final static char I = 'i';

	/**
	 * image id counter
	 */
	private static int count = 0;

	/**
	 * construct new Image with a default begin time
	 * 
	 * @param end
	 *            time to end display
	 * @param src
	 *            source of this object
	 * @param region
	 *            region of this object
	 */
	public Image(double end, String src, Region region) {
		super(end, src, region);
		id = I + String.valueOf(++count);
	}

	/**
	 * construct new Image with no defaults
	 * 
	 * @param begin
	 *            time to begin display
	 * @param end
	 *            time to end display
	 * @param src
	 *            source of this object
	 * @param region
	 *            region of this object
	 */
	public Image(double begin, double end, String src, Region region) {
		super(begin, end, src, region);
		id = I + String.valueOf(++count);
	}

	/**
	 * construct new Image with no defaults; only used from within package to
	 * generate anonymous convenience objects
	 * 
	 * @param begin
	 *            time to begin display
	 * @param end
	 *            time to end display
	 * @param src
	 *            source of this object
	 * @param region
	 *            region of this object
	 * @param id
	 *            id to use
	 */
	Image(double begin, double end, String src, Region region, String id) {
		super(begin, end, src, region);
		this.id = id;
	}
}
