package edu.nku.cs.csc440.team2.composer;

import edu.nku.cs.csc440.team2.message.Image;
import edu.nku.cs.csc440.team2.message.Media;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * An ImageBox is a Box that represents an Image object.
 * 
 * @author William Knauer <knauerw1@nku.edu>
 * @version 2011.0424
 */
public class ImageBox extends Box implements Parcelable {
	/** Constant to indicate the type of Box this is */
	public static final char TYPE = 'I';

	/** Static counter used to generate unique ids */
	private static int sCount = 0;

	/** Used to generate instances of this class from a Parcel */
	public static final Parcelable.Creator<ImageBox> CREATOR
			= new Parcelable.Creator<ImageBox>() {

		@Override
		public ImageBox createFromParcel(Parcel source) {
			return new ImageBox(source);
		}

		@Override
		public ImageBox[] newArray(int size) {
			return new ImageBox[size];
		}

	};

	/**
	 * Class constructor for creating from a Parcel.
	 * 
	 * @param in
	 *            The Parcel to create from.
	 */
	ImageBox(Parcel in) {
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
	 * @param region
	 *            The region of the Media.
	 */
	ImageBox(String source, int begin, int duration, ComposerRegion region) {
		super(source, begin, duration);
		setRegion(region);
		setType(TYPE);
		setId("" + TYPE + sCount++);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	void draw(Canvas canvas) {
		super.draw(canvas, Color.argb(255, 16, 52, 166),
				Color.argb(255, 255, 255, 255), Color.argb(255, 255, 255, 255));
	}

	@Override
	Media toMedia() {
		return new Image((getBegin()) / 10.0, (getEnd()) / 10.0, getSource(),
				getRegion().toRegion());
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		super.writeToParcel(out, flags);
	}

}
