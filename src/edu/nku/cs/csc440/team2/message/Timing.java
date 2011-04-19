/*
 * Timing.java
 */
package edu.nku.cs.csc440.team2.message;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.TreeSet;

/**
 * Java binding for timing elements contained within the body of a SMIL message;
 * the superclass for Parallel and Sequence objects
 *
 * @author Shane Crandall
 * @version 1.0, 04/18/11
 */
public class Timing extends Body {

	/**
	 * ADT containing the contents controlled by this timing container
	 */
	LinkedList<Body> content = new LinkedList<Body>();

	/**
	 * whether or not the content of this container contains an implicit
	 * sequence container
	 */
	boolean isImplicit = false;

	/**
	 * temporary swap variable for an arbitrary body element
	 */
	private static Body b = null;

	/**
	 * sorted set of id's of the objects contained within content
	 */
	private static TreeSet<String> ids = new TreeSet<String>();

	/**
	 * default constructor to construct new Timing container with all defaults
	 */
	Timing() {
	}

	/**
	 * constructs new Timing container with a time to begin
	 * 
	 * @param begin
	 *            time to begin
	 */
	Timing(double begin) {
		setBegin(begin);
	}

	/**
	 * constructs new Timing container with a time to begin and end
	 * 
	 * @param begin
	 *            time to begin
	 * @param end
	 *            time to end
	 */
	Timing(double begin, double end) {
		setBegin(begin);
		setEnd(end);
	}

	/**
	 * @return sorted set of id's
	 */
	TreeSet<String> getAllElementIds() {
		ids = new TreeSet<String>();
		getElement("", false);
		return ids;
	}

	/**
	 * removes an element from content by its id
	 * 
	 * @param id
	 *            id of element to remove
	 */
	void removeElement(String id) {
		getElement(id, true);
	}

	/**
	 * gets a reference to an element by its id and may remove it from content
	 * if requested and found
	 * 
	 * @param id
	 *            id of element to get
	 * @param isRemoving
	 *            whether or not to remove the element if it's found
	 * @return the found element or null if not found or removed
	 */
	Body getElement(String id, boolean isRemoving) {
		b = null;
		getElement(id, content, isRemoving);
		return b;
	}

	/**
	 * recursively search through nested contents for an element by its id and
	 * possibly remove it from content if requested and found
	 * 
	 * @param id
	 *            id of element to get
	 * @param content
	 *            possibly nested contents to search
	 * @param isRemoving
	 *            whether or not to remove the element if it's found
	 */
	private static void getElement(String id, LinkedList<Body> content,
			boolean isRemoving) {
		for (Body body : content) {
			if (body != null && body.getId() != null) {
				boolean isParallel = body.getId().startsWith("p");
				boolean isSequence = body.getId().startsWith("s");
				if (body.getId().equals(id)) {
					if (isRemoving) {
						b = null;
						content.remove(body);
						break;
					} else {
						b = body;
					}
				} else if (isParallel || isSequence) {
					Timing t = (Timing) body;
					Timing.getElement(id, t.getBody(), isRemoving);
				}
				ids.add(body.getId());
			}
		}
	}

	/**
	 * add a body element to the content
	 * 
	 * @param element
	 *            body object to add
	 * @return id of added element or null if not successful
	 */
	public String addElement(Body element) {
		if (element != null) {
			content.add(element);
			return element.getId();
		} else {
			return null;
		}
	}

	/**
	 * add a body element to the content at a certain location
	 * 
	 * @param element
	 *            body object to add
	 * @param index
	 *            location to add element
	 * @return id of added element or null if not successful
	 */
	public String addElement(Body element, int index) {
		if (element != null && index >= 0 && index <= content.size()) {
			content.add(index, element);
			return element.getId();
		} else {
			return null;
		}
	}

	/**
	 * @return content
	 */
	public LinkedList<Body> getBody() {
		return content;
	}

	/**
	 * @return sorted set of all regions contained in possibly nested contents
	 */
	TreeSet<Region> getRegions() {
		TreeSet<Region> regions = new TreeSet<Region>();
		Iterator<Body> i = content.iterator();
		while (i.hasNext()) {
			Body body = i.next();
			if (body.getClass().getSuperclass().getName().contains("Timing")) {
				Timing t = (Timing) body;
				regions.addAll(t.getRegions());
			} else if (body.getClass().getName().contains("Audio"))
				;
			else {
				Media m = (Media) body;
				regions.add(m.getRegion());
			}
		}
		return regions;
	}

	@Override
	String toXml() {
		// <par id="p1" repeat="1" begin="0.0" end="10.0">
		// </par>
		// <seq id="s1" repeat="1" begin="1.0" end="11.0">
		// </seq>
		StringBuilder xml = new StringBuilder();
		String closingTag = null;

		if (!isImplicit) {
			if (id.startsWith("p")) {
				xml.append("<par");
				closingTag = "</par>";
			}
			if (id.startsWith("s")) {
				xml.append("<seq");
				closingTag = "</seq>";
			}
			xml.append(super.toXml());
			xml.append(">\n");
		}
		Iterator<Body> i = content.iterator();
		while (i.hasNext()) {
			xml.append(i.next().toXml());
		}

		if (!isImplicit) {
			xml.append(closingTag);
			xml.append("\n");
		}
		return xml.toString();
	}
}
