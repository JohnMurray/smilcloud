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
 * A Track is the graphical equivalent of a SMIL Sequence player.
 * 
 * @author William Knauer <knauerw1@nku.edu>
 * @version 2011.0421
 */
public class Track implements Parcelable {

	public static final int HEIGHT = 2*Box.SPACING + Box.HEIGHT;
	/** A List of Boxes in this Track */
	private List<Box> mBoxes;

	/** The drawing bounds of this Track */
	private Rect mBounds;
	
	public Track(Parcel in) {
		mBoxes = new LinkedList<Box>();
		int numBoxes = in.readInt();
		for (int i = 0; i < numBoxes; i ++) {
			char type = (char) in.readInt();
			switch(type) {
			case AudioBox.TYPE:
				mBoxes.add((Box) in.readParcelable(AudioBox.class.getClassLoader()));
				break;
			case ImageBox.TYPE:
				mBoxes.add((Box) in.readParcelable(ImageBox.class.getClassLoader()));
				break;
			case TextBox.TYPE:
				mBoxes.add((Box) in.readParcelable(TextBox.class.getClassLoader()));
				break;
			case VideoBox.TYPE:
				mBoxes.add((Box) in.readParcelable(VideoBox.class.getClassLoader()));
				break;
			}
		}
		mBounds = in.readParcelable(Rect.class.getClassLoader());
	}

	/**
	 * Class constructor.
	 * 
	 * @param context
	 *            The context used to construct this Track.
	 */
	public Track() {
		mBounds = new Rect();
		mBoxes = new LinkedList<Box>();
	}

	/**
	 * Adds a given Box to this Track at the given begin time if the Track fits.
	 * 
	 * @param elt
	 *            The Box to be added.
	 * @param begin
	 *            The begin time.
	 * @return Returns true if the Box fits.
	 */
	public boolean addBox(Box elt, int begin) {
		boolean added = false;
		if (fits(elt, begin)) {
			elt.setBegin(begin);
			mBoxes.add(elt);
			added = true;
		}
		return added;
	}

	/**
	 * Determines if this Track contains a given Box.
	 * 
	 * @param elt
	 *            The Box to check.
	 * @return Returns true if this Track contains a given Box.
	 */
	public boolean contains(Box elt) {
		return mBoxes.contains(elt);
	}

	/**
	 * Draws this track onto a given Canvas.
	 * 
	 * @param canvas
	 *            The Canvas to draw on.
	 */
	public void draw(Canvas canvas) {
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
			b.setBounds(
					Composer.secToPx(((double) b.getBegin()) / 10.0),
					getBounds().top + Box.SPACING,
					Composer.secToPx(((double) b.getEnd()) / 10.0),
					getBounds().top + Box.SPACING + Box.HEIGHT);
			b.draw(canvas);
		}
	}

	/**
	 * Determines whether or not a given Box fits in this Track at the specified
	 * begin time.
	 * 
	 * @param elt
	 *            The Box to check.
	 * @param begin
	 *            The specified begin time for the Box.
	 * @return Returns true if the Box fits in the Track.
	 */
	public boolean fits(Box elt, int begin) {
		boolean fits = true;
		double end = begin + elt.getDuration();

		/* If the box fits within the bounds of the Track */
		if (begin < 0) {
			fits = false;
		}

		/* Check each box in this Track */
		for (Box b : mBoxes) {
			/* If elt's begin overlaps with b */
			if (b.getBegin() <= begin && begin < b.getEnd()) {
				fits = false;
			}

			/* If elt's end overlaps with b */
			if (b.getBegin() < end && end <= b.getEnd()) {
				fits = false;
			}
			
			/* If b's begin overlaps with elt */
			if (begin <= b.getBegin() && b.getBegin() < end) {
				fits = false;
			}
			
			/* If b's end overlaps with elt */
			if (begin < b.getEnd() && b.getEnd() <= end) {
				fits = false;
			}
		}
		return fits;
	}

	/**
	 * @return Returns a List of the Boxes contained by this Track.
	 */
	public LinkedList<Box> getAllBoxes() {
		return new LinkedList<Box>(mBoxes);
	}

	/**
	 * @return Returns the drawing bounds of this Track.
	 */
	public Rect getBounds() {
		return mBounds;
	}

	/**
	 * Get the Box that plays back during a given time.
	 * 
	 * @param time
	 *            The time to check.
	 * @return Returns the Box that plays back during the given time. Returns
	 *         null if no such Box exists.
	 */
	public Box getBox(int time) {
		Box result = null;
		for (Box m : mBoxes) {
			if (m.containsTime(time)) {
				result = m;
			}
		}
		return result;
	}

	/**
	 * @return Returns true if this Track contains no Boxes.
	 */
	public boolean isEmpty() {
		return mBoxes.isEmpty();
	}

	/**
	 * Removes and returns the Box that plays back at the specified time.
	 * 
	 * @param time
	 *            The time to check.
	 * @return Returns the Box that plays back at the specified time. Returns
	 *         null if no such box was found.
	 */
	public Box removeBox(int time) {
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
	public void setBounds(int left, int top, int right, int bottom) {
		mBounds.set(left, top, right, bottom);
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
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
	
	/** Used to generate instances of this class from a Parcel */
	public static final Parcelable.Creator<Track> CREATOR = new Parcelable.Creator<Track>() {

		@Override
		public Track createFromParcel(Parcel source) {
			return new Track(source);
		}

		@Override
		public Track[] newArray(int size) {
			return new Track[size];
		}

	};

}
