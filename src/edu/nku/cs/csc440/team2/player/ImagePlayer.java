package edu.nku.cs.csc440.team2.player;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
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
			this.render();
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
		boolean res = this.layout.post(new Runnable(){
			public void run() {
				layout.addView(imImage);
			}
		});
		Log.w("ImagePlyaer", "Result of render: " + ((Boolean)res).toString());
		Log.w("ImagePlayer", this.layout.toString());
		Log.w("ImagePlayer", "Duration: " + this.duration);
		Log.w("ImagePlayer", "playback time: " + this.timePlayed);
	}
	
	public void unRender()
	{
		Log.w("ImagePlayer", "unrendering");
		this.layout.post( new Runnable() {
			public void run() {
				layout.removeView(imImage);
			}
		});
	}
	
	public void prepare()
	{
		this.bmImage = (new MediaProvider()).getImage(this.resourceURL);
		
		this.layout.post(new Runnable() {
			public void run() {
				imImage = new ImageView(layout.getContext());
				imImage.setScaleType(ImageView.ScaleType.FIT_XY);
				imImage.setAdjustViewBounds(false);
				imImage.setMaxHeight(layout.getHeight());
				imImage.setMaxWidth(layout.getHeight());
				
				imImage.setImageBitmap(bmImage);				
			}
		});
		
	}
	
}