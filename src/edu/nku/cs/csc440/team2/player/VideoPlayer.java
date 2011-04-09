package edu.nku.cs.csc440.team2.player;

import android.media.MediaPlayer;
import android.view.SurfaceView;

public class VideoPlayer extends SingleInstancePlayer implements
		MediaPlayer.OnPreparedListener
{
	private MediaPlayer mMediaPlayer;
	private SurfaceView sfView;
	
	public VideoPlayer(String resource, double begin, double duration)
	{
		this.resourceURL = resource;
		this.start = begin;
		this.duration = duration;
	}

	public void play()
	{
		try
		{
			this.mMediaPlayer.start();
		}
		catch(IllegalStateException e)
		{
			e.printStackTrace();
		}
		this.isPlaying = true;
		this.incrementPlaybackTime();
	}
	
	public void pause()
	{
		try
		{
			this.mMediaPlayer.pause();
		}
		catch(IllegalStateException e)
		{
			e.printStackTrace();
		}
	}
	
	public void seekForward()
	{
		
	}
	
	public void seekBackward()
	{
		
	}
	
	public void render()
	{
		this.sfView = new SurfaceView(this.layout.getContext());
		this.sfView.setMinimumHeight(this.mMediaPlayer.getVideoHeight());
		this.sfView.setMinimumWidth(this.mMediaPlayer.getVideoWidth());
		this.mMediaPlayer.setDisplay(this.sfView.getHolder());
		this.layout.addView(this.sfView);
	}
	
	public void unRender()
	{
		this.layout.removeView(this.sfView);
		try
		{
			this.mMediaPlayer.stop();
		}
		catch(IllegalStateException e)
		{
			e.printStackTrace();
		}
	}
	
	public void prepare()
	{
		try
		{
			this.mMediaPlayer = new MediaPlayer();
			try {
				this.mMediaPlayer.reset();
				this.mMediaPlayer.setDataSource(this.resourceURL);
				this.mMediaPlayer.setOnPreparedListener(this);
				this.subject.notifyBufferingWithoutPause();
				this.prepare();
			}
			catch(Exception e)
			{
				this.mMediaPlayer.reset();
				this.mMediaPlayer.setDataSource(this.resourceURL);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public void onPrepared(MediaPlayer mp)
	{
		this.subject.notifyDoneBufferingWithoutRestart();
	}
	
}
