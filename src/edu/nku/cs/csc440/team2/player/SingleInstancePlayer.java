package edu.nku.cs.csc440.team2.player;

import android.widget.RelativeLayout;

/**
 * Abstract player class for objects that do not have children elements. If this
 * were a tree, then you could think of SingleInstancePlayers as leaf nodes.
 * 
 * @author John Murray
 * @version 1.0 4/24/11
 * 
 */
public abstract class SingleInstancePlayer extends Player {

	/**
	 * The layout object from which the specified SMILRegion can be obtained
	 * from.
	 */
	protected RelativeLayout layout;
	/**
	 * The id fo the layout, used for retreiving the the layout if needed
	 */
	public String layoutId;

	/**
	 * URL to the resource that will be loaded during the Media object's
	 * preparation. This will result in a call to the provider services.
	 */
	protected String resourceURL;

	/**
	 * Increments the time that the media object has been playing based on a
	 * constant value defined in the Player class.
	 */
	protected void incrementPlaybackTime() {
		this.timePlayed += Player.PLAYBACK_INTERVAL;
	}

	/**
	 * Bind the layout to a view
	 */
	protected void bindView(RelativeLayout rl) {
		this.layout = rl;
	}

	/**
	 * Start/continue playing the media element and render it if it has not been
	 * rendered yet.
	 */
	@Override
	public void play() {
		// do nothing
		if (this.isPlaying) {
			this.incrementPlaybackTime();
			if (this.timePlayed + this.start > this.duration) {
				this.unRender();
			}
		} else {
			this.render();
			this.isPlaying = true;
			this.incrementPlaybackTime();
		}
	}

	/**
	 * Draw the media object to the screen.
	 */
	public abstract void render();

	/**
	 * Remove the media object from the screen.
	 */
	public abstract void unRender();

}
