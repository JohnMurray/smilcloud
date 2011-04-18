package edu.nku.cs.csc440.team2.composer;

import edu.nku.cs.csc440.team2.message.Audio;
import edu.nku.cs.csc440.team2.message.Media;
import edu.nku.cs.csc460.team2.R;
import android.graphics.Canvas;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * An AudioBox is a Box that represents an Audio object.
 * 
 * @author William Knauer <knauerw1@nku.edu>
 * @version 2011.0416
 */
public class AudioBox extends AudioVideoBox {
	/** A static counter used to generate unique ids */
	private static int sCount = 1;

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
	 * @param in The Parcel to construct from.
	 */
	public AudioBox(Parcel in) {
		super(in);
	}

	/**
	 * Class constructor.
	 * 
	 * @param source The source of the Media.
	 * @param begin The absolute begin time of the Media.
	 * @param duration The duration of the Media.
	 * @param clipDuration The duration of the source media.
	 */
	public AudioBox(String source, double begin, double duration,
			double clipDuration) {
		super(source, begin, duration, clipDuration);
		setId("Audio " + sCount);
		sCount++;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void draw(Canvas canvas) {
		if (getContext() != null) {
			super.draw(canvas,
					getContext().getResources().getColor(R.color.audiobox_bg),
					getContext().getResources().getColor(R.color.audiobox_fg),
					getContext().getResources().getColor(R.color.resize_grip));
		}
	}

	@Override
	public Media toMedia() {
		return new Audio(getBegin(), getEnd(), getSource(),
				getClipBegin(), getClipEnd());
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		super.writeToParcel(dest);
	}
	
}
