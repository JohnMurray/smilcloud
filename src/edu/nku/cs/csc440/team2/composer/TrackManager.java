package edu.nku.cs.csc440.team2.composer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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
import edu.nku.cs.csc440.team2.provider.MessageProvider;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Environment;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * A TrackManager is the primary data structure used by the composer package. It
 * is the graphical equivalent of a message.Message. A TrackManager stores
 * Tracks, which in turn store Boxes; so a single instance of this class stores
 * the contents of an entire SMIL document, and can be used to generate a
 * message.Message object. A TrackManager can be drawn if the drawing bounds are
 * set using setBounds(). Then draw() will draw it onto a given Canvas.
 * 
 * @author William Knauer <knauerw1@nku.edu>
 * @version 2011.0424
 */
public class TrackManager implements Parcelable {

	/**
	 * Generates TrackManager objects.
	 */
	public static class Factory {

		/** The TrackManager being created */
		private static TrackManager mTrackManager = null;

		/** The Media available to the user */
		private static edu.nku.cs.csc440.team2.mediaCloud.Media[] mMedia = null;

		/**
		 * Adds an Audio element to the TrackManager.
		 * 
		 * @param audio
		 *            The Audio element to add.
		 */
		private static void addAudio(Audio audio) {
			/* Get the Audio's source url */
			String source = audio.getSrc();

			/* Find the associated Media in mMedia */
			edu.nku.cs.csc440.team2.mediaCloud.Media m = find(source);

			/* If the media exists and is accessible to the user */
			if (m != null) {
				/* Determine the Box's parameters */
				int begin = (int) Math.round(audio.getBegin() * 10);
				int end = (int) Math.round(audio.getEnd() * 10);
				int duration = end - begin;
				int clipDuration = parseMediaDuration(m.getDuration());
				String name = m.getName();

				/* Create the Box */
				AudioBox box = new AudioBox(source, begin, duration,
						clipDuration);

				/* Set the Box's name */
				box.setName(name);

				/* Add the Box to the TrackManager */
				mTrackManager.addBox(box, begin);
			}
		}

		/**
		 * Adds a Body element to the TrackManager.
		 * 
		 * @param source
		 *            The Body element to add.
		 */
		private static void addElement(Body source) {
			/* Determine what to do with the Body source */
			if (source instanceof Sequence) {
				/* Recursively call this method for every body source has */
				for (Body b : ((Sequence) source).getBody()) {
					addElement(b);
				}
			} else if (source instanceof Parallel) {
				/* Recursively call this method for every body source has */
				for (Body b : ((Parallel) source).getBody()) {
					addElement(b);
				}
			} else if (source instanceof Media) {
				/* Add a Box representing source to mTrackManager */
				if (source instanceof Audio) {
					addAudio((Audio) source);
				} else if (source instanceof Image) {
					addImage((Image) source);
				} else if (source instanceof Text) {
					addText((Text) source);
				} else if (source instanceof Video) {
					addVideo((Video) source);
				}
			}
		}

		/**
		 * Adds an Image element to the TrackManager.
		 * 
		 * @param image
		 *            The Image element to add.
		 */
		private static void addImage(Image image) {
			/* Get the Image's source url */
			String source = image.getSrc();

			/* Find the associated Media in mMedia */
			edu.nku.cs.csc440.team2.mediaCloud.Media m = find(source);

			/* If the media exists and is accessible to the user */
			if (m != null) {
				/* Determine the Box's parameters */
				int begin = (int) Math.round(image.getBegin() * 10);
				int end = (int) Math.round(image.getEnd() * 10);
				int duration = end - begin;
				ComposerRegion region = new ComposerRegion(image.getRegion());
				String name = m.getName();

				/* Create the Box */
				ImageBox box = new ImageBox(source, begin, duration, region);

				/* Set the Box's name */
				box.setName(name);

				/* Add the Box to the TrackManager */
				mTrackManager.addBox(box, begin);
			}
		}

