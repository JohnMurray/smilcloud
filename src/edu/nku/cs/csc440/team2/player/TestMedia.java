package edu.nku.cs.csc440.team2.player;

import android.graphics.Color;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Player for testing. It draws an rectangle on the screen and stays
 * there for the duration of it's plabyback. Used for generated test
 * SMIL documents. 
 * 
 * @author John Murray
 * @version 1.0 4/24/11
 */
public class TestMedia extends SingleInstancePlayer {

	/**
	 * The layout that will be used to draw the rectangle on
	 * the screen.
	 */
	private RelativeLayout rl;
	/**
	 * Hodlthe type of media that we "should" be rendering and put the
	 * text into the rectangle.
	 */
	String testType;
	
	public TestMedia( double start, double duration, String type ) {
		this.start = start;
		this.duration = duration;
		this.testType = type;
	}

	/**
	 * Not used
	 */
	@Override
	public void pause() {
		//do nothing
	}

	/**
	 * Not implemented
	 */
	@Override
	public void seekForward() {
		//not supported for now
		//[un]render if needed
	}

	/**
	 * Not implemented
	 */
	@Override
	public void seekBackward() {
		//not supported for now
		//[un]render if needed
	}

	/**
	 * Display the rectangel with the correct color and text to the screen.
	 */
	@Override
	public void render() {
		//add to parent view
		this.rl = new RelativeLayout(this.layout.getContext());
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(50, 50);
		this.rl.setLayoutParams(lp);
		TextView text = new TextView(this.layout.getContext());
		if( this.testType == "text" ) { text.setBackgroundColor(Color.BLUE); }
		if( this.testType == "audio" ) { text.setBackgroundColor(Color.RED); }
		if( this.testType == "video" ) { text.setBackgroundColor(Color.GREEN); }
		if( this.testType == "image" ) { text.setBackgroundColor(Color.GRAY); }
		lp = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.MATCH_PARENT);
		text.setLayoutParams(lp);
		this.rl.addView(text);
		this.layout.addView(this.rl);
	}

	/**
	 * Remove the rectangle from the screen. 
	 */
	@Override
	public void unRender() {
		//remove from parent view
		this.layout.removeAllViews();
	}
	
	/**
	 * No preparation required.
	 */
	public void prepare()
	{
		
	}

}
