/*
 * AudioVideo.java
 */
package edu.nku.cs.csc440.team2.message;

/**
 * Java binding for dynamic media elements contained within the body of a SMIL
 * message; the superclass for Audio and Video objects;
 * "continuous media objects with an intrinsic duration" - w3.org
 * 
 * @author Shane Crandall
 * @version 1.0, 04/18/11
 */
public class AudioVideo extends Media {

	/**
	 * "Specifies the beginning of a sub-clip of a continuous media object as
	 * offset from the start of the media object." - w3.org
	 * default value is zero
	 */
	private double clipBegin = 0.0;

	/**
	 * "Specifies the end of a sub-clip of a continuous media object (such as
	 * audio, video or another presentation) that should be played." - w3.org
	 * default value is zero
	 */
	private double clipEnd = 0.0;

	/**
	 * constructs new AudioVideo with two arguments and the default values for
	 * begin time and clip begin time; clip end time is set to end time
	 * 
	 * @param end
	 *            time to end play
	 * @param src
	 *            source of this object
	 */
	AudioVideo(double end, String src) {
		setEnd(end);
		setSrc(src);
		setClipEnd(end);
	}

	/**
	 * constructs new AudioVideo with no defaults
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
	AudioVideo(double begin, double end, String src, double clipBegin,
			double clipEnd) {
		this(end, src);
		setBegin(begin);
		setClipBegin(clipBegin);
		setClipEnd(clipEnd);
	}

	/**
	 * @return clip time to begin play
	 */
	public double getClipBegin() {
		return clipBegin;
	}

	/**
	 * sets clip begin time if GTEQ zero
	 * 
	 * @param clipBegin
	 *            clip time to begin play
	 */
	public void setClipBegin(double clipBegin) {
		if (clipBegin >= 0.0) {
			this.clipBegin = clipBegin;
		}
	}

	/**
	 * @return clip time to end play
	 */
	public double getClipEnd() {
		return clipEnd;
	}

	/**
	 * sets clip end time if GTEQ zero
	 * 
	 * @param clipEnd
	 *            clip time to end play
	 */
	public void setClipEnd(double clipEnd) {
		if (clipEnd >= 0.0) {
			this.clipEnd = clipEnd;
		}
	}

	@Override
	String toXml() {
		// <audio id="a1" repeat="1" begin="0.0" end="12.0" src="audio.mp3"
		// clip-begin="3.0" clip-end="15.0"/>
		// <video id="v1" repeat="1" begin="0.5" end="9.5" src="video.mp4"
		// region="r1" fill="remove" clip-begin="12.0" clip-end="21.0"/>
		StringBuilder xml = new StringBuilder(super.toXml());
		xml.append(format("clip-begin", getClipBegin()));
		xml.append(format("clip-end", getClipEnd()));
		xml.append("/>\n");
		return xml.toString();
	}
}
