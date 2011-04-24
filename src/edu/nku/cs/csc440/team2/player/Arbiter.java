package edu.nku.cs.csc440.team2.player;

import java.util.ArrayList;
import java.util.Stack;

/**
 * Acts as a general notifier. Was intented to work in the sense of the
 * Subscriber pattern. Ended up being used more like a shared data- structure.
 * 
 * @author John Murray
 * @version 1.0 4/24/11
 */
public class Arbiter {

	/**
	 * contains a list of objects that have registered with the Arbiter
	 */
	private ArrayList<Player> subscribers = new ArrayList<Player>();
	/**
	 * Contains the root object of the Player. This is what all calls will
	 * cascade through.
	 */
	private SeqPlayer rootSeq;
	/**
	 * The stack of all objects that are currently buffering.
	 */
	private Stack<Boolean> waitForBuffer = new Stack<Boolean>();
	/**
	 * A counter for the progress that the document has played back so far.
	 */
	private double totalPlaybackTime = 0.0;
	/**
	 * Boolean to tell if the document is paused (or if a Player object should
	 * be pausing).
	 */
	public boolean paused;

	/**
	 * Subscribe to the Arbiter.
	 * 
	 * @param p
	 *            Player to add to the "subscription"
	 */
	public void register(Player p) {
		this.subscribers.add(p);
	}

	/**
	 * Unsubscribe from the Arbiter.
	 * 
	 * @param p
	 *            The player to unsubscribe
	 */
	public void deRegister(Player p) {
		this.subscribers.remove(p);
	}

	/**
	 * Call play on the root element, should cascade down the list of Player
	 * objects
	 */
	public void playAll() {
		this.rootSeq.play();
	}

	/**
	 * Let the arbiter know that you are buffering so that the other Player
	 * objects know what they should be doing.
	 * 
	 * @note Implementation of what the other Player objects should do at this
	 *       time is independend of the Arbiter.
	 */
	public void notifyBuffering() {
		this.waitForBuffer.push(true);
	}

	/**
	 * Notify the Arbiter that you are done buffering so that all other
	 * subscribers can know what to do at this time.
	 */
	public void notifyDoneBuffering() {
		if (!this.waitForBuffer.empty()) {
			this.waitForBuffer.pop();
		}
	}

	/**
	 * Check to see if the buffer queue is empty.
	 * 
	 * @return boolean to see if it is empty
	 */
	public boolean isBufferQueueEmpty() {
		return this.waitForBuffer.empty();
	}

	/**
	 * Set the root Player object for the Arbiter
	 * 
	 * @param seq
	 */
	public void setRootSeq(SeqPlayer seq) {
		this.rootSeq = seq;
	}

	/**
	 * Increment the playback time by the increment rate.
	 */
	public void incrementTotalPlaybackTime() {
		this.totalPlaybackTime += Player.PLAYBACK_INTERVAL;
	}

	/**
	 * Get the total amount of time that the document has played so far
	 * 
	 * @return The current progress of the document playback
	 */
	public double getTotalPlaybackTime() {
		return this.totalPlaybackTime;
	}

	/**
	 * reset the playback time to 0. The subject holds not other state that can
	 * be reset to pre-playback.
	 */
	public void reset() {
		this.totalPlaybackTime = 0.0;
	}
}
