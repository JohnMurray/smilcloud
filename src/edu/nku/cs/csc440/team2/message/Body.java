/*
 * Body.java
 */
package edu.nku.cs.csc440.team2.message;

/**
 * Java binding for all elements contained within the body of a SMIL message;
 * the superclass for all Media and Timing objects; logically comprises the body
 * of a SMIL message
 * 
 * @author Shane Crandall
 * @version 1.0, 04/18/11
 */
public abstract class Body implements Comparable<Body> {

	/**
	 * "Uniquely identifies an element within a document." - w3.org
	 */
	String id;

	/**
	 * "Specifies the number of times that the clip or group plays." - real.com
	 * default value is one
	 */
	private int repeat = 1;

	/**
	 * "Specifies the time for the explicit begin of an element." - w3.org
	 * default value is zero
	 */
	private double begin = 0.0;

	/**
	 * "Specifies the explicit end of an element." - w3.org default value is
	 * negative one for an implicit end time
	 */
	private double end = -1.0;

	/**
	 * generates SMIL message members into XML format
	 * 
	 * @return XML representation of objects as elements
	 */
	String toXml() {
		StringBuilder xml = new StringBuilder();
		xml.append(format("id", id));
		xml.append(format("repeat", repeat));
		xml.append(format("begin", begin));
		if (end != -1.0) {
			xml.append(format("end", end));
		}
		return xml.toString();
	}

	/**
	 * formats individual tags and their associated values
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
	 * @return id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return time to begin
	 */
	public double getBegin() {
		return begin;
	}

	/**
	 * sets begin time if GTEQ zero
	 * 
	 * @param begin
	 *            time to begin
	 */
	public void setBegin(double begin) {
		if (begin >= 0.0) {
			this.begin = begin;
		}
	}

	/**
	 * @return time to end
	 */
	public double getEnd() {
		return end;
	}

	/**
	 * sets end time from within package
	 * 
	 * @param end
	 *            time to end
	 */
	void setEndTiming(double end) {
		this.end = end;
	}

	/**
	 * sets end time from outside package if GTEQ zero; resets end time to zero
	 * if LT zero
	 * 
	 * @param end
	 *            time to end
	 */
	public void setEnd(double end) {
		if (end >= 0.0) {
			this.end = end;
		} else if (this.end < 0.0) {
			this.end = 0.0;
		} else
			;
	}

	/**
	 * @return number of repetitions
	 */
	public int getRepeat() {
		return repeat;
	}

	/**
	 * sets number of repetitions if GTEQ one
	 * 
	 * @param repeat
	 *            number of repetitions
	 */
	public void setRepeat(int repeat) {
		if (repeat >= 1) {
			this.repeat = repeat;
		}
	}

	/*
	 * (non-Javadoc) orders body elements by the literal character prefix and
	 * then integer value of their unique id's
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Body b) {
		if (getId().charAt(0) < b.getId().charAt(0)) {
			return -1;
		} else if (getId().charAt(0) > b.getId().charAt(0)) {
			return 1;
		} else {
			if (Integer.valueOf(getId().substring(1)) < Integer.valueOf(b
					.getId().substring(1))) {
				return -1;
			} else if (Integer.valueOf(getId().substring(1)) > Integer
					.valueOf(b.getId().substring(1))) {
				return 1;
			} else {
				return 0;
			}
		}
	}
}
