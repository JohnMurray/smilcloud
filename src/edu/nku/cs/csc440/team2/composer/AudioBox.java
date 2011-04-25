package edu.nku.cs.csc440.team2.composer;

import edu.nku.cs.csc440.team2.message.Audio;
import edu.nku.cs.csc440.team2.message.Media;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * An AudioBox is a Box that represents an Audio object.
 * 
 * @author William Knauer <knauerw1@nku.edu>
 * @version 2011.0424
 */
public class AudioBox extends AudioVideoBox implements Parcelable {
	/** Constant to indicate the type of Box this is */
	public static final char TYPE = 'A';

	/** Static counter used to generate unique ids */
	private static int sCount = 0;

	/** Used to generate instances of this class from a Parcel */
	public static final Parcelable.Creator<AudioBox> CREATOR
			= new Parcelable.Creator<AudioBox>() {

		@Override
		public AudioBox createFromParcel(Parcel source) {
			return new AudioBox(source);
		}

		@Override
		public AudioBox[] newArray(int size) {
			return new AudioBox[size];
		}

	};

	/**
	 * Class constructor for creating from a Parcel.
	 * 
	 * @param in
	 *            The Parcel to create from.
	 */
	AudioBox(Parcel in) {
		super(in);
	}

	/**
	 * Class constructor.
	 * 
	 * @param source
	 *            The source of the Media.
	 * @param begin
	 *            The absolute begin time of the Media.
	 * @param duration
	 *            The duration of the Media.
	 * @param clipDuration
	 *            The duration of the source media.
	 */
	AudioBox(String source, int begin, int duration, int clipDuration) {
		super(source, begin, duration, clipDuration);
		setType(TYPE);
		setId("" + TYPE + sCount++);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	void draw(Canvas canvas) {
		super.draw(canvas, Color.argb(255, 139, 0, 204),
				Color.argb(255, 255, 255, 255), Color.argb(255, 255, 255, 255));
	}

	@Override
	Media toMedia() {
		return new Audio((getBegin()) / 10.0, (getEnd()) / 10.0, getSource(),
				(getClipBegin()) / 10.0, (getClipEnd()) / 10.0);
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		super.writeToParcel(out, flags);
	}

}
