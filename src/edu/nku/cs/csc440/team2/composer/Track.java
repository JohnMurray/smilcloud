package edu.nku.cs.csc440.team2.composer;

import java.util.LinkedList;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author William Knauer <knauerw1@nku.edu>
 * @version 2011.0321
 */
public class Track implements Parcelable {
	public static final int HEIGHT = Box.HEIGHT + (2 * Box.SPACING);
	private LinkedList<Box> mBoxes;
	private Rect mBounds;
	private int mBgColor;
	private int mFgColor;

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

	public Track(int bgColor, int fgColor) {
		mBounds = new Rect();
		mBoxes = new LinkedList<Box>();
		mBgColor = bgColor;
		mFgColor = fgColor;
	}

	public Track(Parcel in) {
		int numBoxes = in.readInt();
		mBoxes = new LinkedList<Box>();
		for (int i = 0; i < numBoxes; i++) {
			int type = in.readInt();
			Box b = null;
			switch (type) {
			case AudioBox.TYPE:
				b = in.readParcelable(AudioBox.class.getClassLoader());
				break;
			case ImageBox.TYPE:
				b = in.readParcelable(ImageBox.class.getClassLoader());
				break;
			case TextBox.TYPE:
				b = in.readParcelable(TextBox.class.getClassLoader());
				break;
			case VideoBox.TYPE:
				b = in.readParcelable(VideoBox.class.getClassLoader());
				break;
			}
			mBoxes.add(b);
		}

		mBounds = in.readParcelable(Rect.class.getClassLoader());
		mBgColor = in.readInt();
		mFgColor = in.readInt();
	}

	public boolean addBox(Box elt, double begin) {
		boolean added = false;
		if (fits(elt, begin)) {
			elt.setBegin(begin);
			mBoxes.add(elt);
			added = true;
		}
		return added;
	}

	public boolean addBox(Box elt, int targetX, int targetY) {
		boolean result = false;
		if (getBounds().contains(targetX, targetY)) {
			double begin = Composer.pxToSec(targetX);
			begin = Composer.snapTo(begin);
			result = addBox(elt, begin);
		}
		return result;
	}

	public boolean contains(Box elt) {
		return mBoxes.contains(elt);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public void draw(Canvas canvas) {
		Paint p = new Paint();
		
		/* Draw background */
		p.setColor(mBgColor);
		p.setAntiAlias(true);
		p.setStyle(Style.FILL);
		canvas.drawRect(getBounds(), p);

		/* Draw outline */
		p.setColor(mFgColor);
		p.setAntiAlias(true);
		p.setStyle(Style.STROKE);
		p.setStrokeWidth(1.0f);
		canvas.drawRect(getBounds(), p);

		/* Draw each box */
		for (Box mb : mBoxes) {
			mb.setBounds(Composer.secToPx(mb.getBegin()), getBounds().top
					+ Box.SPACING, Composer.secToPx(mb.getEnd()),
					getBounds().top + Box.SPACING + Box.HEIGHT);
			mb.draw(canvas);
		}
	}

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

	public Rect getBounds() {
		return mBounds;
	}

	public Box getBox(double time) {
		Box result = null;
		for (Box m : mBoxes) {
			if (m.containsTime(time)) {
				result = m;
			}
		}
		return result;
	}

	public Box getBox(int targetX, int targetY) {
		Box result = null;
		if (getBounds().contains(targetX, targetY)) {
			double time = Composer.pxToSec(targetX);
			result = getBox(time);
		}
		return result;
	}

	public Box getBox(String label) {
		Box result = null;
		for (Box b : mBoxes) {
			if (result == null && b.getId().equals(label)) {
				result = b;
			}
		}
		return result;
	}

	public LinkedList<Box> getBoxes() {
		return new LinkedList<Box>(mBoxes);
	}

	public boolean isEmpty() {
		return mBoxes.isEmpty();
	}

	public Box removeBox(double time) {
		Box result = getBox(time);
		if (result != null) {
			mBoxes.remove(result);
		}
		return result;
	}

	public Box removeBox(int targetX, int targetY) {
		Box result = null;
		if (getBounds().contains(targetX, targetY)) {
			double time = Composer.pxToSec(targetX);
			result = removeBox(time);
		}
		return result;
	}

	public void setBounds(int left, int top, int right, int bottom) {
		mBounds.set(left, top, right, bottom);
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(mBoxes.size());
		for (Box b : mBoxes) {
			if (b instanceof AudioBox) {
				dest.writeInt(AudioBox.TYPE);
				dest.writeParcelable(b, 0);
			} else if (b instanceof ImageBox) {
				dest.writeInt(ImageBox.TYPE);
				dest.writeParcelable(b, 0);
			} else if (b instanceof TextBox) {
				dest.writeInt(TextBox.TYPE);
				dest.writeParcelable(b, 0);
			} else if (b instanceof VideoBox) {
				dest.writeInt(VideoBox.TYPE);
				dest.writeParcelable(b, 0);
			}
		}

		dest.writeParcelable(mBounds, 0);
		dest.writeInt(mBgColor);
		dest.writeInt(mFgColor);
	}

}
