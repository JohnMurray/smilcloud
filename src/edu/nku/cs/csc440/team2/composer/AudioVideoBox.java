package edu.nku.cs.csc440.team2.composer;

import android.os.Parcel;

/**
 * An AudioVideoBox is a Box with additional fields specific to Audio and Video
 * clips, such as clip-begin and clip-duration times.
 * 
 * @author William Knauer <knauerw1@nku.edu>
 * @version 2011.0424
 */
public abstract class AudioVideoBox extends Box {
	/**
	 * Formats an integer time in tenth-seconds to a String in HH:MM:SS.T.
	 * 
	 * @param time
	 *            The time to format.
	 * @return The formatted time as a String.
	 */
	public static String formatTime(int time) {
		int tenths = time % 10;
		time /= 10;
		int seconds = time % 60;
		time /= 60;
		int minutes = time % 60;
		time /= 60;
		
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

	/** The offset into the source media where playback should begin */
	private int mClipBegin;

	/** The duration of the source media */
	private int mClipDuration;

	/**
	 * Class constructor for creating from a Parcel.
	 * 
	 * @param in
	 *            The Parcel to create from.
	 */
	AudioVideoBox(Parcel in) {
		super(in);
		mClipBegin = in.readInt();
		mClipDuration = in.readInt();
	}

	/**
	 * Class constructor.
	 * 
	 * @param source
	 *            The source url.
	 * @param begin
	 *            The absolute begin time.
	 * @param duration
	 *            The playback duration.
	 * @param clipDuration
	 *            The duration of the source media.
	 */
	AudioVideoBox(String source, int begin, int duration, int clipDuration) {
		super(source, begin, duration);
		mClipBegin = 0;
		mClipDuration = clipDuration;
	}

	/**
	 * @return The time within the source media where playback should begin.
	 */
	public int getClipBegin() {
		return mClipBegin;
	}

	/**
	 * @return The duration of the source media.
	 */
	public int getClipDuration() {
		return mClipDuration;
	}

	/**
	 * @return The time within the source media where playback should end.
	 */
	public int getClipEnd() {
		return mClipBegin + getDuration();
	}

	/**
	 * @param clipBegin
	 *            The time within the source media where playback should begin.
	 */
	public void setClipBegin(int clipBegin) {
		mClipBegin = clipBegin;
	}

	/**
	 * @param clipDuration
	 *            The duration of the source media.
	 */
	public void setClipDuration(int clipDuration) {
		mClipDuration = clipDuration;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		super.writeToParcel(out, flags);
		out.writeInt(mClipBegin);
		out.writeInt(mClipDuration);
	}

}
