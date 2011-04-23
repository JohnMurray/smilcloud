package edu.nku.cs.csc440.team2.player;

import android.media.MediaPlayer;
import android.util.Log;

public class AudioPlayer extends SingleInstancePlayer implements
			MediaPlayer.OnPreparedListener
{
	
	private MediaPlayer mMediaPlayer;
	private double mOffsetInto;
	
	public AudioPlayer(String resource, double start, double duration, double offsetInto)
	{
		this.resourceURL = resource;
		this.start = start;
		this.duration = duration;
		this.mOffsetInto = offsetInto;
	}

	public void play()
	{
		try
		{
			this.mMediaPlayer.start();
			Log.w("AUDIO PLAYER", "just started playing");
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
		if( this.isPlaying )
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
		if( this.isPlaying )
		{
			try
			{
				this.mMediaPlayer.stop();
				Log.w("AUDIO PLAYER", "should have just stopped");
			}
			catch(IllegalStateException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public void prepare()
	{
		this.mMediaPlayer = new MediaPlayer();
		try {
			this.mMediaPlayer.reset();
			this.mMediaPlayer.setDataSource(this.resourceURL);
			this.mMediaPlayer.setOnPreparedListener(this);
			this.mMediaPlayer.prepareAsync();
			this.subject.notifyBuffering();
		
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		this.mMediaPlayer.seekTo((int)(this.mOffsetInto * 100));
		this.subject.notifyDoneBuffering();
		Log.e("AUDIO", "I'm done buffering and ready to play!");
	}
	
	@Override
	public void reset()
	{
		this.prepare();
		super.reset();
	}
	
}
