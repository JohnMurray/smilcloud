package edu.nku.cs.csc440.team2.player;

/**
 * 
 * @author john
 *
 * Represents and abstract Player object that has the ability
 * to render itself on the canvas (by getting it's layout from
 * the internal layout object) and control is own playback.
 * The playback commands will be issued to the Player objects
 * from Player container objects such as ParPlayer and
 * SeqPlayer.
 */
public abstract class Player {
	
	public static final float PLAYBACK_INTERVAL = 0.1f;
	
	/*
	 * Used in the subscriber pattern to receive notifications/
	 * commands from in case of a buffer issued by either an
	 * audio or media player object.
	 */
	protected Arbiter subject;
	
	/* 
	 * Boolean to tell if the object is currently paused
	 */
	protected boolean paused;
	
	/*
	 * Determines the start time for the object in a double
	 * type. However, the precision of the time should not
	 * exceed 1 tenth. The time is represented in seconds.
	 */
	protected double start;
	
	/*
	 * Determines how long the media object should remain on
	 * the canvas 
	 */
	protected double duration;
	
	/*
	 * Determines how much of the media object has been played.
	 * Can be used along with duration to determine when the
	 * media object should be removed from the canvas. 
	 */
	protected double timePlayed = 0;
	
	

	/**
	 * Plays the media object. Simple enough. Each media object
	 * implements a slightly different algorithm for playing. 
	 */
	public abstract void play();
	
	/**
	 * Pauses the media object. Playing from this point will cause
	 * the media object to continue at the point from which it
	 * was paused. 
	 */
	public abstract void pause();
	
	/**
	 * Seeks the video forward a set period of time; Fast-Forward.
	 */
	public abstract void seekForward();
	
	/**
	 * Seeks the video backward a set period of time; Rewind.
	 */
	public abstract void seekBackward();
	
	/**
	 * 
	 * @return double
	 * 
	 * Return the total time the video should be played
	 */
	public double getDuration()
	{
		return this.duration;
	}
	
	
	/**
	 * 
	 * @return double
	 * 
	 * Return the total time that has been played. Should always
	 * be less than the duration of the object when playing.
	 */
	public double getTimePlayed()
	{
		return this.timePlayed;
	}
	
	/**
	 * 
	 * @return boolean
	 * 
	 * Determine if the video is paused or not via the boolean
	 * flag that is set in the object.
	 */
	public boolean isPaused()
	{
		return this.paused;
	}
	
	/**
	 * Draw the media object to the screen.
	 */
	protected abstract void render();
	
	/**
	 * Remove the media object from the screen.
	 */
	protected abstract void unRender();
	
}
