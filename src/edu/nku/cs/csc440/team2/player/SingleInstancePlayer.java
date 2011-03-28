package edu.nku.cs.csc440.team2.player;

import android.widget.RelativeLayout;

public abstract class SingleInstancePlayer extends Player 
{

	/*
	 * The layout object from which the specified SMILRegion can
	 * be obtained from. 
	 */
	protected RelativeLayout layout;
	
	/*
	 * Flag to determine (quickly) if playback has started without
	 * having to calculate time and what not.
	 */
	protected boolean hasStartedPlayback;
	
	/*
	 * URL to the resource that will be loaded during the Media
	 * object's preparation. This will result in a call to the 
	 * provider services. 
	 */
	protected String resourceURL;
	
	/*
	 * Increments the time that the media object has been playing 
	 * based on a constant value defined in the Player class.
	 */
	protected void incrementPlaybackTime()
	{
		this.timePlayed += Player.PLAYBACK_INTERVAL;
	}
	
	protected void bindArbiter(Arbiter a)
	{
		this.subject = a;
	}
	
	protected void bindView(RelativeLayout rl)
	{
		this.layout = rl;
	}

}
