package edu.nku.cs.csc440.team2.composer;

import edu.nku.cs.csc440.team2.SMILCloud;
import edu.nku.cs.csc440.team2.UIMenus.ListAllUsers;
import edu.nku.cs.csc440.team2.message.Message;
import edu.nku.cs.csc440.team2.player.SMILPlayer;
import edu.nku.cs.csc440.team2.provider.MessageProvider;
import edu.nku.cs.csc460.team2.R;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnGestureListener;
import android.view.ViewGroup.LayoutParams;

/**
 * The Composer is the main Activity for composing messages. The Composer is
 * responsible for creating, editing, saving, sending, and previewing messages.
 * It uses the TrackManager data structure to manage the message being composed.
 * It also has a ruler that spans the top of the screen that measures the
 * passage of time along the x-axis.
 * 
 * @author William Knauer <knauerw1@nku.edu>
 * @version 2011.0424
 */
public class Composer extends Activity {
	/**
	 * A ComposerView is the view used by the Composer Activity.
	 */
	public class ComposerView extends View implements OnGestureListener {
		/** The GestureDetector used to interpret touch gestures */
		private GestureDetector mGestureDetector;

		/** The drawing bounds of the composer */
		private Rect mBounds;

		/** The Timeline used to measure time in the Composer */
		private Timeline mTimeline;

		/** The TrackManager managed by this composer */
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

		/**
		 * Initializes data structures for creating a new ComposerView.
		 */
		void create() {
			mBounds = new Rect();

			/* Get info from Application */
			String messageId = ((SMILCloud) getApplication())
					.getQueuedDocumentForEditing();
			int userId = ((SMILCloud) getApplication()).getUserId();

			/* Create the trackManager */
			setTrackManager(TrackManager.Factory.create(messageId, userId));
			mTimeline = new Timeline();
		}

		/**
		 * @return The TrackManager managed by this ComposerView.
		 */
		TrackManager getTrackManager() {
			return mTrackManager;
		}

		/**
		 * Loads data structures for this ComposerView from a bundle.
		 * 
		 * @param bundle
		 *            The Bundle to load from.
		 */
		void load(Bundle bundle) {
			mBounds = bundle.getParcelable("bounds");
			mTrackManager = bundle.getParcelable("track_manager");
			mTrackManager.maintain();
			mTimeline = new Timeline();
			invalidate();
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
			canvas.drawColor(Color.argb(255, 0, 0, 0));

			/* Reset our bounds */
			mBounds.set(0, 0, mTrackManager.getMaxX(), mTrackManager.getMaxY());
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
				if (target instanceof AudioBox) {
					launchAudioProperties(target.getId());
				} else if (target instanceof ImageBox) {
					launchImageProperties(target.getId());
				} else if (target instanceof TextBox) {
					launchTextProperties(target.getId());
				} else if (target instanceof VideoBox) {
					launchVideoProperties(target.getId());
				}
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

		/**
		 * Saves the data structures in this ComposerView to a Bundle.
		 * 
		 * @param bundle
		 *            The Bundle to save to.
		 */
		void save(Bundle bundle) {
			bundle.putParcelable("bounds", mBounds);
			bundle.putParcelable("track_manager", mTrackManager);
		}

		/**
		 * @param tm
		 *            The TrackManager to be managed by this ComposerView.
		 */
		void setTrackManager(TrackManager tm) {
			mTrackManager = tm;
			mTrackManager.maintain();
			invalidate();
		}

	}

	/** The horizontal scaling factor used in drawing */
	private static double sScale = 1.0;

	public static final int REQ_PROPERTIES = 23;
	public static final int REQ_NAME_MESSAGE = 24;
	public static final int REQ_SAVE_AND_EXIT = 25;

	public static final int MODE_SAVE = 1;
	public static final int MODE_SEND = 2;
	public static final int MODE_PREVIEW = 3;

	/**
	 * @return The current scaling factor.
	 */
	public static double getScale() {
		return sScale;
	}

	/**
	 * Maps pixels along the x-axis to seconds, subject to the scaling factor.
	 * 
	 * @param px
	 *            The position in pixels to map.
	 * @return The pixels mapped to seconds.
	 */
	public static double pxToSec(int px) {
		return px / (sScale * 100.0);
	}

	/**
	 * Maps seconds to pixels along the x-axis, subject to the scaling factor.
	 * 
	 * @param seconds
	 *            The number of seconds to map.
	 * @return The seconds mapped to pixels.
	 */
	public static int secToPx(double seconds) {
		return (int) (seconds * sScale * 100.0);
	}

	/**
	 * @param scale
	 *            The scaling factor for mapping time to the x-axis.
	 */
	public static void setScale(double scale) {
		sScale = scale;
	}

	/**
	 * Rounds a double to the nearest tenth.
	 * 
	 * @param d
	 *            The double to be rounded.
	 * @return The double mapped to the nearest tenth.
	 */
	public static double snapTo(double d) {
		long result = Math.round(d * 10.0);
		return result / 10.0;
	}

	/** The ComposerView used by this Composer */
	private ComposerView mComposerView;

	/** Used to determine what to execute upon finishing naming a message */
	private int mSaveMode;

	/** The name of the message being created/edited */
	private String mMessageName;

	/**
	 * Launches an AudioProperties Activity to edit an AudioBox.
	 * 
	 * @param id
	 *            The id of the Box to edit. Creates a new Box if null.
	 */
	public void launchAudioProperties(String id) {
		Intent i = new Intent(this, AudioProperties.class);
		i.putExtra("track_manager", mComposerView.getTrackManager());
		i.putExtra("box_id", id);
		startActivityForResult(i, REQ_PROPERTIES);
	}

