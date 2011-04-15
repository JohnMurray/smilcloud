package edu.nku.cs.csc440.team2.composer;

import java.util.LinkedList;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author William Knauer <knauerw1@nku.edu>
 * @version 2011.0321
 */
public class TrackManager implements Parcelable {
	/**
	 * @author William Knauer <knauerw1@nku.edu>
	 * @version 2011.0322
	 */
	class MoveManager {
		private boolean mFitsTarget;
		private Box mBox;
		private Track mOldTrack;
		private double mOldBegin;
		private Track mTargetTrack;
		private double mTargetBegin;

		public MoveManager() {
			reset();
		}

		public void draw(Canvas canvas) {
			if (mBox != null) {
				mBox.draw(canvas);
			}
		}

		public void finish() {
			if (mFitsTarget) {
				mTargetTrack.addBox(mBox, mTargetBegin);
				maintain();
				reset();
			} else {
				mOldTrack.addBox(mBox, mOldBegin);
				maintain();
				reset();
			}
		}

		public Rect getBounds() {
			Rect result = null;
			if (mBox != null) {
				result = mBox.getBounds();
			}
			return result;
		}

		public boolean isMoving() {
			return (mBox != null);
		}

		public void offset(int dx, int dy) {
			/* Offset mBox by (dx, dy) */
			mBox.setBounds(mBox.getBounds().left + dx, mBox.getBounds().top
					+ dy, mBox.getBounds().right + dx, mBox.getBounds().bottom
					+ dy);

			/* Figure out where mBox is now */
			double begin = Composer
					.snapTo(Composer.pxToSec(mBox.getBounds().left));
			Track track = getTrack(mBox.getBounds().centerX(), mBox.getBounds()
					.centerY());

			/* Figure out if mBox fits where it is now */
			if (track != null && track.fits(mBox, begin)) {
				mTargetTrack = track;
				mTargetBegin = begin;
				mFitsTarget = true;
			} else {
				mFitsTarget = false;
			}
		}

		private void reset() {
			mFitsTarget = false;
			mBox = null;
			mOldTrack = null;
			mOldBegin = -1.0;
			mTargetTrack = null;
			mTargetBegin = -1.0;
		}

		public void start(int x, int y) {
			mBox = getBox(x, y);
			if (mBox != null) {
				mOldTrack = getTrack(x, y);
				mOldBegin = mBox.getBegin();
				removeBox(x, y);
			}
		}
		
	}

	class ResizeManager {
		private Box mBox;
		private Track mTrack;
		private int mStartX;
		private double mStartDuration;

		public ResizeManager() {
			reset();
		}

		public void finish() {
			reset();
		}

		public boolean isResizing() {
			return (mBox != null);
		}

		public void reset() {
			mBox = null;
			mTrack = null;
			mStartX = -1;
			mStartDuration = -1;
		}

		public void resize(int x) {
			/* Determine the desired duration */
			double delta = Composer.snapTo(Composer.pxToSec(x - mStartX));
			double duration = mStartDuration + delta;

			/* Check if new duration is ok */
			boolean ok = true;

			/* Make sure the desired duration isn't too small */
			if (duration < Composer.snapTo(0.1)) {
				ok = false;
			}

			/* Make sure the desired duration isn't too large */
			if (mBox instanceof AudioVideoBox) {
				double max = ((AudioVideoBox) mBox).getClipDuration();
				max -= ((AudioVideoBox) mBox).getClipBegin();
				max = Composer.snapTo(max);
				if (duration > max) {
					ok = false;
				}
			}

			if (ok) {
				/* Remove the box from its track */
				mTrack.removeBox(mBox.getBounds().centerX(), mBox.getBounds()
						.centerY());

				/* Update the box's duration */
				double oldDuration = mBox.getDuration();
				mBox.setDuration(duration);

				/* Attempt to re-add the box */
				boolean fits = mTrack.addBox(mBox, mBox.getBegin());

				/* If it fails revert the change and re-add the box */
				if (!fits) {
					mBox.setDuration(oldDuration);
					mTrack.addBox(mBox, mBox.getBegin());
				}
			}
		}

		public boolean start(int x, int y) {
			/* Get the box that was touched */
			Box b = getBox(x, y);
			if (b != null) {
				/* Check if the box was touched for resize */
				if (b.getResizeBounds().contains(x, y)) {
					mBox = b;
					mTrack = getTrack(x, y);
					mStartX = x;
					mStartDuration = b.getDuration();
				}
			}
			return isResizing();
		}
		
	}

	private static final int TOP_BOTTOM_PADDING = 5;
	private LinkedList<Track> mTracks;
	private MoveManager mMoveManager;

	private ResizeManager mResizeManager;

	private Rect mBounds;
	private int mBgColor;
	private int mFgColor;

	public static final Parcelable.Creator<TrackManager> CREATOR = new Parcelable.Creator<TrackManager>() {

		@Override
		public TrackManager createFromParcel(Parcel source) {
			return new TrackManager(source);
		}

		@Override
		public TrackManager[] newArray(int size) {
			return new TrackManager[size];
		}

	};

