package edu.nku.cs.csc440.team2.player;

import android.widget.TextView;
import edu.nku.cs.csc440.team2.provider.MediaProvider;

public class TextPlayer extends SingleInstancePlayer 
{
	private String text;
	private TextView txtView;
	
	public TextPlayer(String resource, double start, double duration)
	{
		this.resourceURL = resource;
		this.start = start;
		this.duration = duration;
	}

	public void play()
	{
		if( ! this.isPlaying )
		{
			this.isPlaying = true;
			this.render();
		}
		this.incrementPlaybackTime();
	}
	
	public void pause()
	{
		//do absolutely nothing!
	}
	
	public void seekForward()
	{
		//not implemented yet
	}
	
	public void seekBackward()
	{
		//not implemented yet
	}
	
	public void render()
	{
		this.layout.post(new Runnable() {
			public void run() {
				txtView = new TextView(layout.getContext());
				txtView.setText(text);
				layout.addView(txtView);				
			}
		});
	}
	
	public void unRender()
	{
		this.layout.post(new Runnable() {
			public void run() {
				layout.removeView(txtView);				
			}
		});
	}
	
	public void prepare()
	{
		this.text = (new MediaProvider()).getText(this.resourceURL);
	}
	
}
