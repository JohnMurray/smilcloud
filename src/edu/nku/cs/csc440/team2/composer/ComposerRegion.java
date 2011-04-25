package edu.nku.cs.csc440.team2.composer;

import edu.nku.cs.csc440.team2.message.Region;
import edu.nku.cs.csc440.team2.message.SmilDimension;
import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * A ComposerRegion mimics the functionality of message.Region but can be saved
 * to and restored from a Parcel.
 * 
 * @author William Knauer <knauerw1@nku.edu>
 * @version 2011.0424
 */
public class ComposerRegion implements Parcelable {
	/** The region's bounds */
	Rect mBounds;

	/** The region's z-index relative to other regions */
	int mZindex;

	/** Used to generate instances of this class from a Parcel */
	public static final Parcelable.Creator<ComposerRegion> CREATOR
			= new Parcelable.Creator<ComposerRegion>() {

		@Override
		public ComposerRegion createFromParcel(Parcel source) {
			return new ComposerRegion(source);
		}

		@Override
		public ComposerRegion[] newArray(int size) {
			return new ComposerRegion[size];
		}

	};

	/**
	 * Class constructor.
	 */
	ComposerRegion() {
		mBounds = new Rect();
		mZindex = Integer.MAX_VALUE;
	}

	/**
	 * Class constructor.
	 * 
	 * @param l
	 *            The left bound.
	 * @param t
	 *            The top bound.
	 * @param r
	 *            The right bound.
	 * @param b
	 *            The bottom bound.
	 * @param zIndex
	 *            The relative z-index.
	 */
	ComposerRegion(int l, int t, int r, int b, int zIndex) {
		mBounds = new Rect(l, t, r, b);
		mZindex = Integer.MAX_VALUE;
	}

	/**
	 * Class constructor for creating from a Parcel.
	 * 
	 * @param in
	 *            The Parcel to construct from.
	 */
	ComposerRegion(Parcel in) {
		mBounds = in.readParcelable(Rect.class.getClassLoader());
		mZindex = in.readInt();
	}

	/**
	 * Class constructor for creating from a Region.
	 * 
	 * @param r
	 *            The Region to create from.
	 */
	ComposerRegion(Region r) {
		mBounds = new Rect(r.getOrigin().getWidth(), r.getOrigin().getHeight(),
				r.getOrigin().getWidth() + r.getDimensions().getWidth(), r
						.getOrigin().getHeight()
						+ r.getDimensions().getHeight());
		setZindex(r.getZindex());
	}

	@Override
	public int describeContents() {
		return 0;
	}

	/**
	 * @return The bounds of this region.
	 */
	Rect getBounds() {
		return mBounds;
	}

	/**
	 * @return The relative z-index of this region.
	 */
	int getZindex() {
		return mZindex;
	}

	/**
	 * @param r
	 *            The bounds of this region.
	 */
	void setBounds(Rect r) {
		mBounds.set(r);
	}

	/**
	 * @param zindex
	 *            The relative z-index of this region.
	 */
	void setZindex(int zindex) {
		mZindex = zindex;
	}

	/**
	 * Generates a Region from this ComposerRegion
	 * 
	 * @return The generated Region.
	 */
	Region toRegion() {
		Region result = new Region(new SmilDimension(getBounds().width(),
				getBounds().height()));
		result.setOrigin(new SmilDimension(getBounds().left, getBounds().top));
		result.setZindex(getZindex());
		return result;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeParcelable(mBounds, 0);
		dest.writeInt(mZindex);
	}
}