	/**
	 * Launches an ImageProperties Activity to edit an ImageBox.
	 * 
	 * @param id
	 *            The id of the Box to edit. Creates a new Box if null.
	 */
	public void launchImageProperties(String id) {
		Intent i = new Intent(this, ImageProperties.class);
		i.putExtra("track_manager", mComposerView.getTrackManager());
		i.putExtra("box_id", id);
		startActivityForResult(i, REQ_PROPERTIES);
	}

	/**
	 * Launches a MessageNamePrompt activity initialized to a given name.
	 * 
	 * @param name
	 *            The name to initialize to.
	 */
	private void launchMessageNamePrompt(String name) {
		Intent i = new Intent(this, MessageNamePrompt.class);
		if (name != null) {
			i.putExtra("name", name);
		}
		startActivityForResult(i, REQ_NAME_MESSAGE);
	}

	/**
	 * Launches a TextProperties Activity to edit a TextBox.
	 * 
	 * @param id
	 *            The id of the Box to edit. Creates a new Box if null.
	 */
	public void launchTextProperties(String id) {
		Intent i = new Intent(this, TextProperties.class);
		i.putExtra("track_manager", mComposerView.getTrackManager());
		i.putExtra("box_id", id);
		startActivityForResult(i, REQ_PROPERTIES);
	}

	/**
	 * Launches a VideoProperties Activity to edit an VideoBox.
	 * 
	 * @param id
	 *            The id of the Box to edit. Creates a new Box if null.
	 */
	public void launchVideoProperties(String id) {
		Intent i = new Intent(this, VideoProperties.class);
		i.putExtra("track_manager", mComposerView.getTrackManager());
		i.putExtra("box_id", id);
		startActivityForResult(i, REQ_PROPERTIES);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQ_PROPERTIES) {
			if (resultCode == RESULT_OK) {
				TrackManager t = (TrackManager) data
						.getParcelableExtra("track_manager");
				mComposerView.setTrackManager(t);
			} else if (resultCode == RESULT_CANCELED) {
				// do nothing
			}
		} else if (requestCode == REQ_NAME_MESSAGE) {
			if (resultCode == RESULT_OK) {
				mMessageName = data.getStringExtra("name");
				if (mSaveMode == MODE_SAVE) {
					saveMessage(mMessageName);
				} else if (mSaveMode == MODE_SEND) {
					sendMessage(mMessageName);
				} else if (mSaveMode == MODE_PREVIEW) {
					previewMessage(mMessageName);
				}
			} else {
				// do nothing
			}
		} else if (requestCode == REQ_SAVE_AND_EXIT) {
			if (resultCode == RESULT_OK) {
				mMessageName = data.getStringExtra("name");
				saveMessage(mMessageName);
				finish();
			} else if (resultCode == RESULT_CANCELED) {
				finish();
			}
		}
	}

	@Override
	public void onBackPressed() {
		Intent i = new Intent(this, MessageNamePrompt.class);
		i.putExtra("name", mMessageName);
		startActivityForResult(i, REQ_SAVE_AND_EXIT);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mSaveMode = -1;
		mMessageName = "Unfinished Message";
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
			launchAudioProperties(null);
			return true;
		case R.id.add_image:
			launchImageProperties(null);
			return true;
		case R.id.add_text:
			launchTextProperties(null);
			return true;
		case R.id.add_video:
			launchVideoProperties(null);
			return true;
		case R.id.preview:
			mSaveMode = MODE_PREVIEW;
			previewMessage(mMessageName);
			return true;
		case R.id.save:
			mSaveMode = MODE_SAVE;
			launchMessageNamePrompt(mMessageName);
			return true;
		case R.id.send:
			mSaveMode = MODE_SEND;
			launchMessageNamePrompt(mMessageName);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		mComposerView.save(outState);
	}

	/**
	 * Saves the message with the given title and launches the Player to preview
	 * it.
	 * 
	 * @param title
	 *            The name to use when saving the message.
	 */
	private void previewMessage(String title) {
		/* Save the message */
		SMILCloud app = (SMILCloud) getApplication();
		String messageId = saveMessage(title);

		/* Add message to play queue */
		app.queueDocumentToPlay(messageId);
		app.setPreviewDocumentId(messageId);

		/* Start the player */
		Intent i = new Intent(this, SMILPlayer.class);
		startActivity(i);
	}

	/**
	 * Saves the message with the given title.
	 * 
	 * @param title
	 *            The name to use when saving the message.
	 * @return The unique id of the saved message.
	 */
	private String saveMessage(String title) {
		/* Set message provider */
		MessageProvider mp = new MessageProvider();

		/*
		 * Check to see if an old message exists, and if so delete it before
		 * previewing again.
		 */
		SMILCloud app = (SMILCloud) getApplication();
		if (app.getPreviewDocumentId() != null) {
			mp.deleteMessage(app.getPreviewDocumentId());
			app.setPreviewDocumentId(null);
		}

		Message m = mComposerView.getTrackManager().toMessage();
		int userId = mComposerView.getTrackManager().getUserId();

		String messageId = mp.saveMessage(userId, title, m);
		return messageId;
	}

	/**
	 * Saves the message with the given title and launches an activity to send
	 * it to another user.
	 * 
	 * @param title
	 *            The name to use when saving the message.
	 */
	private void sendMessage(String title) {
		String id = saveMessage(title);
		((SMILCloud) this.getApplication()).setSharedMessageId(id);
		Intent i = new Intent(this, ListAllUsers.class);
		startActivity(i);
	}

}
