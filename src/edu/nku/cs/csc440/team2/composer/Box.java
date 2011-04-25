package edu.nku.cs.csc440.team2.composer;

import edu.nku.cs.csc440.team2.message.Media;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.os.Parcel;

/**
 * A Box is a graphical representation of a message.Media object. Before being
 * drawn, setBounds() must be called to set the drawing target. Then draw() will
 * draw this Box to a Canvas. All times are stored in tenth-seconds as integers,
 * so 10.3 seconds should be stored as 103.
 * 
 * @author William Knauer <knauerw1@nku.edu>
 * @version 2011.0424
 */
public abstract class Box implements Comparable<Box> {
	/** The distance between the left side of a Box and its name label */
	public static final int TEXT_OFFSET = 5;

	/** The width of the resize grip */
	public static final int RESIZE_WIDTH = 35;

	/** The vertical spacing between a Box and the Track containing it */
	public static final int SPACING = 7;

	/** The height of a Box */
	public static final int HEIGHT = 40;

	/** The absolute begin time of the represented media in */
	private int mBegin;

	/** The absolute duration of the represented media in */
	private int mDuration;

	/** The name of the represented media */
	private String mName;

	/** The source of the represented media */
	private String mSource;

	/** The drawing bounds */
	private Rect mBounds;

	/** The resize grip bounds */
	private Rect mResizeBounds;

	/** The region of the represented media */
	private ComposerRegion mRegion;

	/** A unique id for this box */
	private String mId;

	/** A single character type identifier for this Box's media */
	private char mType;

	/** The generated label to be drawn */
	private String mLabel;

	/**
	 * Class constructor for creating from a Parcel.
	 * 
	 * @param in
	 *            The Parcel to create from.
	 */
	Box(Parcel in) {
		mBegin = in.readInt();
		mDuration = in.readInt();
		mName = in.readString();
		mSource = in.readString();
		mBounds = in.readParcelable(Rect.class.getClassLoader());
		mResizeBounds = in.readParcelable(Rect.class.getClassLoader());
		mRegion = in.readParcelable(ComposerRegion.class.getClassLoader());
		mId = in.readString();
		mType = (char) in.readInt();
		mLabel = in.readString();
	}

	/**
	 * Class constructor.
	 * 
	 * @param source
	 *            The source of the media.
	 * @param begin
	 *            The absolute begin time of the media.
	 * @param duration
	 *            The duration of the media.
	 */
	Box(String source, int begin, int duration) {
		mBounds = new Rect();
		mSource = source;
		mBegin = begin;
		mDuration = duration;
		mResizeBounds = new Rect();
		mRegion = null;
		mId = null;
		mLabel = null;
		mName = null;
		mType = ' ';
	}