		/**
		 * Adds a Text element to the TrackManager.
		 * 
		 * @param text
		 *            The Text element to add.
		 */
		private static void addText(Text text) {
			/* Get the Text's source url */
			String source = text.getSrc();

			/* Find the associated Media in mMedia */
			edu.nku.cs.csc440.team2.mediaCloud.Media m = find(source);

			/* If the media exists and is accessible to the user */
			if (m != null) {
				/* Determine the Box's parameters */
				int begin = (int) Math.round(text.getBegin() * 10);
				int end = (int) Math.round(text.getEnd() * 10);
				int duration = end - begin;
				ComposerRegion region = new ComposerRegion(text.getRegion());
				String name = m.getName().trim();

				/* Create the Box */
				TextBox box = new TextBox(source, begin, duration, region);

				/* Set the TextBox's name (text) */
				box.setName(name);

				/* Add the Box to the TrackManager */
				mTrackManager.addBox(box, begin);
			}
		}

		/**
		 * Adds a Video element to the TrackManager.
		 * 
		 * @param video
		 *            The Video element to add.
		 */
		private static void addVideo(Video video) {
			/* Get the Video's source url */
			String source = video.getSrc();

			/* Find the associated Media in mMedia */
			edu.nku.cs.csc440.team2.mediaCloud.Media m = find(source);

			/* If the media exists and is accessible to the user */
			if (m != null) {
				/* Determine the Box's parameters */
				int begin = (int) Math.round(video.getBegin() * 10);
				int end = (int) Math.round(video.getEnd() * 10);
				int duration = end - begin;
				int clipDuration = parseMediaDuration(m.getDuration());
				ComposerRegion region = new ComposerRegion(video.getRegion());
				String name = m.getName();

				/* Create the Box */
				VideoBox box = new VideoBox(source, begin, duration,
						clipDuration, region);

				/* Set the Box's name */
				box.setName(name);

				/* Add the Box to the TrackManager */
				mTrackManager.addBox(box, begin);
			}
		}

		/**
		 * Creates a TrackManager from a given Message.
		 * 
		 * @param messageId
		 *            The id of the Message to create from.
		 * @param userId
		 *            The user id for cloud interaction.
		 * @return The newly created TrackManager.
		 */
		public static TrackManager create(String messageId, int userId) {
			/* Set up a new TrackManager */
			mTrackManager = new TrackManager(messageId, userId);

			if (messageId != null) {
				/* Get the Message from network */
				Message m = new MessageProvider().getMessageById(messageId);

				/* Get listing of available media from network */
				MediaProvider mediaProvider = new MediaProvider();
				mMedia = mediaProvider.getAllMedia(userId);

				/* Set each text-type media's name to its associated text */
				for (int i = 0; i < mMedia.length; i++) {
					if (mMedia[i].getType().equalsIgnoreCase("text")) {
						String url = mMedia[i].getMediaUrl();
						String text = mediaProvider.getText(url);
						mMedia[i].setName(text);
					}
				}

				/* Add everything from the Message to the TrackManager */
				for (Body b : m.getBody()) {
					addElement(b);
				}
			}

			/* Clean up and return */
			TrackManager result = mTrackManager;
			mTrackManager = null;
			mMedia = null;
			return result;
		}

		/**
		 * Finds a Media object in mMedia by the source url.
		 * 
		 * @param source
		 *            The source url to search with.
		 * @return The Media object whose source matches the given source. Null
		 *         if none was found.
		 */
		private static edu.nku.cs.csc440.team2.mediaCloud.Media find(
				String source) {
			for (int i = 0; i < mMedia.length; i++) {
				if (mMedia[i].getMediaUrl().equals(source)) {
					return mMedia[i];
				}
			}
			return null;
		}

