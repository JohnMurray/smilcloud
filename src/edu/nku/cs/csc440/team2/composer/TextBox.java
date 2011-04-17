package edu.nku.cs.csc440.team2.composer;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author William Knauer <knauerw1@nku.edu>
 * @version 2011.0321
 */
public class TextBox extends Box {
	private static int sCount = 1;
	public static final char TYPE = 't';

	public static final Parcelable.Creator<TextBox> CREATOR = new Parcelable.Creator<TextBox>() {

		@Override
		public TextBox createFromParcel(Parcel source) {
			return new TextBox(source);
		}

		@Override
		public TextBox[] newArray(int size) {
			return new TextBox[size];
		}

	};

	public TextBox(Parcel in) {
		super(in);
	}

	public TextBox(String source, double begin, double duration,
			ParcelableRegion region) {
		super(source, begin, duration);
		setRegion(region);
		setId("Text " + sCount);
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
