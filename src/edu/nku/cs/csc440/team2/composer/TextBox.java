package edu.nku.cs.csc440.team2.composer;

import edu.nku.cs.csc440.team2.message.Media;
import edu.nku.cs.csc440.team2.message.Text;
import edu.nku.cs.csc460.team2.R;
import android.graphics.Canvas;

/**
 * A TextBox is a Box that represents a Text object.
 * 
 * @author William Knauer <knauerw1@nku.edu>
 * @version 2011.0420
 */
public class TextBox extends Box {
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
	public TextBox(String source, double begin, double duration,
			ComposerRegion region) {
		super(source, begin, duration);
		setRegion(region);
		setId("Text " + sCount);
		sCount++;
	}

	@Override
	public void draw(Canvas canvas) {
		if (getContext() != null) {
			super.draw(canvas,
					getContext().getResources().getColor(R.color.textbox_bg),
					getContext().getResources().getColor(R.color.textbox_fg),
					getContext().getResources().getColor(R.color.resize_grip));
		}
	}

	@Override
	public Media toMedia() {
		return new Text(getBegin(), getEnd(), getSource(),
				getRegion().toRegion());
	}
	
}
