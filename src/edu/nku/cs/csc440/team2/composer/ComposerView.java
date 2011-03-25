package edu.nku.cs.csc440.team2.composer;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.GestureDetector;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;

/**
 * A ComposerView is the View for a ComposerActivity. The ComposerView contains
 * the UI for editing messages. Touch gestures are the only way to interact
 * with this View.
 * 
 * @author William Knauer <knauerw1@nku.edu>
 * @version 2011.0322
 */
public class ComposerView extends View implements OnGestureListener,
        OnDoubleTapListener
{
	private static float sScale = 1.5f;
	
	/**
	 * @return Returns the current scaling factor.
	 */
	public static float getScale()
	{
		return sScale;
	}
	
	/**
	 * Maps pixels along the x-axis to seconds, subject to the scaling factor.
	 * 
	 * @param px The position in pixels to map.
	 * @return Returns the pixels mapped to seconds.
	 */
	public static double pxToSec(int px)
	{
		return px / (sScale * 100.0);
	}
	
	/**
	 * Maps seconds to pixels along the x-axis, subject to the scaling factor.
	 * 
	 * @param seconds The number of seconds to map.
	 * @return Returns the seconds mapped to pixels.
	 */
	public static int secToPx(double seconds)
	{
		return (int) (seconds * sScale * 100.0);
	}
	
	/**
	 * @param scale The scaling factor for mapping time to the x-axis.
	 */
	public static void setScale(float scale)
	{
		sScale = scale;
	}
	
	/**
	 * Rounds a double to the nearest tenth.
	 * 
	 * @param d The double to be rounded.
	 * @return Returns the double mapped to the nearest tenth.
	 */
	public static double snapTo(double d)
	{
		long result = Math.round(d * 10.0);
		return result / 10.0;
	}
	
	private ComposerActivity mParent;
	private GestureDetector mGestureDetector;
	private Rect mBounds;
	private float mLength;
	private Timeline mTimeline;
	private TrackManager mTrackManager;
	
	/**
	 * Class constructor.
	 * 
	 * @param context The Context needed to construct Views.
	 */
	public ComposerView(ComposerActivity parent)
	{
		super(parent.getBaseContext());
		mParent = parent;
		mGestureDetector = new GestureDetector(this);
		mLength = 20.0f; // temporary -> set from message
		mBounds = new Rect(0, 0, secToPx(mLength), 1000);
		mTimeline = new Timeline(mLength);
		mTrackManager = new TrackManager(mLength);
		
		// TODO TEST CODE
		AudioBox a1 = new AudioBox("", 3.0, 2.0, 120.0);
		VideoBox v1 = new VideoBox("", 2.0, 2.0, 120.0, null);
		mTrackManager.addBox(a1, a1.getBegin());
		mTrackManager.addBox(v1, v1.getBegin());
	}
	
	/**
	 * Scroll this View by (x, y) = (dx, dy) pixels. Does not allow scrolling
	 * past the bounds.
	 * 
	 * @param dx The horizontal scroll distance.
	 * @param dy The vertical scroll distance.
	 */
	public void boundedScrollBy(float dx, float dy)
	{
		if (getScrollX() + dx > mBounds.right - getWidth())
		{
			dx = mBounds.right - getWidth() - getScrollX();
		}
		else if (getScrollX() + dx < mBounds.left)
		{
			dx = mBounds.left - getScrollX();
		}
		
		if (getScrollY() + dy > mBounds.bottom - getHeight())
		{
			dy = mBounds.bottom - getHeight() - getScrollY();
		}
		else if (getScrollY() + dy < mBounds.top)
		{
			dy = mBounds.top - getScrollY();
		}
		
		super.scrollBy((int)dx, (int)dy);
	}
	
	@Override
	public boolean onDoubleTap(MotionEvent e)
	{
		return false;
	}

	@Override
	public boolean onDoubleTapEvent(MotionEvent e)
	{
		return false;
	}

	@Override
	public boolean onDown(MotionEvent e)
	{
		if (!mTrackManager.getMoveManager().isMoving())
		{
			Box target = mTrackManager.getBox(
					(int) (e.getX() + getScrollX()),
					(int) (e.getY() + getScrollY()));
			if (target != null)
			{
				mTrackManager.getMoveManager().start(target, false);
			}
		}
		return true;
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		/* Draw tracks */
		mTrackManager.setBounds(
				mBounds.left,
				mBounds.top + Timeline.HEIGHT + 5,
				mBounds.right,
				mBounds.top + Timeline.HEIGHT + 5
					+ mTrackManager.measureHeight());
		mTrackManager.draw(canvas);
		
		/* Draw timeline */
		mTimeline.setBounds(
				mBounds.left,
				getScrollY(),
				mBounds.right,
				getScrollY() + Timeline.HEIGHT);
		mTimeline.draw(canvas);
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY)
	{
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e)
	{
		int x = (int) e.getX() + getScrollX();
		int y = (int) e.getY() + getScrollY();
		Box target = mTrackManager.getBox(x, y);
		if (target != null)
		{
			mParent.launchPropertiesActivity(target, mTrackManager.getConcurrentElements(target));
			invalidate();
		}
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY)
	{
		if (mTrackManager.getMoveManager().isMoving())
		{
			mTrackManager.getMoveManager().offset(
					(int) -distanceX, (int) -distanceY);
			invalidate();
		}
		else
		{
			boundedScrollBy(distanceX, distanceY);
		}
		return true;
	}

	@Override
	public void onShowPress(MotionEvent e) {}
	
	@Override
	public boolean onSingleTapConfirmed(MotionEvent e)
	{
		return false;
	}
	
	@Override
	public boolean onSingleTapUp(MotionEvent e)
	{
		return false;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent ev)
	{
		mGestureDetector.onTouchEvent(ev);
		if (ev.getAction() == MotionEvent.ACTION_UP)
		{
			onUp(ev);
		}
		return true;
	}
	
	/**
	 * Called when a finger is lifted from the touch screen.
	 * 
	 * @param ev The MotionEvent that triggered this call.
	 * @return Returns true if the event was handled.
	 */
	private boolean onUp(MotionEvent ev)
	{
		if (mTrackManager.getMoveManager().isMoving())
		{
			mTrackManager.getMoveManager().finish();
			invalidate();
		}
		return true;
	}
}