	public TrackManager(int bgColor, int fgColor) {
		mBounds = new Rect();
		mMoveManager = new MoveManager();
		mResizeManager = new ResizeManager();
		mTracks = new LinkedList<Track>();
		mBgColor = bgColor;
		mFgColor = fgColor;
		maintain();
	}

	public TrackManager(Parcel in) {
		mMoveManager = new MoveManager();
		mResizeManager = new ResizeManager();
		mTracks = new LinkedList<Track>();
		in.readTypedList(mTracks, Track.CREATOR);
		mBounds = in.readParcelable(Rect.class.getClassLoader());
		mBgColor = in.readInt();
		mFgColor = in.readInt();
	}

	public boolean addBox(Box elt, double begin) {
		boolean added = false;
		for (Track t : mTracks) {
			if (!added) {
				added = t.addBox(elt, begin);
			}
		}
		maintain();
		return added;
	}

	public boolean addBox(Box elt, int targetX, int targetY) {
		boolean added = false;
		Track target = getTrack(targetX, targetY);
		if (target != null) {
			added = target.addBox(elt, targetX, targetY);
		}
		return added;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public void draw(Canvas canvas) {
		/* Draw tracks */
		int y = getBounds().top;
		for (Track t : mTracks) {
			t.setBounds(0, y, mBounds.width(), y + Track.HEIGHT);
			t.draw(canvas);
			y += Track.HEIGHT;
		}

		/* Draw moving box (if any) */
		mMoveManager.draw(canvas);
	}
	
	public Rect getBounds() {
		return mBounds;
	}

	public Box getBox(int targetX, int targetY) {
		Box result = null;
		Track t = getTrack(targetX, targetY);
		if (t != null) {
			result = t.getBox(targetX, targetY);
		}
		return result;
	}

	public Box getBox(String label) {
		Box result = null;
		for (Track t : mTracks) {
			if (result == null) {
				result = t.getBox(label);
			}
		}
		return result;
	}

	public LinkedList<Box> getBoxes() {
		LinkedList<Box> boxes = new LinkedList<Box>();
		for (Track t : mTracks) {
			for (Box b : t.getBoxes()) {
				boxes.add(b);
			}
		}
		return boxes;
	}

	public LinkedList<Box> getConcurrentElements(Box box) {
		LinkedList<Box> concurrent = new LinkedList<Box>();
		double begin = box.getBegin();
		double end = box.getEnd();
		for (Track t : mTracks) {
			for (Box m : t.getBoxes()) {
				if (m.getBegin() <= begin && begin < m.getEnd()) {
					concurrent.add(m);
				} else if (m.getBegin() < end && end <= m.getEnd()) {
					concurrent.add(m);
				}
			}
		}
		return concurrent;
	}
	
	public double getMaxEndTime() {
		double result = 0;
		for (Box b : getBoxes()) {
			if (b.getEnd() > result) {
				result = b.getEnd();
			}
		}
		if (mMoveManager.isMoving()) {
			if (Composer.pxToSec(mMoveManager.getBounds().right) > result) {
				result = Composer.pxToSec(mMoveManager.getBounds().right);
			}
		}
		return result;
	}
	
	public int getMaxY() {
		int result = 0;
		for (Track t : mTracks) {
			if (t.getBounds().bottom > result) {
				result = t.getBounds().bottom;
			}
		}
		return result;
	}

	public MoveManager getMoveManager() {
		return mMoveManager;
	}

	public ResizeManager getResizeManager() {
		return mResizeManager;
	}

	private Track getTrack(int targetX, int targetY) {
		Track result = null;
		for (Track t : mTracks) {
			if (t.getBounds().contains(targetX, targetY)) {
				result = t;
			}
		}
		return result;
	}

	public void maintain() {
		if (mTracks.isEmpty() || !mTracks.getLast().isEmpty()) {
			Track t = new Track(mBgColor, mFgColor);
			mTracks.addLast(t);
		} else {
			if (mTracks.size() >= 2) {
				boolean done = false;
				int indexOfSecondLast = mTracks.size() - 2;
				while (!done && indexOfSecondLast >= 0) {
					if (mTracks.get(indexOfSecondLast).isEmpty()) {
						mTracks.remove(indexOfSecondLast);
						indexOfSecondLast--;
					} else {
						done = true;
					}
				}
			}
		}
	}

	public int measureHeight() {
		return (Track.HEIGHT * mTracks.size()) + (2 * TOP_BOTTOM_PADDING);
	}

	public void removeBox(Box b) {
		for (Track t : mTracks) {
			if (t.contains(b)) {
				t.removeBox(b.getBegin());
			}
		}
	}

	public Box removeBox(int targetX, int targetY) {
		Box result = null;
		Track t = getTrack(targetX, targetY);
		if (t != null) {
			result = t.removeBox(targetX, targetY);
		}
		return result;
	}
	
	public void setBounds(int left, int top, int right, int bottom) {
		mBounds.set(left, top, right, bottom);
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeTypedList(mTracks);
		dest.writeParcelable(mBounds, 0);
		dest.writeInt(mBgColor);
		dest.writeInt(mFgColor);
	}
	
}
