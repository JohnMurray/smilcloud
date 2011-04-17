package edu.nku.cs.csc440.team2.composer;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author William Knauer <knauerw1@nku.edu>
 * @version 2011.0321
 */
public class ImageBox extends Box {
	private static int sCount = 1;
	public static final char TYPE = 'i';

	public static final Parcelable.Creator<ImageBox> CREATOR = new Parcelable.Creator<ImageBox>() {

		@Override
		public ImageBox createFromParcel(Parcel source) {
			return new ImageBox(source);
		}

		@Override
		public ImageBox[] newArray(int size) {
			return new ImageBox[size];
		}

	};

	public ImageBox(Parcel in) {
		super(in);
	}

	public ImageBox(String source, double begin, double duration,
			ParcelableRegion region) {
		super(source, begin, duration);
		setRegion(region);
		setId("Image " + sCount);
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
