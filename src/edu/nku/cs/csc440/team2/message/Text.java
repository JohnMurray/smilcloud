/*
 * Text.java
 */
package edu.nku.cs.csc440.team2.message;

/**
 * Java binding for text media elements within a SMIL message; each text object
 * has an automatically generated unique id
 * 
 * @author Shane Crandall
 * @version 1.0, 04/18/11
 */
public class Text extends TextImage {

	/**
	 * text id prefix
	 */
	private static final char T = 't';

	/**
	 * text id counter
	 */
	private static int count = 0;

	/**
	 * construct new Text with a default begin time
	 * 
	 * @param end
	 *            time to end display
	 * @param src
	 *            source of this object
	 * @param region
	 *            region of this object
	 */
	public Text(double end, String src, Region region) {
		super(end, src, region);
		id = T + String.valueOf(++count);
	}

	/**
	 * construct new Text with no defaults
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
	public Text(double begin, double end, String src, Region region) {
		super(begin, end, src, region);
		id = T + String.valueOf(++count);
	}

	/**
	 * construct new Text with no defaults; only used from within package to
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
	Text(double begin, double end, String src, Region region, String id) {
		super(begin, end, src, region);
		this.id = id;
	}
}
