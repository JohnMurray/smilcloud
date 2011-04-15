package edu.nku.cs.csc440.team2.composer;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author William Knauer <knauerw1@nku.edu>
 * @version 2011.0321
 */
public class VideoBox extends AudioVideoBox {
	private static int sCount = 1;
	public static final char TYPE = 'v';

	public static final Parcelable.Creator<VideoBox> CREATOR = new Parcelable.Creator<VideoBox>() {

		@Override
		public VideoBox createFromParcel(Parcel source) {
			return new VideoBox(source);
		}

		@Override
		public VideoBox[] newArray(int size) {
			return new VideoBox[size];
		}

	};

	public VideoBox(Parcel in) {
		super(in);
	}

	public VideoBox(String source, double begin, double duration,
			double clipDuration, ParcelableRegion region) {
		super(source, begin, duration, clipDuration);
		setRegion(region);
		setId("Video " + sCount);
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
