/*
 * Video.java
 */
package edu.nku.cs.csc440.team2.message;

/**
 * Java binding for video media elements within a SMIL message; each video
 * object has an automatically generated unique id
 * 
 * @author Shane Crandall
 * @version 1.0, 04/18/11
 */
public class Video extends AudioVideo {

	/**
	 * video id prefix
	 */
	private static final char V = 'v';

	/**
	 * video id counter
	 */
	private static int count = 0;

	/**
	 * construct new Video with a default begin time and clip begin time; clip
	 * end time is set to end time
	 * 
	 * @param end
	 *            time to end play
	 * @param src
	 *            source of this object
	 * @param region
	 *            region of this object
	 */
	public Video(double end, String src, Region region) {
		super(end, src);
		setRegion(region);
		id = V + String.valueOf(++count);
	}

	/**
	 * constructs new Video with no defaults
	 * 
	 * @param begin
	 *            time to begin play
	 * @param end
	 *            time to end play
	 * @param src
	 *            source of this object
	 * @param region
	 *            region of this object
	 * @param clipBegin
	 *            clip time to begin play
	 * @param clipEnd
	 *            clip time to end play
	 */
	public Video(double begin, double end, String src, Region region,
			double clipBegin, double clipEnd) {
		super(begin, end, src, clipBegin, clipEnd);
		setRegion(region);
		id = V + String.valueOf(++count);
	}

	/**
	 * constructs new Video with no defaults; only used from within package to
	 * generate anonymous convenience objects
	 * 
	 * @param begin
	 *            time to begin play
	 * @param end
	 *            time to end play
	 * @param src
	 *            source of this object
	 * @param region
	 *            region of this object
	 * @param clipBegin
	 *            clip time to begin play
	 * @param clipEnd
	 *            clip time to end play
	 * @param id
	 *            id to use
	 */
	Video(double begin, double end, String src, Region region,
			double clipBegin, double clipEnd, String id) {
		super(begin, end, src, clipBegin, clipEnd);
		setRegion(region);
		this.id = id;
	}
}
