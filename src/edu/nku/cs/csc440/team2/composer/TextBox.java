package edu.nku.cs.csc440.team2.composer;

import edu.nku.cs.csc440.team2.message.Media;
import edu.nku.cs.csc440.team2.message.Text;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * A TextBox is a Box that represents a Text object.
 * 
 * @author William Knauer <knauerw1@nku.edu>
 * @version 2011.0424
 */
public class TextBox extends Box implements Parcelable {
	/** Constant to indicate the type of Box this is */
	public static final char TYPE = 'T';

	/** Static counter used to generate unique ids */
	private static int sCount = 0;

	/** Used to generate instances of this class from a Parcel */
	public static final Parcelable.Creator<TextBox> CREATOR
			= new Parcelable.Creator<TextBox>() {

		@Override
		public TextBox createFromParcel(Parcel source) {
			return new TextBox(source);
		}

		@Override
		public TextBox[] newArray(int size) {
			return new TextBox[size];
		}

	};

	/**
	 * Class constructor for creating from a Parcel.
	 * 
	 * @param in
	 *            The Parcel to create from.
	 */
	TextBox(Parcel in) {
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
	TextBox(String source, int begin, int duration, ComposerRegion region) {
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
		super.draw(canvas, Color.argb(255, 0, 127, 0),
				Color.argb(255, 255, 255, 255), Color.argb(255, 255, 255, 255));
	}

	@Override
	Media toMedia() {
		return new Text((getBegin()) / 10.0, (getEnd()) / 10.0, getSource(),
				getRegion().toRegion());
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		super.writeToParcel(out, flags);
	}

}
