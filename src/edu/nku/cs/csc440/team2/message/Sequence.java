/*
 * Sequence.java
 */
package edu.nku.cs.csc440.team2.message;

import java.util.LinkedList;

/**
 * Java binding for sequence timing containers within a SMIL message; each
 * sequence object has an automatically generated unique id; sequence containers
 * cand be implicit as the root container of a message body or explicit as any
 * other; "The children of a "seq" element form a temporal sequence." - w3.org
 * 
 * @author Shane Crandall
 * @version 1.0, 04/18/11
 */
public class Sequence extends Timing {

	/**
	 * sequence id prefix
	 */
	private static final char S = 's';

	/**
	 * sequence id counter
	 */
	private static int count = 0;

	/**
	 * default constructor to construct new Sequence with defaults
	 */
	public Sequence() {
		super();
		id = S + String.valueOf(++count);
	}

	/**
	 * constructs new Sequence with begin time and implicit end time
	 * 
	 * @param begin
	 *            time to begin play
	 */
	public Sequence(double begin) {
		super(begin);
		id = S + String.valueOf(++count);
	}

	/**
	 * constructs new Sequence with begin time and explicit end time
	 * 
	 * @param begin
	 *            time to begin play
	 * @param end
	 *            time to end play
	 */
	public Sequence(double begin, double end) {
		super(begin, end);
		id = S + String.valueOf(++count);
	}

	/**
	 * constructs new Sequence from existing content; can be an implicit or
	 * explicit sequence; only used from within package to generate anonymous
	 * convenience objects
	 * 
	 * @param content
	 *            content to use
	 * @param isImplicit
	 *            whether or not sequence should be implicit
	 */
	Sequence(LinkedList<Body> content, boolean isImplicit) {
		this.content = content;
		this.isImplicit = isImplicit;
	}

	/**
	 * constructs new Sequence with begin time, explicit end time and id; only
	 * used from within package to generate anonymous convenience objects
	 * 
	 * @param begin
	 *            time to begin play
	 * @param end
	 *            time to end play
	 * @param id
	 *            id to use
	 */
	Sequence(double begin, double end, String id) {
		super(begin);
		setEndTiming(end);
		this.id = id;
	}
}
