/*
 * Parallel.java
 */
package edu.nku.cs.csc440.team2.message;

/**
 * Java binding for parallel timing containers within a SMIL message; each
 * parallel object has an automatically generated unique id; parallel containers
 * are never implicit as sequence containers can sometimes be; "The children of
 * a par element can overlap in time. The textual order of appearance of
 * children in a par has no significance for the timing of their presentation."
 * - w3.org
 * 
 * @author Shane Crandall
 * @version 1.0, 04/18/11
 */
public class Parallel extends Timing {

	/**
	 * parallel id prefix
	 */
	private final static char P = 'p';

	/**
	 * parallel id counter
	 */
	private static int count = 0;

	/**
	 * default constructor to construct new Parallel with defaults
	 */
	public Parallel() {
		super();
		id = P + String.valueOf(++count);
	}

	/**
	 * constructs new Parallel with begin time and implicit end time
	 * 
	 * @param begin
	 *            time to begin play
	 */
	public Parallel(double begin) {
		super(begin);
		id = P + String.valueOf(++count);
	}

	/**
	 * constructs new Parallel with begin time and explicit end time
	 * 
	 * @param begin
	 *            time to begin play
	 * @param end
	 *            time to end play
	 */
	public Parallel(double begin, double end) {
		super(begin, end);
		id = P + String.valueOf(++count);
	}

	/**
	 * constructs new Parallel with begin time, explicit end time and id; 
	 * only used from within package to generate anonymous convenience objects
	 * 
	 * @param begin
	 *            time to begin play
	 * @param end
	 *            time to end play
	 * @param id
	 *            id to use
	 */
	Parallel(double begin, double end, String id) {
		super(begin);
		setEndTiming(end);
		this.id = id;
	}
}