		/**
		 * Converts a media duration String from the cloud to an integer to be
		 * used by a Box.
		 * 
		 * @param mediaDuration
		 *            The String duration to convert.
		 * @return The converted duration.
		 */
		private static int parseMediaDuration(String mediaDuration) {
			/* Split by delimiters */
			String[] firstSplit = mediaDuration.split(":");
			if (firstSplit[2].contains(".")) {
				String[] secondSplit = firstSplit[2].split(".");

				/* Parse integers from splits */
				int tenths = Integer.parseInt(secondSplit[1]);
				int seconds = Integer.parseInt(secondSplit[0]);
				int minutes = Integer.parseInt(firstSplit[1]);
				int hours = Integer.parseInt(firstSplit[0]);

				/* Determine total time in tenth-seconds */
				minutes += hours * 60;
				seconds += minutes * 60;
				tenths += seconds * 10;

				return tenths;
			} else {
				int tenths = 0;
				int seconds = Integer.parseInt(firstSplit[2]);
				int minutes = Integer.parseInt(firstSplit[1]);
				int hours = Integer.parseInt(firstSplit[0]);

				/* Determine total time in tenth-seconds */
				minutes += hours * 60;
				seconds += minutes * 60;
				tenths += seconds * 10;

				return tenths;
			}
		}

	}

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
		private int mOldBegin;

		/** The Track to move mBox into */
		private Track mTargetTrack;

		/** The begin time where mBox should be added to its new Track */
		private int mTargetBegin;

		/**
		 * Class constructor.
		 */
		MoveManager() {
			reset();
		}

		/**
		 * Draws mBox to a Canvas wherever the user has dragged it.
		 * 
		 * @param canvas
		 *            The canvas to draw on.
		 */
		void draw(Canvas canvas) {
			if (mBox != null) {
				mBox.draw(canvas);
			}
		}

