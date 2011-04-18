/*
 * TextImage.java
 */
package edu.nku.cs.csc440.team2.message;

/**
 * Java binding for static media elements contained within the body of a SMIL
 * message; the superclass for Text and Image objects used to maintain symmetry
 * with the AudioVideo class;
 * "discrete media objects without intrinsic duration" - w3.org
 * 
 * @author Shane Crandall
 * @version 1.0, 04/18/11
 */
class TextImage extends Media {

	/**
	 * constructs new TextImage with three arguments and the default value for
	 * begin time
	 * 
	 * @param end
	 *            time to end display
	 * @param src
	 *            source of this object
	 * @param region
	 *            region to use
	 */
	TextImage(double end, String src, Region region) {
		setEnd(end);
		setSrc(src);
		setRegion(region);
	}

	/**
	 * constructs new TextImage with four arguments and no defaults
	 * 
	 * @param begin
	 *            time to begin display
	 * @param end
	 *            time to end display
	 * @param src
	 *            source of this object
	 * @param region
	 *            region to use
	 */
	TextImage(double begin, double end, String src, Region region) {
		this(end, src, region);
		setBegin(begin);
	}
}