	@Override
	public int compareTo(Box another) {
		int result = Integer.MIN_VALUE;

		/* Sort by z-index ascending */
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

	/**
	 * Determines if the represented media will play during a given time.
	 * 
	 * @param time
	 *            The time to check.
	 * @return True if the media will play during the given time.
	 */
	boolean containsTime(int time) {
		boolean contains = false;
		if (getBegin() <= time && time < getEnd()) {
			contains = true;
		}
		return contains;
	}

	/**
	 * Draws this Box onto a given Canvas within the set bounds.
	 * 
	 * @param canvas
	 *            The canvas to draw on.
	 */
	abstract void draw(Canvas canvas);

	/**
	 * Draws this Box onto a given Canvas within the set bounds.
	 * 
	 * @param canvas
	 *            The canvas to draw on.
	 * @param bgColor
	 *            The background color of this Box.
	 * @param fgColor
	 *            The text and outline color of this Box.
	 * @param resizeColor
	 *            The color of the resize grip.
	 */
	protected void draw(Canvas canvas, int bgColor, int fgColor, int resizeColor) {
		Paint p = new Paint();

		/* Draw background */
		p.setColor(bgColor);
		p.setAntiAlias(true);
		p.setStyle(Style.FILL);
		canvas.drawRect(getBounds(), p);

		/* Draw label */
		p.setColor(fgColor);
		p.setTextAlign(Align.LEFT);
		p.setTextSize(16.0f);
		if (mLabel == null) {
			generateLabel(p);
		}
		canvas.drawText(mLabel, getBounds().left + TEXT_OFFSET, getBounds().top
				+ (getBounds().height() / 2) + p.getFontMetrics().descent, p);

		/* Draw resize grip */
		updateResizeBounds();
		p.setColor(resizeColor);
		canvas.drawRect(getResizeBounds(), p);

		/* Draw border */
		p.setColor(fgColor);
		p.setStyle(Style.STROKE);
		p.setStrokeWidth(2.0f);
		canvas.drawRect(getBounds(), p);
	}

	/**
	 * Formats the name of the Box so it fits nicely within the Box when drawn.
	 * The String is stored in mLabel.
	 * 
	 * @param p
	 *            The Paint which will be used to draw the label.
	 */
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
		int available = mBounds.width() - TEXT_OFFSET - RESIZE_WIDTH;

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

	/**
	 * @return The media's absolute begin time.
	 */
	int getBegin() {
		return mBegin;
	}

	/**
	 * @return This Box's drawing bounds.
	 */
	Rect getBounds() {
		return mBounds;
	}

	/**
	 * @return The media's playback duration.
	 */
	int getDuration() {
		return mDuration;
	}

	/**
	 * @return The media's absolute end time.
	 */
	int getEnd() {
		return mBegin + mDuration;
	}

	/**
	 * @return The unique id for this Box.
	 */
	String getId() {
		return mId;
	}

	/**
	 * @return The media's name.
	 */
	String getName() {
		return mName;
	}

	/**
	 * @return The ComposerRegion associated with the media.
	 */
	ComposerRegion getRegion() {
		return mRegion;
	}

	/**
	 * @return The drawing bounds of the resize grip.
	 */
	Rect getResizeBounds() {
		return mResizeBounds;
	}

	/**
	 * @return The source url of the media.
	 */
	String getSource() {
		return mSource;
	}

	/**
	 * @return The type of the media.
	 */
	char getType() {
		return mType;
	}

	/**
	 * Signals that mLabel must be regenerated on the next draw.
	 */
	void postUpdateLabel() {
		mLabel = null;
	}

	/**
	 * @param begin
	 *            The media's absolute begin time.
	 */
	void setBegin(int begin) {
		mBegin = begin;
	}

	/**
	 * Sets the drawing bounds of this Box.
	 * 
	 * @param left
	 *            The left drawing bound.
	 * @param top
	 *            The top drawing bound.
	 * @param right
	 *            The right drawing bound.
	 * @param bottom
	 *            The bottom drawing bound.
	 */
	void setBounds(int left, int top, int right, int bottom) {
		mBounds.set(left, top, right, bottom);
	}

	/**
	 * @param duration
	 *            The media's playback duration.
	 */
	void setDuration(int duration) {
		mDuration = duration;
	}

	/**
	 * @param id
	 *            The unique id for this box.
	 */
	void setId(String id) {
		mId = id;
	}

	/**
	 * @param name
	 *            The name of the media.
	 */
	void setName(String name) {
		mName = name;
	}

	/**
	 * @param region
	 *            The ComposerRegion associated with the media.
	 */
	void setRegion(ComposerRegion region) {
		mRegion = region;
	}

	/**
	 * @param source
	 *            The source url of the media.
	 */
	void setSource(String source) {
		mSource = source;
	}

	/**
	 * @param type
	 *            The type of the media.
	 */
	protected void setType(char type) {
		mType = type;
	}

	/**
	 * Generates a message.Media object from this Box.
	 * 
	 * @return Returns a new Media object.
	 */
	abstract Media toMedia();

	/**
	 * Sets the drawing bounds of the resize grip relative to the drawing bounds
	 * of this Box.
	 */
	protected void updateResizeBounds() {
		mResizeBounds.set(getBounds());
		mResizeBounds.left = mResizeBounds.right;
		mResizeBounds.left -= RESIZE_WIDTH;
	}

	/**
	 * Writes the contents of this Box to a Parcel.
	 * 
	 * @param out
	 *            The Parcel to write to.
	 * @param flags
	 *            Flags for writing to the Parcel.
	 */
	void writeToParcel(Parcel out, int flags) {
		out.writeInt(mBegin);
		out.writeInt(mDuration);
		out.writeString(mName);
		out.writeString(mSource);
		out.writeParcelable(mBounds, 0);
		out.writeParcelable(mResizeBounds, 0);
		out.writeParcelable(mRegion, 0);
		out.writeString(mId);
		out.writeInt(mType);
		out.writeString(mLabel);
	}

}
