package edu.nku.cs.csc440.team2.player;

import android.widget.TextView;

public class TextPlayer extends SingleInstancePlayer 
{
	private String text;
	private TextView txtView;
	
	public TextPlayer(String text, String resource, double start, double duration)
	{
		this.text = text;
		this.resourceURL = resource;
		this.start = start;
		this.duration = duration;
	}

	public void play()
	{
		this.incrementPlaybackTime();
	}
	
	public void pause()
	{
		//do absolutely nothing!
	}
	
	public void seekForward()
	{
		
	}
	
	public void seekBackward()
	{
		
	}
	
	public void render()
	{
		this.txtView = new TextView(this.layout.getContext());
		this.txtView.setText(this.text);
		this.layout.addView(this.txtView);
	}
	
	public void unRender()
	{
		this.layout.removeView(this.txtView);
	}
	
	public void prepare()
	{
		if( this.text == null )
		{
			//retrieve the text from the Provider
			//		which it can't do right now! :-(
			this.text = "testing text and what not";
		}
	}
	
}
