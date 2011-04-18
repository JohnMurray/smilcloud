/*
 * SmilDimension.java
 */
package edu.nku.cs.csc440.team2.message;

/**
 * custom partial implementation of AWT Dimension class as Android doesn't
 * support AWT; represents a dimension in two dimensional space with two
 * coordinates
 * 
 * @author Shane Crandall
 * @version 1.0, 04/18/11
 */
public class SmilDimension {

	/**
	 * coordinate 'x'; "Specifies the content width of boxes." - w3.org
	 */
	private int width;

	/**
	 * coordinate 'y'; "Specifies the content height of boxes." - w3.org
	 */
	private int height;

	/**
	 * construct a new dimension at an origin of zero
	 */
	public SmilDimension() {
		width = 0;
		height = 0;
	}

	/**
	 * construct a new dimension from an existing dimension
	 * 
	 * @param smilDimension
	 *            existing dimension
	 */
	public SmilDimension(SmilDimension smilDimension) {
		width = smilDimension.getWidth();
		height = smilDimension.getHeight();
	}

	/**
	 * construct a new dimension from two coordinates
	 * 
	 * @param width
	 *            width to use
	 * @param height
	 *            height to use
	 */
	public SmilDimension(int width, int height) {
		this.width = width;
		this.height = height;
	}

	/**
	 * @return height, coordinate 'y'
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * sets the height
	 * 
	 * @param height
	 *            height to use
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	/**
	 * @return width, coordinate 'x'
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * sets the width
	 * 
	 * @param width
	 *            width to use
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	@Override
	public boolean equals(Object obj) {
		SmilDimension smilDimension = (SmilDimension) obj;
		if (this.width == smilDimension.getWidth()
				&& this.height == smilDimension.getHeight()) {
			return true;
		} else {
			return false;
		}
	}

}
