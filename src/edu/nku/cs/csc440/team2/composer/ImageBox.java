package edu.nku.cs.csc440.team2.composer;

import edu.nku.cs.csc440.team2.message.Image;
import edu.nku.cs.csc440.team2.message.Media;
import edu.nku.cs.csc460.team2.R;
import android.graphics.Canvas;

/**
 * An AudioBox is a Box that represents an Audio object.
 * 
 * @author William Knauer <knauerw1@nku.edu>
 * @version 2011.0416
 */
public class ImageBox extends Box {
	/** A static counter used to generate unique ids */
	private static int sCount = 1;

	/**
	 * Class constructor.
	 * 
	 * @param source The source of the Media.
	 * @param begin The absolute begin time of the Media.
	 * @param duration The duration of the Media.
	 * @param region The region of the Media.
	 */
	public ImageBox(String source, double begin, double duration,
			ParcelableRegion region) {
		super(source, begin, duration);
		setRegion(region);
		setId("Image " + sCount);
		sCount++;
	}
	
	@Override
	public void draw(Canvas canvas) {
		if (getContext() != null) {
			super.draw(canvas,
					getContext().getResources().getColor(R.color.imagebox_bg),
					getContext().getResources().getColor(R.color.imagebox_fg),
					getContext().getResources().getColor(R.color.resize_grip));
		}
	}

	@Override
	public Media toMedia() {
		return new Image(getBegin(), getEnd(), getSource(),
				getRegion().toRegion());
	}
	
}
