package edu.nku.cs.csc440.team2.player;

import android.graphics.Bitmap;
import android.widget.ImageView;
import edu.nku.cs.csc440.team2.provider.MediaProvider;

public class ImagePlayer extends SingleInstancePlayer 
{
	private ImageView imImage;
	private Bitmap bmImage;
	
	public ImagePlayer(String resource, double start, double duration)
	{
		this.duration = duration;
		this.resourceURL = resource;
		this.start = start;
	}

	public void play()
	{
		if( ! this.isPlaying )
		{
			this.isPlaying = true;
		}
		this.incrementPlaybackTime();
	}
	
	public void pause()
	{
		//it's an image... really...   -_-
	}
	
	public void seekForward()
	{
		//it's an image... really...   -_-
	}
	
	public void seekBackward()
	{
		//it's an image... really...   -_-
	}
	
	public void render()
	{
		this.layout.addView(this.imImage);
	}
	
	public void unRender()
	{
		this.layout.removeView(this.imImage);
	}
	
	public void prepare()
	{
		this.bmImage = (new MediaProvider()).getImage(this.resourceURL);
		this.imImage = new ImageView(this.layout.getContext());
		this.imImage.setImageBitmap(this.bmImage);
	}
	
}