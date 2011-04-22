package edu.nku.cs.csc440.team2.player;

import android.graphics.Color;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TestMedia extends SingleInstancePlayer {

	private RelativeLayout rl;
	String testType;
	
	public TestMedia( double start, double duration, String type ) {
		this.start = start;
		this.duration = duration;
		this.testType = type;
	}

	@Override
	public void pause() {
		//do nothing
	}

	@Override
	public void seekForward() {
		//not supported for now
		//[un]render if needed
	}

	@Override
	public void seekBackward() {
		//not supported for now
		//[un]render if needed
	}

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

	@Override
	public void unRender() {
		//remove from parent view
		this.layout.removeAllViews();
	}
	
	
	public void prepare()
	{
		
	}

}
