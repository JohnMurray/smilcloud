package edu.nku.cs.csc440.team2.composer;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author William Knauer <knauerw1@nku.edu>
 * @version 2011.0321
 */
public class AudioBox extends AudioVideoBox {
	private static int sCount = 1;
	public static final char TYPE = 'a';

	public static final Parcelable.Creator<AudioBox> CREATOR = new Parcelable.Creator<AudioBox>() {

		@Override
		public AudioBox createFromParcel(Parcel source) {
			return new AudioBox(source);
		}

		@Override
		public AudioBox[] newArray(int size) {
			return new AudioBox[size];
		}
	};

	public AudioBox(Parcel in) {
		super(in);
	}

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
	public void writeToParcel(Parcel dest, int flags) {
		super.writeToParcel(dest);
	}
	
}
