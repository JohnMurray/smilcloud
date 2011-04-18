package edu.nku.cs.csc440.team2.composer;

import edu.nku.cs.csc440.team2.message.Media;
import edu.nku.cs.csc440.team2.message.Video;
import edu.nku.cs.csc460.team2.R;
import android.graphics.Canvas;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * A VideoBox is a Box that represents an Image object.
 * 
 * @author William Knauer <knauerw1@nku.edu>
 * @version 2011.0416
 */
public class VideoBox extends AudioVideoBox {
	/** A static counter used to generate unique ids */
	private static int sCount = 1;

	/** Used to generate instances of this class from a Parcel */
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

	/**
	 * Class constructor for creating from a Parcel.
	 * 
	 * @param in
	 *            The Parcel to construct from.
	 */
	public VideoBox(Parcel in) {
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
	 * @param region
	 *            The region of the Media.
	 */
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
	public void draw(Canvas canvas) {
		if (getContext() != null) {
			super.draw(canvas,
					getContext().getResources().getColor(R.color.videobox_bg),
					getContext().getResources().getColor(R.color.videobox_fg),
					getContext().getResources().getColor(R.color.resize_grip));
		}
	}

	@Override
	public Media toMedia() {
		return new Video(getBegin(), getEnd(), getSource(), getRegion()
				.toRegion(), getClipBegin(), getClipEnd());
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		super.writeToParcel(dest);
	}

}
