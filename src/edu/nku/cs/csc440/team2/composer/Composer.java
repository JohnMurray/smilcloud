package edu.nku.cs.csc440.team2.composer;

import edu.nku.cs.csc460.team2.R;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnGestureListener;
import android.view.ViewGroup.LayoutParams;

/**
 * @author William Knauer <knauerw1@nku.edu>
 * @version 2011.0414
 */
public class Composer extends Activity {
	public class ComposerView extends View implements OnGestureListener {
		private GestureDetector mGestureDetector;
		private Rect mBounds;
		private Timeline mTimeline;
		private TrackManager mTrackManager;

		/**
		 * Class constructor.
		 * 
		 * @param context
		 *            The Context needed to construct Views.
		 */
		public ComposerView(Composer parent) {
			super(parent);
			mGestureDetector = new GestureDetector(this);
		}

		/**
		 * Scroll this View by (x, y) = (dx, dy) pixels. Does not allow
		 * scrolling past the bounds.
		 * 
		 * @param dx
		 *            The horizontal scroll distance.
		 * @param dy
		 *            The vertical scroll distance.
		 */
		public void boundedScrollBy(float dx, float dy) {
			if (getScrollX() + dx > mBounds.right - getWidth()) {
				dx = mBounds.right - getWidth() - getScrollX();
			} else if (getScrollX() + dx < mBounds.left) {
				dx = mBounds.left - getScrollX();
			}

			if (getScrollY() + dy > mBounds.bottom - getHeight()) {
				dy = mBounds.bottom - getHeight() - getScrollY();
			} else if (getScrollY() + dy < mBounds.top) {
				dy = mBounds.top - getScrollY();
			}

			super.scrollBy((int) dx, (int) dy);
		}

		void create() {
			mBounds = new Rect(0, 0, 0, 1000);
			mTrackManager = new TrackManager(
					getResources().getColor(R.color.track_bg),
					getResources().getColor(R.color.track_fg));
			mTimeline = new Timeline(
					getResources().getColor(R.color.timeline_bg),
					getResources().getColor(R.color.timeline_fg));
		}

		TrackManager getTrackManager() {
			return mTrackManager;
		}

		void load(Bundle bundle) {
			mBounds = bundle.getParcelable("mBounds");
			mTrackManager = bundle.getParcelable("mTrackManager");
			mTimeline = new Timeline(
					getResources().getColor(R.color.timeline_bg),
					getResources().getColor(R.color.timeline_fg));
		}

		@Override
		public boolean onDown(MotionEvent e) {
			int x = getScrollX() + (int) e.getX();
			int y = getScrollY() + (int) e.getY();

			/* Start resize if touching a resize handle */
			if (!mTrackManager.getResizeManager().start(x, y)) {
				/* Otherwise try to start move */
				mTrackManager.getMoveManager().start(x, y);
			}
			return true;
		}

		@Override
		protected void onDraw(Canvas canvas) {
			/* Repaint our background */
			canvas.drawColor(getResources().getColor(R.color.composer_bg));
			
			/* Reset our bounds */
			mBounds.set(
					0,
					0,
					secToPx(mTrackManager.getMaxEndTime()),
					mTrackManager.getMaxY());
			mBounds.right += 2 * getWidth();
			mBounds.bottom += getHeight();
			
			/* Draw tracks */
			mTrackManager.setBounds(mBounds.left, mBounds.top + Timeline.HEIGHT
					+ 5, mBounds.right, mBounds.top + Timeline.HEIGHT + 5
					+ mTrackManager.measureHeight());
			mTrackManager.draw(canvas);

			/* Draw timeline */
			mTimeline.setBounds(mBounds.left, getScrollY(), mBounds.right,
					getScrollY() + Timeline.HEIGHT);
			mTimeline.draw(canvas);
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			return false;
		}

		@Override
		public void onLongPress(MotionEvent e) {
			int x = (int) e.getX() + getScrollX();
			int y = (int) e.getY() + getScrollY();
			
			if (mTrackManager.getMoveManager().isMoving()) {
				getTrackManager().getMoveManager().finish();
			}
			
			Box target = mTrackManager.getBox(x, y);
			if (target != null) {
				launchPropertiesActivity(target);
				invalidate();
			}

		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			if (mTrackManager.getMoveManager().isMoving()) {
				mTrackManager.getMoveManager().offset((int) -distanceX,
						(int) -distanceY);
				invalidate();
			} else if (mTrackManager.getResizeManager().isResizing()) {
				mTrackManager.getResizeManager().resize(
						getScrollX() + (int) e2.getX());
				invalidate();
			} else {
				boundedScrollBy(distanceX, distanceY);
			}
			return true;
		}

		@Override
		public void onShowPress(MotionEvent e) {
		}
		
		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			return false;
		}

		@Override
		public boolean onTouchEvent(MotionEvent ev) {
			mGestureDetector.onTouchEvent(ev);
			if (ev.getAction() == MotionEvent.ACTION_UP) {
				onUp(ev);
			}
			return true;
		}

