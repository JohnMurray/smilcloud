package edu.nku.cs.csc440.team2.player;

import android.media.MediaPlayer;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

public class VideoPlayer extends SingleInstancePlayer implements
		MediaPlayer.OnPreparedListener, SurfaceHolder.Callback
{
	private MediaPlayer mMediaPlayer;
	private SurfaceView sfView;
	private SurfaceHolder sfHolder;
	
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
		this.layout.post(new Runnable() {
			public void run() {
				//layout.setVisibility(View.VISIBLE);
				//sfView.setMinimumHeight(mMediaPlayer.getVideoHeight());
				//sfView.setMinimumWidth(mMediaPlayer.getVideoWidth());
				sfHolder.setFixedSize(mMediaPlayer.getVideoWidth(), mMediaPlayer.getVideoHeight());
				mMediaPlayer.setDisplay(sfHolder);
				//layout.addView(sfView);
			}
		});
	}
	
	public void unRender()
	{
		this.layout.post(new Runnable() {
			public void run() {
				layout.removeView(sfView);
				try
				{
					mMediaPlayer.stop();
				}
				catch(IllegalStateException e)
				{
					e.printStackTrace();
				}
				//layout.setVisibility(View.INVISIBLE);				
			}
		});
	}
	
	public void prepare()
	{
		this.mMediaPlayer = new MediaPlayer();
		try {
			this.mMediaPlayer.reset();
			this.mMediaPlayer.setDataSource(this.resourceURL);
			this.mMediaPlayer.setOnPreparedListener(this);
			this.subject.notifyBufferingWithoutPause();
			this.mMediaPlayer.prepareAsync();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		this.layout.post(new Runnable() {
			@Override
			public void run() {
				sfView = new SurfaceView(layout.getContext());
				layout.addView(sfView);
				sfHolder = sfView.getHolder();
				sfHolder.addCallback(VideoPlayer.this);
				//sfView.setVisibility(View.INVISIBLE);
			}
		});
		this.subject.notifyBufferingWithoutPause();
	}
	
	@Override
	public void onPrepared(MediaPlayer mp)
	{
		this.subject.notifyDoneBufferingWithoutRestart();
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		this.subject.notifyDoneBufferingWithoutRestart();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {}
	
}
