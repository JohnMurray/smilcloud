package edu.nku.cs.csc440.team2.composer;

import java.util.Collections;
import java.util.LinkedList;

import edu.nku.cs.csc460.team2.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

/**
 * A RegionEditor allows a user to graphically modify a Region using
 * touch-based input. It supports setting z-indices of Regions and guarantees
 * that every z-index in a Message is unique.
 * 
 * @author William Knauer <knauerw1@nku.edu>
 * @version 2011.0420
 */
public class RegionEditor extends Activity {
	/**
	 * A RegionEditorView is the view set by a RegionEditor to facilitate the
	 * graphical editing of a Region.
	 */
	public class RegionEditorView extends View implements OnGestureListener {
		/** The height and width of the handles on each corner of the Region */
		private final int cornerSize;
		
		/** Interprets touch-based input to trigger callbacks */
		private GestureDetector mGestureDetector;
		
		/** List of Boxes that have non-null Regions */
		private LinkedList<Box> mBoxesWithRegions;
		
		/** 
		 * List of Boxes whose playback overlaps with the Box whose Region is
		 * being edited.
		 */
		private LinkedList<Box> mConcurrentBoxes;
		
		/** The bounds of this Region */
		private Rect mBounds;
		
		/** The bounds of the top-left corner grip within mBounds */
		private Rect mTopLeft;
		
		/** The bounds of the top-right corner grip within mBounds */
		private Rect mTopRight;
		
		/** The bounds of the bottom-left corner grip within mBounds */
		private Rect mBottomLeft;
		
		/** The bounds of the bottom-right corner grip within mBounds */
		private Rect mBottomRight;

		/** Stores whatever Rect is being touched at a given moment */
		private Rect mTarget;

		/**
		 * Class constructor.
		 * 
		 * @param context The context used to construct this view.
		 */
		public RegionEditorView(Context context) {
			super(context);
			cornerSize = context.getResources().getInteger(
					R.integer.region_corner_size);
			mTarget = null;
			mGestureDetector = new GestureDetector(this);
			mBoxesWithRegions = new LinkedList<Box>();
			mBounds = mBox.getRegion().getBounds();
			if (mBounds.width() <= cornerSize
					|| mBounds.height() <= cornerSize) {
				mBounds.set(0, 0, 4 * cornerSize, 4 * cornerSize);
			}
			mTopLeft = new Rect(0, 0, cornerSize, cornerSize);
			mTopRight = new Rect(0, 0, cornerSize, cornerSize);
			mBottomLeft = new Rect(0, 0, cornerSize, cornerSize);
			mBottomRight = new Rect(0, 0, cornerSize, cornerSize);
			
			/* Assign bounds to the region's corner rectangles */
			updateBounds();
			
			/* Populate mBoxesWithRegions */
			for (Box b : mTrackManager.getAllBoxes()) {
				if (b.getRegion() != null) {
					mBoxesWithRegions.add(b);
				}
			}
			
			/* Sort mBoxesWithRegions by z-index ascending */
			Collections.sort(mBoxesWithRegions);
			
			/* Set the z-indices of mBoxesWithRegions to their List indices */
			commitZindices();
			
			/* Populate mConcurrentBoxes */
			mConcurrentBoxes = mTrackManager.getConcurrentBoxes(mBox);
		}

		/**
		 * Raises the z-index of mBox just enough to bring it in front of the
		 * next shallowest region visible on the screen.
		 */
		public void bringForward() {
			boolean done = false;
			while (!done && mBoxesWithRegions.getLast() != mBox) {
				/* Get the index of the next element in the list */
				int next = mBoxesWithRegions.indexOf(mBox) + 1;
				
				/* Stop if the next element's playback overlaps with mBox */
				done = mConcurrentBoxes.contains(mBoxesWithRegions.get(next));

				/* Swap mBox with element at next */
				mBoxesWithRegions.remove(mBox);
				mBoxesWithRegions.add(next, mBox);
			}
			commitZindices();
			invalidate();
		}

		/**
		 * Sets the z-index of each Box in mBoxesWithRegions to its index
		 * within mBoxesWithRegions.
		 */
		public void commitZindices() {
			for (int i = 0; i < mBoxesWithRegions.size(); i++) {
				mBoxesWithRegions.get(i).getRegion().setZindex(i);
			}
		}

