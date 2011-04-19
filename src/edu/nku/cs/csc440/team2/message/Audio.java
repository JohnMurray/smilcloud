/*
 * Audio.java
 */
package edu.nku.cs.csc440.team2.message;

/**
 * Java binding for audio media elements within a SMIL message; each audio
 * object has an automatically generated unique id; audio objects have no fill
 * as they are not displayed; audio objects have no region and therefore no fit
 * for the same reason
 * 
 * @author Shane Crandall
 * @version 1.0, 04/18/11
 */
public class Audio extends AudioVideo {

	/**
	 * audio id prefix
	 */
	private final static char A = 'a';

	/**
	 * audio id counter
	 */
	private static int count = 0;

	/**
	 * construct new Audio with a default begin time and clip begin time; clip
	 * end time is set to end time
	 * 
	 * @param end
	 *            time to end play
	 * @param src
	 *            source of this object
	 */
	public Audio(double end, String src) {
		super(end, src);
		id = A + String.valueOf(++count);
		this.setFill(null);
		this.setRegion(null);
	}

	/**
	 * constructs new Audio with no defaults
	 * 
	 * @param begin
	 *            time to begin play
	 * @param end
	 *            time to end play
	 * @param src
	 *            source of this object
	 * @param clipBegin
	 *            clip time to begin play
	 * @param clipEnd
	 *            clip time to end play
	 */
	public Audio(double begin, double end, String src, double clipBegin,
			double clipEnd) {
		super(begin, end, src, clipBegin, clipEnd);
		id = A + String.valueOf(++count);
		this.setFill(null);
		this.setRegion(null);
	}

	/**
	 * constructs new Audio with no defaults; only used from within package to
	 * generate anonymous convenience objects
	 * 
	 * @param begin
	 *            time to begin play
	 * @param end
	 *            time to end play
	 * @param src
	 *            source of this object
	 * @param clipBegin
	 *            clip time to begin play
	 * @param clipEnd
	 *            clip time to end play
	 * @param id
	 *            id to use
	 */
	Audio(double begin, double end, String src, double clipBegin,
			double clipEnd, String id) {
		super(begin, end, src, clipBegin, clipEnd);
		this.id = id;
	}
}
