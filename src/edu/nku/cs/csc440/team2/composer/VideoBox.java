package edu.nku.cs.csc440.team2.composer;

import edu.nku.cs.csc440.team2.message.Media;
import edu.nku.cs.csc440.team2.message.Video;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * A VideoBox is a Box that represents an Image object.
 * 
 * @author William Knauer <knauerw1@nku.edu>
 * @version 2011.0423
 */
public class VideoBox extends AudioVideoBox implements Parcelable {
	public static final char TYPE = 'V';
	private static int sCount = 0;
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
	public VideoBox(String source, int begin, int duration,
			int clipDuration, ComposerRegion region) {
		super(source, begin, duration, clipDuration);
		setRegion(region);
		setType(TYPE);
		setId("" + TYPE + sCount++);
	}

	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas,
				Color.argb(255, 255, 64, 0),
				Color.argb(255, 255, 255, 255),
				Color.argb(255, 255, 255, 255));
	}

	@Override
	public Media toMedia() {
		return new Video(
				((double) getBegin()) / 10.0,
				((double) getEnd()) / 10.0,
				getSource(),
				getRegion().toRegion(),
				((double) getClipBegin()) / 10.0,
				((double) getClipEnd()) / 10.0);
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public void writeToParcel(Parcel out, int flags) {
		super.writeToParcel(out, flags);
	}
	
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
}
