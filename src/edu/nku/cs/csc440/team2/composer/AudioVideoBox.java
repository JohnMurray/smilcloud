package edu.nku.cs.csc440.team2.composer;

/**
 * An AudioVideoBox is a Box with additional fields specific to Audio and Video
 * objects, such as clip begin and clip duration times.
 * 
 * @author William Knauer <knauerw1@nku.edu>
 * @version 2011.0419
 */
public abstract class AudioVideoBox extends Box {
	/** The offset into the source media where playback should begin */
	private double mClipBegin;
	
	/** The duration of the source media */
	private double mClipDuration;
	
	/**
	 * Class constructor.
	 * 
	 * @param source The source of the represented Media.
	 * @param begin The absolute begin time of the represented Media.
	 * @param duration The duration of the represented Media.
	 * @param clipDuration The duration of the source media.
	 */
	public AudioVideoBox(String source, double begin, double duration,
			double clipDuration) {
		super(source, begin, duration);
		mClipBegin = 0.0;
		mClipDuration = clipDuration;
	}

	public double getClipBegin() {
		return mClipBegin;
	}

	public double getClipDuration() {
		return mClipDuration;
	}

	public double getClipEnd() {
		return mClipBegin + getDuration();
	}

	public void setClipBegin(double clipBegin) {
		mClipBegin = clipBegin;
	}
	
	public void setClipDuration(double clipDuration) {
		mClipDuration = clipDuration;
	}
	
}
