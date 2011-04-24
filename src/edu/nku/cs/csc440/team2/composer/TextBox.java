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
 * @version 2011.0423
 */
public class TextBox extends Box implements Parcelable {
	public static final char TYPE = 'T';
	private static int sCount = 0;
	public TextBox(Parcel in) {
		super(in);
	}
	
	/**
	 * Class constructor.
	 * 
	 * @param source The source of the Media.
	 * @param begin The absolute begin time of the Media.
	 * @param duration The duration of the Media.
	 * @param region The region of the Media.
	 */
	public TextBox(String source, int begin, int duration,
			ComposerRegion region) {
		super(source, begin, duration);
		setRegion(region);
		setType(TYPE);
		setId("" + TYPE + sCount++);
	}

	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas,
				Color.argb(255, 0, 127, 0),
				Color.argb(255, 255, 255, 255),
				Color.argb(255, 255, 255, 255));
	}

	@Override
	public Media toMedia() {
		return new Text(
				((double) getBegin()) / 10.0,
				((double) getEnd()) / 10.0,
				getSource(),
				getRegion().toRegion());
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
	
}
