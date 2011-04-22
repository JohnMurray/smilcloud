package edu.nku.cs.csc440.team2.composer;

import edu.nku.cs.csc440.team2.message.Audio;
import edu.nku.cs.csc440.team2.message.Media;
import edu.nku.cs.csc460.team2.R;
import android.graphics.Canvas;

/**
 * An AudioBox is a Box that represents an Audio object.
 * 
 * @author William Knauer <knauerw1@nku.edu>
 * @version 2011.0416
 */
public class AudioBox extends AudioVideoBox {
	/**
	 * Class constructor.
	 * 
	 * @param source The source of the Media.
	 * @param begin The absolute begin time of the Media.
	 * @param duration The duration of the Media.
	 * @param clipDuration The duration of the source media.
	 */
	public AudioBox(String source, int begin, int duration, int clipDuration) {
		super(source, begin, duration, clipDuration);
		setType('A');
	}
	
	@Override
	public void draw(Canvas canvas) {
		if (getContext() != null) {
			super.draw(canvas,
					getContext().getResources().getColor(R.color.audiobox_bg),
					getContext().getResources().getColor(R.color.audiobox_fg),
					getContext().getResources().getColor(R.color.resize_grip));
		}
	}

	@Override
	public Media toMedia() {
		return new Audio(
				((double) getBegin()) / 10.0,
				((double) getEnd()) / 10.0,
				getSource(),
				((double) getClipBegin()) / 10.0,
				((double) getClipEnd()) / 10.0);
	}
	
}
