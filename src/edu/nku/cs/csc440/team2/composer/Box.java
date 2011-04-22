package edu.nku.cs.csc440.team2.composer;

import edu.nku.cs.csc440.team2.message.Media;
import edu.nku.cs.csc460.team2.R;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;

/**
 * A Box is a graphical representation of a message.Media object. Before being
 * drawn, a Context must be set and setBounds() must be called to set the
 * drawing target. Then draw() will draw this Box to a Canvas. 
 *  
 * @author William Knauer <knauerw1@nku.edu>
 * @version 2011.0421
 */
public abstract class Box implements Comparable<Box> {
	/** A Context used to get resources */
	private Context mContext;
	
	/** The absolute begin time of the represented Media */
	private int mBegin;
	
	/** The absolute duration of the represented Media */
	private int mDuration;
	
	/** The name of the represented Media */
	private String mName;
	
	/** The source of the represented Media */
	private String mSource;
	
	/** The drawing bounds */
	private Rect mBounds;
	
	/** The resize grip bounds */
	private Rect mResizeBounds;
	
	/** The region of the represented Media */
	private ComposerRegion mRegion;
	
	/** A unique id for this box */
	private String mId;
	
	/** A single character type identifier for this Box's media */
	private char mType;
	
	/** The generated label to be drawn */
	private String mLabel;

	/**
	 * Class constructor.
	 * 
	 * @param source The source of the Media.
	 * @param begin The absolute begin time of the Media. 
	 * @param duration The duration of the Media.
	 */
	public Box(String source, int begin, int duration) {
		mBounds = new Rect();
		mSource = source;
		mBegin = begin;
		mDuration = duration;
		mResizeBounds = new Rect();
		mRegion = null;
		mId = null;
		mLabel = null;
		mName = null;
		mContext = null;
		mType = ' ';
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
	public boolean containsTime(int time) {
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
			if (mLabel == null) {
				generateLabel(p);
			}
			canvas.drawText(
					mLabel,
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

	public int getBegin() {
		return mBegin;
	}

	public Rect getBounds() {
		return mBounds;
	}
	
	public Context getContext() {
		return mContext;
	}

	public int getDuration() {
		return mDuration;
	}

	public int getEnd() {
		return mBegin + mDuration;
	}

	public String getId() {
		return mId;
	}

	public String getName() {
		return mName;
	}
	
	public ComposerRegion getRegion() {
		return mRegion;
	}

	public Rect getResizeBounds() {
		return mResizeBounds;
	}

	public String getSource() {
		return mSource;
	}
	
	public char getType() {
		return mType;
	}

	public void setBegin(int begin) {
		mBegin = begin;
	}
	
	public void setBounds(int left, int top, int right, int bottom) {
		mBounds.set(left, top, right, bottom);
	}
	
	public void setContext(Context context) {
		mContext = context;
	}

	public void setDuration(int duration) {
		mDuration = duration;
	}

	public void setId(String id) {
		mId = id;
	}

	public void setName(String name) {
		mName = name;
	}
	
	public void setRegion(ComposerRegion region) {
		mRegion = region;
	}
	
	public void setSource(String source) {
		mSource = source;
	}
	
	public void setType(char type) {
		mType = type;
	}
	
	/**
	 * Generates a Media object from this Box.
	 * 
	 * @return Returns a new Media object. 
	 */
	public abstract Media toMedia();
	
	private void generateLabel(Paint p) {
		StringBuilder s = new StringBuilder();
		
		/* Append type indicator to beginning */
		s.append('(');
		s.append(getType());
		s.append(") ");
		
		/* Append the human-readable name */
		s.append(getName());
		
		/* See how much space it takes up */
		int measurement = (int) p.measureText(s.toString());
		
		/* Determine how much space we have to work with */
		int resizeWidth = getContext().getResources().getInteger(R.integer.resize_grip_width);
		int textOffset = getContext().getResources().getInteger(R.integer.box_text_offset);
		int available = mBounds.width() - textOffset - resizeWidth;
		
		/* Figure out how long the label should be */
		if (available < 0) {
			mLabel = new String();
		} else if (available > measurement) {
			mLabel = s.toString();
		} else {
			s.append("...");
			while (available <= measurement && s.length() >= 4) {
				s.deleteCharAt(s.length() - 4);
				measurement = (int) p.measureText(s.toString()); 
			}
			if (available > measurement) {
				mLabel = s.toString();
			} else {
				mLabel = new String();
			}
		}
		
	}
	
	public void postUpdateLabel() {
		mLabel = null;
	}

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
	
}
