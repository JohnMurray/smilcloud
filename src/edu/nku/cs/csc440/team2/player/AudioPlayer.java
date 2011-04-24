package edu.nku.cs.csc440.team2.player;

import android.media.MediaPlayer;
import android.util.Log;

/**
 * Implements playback features for Audio
 * @author John Murray
 * @version 1.0 4/24/11
 */
public class AudioPlayer extends SingleInstancePlayer implements
			MediaPlayer.OnPreparedListener
{
	
	/**
	 * The media player object that will be used to stream the media
	 */
	private MediaPlayer mMediaPlayer;
	
	/**
	 * The offset that the audio will start playback at.
	 * @note This will work on a per-device basis as it is hardware and platform
	 * dependent.
	 */
	private double mOffsetInto;
	
	/**
	 * Initialize the AudioPlayer object
	 * @param resource
	 * 			The URI to the Audio object
	 * @param start
	 * 			The time in the document when the media should start
	 * @param duration
	 * 			How long the media should play
	 * @param offsetInt
	 * 			The offset at which the audio should start playback
	 */
	public AudioPlayer(String resource, double start, double duration, double offsetInto)
	{
		this.resourceURL = resource;
		this.start = start;
		this.duration = duration;
		this.mOffsetInto = offsetInto;
	}

	/**
	 * Start playing the audio object in the document
	 */
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
	
	/**
	 * Pause the audio object in the document.
	 */
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
	
	/**
	 * Not implemented
	 */
	public void seekForward()
	{
		
	}
	
	/**
	 * Not implemented
	 */
	public void seekBackward()
	{
		
	}
	
	/**
	 * Does nothing... encforced method.
	 */
	public void render()
	{
		//do nothing... as we should
	}
	
	/**
	 * Release the mediaPlayer object that is being used to wrap the audio
	 * object.
	 */
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
	
	/**
	 * Prepare and the audio object. Start stream and set necessary callbacks.
	 */
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

	/**
	 * Callback for when the media is prepared.
	 */
	@Override
	public void onPrepared(MediaPlayer mp) {
		this.mMediaPlayer.seekTo((int)(this.mOffsetInto * 100));
		this.subject.notifyDoneBuffering();
		Log.e("AUDIO", "I'm done buffering and ready to play!");
	}
	
	/**
	 * Reset the media for another playback. 
	 */
	@Override
	public void reset()
	{
		this.prepare();
		super.reset();
	}
	
}
