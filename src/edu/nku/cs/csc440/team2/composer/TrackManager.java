package edu.nku.cs.csc440.team2.composer;

import java.util.LinkedList;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

/**
 * @author William Knauer <knauerw1@nku.edu>
 * @version 2011.0321
 */
public class TrackManager extends Drawable
{
	private static final int TOP_BOTTOM_PADDING = 5;
	private final double mLength;
	private LinkedList<Track> mTracks;
	private MoveManager mMoveManager;
	
	public TrackManager(double length)
	{
		mLength = length;
		mMoveManager = new MoveManager(this);
		mTracks = new LinkedList<Track>();
		maintain();
	}
	
	public boolean addBox(Box elt, double begin)
	{
		boolean added = false;
		for (Track t : mTracks)
		{ 
			if (!added)
			{
				added = t.addBox(elt, begin);
			}
		}
		maintain();
		return added;
	}
	
	public boolean addBox(Box elt, int targetX, int targetY)
	{
		boolean added = false;
		Track target = getTrack(targetX, targetY);
		if (target != null)
		{
			added = target.addBox(elt, targetX, targetY);
		}
		return added;
	}
	
	public int measureHeight()
	{
		return (Track.HEIGHT * mTracks.size()) + (2 * TOP_BOTTOM_PADDING);
	}
	
	@Override
	public void draw(Canvas canvas)
	{
		/* Draw tracks */
		int y = getBounds().top;
		for (Track t : mTracks)
		{
			t.setBounds(
					0,
					y,
					ComposerView.secToPx(mLength),
					y + Track.HEIGHT);
			t.draw(canvas);
			y += Track.HEIGHT;
		}
		
		/* Draw moving box (if any) */
		mMoveManager.draw(canvas);
	}
	
	public Box getBox(int targetX, int targetY)
	{
		Box result = null;
		Track t = getTrack(targetX, targetY);
		if (t != null)
		{
			result = t.getBox(targetX, targetY);
		}
		return result;
	}
	
	public MoveManager getMoveManager()
	{
		return mMoveManager;
	}
	
	public LinkedList<Box> getConcurrentElements(Box box)
	{
		LinkedList<Box> concurrent = new LinkedList<Box>();
		double begin = box.getBegin();
		double end = box.getEnd();
		for (Track t : mTracks)
		{
			for (Box m : t.getBoxes())
			{
				if (m.getBegin() <= begin && begin < m.getEnd())
				{
					concurrent.add(m);
				}
				else if (m.getBegin() < end && end <= m.getEnd())
				{
					concurrent.add(m);
				}
			}
		}
		return concurrent;
	}
	
	@Override
	public int getOpacity()
	{
		// Unimplemented for now.
		return 0;
	}
	
	private Track getTrack(int targetX, int targetY)
	{
		Track result = null;
		for (Track t : mTracks)
		{
			if (t.getBounds().contains(targetX, targetY))
			{
				result = t;
			}
		}
		return result;
	}
	
	public void maintain()
	{
		if (mTracks.isEmpty() || !mTracks.getLast().isEmpty())
		{
			mTracks.addLast(new Track(mLength));
		}
		else
		{
			if (mTracks.size() >= 2)
			{
				boolean done = false;
				int indexOfSecondLast = mTracks.size() - 2;
				while (!done && indexOfSecondLast >= 0)
				{
					if (mTracks.get(indexOfSecondLast).isEmpty())
					{
						mTracks.remove(indexOfSecondLast);
						indexOfSecondLast --;
					}
					else
					{
						done = true;
					}
				}
			}
		}
	}
	
	public Box removeBox(int targetX, int targetY)
	{
		Box result = null;
		Track t = getTrack(targetX, targetY);
		if (t != null)
		{
			result = t.removeBox(targetX, targetY);
		}
		return result;
	}

	@Override
	public void setAlpha(int alpha)
	{
		// Unimplemented for now.
	}

	@Override
	public void setColorFilter(ColorFilter cf)
	{
		// Unimplemented for now.
	}
	
	/**
	 * @author William Knauer <knauerw1@nku.edu>
	 * @version 2011.0322
	 */
	class MoveManager
	{
		private boolean mHasOldHome;
		private boolean mFitsTarget;
		private boolean mMoving;
		private Box mMovingBox;
		private Track mOldTrack;
		private double mOldBegin;
		private Track mTargetTrack;
		private double mTargetBegin;
		private TrackManager mTrackManager;
		
		public MoveManager(TrackManager trackManager)
		{
			mTrackManager = trackManager;
			reset();
		}
		
		public void start(Box movingBox, boolean isNew)
		{
			mMoving = true;
			mMovingBox = movingBox;
			if (isNew)
			{
				mHasOldHome = false;
			}
			else
			{
				mHasOldHome = true;
				mOldTrack = mTrackManager.getTrack(mMovingBox.getBounds().centerX(), mMovingBox.getBounds().centerY());
				mOldBegin = movingBox.getBegin();
				mTrackManager.removeBox(movingBox.getBounds().centerX(), movingBox.getBounds().centerY());
			}
		}
		
		public void draw(Canvas canvas)
		{
			if (mMovingBox != null)
			{
				mMovingBox.draw(canvas);
			}
		}
		
		public void finish()
		{
			if (mFitsTarget)
			{
				mTargetTrack.addBox(mMovingBox, mTargetBegin);
				mTrackManager.maintain();
				reset();
			}
			else if (mHasOldHome)
			{
				mOldTrack.addBox(mMovingBox, mOldBegin);
				mTrackManager.maintain();
				reset();
			}
		}
		
		public Rect getBounds()
		{
			Rect result = null;
			if (mMovingBox != null)
			{
				result = mMovingBox.getBounds();
			}
			return result;
		}
		
		public boolean isMoving()
		{
			return mMoving;
		}
		
		public void offset(int dx, int dy)
		{
			Rect r = new Rect(mMovingBox.getBounds());
			r.offset(dx, dy);
			mMovingBox.setBounds(r);
			double begin = ComposerView.snapTo(ComposerView.pxToSec(r.left));
			Track track = getTrack(r.left, r.centerY());
			if (track != null)
			{
				if (track.fits(mMovingBox, begin))
				{
					mTargetTrack = track;
					mTargetBegin = begin;
					mFitsTarget = true;
				}
			}
			else
			{
				mFitsTarget = false;
			}
		}
		
		private void reset()
		{
			mHasOldHome = false;
			mFitsTarget = false;
			mMoving = false;
			mMovingBox = null;
			mOldTrack = null;
			mOldBegin = -1.0;
			mTargetTrack = null;
			mTargetBegin = -1.0;
		}
	}
}