		@Override
		public boolean onDown(MotionEvent ev) {
			/* Assign mTarget based on input */
			int targetX = (int) ev.getX();
			int targetY = (int) ev.getY();
			if (mBounds.contains(targetX, targetY)) {
				if (mTopLeft.contains(targetX, targetY)) {
					mTarget = mTopLeft;
				} else if (mTopRight.contains(targetX, targetY)) {
					mTarget = mTopRight;
				} else if (mBottomRight.contains(targetX, targetY)) {
					mTarget = mBottomRight;
				} else if (mBottomLeft.contains(targetX, targetY)) {
					mTarget = mBottomLeft;
				} else {
					mTarget = mBounds;
				}
			}
			invalidate();
			return true;
		}

		@Override
		public void onDraw(Canvas canvas) {
			Paint p = new Paint();
			
			/* Draw background */
			p.setAntiAlias(true);
			p.setColor(getResources().getColor(R.color.region_editor_bg));
			canvas.drawPaint(p);
			
			/* Draw target screen size */
			// TODO set target screen size // p.setColor(getResources().getColor(R.color.));
			
			/* Draw all overlapping regions */
			for (int i = 0; i < mBoxesWithRegions.size(); i++) {
				Box b = mBoxesWithRegions.get(i);
				if (mConcurrentBoxes.contains(b) && b.getRegion() != null) {
					if (b == mBox) {
						/* Draw region background */
						p.setColor(getResources().getColor(R.color.region_bg));
						canvas.drawRect(mBounds, p);
						
						/* Draw each region corner background */
						p.setColor(getResources().getColor(R.color.region_corner_bg));
						canvas.drawRect(mTopLeft, p);
						canvas.drawRect(mTopRight, p);
						canvas.drawRect(mBottomRight, p);
						canvas.drawRect(mBottomLeft, p);

					} else {
						/* Draw overlapping region background */
						ComposerRegion r = b.getRegion();
						p.setColor(getResources().getColor(R.color.region_bgbox_bg));
						canvas.drawRect(r.getBounds(), p);
						
						/* Draw overlapping region foreground */
						p.setColor(getResources().getColor(R.color.region_bgbox_fg));
						p.setStyle(Style.STROKE);
						canvas.drawRect(r.getBounds(), p);
						p.setStyle(Style.FILL);
					}
				}
			}
			
			/* Draw region outline */
			p.setColor(getResources().getColor(R.color.region_fg));
			p.setStyle(Style.STROKE);
			canvas.drawRect(mBounds, p);

			/* Draw each corner outline */
			p.setColor(getResources().getColor(R.color.region_corner_fg));
			canvas.drawRect(mTopLeft, p);
			canvas.drawRect(mTopRight, p);
			canvas.drawRect(mBottomRight, p);
			canvas.drawRect(mBottomLeft, p);
		}

		@Override
		public boolean onFling(MotionEvent arg0, MotionEvent arg1, float arg2,
				float arg3) {
			return true;
		}

		@Override
		public void onLongPress(MotionEvent arg0) {}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distX,
				float distY) {
			/*
			 *  Resize the Region based on the distance the touch pointer
			 *  moves.
			 */
			if (mTarget != null) {
				if (mTarget == mBounds) {
					mBounds.offset((int) -distX, (int) -distY);
				} else if (mTarget == mTopLeft) {
					mBounds.left -= distX;
					mBounds.top -= distY;
				} else if (mTarget == mTopRight) {
					mBounds.right -= distX;
					mBounds.top -= distY;
				} else if (mTarget == mBottomRight) {
					mBounds.right -= distX;
					mBounds.bottom -= distY;
				} else if (mTarget == mBottomLeft) {
					mBounds.left -= distX;
					mBounds.bottom -= distY;
				}
				updateBounds();
			}
			return true;
		}

		@Override
		public void onShowPress(MotionEvent arg0) {}

		@Override
		public boolean onSingleTapUp(MotionEvent arg0) {
			return false;
		}

		@Override
		public boolean onTouchEvent(MotionEvent ev) {
			/* Pass touch event to the GestureDetector */
			mGestureDetector.onTouchEvent(ev);
			
			if (ev.getAction() == MotionEvent.ACTION_UP) {
				onUp(ev);
			}
			return true;
		}

		/**
		 * Actions to be performed whenever the user lifts their finger from
		 * the touch screen.
		 * 
		 * @param ev The MotionEvent that triggered the function call.
		 */
		public void onUp(MotionEvent ev) {
			mTarget = null;
		}

