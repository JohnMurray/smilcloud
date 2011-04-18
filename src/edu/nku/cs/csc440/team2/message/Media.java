/*
 * Media.java
 */
package edu.nku.cs.csc440.team2.message;

/**
 * Java binding for all media elements contained within the body of a SMIL
 * message; the superclass for all TextImage and AudioVideo objects; logically
 * comprises all the characteristics of any media object
 * 
 * @author Shane Crandall
 * @version 1.0, 04/18/11
 */
public abstract class Media extends Body {

	/**
	 * "The effective end of the child element is the minimum of the effective
	 * end of the parent and the desired end of the child element." - w3.org
	 */
	public final static String REMOVE = "remove";

	/**
	 * "The effective end of the child element is equal to the effective end of
	 * the parent. The last state of the element is retained on the screen until
	 * the effective end of the element." - w3.org
	 */
	public final static String FREEZE = "freeze";

	/**
	 * "Describes the clip type and location." - real.com
	 */
	private String src = null;

	/**
	 * "Specifies an abstract rendering surface [and] controls the position,
	 * size and scaling of media object elements." - w3.org
	 */
	private Region region = null;

	/**
	 * "If the child element has no "fill" attribute, the effective end of the
	 * child depends on whether or not the child has an explicit duration or
	 * end." - w3.org; default value is "remove"
	 */
	private String fill = REMOVE;

	/**
	 * @return type of fill
	 */
	public String getFill() {
		return fill;
	}

	/**
	 * sets fill type unless media is an audio object
	 * 
	 * @param fill
	 *            type of fill
	 */
	public void setFill(String fill) {
		boolean isAudio = this.getClass().getName().contains("Audio");
		if (fill == null) {
			if (isAudio) {
				this.fill = fill;
			}
		} else {
			if ((!isAudio && fill.equals(REMOVE))
					|| (!isAudio && fill.equals(FREEZE))) {
				this.fill = fill;
			}
		}
	}

	/**
	 * @return region
	 */
	public Region getRegion() {
		return region;
	}

	/**
	 * sets region unless media is an audio object
	 * 
	 * @param region
	 *            region to use
	 */
	public void setRegion(Region region) {
		boolean isAudio = this.getClass().getName().contains("Audio");
		if (region == null) {
			if (isAudio) {
				this.region = region;
			} else {
				this.region = new Region();
			}
		} else {
			if (!isAudio) {
				this.region = region;
			}
		}
	}

	/**
	 * @return source
	 */
	public String getSrc() {
		return src;
	}

	/**
	 * sets the source of this element if the source has valid character data
	 * 
	 * @param src
	 *            source to use
	 */
	public void setSrc(String src) {
		this.src = new CdataValidator().validate(src, "%");
	}

	@Override
	String toXml() {
		// <text id="t0" repeat="1" begin="1.5" end="8.5"
		// src="http://www.textfiles.com/anarchy/206.txt" region="r1"
		// fill="remove"/>
		// <img id="i0" repeat="1" begin="0.5" end="2.0" src="image.jpg"
		// region="r3" fill="remove"/>
		// <audio id="a1" repeat="1" begin="0.0" end="12.0" src="audio.mp3"
		// clip-begin="3.0" clip-end="15.0"/>
		// <video id="v1" repeat="1" begin="0.5" end="9.5" src="video.mp4"
		// region="r1" fill="remove" clip-begin="12.0" clip-end="21.0"/>
		StringBuilder xml = new StringBuilder();
		if (id.startsWith("t")) {
			xml.append("<text");
		}
		if (id.startsWith("i")) {
			xml.append("<img");
		}
		if (id.startsWith("a")) {
			xml.append("<audio");
		}
		if (id.startsWith("v")) {
			xml.append("<video");
		}
		xml.append(super.toXml());
		xml.append(format("src", getSrc()));
		if (!id.startsWith("a")) {
			xml.append(format("region", getRegion().getId()));
			xml.append(format("fill", getFill()));
		}
		if (!id.startsWith("a") && !id.startsWith("v")) {
			xml.append("/>\n");
		}
		return xml.toString();
	}
}
