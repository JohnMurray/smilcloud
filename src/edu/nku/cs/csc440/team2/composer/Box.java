package edu.nku.cs.csc440.team2.composer;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author William Knauer <knauerw1@nku.edu>
 * @version 2011.0321
 */
public abstract class Box implements Parcelable, Comparable<Box> {
	public static final int HEIGHT = 40;
	public static final int SPACING = 7;
	public static final int TEXT_OFFSET = 5;
	public static final int RESIZE_WIDTH = 35;

	private double mBegin;
	private double mDuration;
	private String mSource;
	private Rect mBounds;
	private Rect mResizeBounds;
	private ParcelableRegion mRegion;
	private String mId;
	private int mBgColor;
	private int mFgColor;
	private int mResizeColor;

	public Box(Parcel in) {
		mBegin = in.readDouble();
		mDuration = in.readDouble();
		mSource = in.readString();
		mBounds = in.readParcelable(Rect.class.getClassLoader());
		mResizeBounds = in.readParcelable(Rect.class.getClassLoader());
		mRegion = in.readParcelable(ParcelableRegion.class.getClassLoader());
		mId = in.readString();
		mBgColor = in.readInt();
		mFgColor = in.readInt();
		mResizeColor = in.readInt();
	}

	public Box(String source, double begin, double duration) {
		mBounds = new Rect(0, 0, Composer.secToPx(duration), HEIGHT);
		mSource = source;
		mBegin = begin;
		mDuration = duration;
		mResizeBounds = new Rect();
		mRegion = null;
		mId = null;
		mBgColor = -1;
		mFgColor = -1;
		mResizeColor = -1;
	}

	@Override
	public int compareTo(Box another) {
		int result = -9001;

		if (mRegion != null && another.getRegion() != null) {
			if (mRegion.getZindex() < another.getRegion().getZindex()) {
				result = -1;
			} else if (mRegion.getZindex() == another.getRegion().getZindex()) {
				result = 0;
			} else {
				result = 1;
			}
		}

		return result;
	}

	public boolean containsTime(double time) {
		boolean contains = false;
		if (getBegin() <= time && time < getEnd()) {
			contains = true;
		}
		return contains;
	}

	public void draw(Canvas canvas) {
		Paint p = new Paint();
		
		/* Draw background */
		p.setColor(getBgColor());
		p.setAntiAlias(true);
		p.setStyle(Style.FILL);
		canvas.drawRect(getBounds(), p);

		/* Draw label */
		p.setColor(getFgColor());
		p.setTextAlign(Align.LEFT);
		p.setTextSize(16.0f);
		canvas.drawText(
				getId(),
				getBounds().left + TEXT_OFFSET,
				getBounds().top + (getBounds().height() / 2)
						+ p.getFontMetrics().descent, p);

		/* Draw resize grip */
		updateResizeBounds();
		p.setColor(getResizeColor());
		canvas.drawRect(getResizeBounds(), p);

		/* Draw border */
		p.setColor(getFgColor());
		p.setStyle(Style.STROKE);
		p.setStrokeWidth(2.0f);
		canvas.drawRect(getBounds(), p);
	}

	public double getBegin() {
		return mBegin;
	}
	
	public int getBgColor() {
		return mBgColor;
	}

	public Rect getBounds() {
		return mBounds;
	}

	public double getDuration() {
		return mDuration;
	}

	public double getEnd() {
		return mBegin + mDuration;
	}
	
	public int getFgColor() {
		return mFgColor;
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
	
	public int getResizeColor() {
		return mResizeColor;
	}

	public String getSource() {
		return mSource;
	}

	public void setBegin(double begin) {
		mBegin = begin;
	}
	
	public void setBgColor(int color) {
		mBgColor = color;
	}

	public void setBounds(int left, int top, int right, int bottom) {
		mBounds.set(left, top, right, bottom);
	}

	public void setDuration(double duration) {
		mDuration = duration;
	}
	
	public void setFgColor(int color) {
		mFgColor = color;
	}

	public void setId(String id) {
		mId = id;
	}

	public void setRegion(ParcelableRegion region) {
		mRegion = region;
	}
	
	public void setResizeColor(int color) {
		mResizeColor = color;
	}
	
	public void setSource(String source) {
		mSource = source;
	}

	public void updateResizeBounds() {
		mResizeBounds.set(getBounds());
		mResizeBounds.left = mResizeBounds.right - RESIZE_WIDTH;
	}

	public void writeToParcel(Parcel dest) {
		dest.writeDouble(mBegin);
		dest.writeDouble(mDuration);
		dest.writeString(mSource);
		dest.writeParcelable(mBounds, 0);
		dest.writeParcelable(mResizeBounds, 0);
		dest.writeParcelable(mRegion, 0);
		dest.writeString(mId);
		dest.writeInt(mBgColor);
		dest.writeInt(mFgColor);
		dest.writeInt(mResizeColor);
	}
	
}
