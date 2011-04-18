package edu.nku.cs.csc440.team2.composer;

import edu.nku.cs.csc440.team2.message.Media;
import edu.nku.cs.csc460.team2.R;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * A Box is a graphical representation of a message.Media object. Before being
 * drawn, a Context must be set and setBounds() must be called to set the
 * drawing target. Then draw() will draw this Box to a Canvas. 
 *  
 * @author William Knauer <knauerw1@nku.edu>
 * @version 2011.0416
 */
public abstract class Box implements Parcelable, Comparable<Box> {
	/** A Context used to get resources */
	private Context mContext;
	
	/** The absolute begin time of the represented Media */
	private double mBegin;
	
	/** The absolute duration of the represented Media */
	private double mDuration;
	
	/** The source of the represented Media */
	private String mSource;
	
	/** The drawing bounds */
	private Rect mBounds;
	
	/** The resize grip bounds */
	private Rect mResizeBounds;
	
	/** The region of the represented Media */
	private ParcelableRegion mRegion;
	
	/** A unique human-readable id for this box */
	private String mId;

	/**
	 * Class constructor for creating from a Parcel.
	 * 
	 * @param in The Parcel to construct from.
	 */
	public Box(Parcel in) {
		mBegin = in.readDouble();
		mDuration = in.readDouble();
		mSource = in.readString();
		mBounds = in.readParcelable(Rect.class.getClassLoader());
		mResizeBounds = in.readParcelable(Rect.class.getClassLoader());
		mRegion = in.readParcelable(ParcelableRegion.class.getClassLoader());
		mId = in.readString();
		mContext = null;
	}

	/**
	 * Class constructor.
	 * 
	 * @param source The source of the Media.
	 * @param begin The absolute begin time of the Media. 
	 * @param duration The duration of the Media.
	 */
	public Box(String source, double begin, double duration) {
		mBounds = new Rect();
		mSource = source;
		mBegin = begin;
		mDuration = duration;
		mResizeBounds = new Rect();
		mRegion = null;
		mId = null;
		mContext = null;
	}

	@Override
	public int compareTo(Box another) {
		int result = Integer.MIN_VALUE;

		/* Sort by z-index ascending */
		if (mRegion != null && another.getRegion() != null) {
			if (mRegion.getZindex() < another.getRegion().getZindex()) {
				result = -1;
			} else if (mRegion.getZindex()
					== another.getRegion().getZindex()) {
				result = 0;
			} else {
				result = 1;
			}
		}

		return result;
	}

	/**
	 * Determines if the Media will play during a given time.
	 * 
	 * @param time The time to check. 
	 * @return Returns true if the Media will play during the given time.
	 */
	public boolean containsTime(double time) {
		boolean contains = false;
		if (getBegin() <= time && time < getEnd()) {
			contains = true;
		}
		return contains;
	}
	
	/**
	 * Draws this Box onto a given Canvas within the set bounds.
	 * 
	 * @param canvas The canvas to draw on.
	 */
	public abstract void draw(Canvas canvas);

	/**
	 * Draws this Box onto a given Canvas within the set bounds.
	 * 
	 * @param canvas The canvas to draw on.
	 * @param bgColor The background color of this Box.
	 * @param fgColor The text and outline color of this Box.
	 * @param resizeColor The color of the resize grip.
	 */
	public void draw(
			Canvas canvas, int bgColor, int fgColor, int resizeColor) {
		if (mContext != null) {
			Paint p = new Paint();
			
			/* Draw background */
			p.setColor(bgColor);
			p.setAntiAlias(true);
			p.setStyle(Style.FILL);
			canvas.drawRect(getBounds(), p);
		
			/* Draw label */
			p.setColor(fgColor);
			p.setTextAlign(Align.LEFT);
			p.setTextSize(mContext.getResources().getInteger(
					R.integer.box_text_size));
			canvas.drawText(
					getId(),
					getBounds().left + mContext.getResources().getInteger(
							R.integer.box_text_offset),
					getBounds().top + (getBounds().height() / 2)
							+ p.getFontMetrics().descent, p);
		
			/* Draw resize grip */
			updateResizeBounds();
			p.setColor(resizeColor);
			canvas.drawRect(getResizeBounds(), p);
		
			/* Draw border */
			p.setColor(fgColor);
			p.setStyle(Style.STROKE);
			p.setStrokeWidth(mContext.getResources().getInteger(
					R.integer.box_border_width));
			canvas.drawRect(getBounds(), p);
		}
	}

	public double getBegin() {
		return mBegin;
	}

	public Rect getBounds() {
		return mBounds;
	}
	
	public Context getContext() {
		return mContext;
	}

	public double getDuration() {
		return mDuration;
	}

	public double getEnd() {
		return mBegin + mDuration;
	}

	public String getId() {
		return mId;
	}

	public ParcelableRegion getRegion() {
		return mRegion;
	}

	public Rect getResizeBounds() {
		return mResizeBounds;
	}

	public String getSource() {
		return mSource;
	}

	public void setBegin(double begin) {
		mBegin = begin;
	}
	
	public void setBounds(int left, int top, int right, int bottom) {
		mBounds.set(left, top, right, bottom);
	}
	
	public void setContext(Context context) {
		mContext = context;
	}

	public void setDuration(double duration) {
		mDuration = duration;
	}

	public void setId(String id) {
		mId = id;
	}

	public void setRegion(ParcelableRegion region) {
		mRegion = region;
	}
	
	public void setSource(String source) {
		mSource = source;
	}
	
	/**
	 * Generates a Media object from this Box.
	 * 
	 * @return Returns a new Media object. 
	 */
	public abstract Media toMedia();

	/**
	 * Sets the drawing bounds of the resize grip relative to the drawing
	 * bounds of this Box.
	 */
	protected void updateResizeBounds() {
		mResizeBounds.set(getBounds());
		mResizeBounds.left = mResizeBounds.right;
		mResizeBounds.left -= mContext.getResources().getInteger(
				R.integer.resize_grip_width);
	}

	/**
	 * Writes this class to a Parcel.
	 * 
	 * @param dest The Parcel to write to.
	 */
	public void writeToParcel(Parcel dest) {
		dest.writeDouble(mBegin);
		dest.writeDouble(mDuration);
		dest.writeString(mSource);
		dest.writeParcelable(mBounds, 0);
		dest.writeParcelable(mResizeBounds, 0);
		dest.writeParcelable(mRegion, 0);
		dest.writeString(mId);
	}
	
}
