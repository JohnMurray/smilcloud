package edu.nku.cs.csc440.team2.composer;

import java.util.LinkedList;

import edu.nku.cs.csc440.team2.message.Audio;
import edu.nku.cs.csc440.team2.message.Body;
import edu.nku.cs.csc440.team2.message.Image;
import edu.nku.cs.csc440.team2.message.Media;
import edu.nku.cs.csc440.team2.message.Message;
import edu.nku.cs.csc440.team2.message.Parallel;
import edu.nku.cs.csc440.team2.message.Sequence;
import edu.nku.cs.csc440.team2.message.Text;
import edu.nku.cs.csc440.team2.message.Video;
import edu.nku.cs.csc440.team2.provider.MediaProvider;
import edu.nku.cs.csc460.team2.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;

/**
 * @author William Knauer <knauerw1@nku.edu>
 * @version 2011.0421
 */
public class TrackManager {
	/**
	 * A MoveManager helps to facilitate the moving of a Box from one Track to
	 * another or from one location in a Track to another within the same Track.
	 */
	class MoveManager {
		/** Flag for whether or not mBox fits its target Track. */
		private boolean mFitsTarget;

		/** The Box being moved */
		private Box mBox;

		/** The Track that contained mBox before the move started */
		private Track mOldTrack;

		/** The begin time of mBox before the move started */
		private double mOldBegin;

		/** The Track to move mBox into */
		private Track mTargetTrack;

		/** The begin time where mBox should be added to its new Track */
		private double mTargetBegin;

		/**
		 * Class constructor.
		 */
		public MoveManager() {
			reset();
		}

		/**
		 * Draws mBox to a Canvas wherever the user has dragged it.
		 * 
		 * @param canvas
		 *            The canvas to draw on.
		 */
		public void draw(Canvas canvas) {
			if (mBox != null) {
				mBox.draw(canvas);
			}
		}

		/**
		 * Commits the move if possible. Resets the Box to its old location if
		 * it is not possible.
		 */
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

		/**
		 * @return Returns the drawing bounds of the Box being moved.
		 */
		public Rect getBounds() {
			Rect result = null;
			if (mBox != null) {
				result = mBox.getBounds();
			}
			return result;
		}

		/**
		 * @return Returns true if a move is in progress.
		 */
		public boolean isMoving() {
			return (mBox != null);
		}

		/**
		 * Offsets the drawing bounds of the Box.
		 * 
		 * @param dx
		 *            The horizontal distance to offset.
		 * @param dy
		 *            The vertical distance to offset.
		 */
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

		/**
		 * Resets all local variables to their starting state.
		 */
		private void reset() {
			mFitsTarget = false;
			mBox = null;
			mOldTrack = null;
			mOldBegin = -1.0;
			mTargetTrack = null;
			mTargetBegin = -1.0;
		}

		/**
		 * Initiates a move at a given set of coordinates.
		 * 
		 * @param x
		 *            The x-coordinate.
		 * @param y
		 *            The y-coordinate.
		 */
		public void start(int x, int y) {
			mBox = getBox(x, y);
			if (mBox != null) {
				mOldTrack = getTrack(x, y);
				mOldBegin = mBox.getBegin();
				removeBox(mBox);
			}
		}

	}

	/**
	 * A ResizeManager helps facilitate changing the duration of a Box within a
	 * Track.
	 */
	class ResizeManager {
		/** The Box to resize */
		private Box mBox;

		/** The Track that contains mBox */
		private Track mTrack;

		/** The x-coordinate where the resize was started */
		private int mStartX;

		/** The duration of mBox when the resize was started */
		private double mStartDuration;

		/**
		 * Class constructor.
		 */
		public ResizeManager() {
			reset();
		}

		/**
		 * Finishes the resize by resetting the ResizeManager.
		 */
		public void finish() {
			reset();
		}

		/**
		 * @return Returns true if a resize is in progress.
		 */
		public boolean isResizing() {
			return (mBox != null);
		}

		/**
		 * Resets the local variables to their starting state.
		 */
		private void reset() {
			mBox = null;
			mTrack = null;
			mStartX = -1;
			mStartDuration = -1;
		}

