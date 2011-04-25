package edu.nku.cs.csc440.team2.composer;

import java.util.LinkedList;
import java.util.List;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * A Track is the graphical equivalent of a SMIL Sequence player. Tracks contain
 * Boxes, and cannot contain any pair of Boxes whose playback times overlap. A
 * Track can be drawn by calling setBounds() to set the Track's drawing bounds.
 * Then draw() draws this Track onto a given Canvas.
 * 
 * @author William Knauer <knauerw1@nku.edu>
 * @version 2011.0424
 */
public class Track implements Parcelable {

	/** The drawing height of a Track */
	public static final int HEIGHT = 2 * Box.SPACING + Box.HEIGHT;

	/** A List of Boxes in this Track */
	private List<Box> mBoxes;

	/** The drawing bounds of this Track */
	private Rect mBounds;

	/** Used to generate instances of this class from a Parcel */
	public static final Parcelable.Creator<Track> CREATOR
			= new Parcelable.Creator<Track>() {

		@Override
		public Track createFromParcel(Parcel source) {
			return new Track(source);
		}

		@Override
		public Track[] newArray(int size) {
			return new Track[size];
		}

	};

	/**
	 * Class constructor.
	 * 
	 * @param context
	 *            The context used to construct this Track.
	 */
	Track() {
		mBounds = new Rect();
		mBoxes = new LinkedList<Box>();
	}

	/**
	 * Class constructor for creating from a Parcel.
	 * 
	 * @param in
	 *            The Parcel to create from.
	 */
	Track(Parcel in) {
		mBoxes = new LinkedList<Box>();
		int numBoxes = in.readInt();
		for (int i = 0; i < numBoxes; i++) {
			char type = (char) in.readInt();
			switch (type) {
			case AudioBox.TYPE:
				mBoxes.add((Box) in.readParcelable(AudioBox.class
						.getClassLoader()));
				break;
			case ImageBox.TYPE:
				mBoxes.add((Box) in.readParcelable(ImageBox.class
						.getClassLoader()));
				break;
			case TextBox.TYPE:
				mBoxes.add((Box) in.readParcelable(TextBox.class
						.getClassLoader()));
				break;
			case VideoBox.TYPE:
				mBoxes.add((Box) in.readParcelable(VideoBox.class
						.getClassLoader()));
				break;
			}
		}
		mBounds = in.readParcelable(Rect.class.getClassLoader());
	}

	/**
	 * Adds a given Box to this Track at the given begin time if the Track fits.
	 * 
	 * @param box
	 *            The Box to be added.
	 * @param begin
	 *            The begin time.
	 * @return True if the Box fits.
	 */
	boolean addBox(Box box, int begin) {
		boolean added = false;
		if (fits(box, begin)) {
			box.setBegin(begin);
			mBoxes.add(box);
			added = true;
		}
		return added;
	}

	/**
	 * Determines if this Track contains a given Box.
	 * 
	 * @param box
	 *            The Box to check.
	 * @return True if this Track contains a given Box.
	 */
	boolean contains(Box box) {
		return mBoxes.contains(box);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	/**
	 * Draws this track onto a given Canvas.
	 * 
	 * @param canvas
	 *            The Canvas to draw on.
	 */
	void draw(Canvas canvas) {
		Paint p = new Paint();

		/* Draw background */
		p.setColor(Color.argb(255, 85, 85, 85));
		p.setAntiAlias(true);
		p.setStyle(Style.FILL);
		canvas.drawRect(getBounds(), p);

		/* Draw outline */
		p.setColor(Color.argb(255, 204, 204, 204));
		p.setAntiAlias(true);
		p.setStyle(Style.STROKE);
		p.setStrokeWidth(1.0f);
		canvas.drawRect(getBounds(), p);

		/* Draw each box */
		for (Box b : mBoxes) {
			b.setBounds(Composer.secToPx((b.getBegin()) / 10.0),
					getBounds().top + Box.SPACING,
					Composer.secToPx((b.getEnd()) / 10.0), getBounds().top
							+ Box.SPACING + Box.HEIGHT);
			b.draw(canvas);
		}
	}

	/**
	 * Determines whether or not a given Box fits in this Track at the specified
	 * begin time.
	 * 
	 * @param box
	 *            The Box to check.
	 * @param begin
	 *            The specified begin time for the Box.
	 * @return True if the Box fits in the Track.
	 */
	boolean fits(Box box, int begin) {
		boolean fits = true;
		double end = begin + box.getDuration();

		/* If the box fits within the bounds of the Track */
		if (begin < 0) {
			fits = false;
		}

		/* Check each box in this Track */
		for (Box b : mBoxes) {
			/* If box's begin overlaps with b */
			if (b.getBegin() <= begin && begin < b.getEnd()) {
				fits = false;
			}

			/* If box's end overlaps with b */
			if (b.getBegin() < end && end <= b.getEnd()) {
				fits = false;
			}

			/* If b's begin overlaps with box */
			if (begin <= b.getBegin() && b.getBegin() < end) {
				fits = false;
			}

			/* If b's end overlaps with box */
			if (begin < b.getEnd() && b.getEnd() <= end) {
				fits = false;
			}
		}
		return fits;
	}

	/**
	 * @return A List of the Boxes contained by this Track.
	 */
	LinkedList<Box> getAllBoxes() {
		return new LinkedList<Box>(mBoxes);
	}

	/**
	 * @return The drawing bounds of this Track.
	 */
	Rect getBounds() {
		return mBounds;
	}

	/**
	 * Get the Box that plays back during a given time.
	 * 
	 * @param time
	 *            The time to check.
	 * @return The Box that plays back during the given time. Returns null if no
	 *         such Box exists.
	 */
	Box getBox(int time) {
		Box result = null;
		for (Box m : mBoxes) {
			if (m.containsTime(time)) {
				result = m;
			}
		}
		return result;
	}

	/**
	 * @return True if this Track contains no Boxes.
	 */
	boolean isEmpty() {
		return mBoxes.isEmpty();
	}

	/**
	 * Removes and returns the Box that plays back at the specified time.
	 * 
	 * @param time
	 *            The time to check.
	 * @return The Box that plays back at the specified time. Returns null if no
	 *         such box was found.
	 */
	Box removeBox(int time) {
		Box result = getBox(time);
		if (result != null) {
			mBoxes.remove(result);
		}
		return result;
	}

	/**
	 * Sets the drawing bounds of this Track.
	 * 
	 * @param left
	 *            The left bound.
	 * @param top
	 *            The top bound.
	 * @param right
	 *            The right bounds.
	 * @param bottom
	 *            The bottom bound.
	 */
	void setBounds(int left, int top, int right, int bottom) {
		mBounds.set(left, top, right, bottom);
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(mBoxes.size());
		for (Box b : mBoxes) {
			dest.writeInt(b.getType());
			dest.writeParcelable((Parcelable) b, 0);
		}
		dest.writeParcelable(mBounds, 0);
	}

}
