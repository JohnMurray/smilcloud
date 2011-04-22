package edu.nku.cs.csc440.team2.composer;

/**
 * An AudioVideoBox is a Box with additional fields specific to Audio and Video
 * objects, such as clip begin and clip duration times.
 * 
 * @author William Knauer <knauerw1@nku.edu>
 * @version 2011.0421
 */
public abstract class AudioVideoBox extends Box {
	/** The offset into the source media where playback should begin */
	private int mClipBegin;
	
	/** The duration of the source media */
	private int mClipDuration;
	
	/**
	 * Class constructor.
	 * 
	 * @param source The source of the represented Media.
	 * @param begin The absolute begin time of the represented Media.
	 * @param duration The duration of the represented Media.
	 * @param clipDuration The duration of the source media.
	 */
	public AudioVideoBox(String source, int begin, int duration,
			int clipDuration) {
		super(source, begin, duration);
		mClipBegin = 0;
		mClipDuration = clipDuration;
	}
	
	public static String formatTime(int time) {
		int tenths = time % 10;
		time /= 10;
		int seconds = time % 60;
		time /= 60;
		int minutes = time % 60;
		time /= 60;
		
		// Hey Murray, watch this.
		StringBuilder s = new StringBuilder(12);
		if (time < 10) {
			s.append('0');
		}
		s.append(time);
		s.append(':');
		if (minutes < 10) {
			s.append('0');
		}
		s.append(minutes);
		s.append(':');
		if (seconds < 10) {
			s.append('0');
		}
		s.append(seconds);
		s.append('.');
		s.append(tenths);
		return s.toString();
	}

	public int getClipBegin() {
		return mClipBegin;
	}

	public int getClipDuration() {
		return mClipDuration;
	}

	public int getClipEnd() {
		return mClipBegin + getDuration();
	}

	public void setClipBegin(int clipBegin) {
		mClipBegin = clipBegin;
	}
	
	public void setClipDuration(int clipDuration) {
		mClipDuration = clipDuration;
	}
	
}