		/**
		 * Commits the move if possible. Resets the Box to its old location if
		 * it is not possible.
		 */
		void finish() {
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
		Rect getBounds() {
			Rect result = null;
			if (mBox != null) {
				result = mBox.getBounds();
			}
			return result;
		}

		/**
		 * @return Returns true if a move is in progress.
		 */
		boolean isMoving() {
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
		void offset(int dx, int dy) {
			/* Offset mBox by (dx, dy) */
			mBox.setBounds(mBox.getBounds().left + dx, mBox.getBounds().top
					+ dy, mBox.getBounds().right + dx, mBox.getBounds().bottom
					+ dy);

			/* Figure out where mBox is now */
			int begin = (int) (Composer.pxToSec(mBox.getBounds().left) * 10);
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
		void reset() {
			mFitsTarget = false;
			mBox = null;
			mOldTrack = null;
			mOldBegin = -1;
			mTargetTrack = null;
			mTargetBegin = -1;
		}

		/**
		 * Initiates a move at a given set of coordinates.
		 * 
		 * @param x
		 *            The x-coordinate.
		 * @param y
		 *            The y-coordinate.
		 */
		void start(int x, int y) {
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
		private int mStartDuration;

		/**
		 * Class constructor.
		 */
		ResizeManager() {
			reset();
		}

		/**
		 * Finishes the resize by resetting the ResizeManager.
		 */
		void finish() {
			reset();
		}

		/**
		 * @return Returns true if a resize is in progress.
		 */
		boolean isResizing() {
			return (mBox != null);
		}

		/**
		 * Resets the local variables to their starting state.
		 */
		void reset() {
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
		void resize(int x) {
			/* Determine the desired duration */
			int delta = (int) (Composer.pxToSec(x - mStartX) * 10);
			int duration = mStartDuration + delta;

			/* Check if new duration is ok */
			boolean ok = true;

			/* Make sure the desired duration isn't too small */
			if (duration < 5) {
				ok = false;
			}

			/* Make sure the desired duration isn't too large */
			if (mBox instanceof AudioVideoBox) {
				int max = ((AudioVideoBox) mBox).getClipDuration();
				max -= ((AudioVideoBox) mBox).getClipBegin();
				if (duration > max) {
					ok = false;
				}
			}

			if (ok) {
				/* Remove the box from its track */
				mTrack.removeBox(mBox.getBegin());

				/* Update the box's duration */
				int oldDuration = mBox.getDuration();
				mBox.setDuration(duration);

				/* Attempt to re-add the box */
				boolean fits = mTrack.addBox(mBox, mBox.getBegin());

				/* If it fails revert the change and re-add the box */
				if (!fits) {
					mBox.setDuration(oldDuration);
					mTrack.addBox(mBox, mBox.getBegin());
				} else {
					mBox.postUpdateLabel();
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
		 * @return True if a resize was initiated.
		 */
		boolean start(int x, int y) {
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

	/** The global identifier for the represented Message */
	private String mId;

	/** The userId for cloud interaction */
	private int mUserId;

	/** Used to generate instances of this class from a Parcel */
	public static final Parcelable.Creator<TrackManager> CREATOR
			= new Parcelable.Creator<TrackManager>() {

		@Override
		public TrackManager createFromParcel(Parcel source) {
			return new TrackManager(source);
		}

		@Override
		public TrackManager[] newArray(int size) {
			return new TrackManager[size];
		}

	};

	/**
	 * Class constructor for creating from a Parcel.
	 * 
	 * @param in
	 *            The parcel to create from.
	 */
	TrackManager(Parcel in) {
		mTracks = new LinkedList<Track>();
		in.readTypedList(mTracks, Track.CREATOR);
		mMoveManager = new MoveManager();
		mResizeManager = new ResizeManager();
		mBounds = in.readParcelable(Rect.class.getClassLoader());
		mId = in.readString();
		mUserId = in.readInt();
	}

	/**
	 * Class constructor.
	 */
	TrackManager(String id, int userId) {
		mBounds = new Rect();
		mMoveManager = new MoveManager();
		mResizeManager = new ResizeManager();
		mTracks = new LinkedList<Track>();
		setId(id);
		setUserId(userId);
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
	void addBox(Box elt, int begin) {
		boolean added = false;
		for (Track t : mTracks) {
			if (!added) {
				added = t.addBox(elt, begin);
			}
		}
		maintain();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	/**
	 * Draws all Tracks onto the given Canvas.
	 * 
	 * @param canvas
	 *            The Canvas to draw on.
	 */
	void draw(Canvas canvas) {
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

	/**
	 * @return A List containing all the Boxes contained by all the Tracks in
	 *         this TrackManager.
	 */
	LinkedList<Box> getAllBoxes() {
		LinkedList<Box> boxes = new LinkedList<Box>();
		for (Track t : mTracks) {
			for (Box b : t.getAllBoxes()) {
				boxes.add(b);
			}
		}
		return boxes;
	}

	/**
	 * @return The drawing bounds of this TrackManager.
	 */
	Rect getBounds() {
		return mBounds;
	}

	/**
	 * Gets the Box at a specified set of coordinates.
	 * 
	 * @param targetX
	 *            The x-coordinate.
	 * @param targetY
	 *            The y-coordinate.
	 * @return The Box at the set of coordinates. Returns null if no such Box is
	 *         found.
	 */
	Box getBox(int targetX, int targetY) {
		Box result = null;
		Track t = getTrack(targetX, targetY);
		if (t != null) {
			result = t.getBox((int) (Composer.pxToSec(targetX) * 10));
		}
		return result;
	}

	/**
	 * Gets a Box by its id.
	 * 
	 * @param id
	 *            The id to search for.
	 * @return The box whose id matches the supplied id.
	 */
	Box getBox(String id) {
		for (Box b : getAllBoxes()) {
			if (b.getId().equals(id)) {
				return b;
			}
		}
		return null;
	}

	/**
	 * Finds all the Boxes whose playback times overlap with a given Box.
	 * 
	 * @param box
	 *            The Box to check.
	 * @return Returns a list of all the Boxes whose playback times overlap with
	 *         a given Box.
	 */
	LinkedList<Box> getConcurrentBoxes(Box box) {
		LinkedList<Box> concurrent = new LinkedList<Box>();
		double begin = box.getBegin();
		double end = box.getEnd();
		for (Track t : mTracks) {
			for (Box b : t.getAllBoxes()) {
				if (b.getBegin() <= begin && begin < b.getEnd()) {
					concurrent.add(b);
				} else if (b.getBegin() < end && end <= b.getEnd()) {
					concurrent.add(b);
				} else if (begin <= b.getBegin() && b.getBegin() < end) {
					concurrent.add(b);
				} else if (begin < b.getEnd() && b.getEnd() <= end) {
					concurrent.add(b);
				}
			}
		}
		return concurrent;
	}

	/**
	 * @return The id of the Message associated with this TrackManager.
	 */
	String getId() {
		return mId;
	}

	/**
	 * @return Returns the farthest x-coordinate where this TrackManager needs
	 *         to draw.
	 */
	int getMaxX() {
		int result = 0;
		for (Box b : getAllBoxes()) {
			if (Composer.secToPx((b.getEnd()) / 10.0) > result) {
				result = Composer.secToPx((b.getEnd()) / 10.0);
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
	int getMaxY() {
		int result = 0;
		for (Track t : mTracks) {
			if (t.getBounds().bottom > result) {
				result = t.getBounds().bottom + 5;
			}
		}
		return result;
	}

	MoveManager getMoveManager() {
		return mMoveManager;
	}

	ResizeManager getResizeManager() {
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
	 * @return The user id for cloud interaction.
	 */
	int getUserId() {
		return mUserId;
	}

	/**
	 * Ensures that there is precisely one empty Track at the bottom of this
	 * TrackManager.
	 */
	void maintain() {
		if (mTracks.isEmpty() || !mTracks.getLast().isEmpty()) {
			Track t = new Track();
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
	int measureHeight() {
		return Track.HEIGHT * mTracks.size() + 2 * Box.SPACING;
	}

	/**
	 * Removes a given Box from any Track where it is found.
	 * 
	 * @param b
	 *            The Box to remove.
	 */
	void removeBox(Box b) {
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
	void setBounds(int l, int t, int r, int b) {
		mBounds.set(l, t, r, b);
	}

	/**
	 * @param id
	 *            The id of the Message associated with this TrackManager.
	 */
	void setId(String id) {
		mId = id;
	}

	/**
	 * @param userId
	 *            The user id for cloud interaction.
	 */
	void setUserId(int userId) {
		mUserId = userId;
	}

	/**
	 * Generates a message.Message object from this TrackManager.
	 * 
	 * @return The generated Message;
	 */
	Message toMessage() {
		/* Create the message */
		Message m = new Message(getId());

		/* Create the root element */
		Parallel p = new Parallel();

		/* Put all Boxes in the Parallel */
		int maxTime = -1;
		for (Box b : getAllBoxes()) {
			if (b instanceof TextBox) {
				b.setSource(uploadText(b.getName(), getUserId()));
			}
			if (b.getEnd() > maxTime) {
				maxTime = b.getEnd();
			}
			p.addElement(b.toMedia());
		}
		p.setBegin(0);
		p.setEnd((maxTime) / 10.0);

		/* Put the Parallel in the Message */
		m.addElement(p);

		return m;
	}

	/**
	 * Uploads a String as a text file to the cloud.
	 * 
	 * @param text
	 *            The text to upload.
	 * @param userId
	 *            The user id for cloud interaction.
	 * @return The url where the text can be found.
	 */
	String uploadText(String text, int userId) {
		String path = Environment.getExternalStorageDirectory() + "/smilcache/";
		path += java.util.UUID.randomUUID().toString() + ".txt";

		try {
			/* Make a file from the text */
			FileOutputStream fos = new FileOutputStream(new File(path));
			fos.write(text.getBytes());
			fos.flush();
			fos.close();

			/* Send the file to the network */
			MediaProvider mp = new MediaProvider();
			path = mp.saveMedia(path, "text", userId);

		} catch (FileNotFoundException e) {
			// do nothing.
		} catch (IOException e) {
			// do nothing.
		}
		return path;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeTypedList(mTracks);
		dest.writeParcelable(mBounds, 0);
		dest.writeString(mId);
		dest.writeInt(mUserId);
	}

}