		/**
		 * Performs the resize and commits the changes to mBox.
		 * 
		 * @param x
		 *            The pointer's current x-coordinate.
		 */
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
				mTrack.removeBox(mBox.getBegin());

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

		/**
		 * Initiates a resize at given set of coordinates.
		 * 
		 * @param x
		 *            The x-coordinate.
		 * @param y
		 *            The y-coordinate.
		 * @return Returns true if a resize was initiated.
		 */
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

	/** The list of Tracks contained by this TrackManager */
	private LinkedList<Track> mTracks;

	/** Helper class for moving Boxes between the Tracks */
	private MoveManager mMoveManager;

	/** Helper class for changing the duration of Boxes */
	private ResizeManager mResizeManager;

	/** The drawing bounds */
	private Rect mBounds;

	/** The Context used to get resources and create Tracks */
	private Context mContext;

	/**
	 * Class constructor.
	 */
	public TrackManager() {
		mBounds = new Rect();
		mMoveManager = new MoveManager();
		mResizeManager = new ResizeManager();
		mTracks = new LinkedList<Track>();
		maintain();
	}

	/**
	 * Adds a given Box at a specified time to the first track where it fits.
	 * 
	 * @param elt
	 *            The Box to add.
	 * @param begin
	 *            The time to add the Box.
	 */
	public void addBox(Box elt, double begin) {
		boolean added = false;
		for (Track t : mTracks) {
			if (!added) {
				added = t.addBox(elt, begin);
			}
		}
		maintain();
	}

	/**
	 * Draws all Tracks onto the given Canvas.
	 * 
	 * @param canvas
	 *            The Canvas to draw on.
	 */
	public void draw(Canvas canvas) {
		if (mContext != null) {
			int height = mContext.getResources().getInteger(
					R.integer.track_height);

			/* Draw tracks */
			int y = getBounds().top;
			for (Track t : mTracks) {
				t.setBounds(0, y, mBounds.width(), y + height);
				t.draw(canvas);
				y += height;
			}

			/* Draw moving box (if any) */
			mMoveManager.draw(canvas);
		}
	}

	/**
	 * @return Returns a List containing all the Boxes containted by all the
	 *         Tracks in this TrackManager.
	 */
	public LinkedList<Box> getAllBoxes() {
		LinkedList<Box> boxes = new LinkedList<Box>();
		for (Track t : mTracks) {
			for (Box b : t.getAllBoxes()) {
				boxes.add(b);
			}
		}
		return boxes;
	}

	public Rect getBounds() {
		return mBounds;
	}

	/**
	 * Gets the Box at a specified set of coordinates.
	 * 
	 * @param targetX
	 *            The x-coordinate.
	 * @param targetY
	 *            The y-coordinate.
	 * @return Returns the Box at the set of coordinates. Returns null if no
	 *         such Box is found.
	 */
	public Box getBox(int targetX, int targetY) {
		Box result = null;
		Track t = getTrack(targetX, targetY);
		if (t != null) {
			result = t.getBox(Composer.snapTo(Composer.pxToSec(targetX)));
		}
		return result;
	}

	/**
	 * Finds all the Boxes whose playback times overlap with a given Box.
	 * 
	 * @param box
	 *            The Box to check.
	 * @return Returns a list of all the Boxes whose playback times overlap with
	 *         a given Box.
	 */
	public LinkedList<Box> getConcurrentBoxes(Box box) {
		LinkedList<Box> concurrent = new LinkedList<Box>();
		double begin = box.getBegin();
		double end = box.getEnd();
		for (Track t : mTracks) {
			for (Box m : t.getAllBoxes()) {
				if (m.getBegin() <= begin && begin < m.getEnd()) {
					concurrent.add(m);
				} else if (m.getBegin() < end && end <= m.getEnd()) {
					concurrent.add(m);
				}
			}
		}
		return concurrent;
	}

