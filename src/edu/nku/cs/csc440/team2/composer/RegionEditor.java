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

public class RegionEditor extends Activity {
	public class RegionEditorView extends View implements OnGestureListener {
		public static final int CORNER_SIZE = 30;

		private GestureDetector mGestureDetector;
		private LinkedList<Box> mBoxesWithRegions;
		private LinkedList<Box> mConcurrentBoxes;

		private Rect mBounds;
		private Rect mTopLeft;
		private Rect mTopRight;
		private Rect mBottomLeft;
		private Rect mBottomRight;

		private Rect mTarget;

		public RegionEditorView(Context context) {
			super(context);
			
			mTarget = null;
			mBounds = mRegion.getBounds();

			if (mBounds.width() <= CORNER_SIZE
					|| mBounds.height() <= CORNER_SIZE) {
				mBounds.set(0, 0, 4 * CORNER_SIZE, 4 * CORNER_SIZE);
			}

			mTopLeft = new Rect(0, 0, CORNER_SIZE, CORNER_SIZE);
			mTopRight = new Rect(0, 0, CORNER_SIZE, CORNER_SIZE);
			mBottomLeft = new Rect(0, 0, CORNER_SIZE, CORNER_SIZE);
			mBottomRight = new Rect(0, 0, CORNER_SIZE, CORNER_SIZE);
			updateBounds();

			mGestureDetector = new GestureDetector(this);

			mBoxesWithRegions = new LinkedList<Box>();
			for (Box b : mTrackManager.getBoxes()) {
				if (b.getRegion() != null) {
					mBoxesWithRegions.add(b);
				}
			}

			Collections.sort(mBoxesWithRegions);
			commitZindices();
			mConcurrentBoxes = mTrackManager.getConcurrentElements(mBox);
		}

		public void bringForward() {
			boolean done = false;
			while (!done && mBoxesWithRegions.getLast() != mBox) {
				// if next is in mconcurrentboxes done is true
				int next = mBoxesWithRegions.indexOf(mBox) + 1;
				done = mConcurrentBoxes.contains(mBoxesWithRegions.get(next));

				// move up by one within mBoxesWithRegions
				mBoxesWithRegions.remove(mBox);
				mBoxesWithRegions.add(next, mBox);
			}
			commitZindices();
			invalidate();
		}

		public void commitZindices() {
			for (int i = 0; i < mBoxesWithRegions.size(); i++) {
				mBoxesWithRegions.get(i).getRegion().setZindex(i);
			}
		}

		@Override
		public boolean onDown(MotionEvent arg0) {
			int targetX = (int) arg0.getX();
			int targetY = (int) arg0.getY();

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
			
			/* Draw all overlapping regions */
			for (int i = 0; i < mBoxesWithRegions.size(); i++) {
				Box b = mBoxesWithRegions.get(i);
				if (mConcurrentBoxes.contains(b) && b.getRegion() != null) {
					if (b == mBox) {
						/* Draw region background */
						p.setColor(getResources().getColor(R.color.region_bg));
						canvas.drawRect(mRegion.getBounds(), p);
						
						/* Draw each region corner background */
						p.setColor(getResources().getColor(R.color.region_corner_bg));
						canvas.drawRect(mTopLeft, p);
						canvas.drawRect(mTopRight, p);
						canvas.drawRect(mBottomRight, p);
						canvas.drawRect(mBottomLeft, p);

					} else {
						/* Draw overlapping region background */
						ParcelableRegion r = b.getRegion();
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
			return false;
		}

		@Override
		public void onLongPress(MotionEvent arg0) {
		}

		@Override
		public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2,
				float arg3) {
			if (mTarget != null) {
				if (mTarget == mBounds) {
					mBounds.offset((int) -arg2, (int) -arg3);
				} else if (mTarget == mTopLeft) {
					mBounds.left -= arg2;
					mBounds.top -= arg3;
				} else if (mTarget == mTopRight) {
					mBounds.right -= arg2;
					mBounds.top -= arg3;
				} else if (mTarget == mBottomRight) {
					mBounds.right -= arg2;
					mBounds.bottom -= arg3;
				} else if (mTarget == mBottomLeft) {
					mBounds.left -= arg2;
					mBounds.bottom -= arg3;
				}
				updateBounds();
			}
			return true;
		}

		@Override
		public void onShowPress(MotionEvent arg0) {
		}

		@Override
		public boolean onSingleTapUp(MotionEvent arg0) {
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

		public boolean onUp(MotionEvent ev) {
			mTarget = null;
			return true;
		}

		public void pushBack() {
			boolean done = false;
			while (!done && mBoxesWithRegions.getFirst() != mBox) {
				// if prev is in mconcboxes then done is true
				int prev = mBoxesWithRegions.indexOf(mBox) - 1;
				done = mConcurrentBoxes.contains(mBoxesWithRegions.get(prev));

				// move down by one within mBoxesWithRegions
				mBoxesWithRegions.remove(mBox);
				mBoxesWithRegions.add(prev, mBox);
			}
			commitZindices();
			invalidate();
		}

		public void updateBounds() {
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

			// place corners
			mTopLeft.offsetTo(mBounds.left, mBounds.top);
			mTopRight.offsetTo(mBounds.right - CORNER_SIZE, mBounds.top);
			mBottomRight.offsetTo(mBounds.right - CORNER_SIZE, mBounds.bottom
					- CORNER_SIZE);
			mBottomLeft.offsetTo(mBounds.left, mBounds.bottom - CORNER_SIZE);

			// fix overlaps
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

	private TrackManager mTrackManager;
	private Box mBox;
	private RegionEditorView mView;

	private ParcelableRegion mRegion;

	@Override
	public void onBackPressed() {
		Intent i = new Intent();
		i.putExtra("track_manager", mTrackManager);
		setResult(RESULT_OK, i);
		finish();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (savedInstanceState != null) {
			mTrackManager = savedInstanceState.getParcelable("track_manager");
			mBox = mTrackManager.getBox(savedInstanceState.getString("box_id"));
		} else {
			mTrackManager = getIntent().getParcelableExtra("track_manager");
			mBox = mTrackManager.getBox(getIntent().getStringExtra("box_id"));
		}

		if (mBox.getRegion() == null) {
			mBox.setRegion(new ParcelableRegion());
		}

		mRegion = mBox.getRegion();

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

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putParcelable("track_manager", mTrackManager);
		outState.putString("box_id", mBox.getId());
	}
	
}