		/**
		 * Called when a finger is lifted from the touch screen.
		 * 
		 * @param ev
		 *            The MotionEvent that triggered this call.
		 * @return Returns true if the event was handled.
		 */
		public boolean onUp(MotionEvent ev) {
			if (mTrackManager.getMoveManager().isMoving()) {
				mTrackManager.getMoveManager().finish();
				invalidate();
			} else if (mTrackManager.getResizeManager().isResizing()) {
				mTrackManager.getResizeManager().finish();
				invalidate();
			}
			return true;
		}

		void save(Bundle bundle) {
			bundle.putParcelable("mBounds", mBounds);
			bundle.putParcelable("mTrackManager", mTrackManager);
		}

		void setTrackManager(TrackManager tm) {
			mTrackManager = tm;
			invalidate();
		}
		
	}

	private static float sScale = 1.5f;

	public static final int REQ_AUDIO = 23;

	public static final int REQ_IMAGE = 24;

	public static final int REQ_TEXT = 25;

	public static final int REQ_VIDEO = 26;

	public static final int RESULT_SAVE = 1;
	public static final int RESULT_DELETED = 2;
	public static final int RESULT_UNCHANGED = 3;

	/**
	 * @return Returns the current scaling factor.
	 */
	public static float getScale() {
		return sScale;
	}

	/**
	 * Maps pixels along the x-axis to seconds, subject to the scaling factor.
	 * 
	 * @param px
	 *            The position in pixels to map.
	 * @return Returns the pixels mapped to seconds.
	 */
	public static double pxToSec(int px) {
		return px / (sScale * 100.0);
	}

	/**
	 * Maps seconds to pixels along the x-axis, subject to the scaling factor.
	 * 
	 * @param seconds
	 *            The number of seconds to map.
	 * @return Returns the seconds mapped to pixels.
	 */
	public static int secToPx(double seconds) {
		return (int) (seconds * sScale * 100.0);
	}

	/**
	 * @param scale
	 *            The scaling factor for mapping time to the x-axis.
	 */
	public static void setScale(float scale) {
		sScale = scale;
	}

	/**
	 * Rounds a double to the nearest tenth.
	 * 
	 * @param d
	 *            The double to be rounded.
	 * @return Returns the double mapped to the nearest tenth.
	 */
	public static double snapTo(double d) {
		long result = Math.round(d * 10.0);
		return result / 10.0;
	}

	private ComposerView mComposerView;

	public boolean addBox(int type) {
		Intent i = null;
		int code = -1;
		switch (type) {
		case AudioBox.TYPE:
			i = new Intent(this, AudioProperties.class);
			code = REQ_AUDIO;
			break;
		case ImageBox.TYPE:
			i = new Intent(this, ImageProperties.class);
			code = REQ_IMAGE;
			break;
		case TextBox.TYPE:
			i = new Intent(this, TextProperties.class);
			code = REQ_TEXT;
			break;
		case VideoBox.TYPE:
			i = new Intent(this, VideoProperties.class);
			code = REQ_VIDEO;
			break;
		}
		if (i != null) {
			i.putExtra("track_manager", mComposerView.getTrackManager());
			startActivityForResult(i, code);
		}
		return true;
	}
	
	public void launchPlayer() {}

	public void launchPropertiesActivity(Box b) {
		if (b instanceof AudioBox) {
			Intent i = new Intent(this, AudioProperties.class);
			i.putExtra("track_manager", mComposerView.getTrackManager());
			i.putExtra("box_id", b.getId());
			startActivityForResult(i, REQ_AUDIO);
		} else if (b instanceof ImageBox) {
			Intent i = new Intent(this, ImageProperties.class);
			i.putExtra("track_manager", mComposerView.getTrackManager());
			i.putExtra("box_id", b.getId());
			startActivityForResult(i, REQ_IMAGE);
		} else if (b instanceof TextBox) {
			Intent i = new Intent(this, TextProperties.class);
			i.putExtra("track_manager", mComposerView.getTrackManager());
			i.putExtra("box_id", b.getId());
			startActivityForResult(i, REQ_TEXT);
		} else if (b instanceof VideoBox) {
			Intent i = new Intent(this, VideoProperties.class);
			i.putExtra("track_manager", mComposerView.getTrackManager());
			i.putExtra("box_id", b.getId());
			startActivityForResult(i, REQ_VIDEO);
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQ_AUDIO || requestCode == REQ_IMAGE
				|| requestCode == REQ_TEXT || requestCode == REQ_VIDEO) {
			if (resultCode == RESULT_OK || resultCode == RESULT_DELETED) {
				Log.d("intent", "intent is " + data);
				TrackManager t = data.getParcelableExtra("track_manager");
				t.maintain();
				mComposerView.setTrackManager(t);
			}
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mComposerView = new ComposerView(this);
		if (savedInstanceState == null) {
			mComposerView.create();
		} else {
			mComposerView.load(savedInstanceState);
		}
		this.setContentView(mComposerView, new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.composer_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.add_audio:
			return addBox(AudioBox.TYPE);
		case R.id.add_image:
			return addBox(ImageBox.TYPE);
		case R.id.add_text:
			return addBox(TextBox.TYPE);
		case R.id.add_video:
			return addBox(VideoBox.TYPE);
		case R.id.preview:
			launchPlayer();
			return true;
		case R.id.send:
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		mComposerView.save(outState);
	}
	
}
