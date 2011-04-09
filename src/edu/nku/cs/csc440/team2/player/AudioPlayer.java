package edu.nku.cs.csc440.team2.player;

import android.media.MediaPlayer;

public class AudioPlayer extends SingleInstancePlayer implements
			MediaPlayer.OnPreparedListener
{
	
	private MediaPlayer mMediaPlayer;
	
	public AudioPlayer(String resource, double start, double duration)
	{
		this.resourceURL = resource;
		this.start = start;
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
		//do nothing... as we should
	}
	
	public void unRender()
	{
		//do nothing... as we should
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
			catch (Exception e) {
				this.mMediaPlayer.reset();
				this.mMediaPlayer.setDataSource(this.resourceURL);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		this.subject.notifyDoneBufferingWithoutRestart();
	}
	
}