		/**
		 * Lowers the z-index of mBox just enough to push it behind the next
		 * deepest region visible on the screen.
		 */
		public void pushBack() {
			boolean done = false;
			while (!done && mBoxesWithRegions.getFirst() != mBox) {
				/* Get the index of the previous element in the list */
				int prev = mBoxesWithRegions.indexOf(mBox) - 1;
				
				/* Stop if the next element's playback overlaps with mBox */
				done = mConcurrentBoxes.contains(mBoxesWithRegions.get(prev));

				/* Swap mBox with element at prev */
				mBoxesWithRegions.remove(mBox);
				mBoxesWithRegions.add(prev, mBox);
			}
			commitZindices();
			invalidate();
		}

		/**
		 * Updates the bounds of the corner grips so they are actually at
		 * the corners of the Region's bounds.
		 */
		public void updateBounds() {
			/* Correct if the user somehow inverted mBounds */
			if (mBounds.left > mBounds.right) {
				int temp = mBounds.left;
				mBounds.left = mBounds.right;
				mBounds.right = temp;
			}
			if (mBounds.top > mBounds.bottom) {
				int temp = mBounds.top;
				mBounds.top = mBounds.bottom;
				mBounds.bottom = temp;
			}

			/* Place the corner grips at the corners of mBounds */
			mTopLeft.offsetTo(mBounds.left, mBounds.top);
			mTopRight.offsetTo(mBounds.right - cornerSize, mBounds.top);
			mBottomRight.offsetTo(mBounds.right - cornerSize, mBounds.bottom
					- cornerSize);
			mBottomLeft.offsetTo(mBounds.left, mBounds.bottom - cornerSize);

			/* Fix any overlaps between the corner grips */
			if (Rect.intersects(mTopLeft, mTopRight)) {
				mTopRight.offsetTo(mTopLeft.right, mTopRight.top);
				mBottomRight.offsetTo(mTopRight.left, mBottomRight.top);
				mBounds.right = mTopRight.right;
			}
			if (Rect.intersects(mTopRight, mBottomRight)) {
				mBottomRight.offsetTo(mBottomRight.left, mTopRight.bottom);
				mBottomLeft.offsetTo(mBottomLeft.left, mBottomRight.top);
				mBounds.bottom = mBottomRight.bottom;
			}
			if (Rect.intersects(mBottomRight, mBottomLeft)) {
				mBottomLeft.offsetTo(mBottomRight.left - mBottomLeft.width(),
						mBottomLeft.top);
				mTopLeft.offsetTo(mBottomLeft.left, mTopLeft.top);
				mBounds.left = mBottomLeft.left;
			}
			if (Rect.intersects(mBottomLeft, mTopLeft)) {
				mTopLeft.offsetTo(mTopLeft.left,
						mBottomLeft.top - mTopLeft.height());
				mTopRight.offsetTo(mTopRight.left, mTopLeft.top);
				mBounds.top = mTopLeft.top;
			}
			invalidate();
		}
		
	}

	/** The TrackManager that contains all the Boxes we are working with */
	private TrackManager mTrackManager;
	
	/** The Box within the mTrackManager whose Region we are editing */
	private Box mBox;
	
	/** The view that facilitates the graphical editing of the Region */
	private RegionEditorView mView;
	
	@Override
	public void onBackPressed() {
		Intent i = new Intent();
		i.putExtra("track_manager", mTrackManager);
		i.putExtra("box_id", mBox.getId());
		setResult(RESULT_OK, i);
		finish();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		/* Load from Intent */
		mTrackManager = getIntent().getParcelableExtra("track_manager");
		String boxId = getIntent().getStringExtra("box_id");
		mBox = mTrackManager.getBox(boxId);
		
		/* Create a region for mBox if we must */
		if (mBox.getRegion() == null) {
			mBox.setRegion(new ComposerRegion());
		}
		
		mView = new RegionEditorView(this);
		setContentView(mView);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.region_editor_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		boolean result = false;

		switch (item.getItemId()) {
		case R.id.bring_forward:
			mView.bringForward();
			result = true;
			break;
		case R.id.push_back:
			mView.pushBack();
			result = true;
			break;
		default:
			result = super.onOptionsItemSelected(item);
			break;
		}

		return result;
	}
	
}
