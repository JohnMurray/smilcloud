package edu.nku.cs.csc440.team2.composer;

import java.util.LinkedList;

import edu.nku.cs.csc460.team2.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;

/**
 * A Track is the graphical equivalent of a SMIL Sequence player.
 * 
 * @author William Knauer <knauerw1@nku.edu>
 * @version 2011.0417
 */
public class Track {
	/** The Context used to get resources */
	private Context mContext;

	/** A List of Boxes in this Track */
	private LinkedList<Box> mBoxes;

	/** The drawing bounds of this Track */
	private Rect mBounds;

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
	public boolean addBox(Box elt, double begin) {
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
		int spacing = mContext.getResources().getInteger(R.integer.box_spacing);
		int height = mContext.getResources().getInteger(R.integer.box_height);

		/* Draw background */
		p.setColor(mContext.getResources().getColor(R.color.track_bg));
		p.setAntiAlias(true);
		p.setStyle(Style.FILL);
		canvas.drawRect(getBounds(), p);

		/* Draw outline */
		p.setColor(mContext.getResources().getColor(R.color.track_fg));
		p.setAntiAlias(true);
		p.setStyle(Style.STROKE);
		p.setStrokeWidth(1.0f);
		canvas.drawRect(getBounds(), p);

		/* Draw each box */
		for (Box mb : mBoxes) {
			mb.setBounds(Composer.secToPx(mb.getBegin()), getBounds().top
					+ spacing, Composer.secToPx(mb.getEnd()), getBounds().top
					+ spacing + height);
			mb.draw(canvas);
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
	public boolean fits(Box elt, double begin) {
		boolean fits = true;
		double end = begin + elt.getDuration();

		/* If the box fits within the bounds of the Track */
		if (begin < 0) {
			fits = false;
		}

		/* Check each box in this Track */
		for (Box mb : mBoxes) {
			/* If elt's begin overlaps with b */
			if (mb.getBegin() <= begin && begin < mb.getEnd()) {
				fits = false;
			}

			/* If elt's end overlaps with b */
			if (mb.getBegin() < end && end <= mb.getEnd()) {
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
	public Box getBox(double time) {
		Box result = null;
		for (Box m : mBoxes) {
			if (m.containsTime(time)) {
				result = m;
			}
		}
		return result;
	}

	/**
	 * Get the Box whose id matches a given String
	 * 
	 * @param id
	 *            The String to check.
	 * @return Returns the Box whose id matches the given String. Returns null
	 *         if no such Box exists.
	 */
	public Box getBox(String id) {
		Box result = null;
		for (Box b : mBoxes) {
			if (result == null && b.getId().equals(id)) {
				result = b;
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
	public Box removeBox(double time) {
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

	/**
	 * @param context
	 *            The Context to set.
	 */
	public void setContext(Context context) {
		mContext = context;
		for (Box b : mBoxes) {
			b.setContext(mContext);
		}
	}

}
