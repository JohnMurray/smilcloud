package edu.nku.cs.csc440.team2.composer;

import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;

public class ParcelableRegion implements Parcelable {
	Rect mBounds;
	int mZindex;

	public static final Parcelable.Creator<ParcelableRegion> CREATOR = new Parcelable.Creator<ParcelableRegion>() {

		@Override
		public ParcelableRegion createFromParcel(Parcel source) {
			return new ParcelableRegion(source);
		}

		@Override
		public ParcelableRegion[] newArray(int size) {
			return new ParcelableRegion[size];
		}

	};

	public ParcelableRegion() {
		mBounds = new Rect();
		mZindex = 999;
	}

	public ParcelableRegion(int l, int t, int r, int b, int zIndex) {
		mBounds = new Rect(l, t, r, b);
		mZindex = 999;
	}

	public ParcelableRegion(Parcel in) {
		mBounds = in.readParcelable(Rect.class.getClassLoader());
		mZindex = in.readInt();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public Rect getBounds() {
		return mBounds;
	}

	public int getZindex() {
		return mZindex;
	}

	public void setBounds(Rect r) {
		mBounds.set(r);
	}

	public void setZindex(int zindex) {
		mZindex = zindex;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeParcelable(mBounds, 0);
		dest.writeInt(mZindex);
	}

}