	/**
	 * @return Returns the furthest x-coordinate where this TrackManager needs
	 *         to draw.
	 */
	public int getMaxX() {
		int result = 0;
		for (Box b : getAllBoxes()) {
			if (b.getEnd() > result) {
				result = Composer.secToPx(b.getEnd());
			}
		}
		if (mMoveManager.isMoving()) {
			if (mMoveManager.getBounds().right > result) {
				result = mMoveManager.getBounds().right;
			}
		}
		return result;
	}

	/**
	 * @return Returns the furthest y-coordinate where this TrackManager needs
	 *         to draw.
	 */
	public int getMaxY() {
		int result = 0;
		for (Track t : mTracks) {
			if (t.getBounds().bottom > result) {
				result = t.getBounds().bottom + 5;
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

	/**
	 * Gets the track at a given set of coordinates.
	 * 
	 * @param targetX
	 *            The x-coordinate.
	 * @param targetY
	 *            The y-coordinate.
	 * @return Returns the Track at the given set of coordinates. Returns null
	 *         if no such Track is found.
	 */
	private Track getTrack(int targetX, int targetY) {
		Track result = null;
		for (Track t : mTracks) {
			if (t.getBounds().contains(targetX, targetY)) {
				result = t;
			}
		}
		return result;
	}

	/**
	 * Ensures that there is precisely one empty Track at the bottom of this
	 * TrackManager.
	 */
	public void maintain() {
		if (mTracks.isEmpty() || !mTracks.getLast().isEmpty()) {
			Track t = new Track();
			t.setContext(mContext);
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

	/**
	 * Determines the estimated drawing height of this TrackManager.
	 * 
	 * @return Returns the estimated height.
	 */
	public int measureHeight() {
		return (mContext.getResources().getInteger(R.integer.track_height) * mTracks
				.size())
				+ (2 * mContext.getResources()
						.getInteger(R.integer.box_spacing));
	}

	/**
	 * Removes a given Box from any Track where it is found.
	 * 
	 * @param b
	 *            The Box to remove.
	 */
	public void removeBox(Box b) {
		for (Track t : mTracks) {
			if (t.contains(b)) {
				t.removeBox(b.getBegin());
			}
		}
	}

	/**
	 * Sets the drawing bounds of this TrackManager.
	 * 
	 * @param l
	 *            The left bound.
	 * @param t
	 *            The top bound.
	 * @param r
	 *            The right bound.
	 * @param b
	 *            The bottom bound.
	 */
	public void setBounds(int l, int t, int r, int b) {
		mBounds.set(l, t, r, b);
	}

	public void setContext(Context context) {
		mContext = context;
		for (Track t : mTracks) {
			t.setContext(mContext);
		}
	}
	
	public Message toMessage() {
		Message m = new Message("someId"); // TODO replace with something from the Application
		
		Parallel p = new Parallel();
		for (Box b : getAllBoxes()) {
			if (b instanceof TextBox) {
				// TODO upload ((TextBox) b).getName(); to cloud
				// TODO modify the MediaProvider to make it happen?
			}
			p.addElement(b.toMedia());
		}
		
		m.addElement(p);
		return m;
	}
	
	public static class Factory {
		
		public static TrackManager create(Message m) {
			TrackManager t = new TrackManager();
			MediaProvider p = new MediaProvider();
			edu.nku.cs.csc440.team2.mediaCloud.Media[] allMedia 
					= p.getAllMedia(1); // TODO replace user id
			
			/* Set each text-type Media's name to its associated text */
			for (int i = 0; i < allMedia.length; i ++) {
				if (allMedia[i].getType().equalsIgnoreCase("text")) {
					allMedia[i].setName(p.getText(allMedia[i].getMediaUrl()));
				}
			}
			
			/* Add everything to the TrackManager */
			for (Body b : m.getBody()) {
				addElement(b, t, allMedia);
			}
			
			return t;
		}
		
		private static void addElement(Body source, TrackManager t,
				edu.nku.cs.csc440.team2.mediaCloud.Media[] allMedia) {
			if (source instanceof Sequence) {
				for (Body b : ((Sequence) source).getBody()) {
					addElement(b, t, allMedia);
				}
			} else if (source instanceof Parallel) {
				for (Body b : ((Parallel) source).getBody()) {
					addElement(b, t, allMedia);
				}
			} else if (source instanceof Media) {
				if (source instanceof Audio) {
					addAudio((Audio) source, t, allMedia);
				} else if (source instanceof Image) {
					addImage((Image) source, t, allMedia);
				} else if (source instanceof Text) {
					addText((Text) source, t, allMedia);
				} else if (source instanceof Video) {
					addVideo((Video) source, t, allMedia);
				}
			}
		}
		
		private static void addAudio(Audio source, TrackManager t,
				edu.nku.cs.csc440.team2.mediaCloud.Media[] allMedia) {
			
			/* Create the AudioBox */
			AudioBox box = new AudioBox(
					source.getSrc(),
					source.getBegin(),
					source.getEnd() - source.getBegin(),
					-1.0);
			
			/* Find our Media object */
			edu.nku.cs.csc440.team2.mediaCloud.Media m;
			m = find(allMedia, source.getSrc());
			
			/* Finish the AudioBox and add it to the TrackManager */
			if (m != null) {
				box.setClipDuration(getClipDuration(m.getDuration()));
				box.setName(m.getName());
				t.addBox(box, box.getBegin());
			}
		}
		
		private static void addImage(Image source, TrackManager t,
				edu.nku.cs.csc440.team2.mediaCloud.Media[] allMedia) {
			
			/* Create the ImageBox */
			ImageBox box = new ImageBox(
					source.getSrc(),
					source.getBegin(),
					source.getEnd() - source.getBegin(),
					new ComposerRegion(source.getRegion()));
			
			/* Find our Media object */
			edu.nku.cs.csc440.team2.mediaCloud.Media m;
			m = find(allMedia, source.getSrc());
			
			/* Finish the ImageBox and add it to the TrackManager */
			if (m != null) {
				box.setName(m.getName());
				t.addBox(box, box.getBegin());
			}
		}
		
		private static void addText(Text source, TrackManager t,
				edu.nku.cs.csc440.team2.mediaCloud.Media[] allMedia) {
			
			/* Create the TextBox */
			TextBox box = new TextBox(
					null,
					source.getBegin(),
					source.getEnd() - source.getBegin(),
					new ComposerRegion(source.getRegion()));
			
			/* Find our Media object */
			edu.nku.cs.csc440.team2.mediaCloud.Media m;
			m = find(allMedia, source.getSrc());
			
			/* Finish the TextBox and add it to the TrackManager */
			if (m != null) {
				box.setName(m.getName()); // TODO wat
				t.addBox(box, box.getBegin());
			}
		}
		
		private static void addVideo(Video source, TrackManager t,
				edu.nku.cs.csc440.team2.mediaCloud.Media[] allMedia) {
			
			/* Create the VideoBox */
			VideoBox box = new VideoBox(
					source.getSrc(),
					source.getBegin(),
					source.getEnd() - source.getBegin(),
					-1.0,
					new ComposerRegion(source.getRegion()));
			
			/* Find our Media object */
			edu.nku.cs.csc440.team2.mediaCloud.Media m;
			m = find(allMedia, source.getSrc());
			
			/* Finish the VideoBox and add it to the TrackManager */
			if (m != null) {
				box.setClipDuration(getClipDuration(m.getDuration()));
				box.setName(m.getName());
				t.addBox(box, box.getBegin());
			}
		}
		
		private static double getClipDuration(String duration) {
			/* Calculate duration in seconds */
			String[] dur = duration.split(":");
			double seconds = Double.parseDouble(dur[2]);
			seconds += Double.parseDouble(dur[1]) * 60.0;
			seconds += Double.parseDouble(dur[0]) * 60.0 * 60.0;
			return seconds;
		}
		
		private static edu.nku.cs.csc440.team2.mediaCloud.Media find(
				edu.nku.cs.csc440.team2.mediaCloud.Media[] media,
				String source) {
			for (int i = 0; i < media.length; i ++) {
				if (media[i].getMediaUrl().equals(source)) {
					return media[i];
				}
			}
			return null;
		}
		
	}
	
}
