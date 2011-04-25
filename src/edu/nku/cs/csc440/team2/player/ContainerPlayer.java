package edu.nku.cs.csc440.team2.player;

import java.util.ArrayList;

/**
 * Abstract class implimented by ParPlayer and SeqPlayer. Contains methods for
 * working with a collection of Player objects.
 * 
 * @author John Murray
 * 
 */
public abstract class ContainerPlayer extends Player {

	/**
	 * Contains a list of all of the contained Players
	 */
	protected ArrayList<Player> components = new ArrayList<Player>();

	/**
	 * Flag to see if we have cached the duration yet or not
	 */
	private boolean usingDurationCache = false;

	/**
	 * Pause all of the components located in this container
	 */
	@Override
	public void pause() {
		for (Player p : this.components) {
			p.pause();
		}
	}

	/**
	 * Add a component to the container
	 * 
	 * @param p
	 *            Either a single instance or container player object
	 */
	public void addComponent(Player p) {
		this.components.add(p);
	}

	/**
	 * Retreive the list of Player objects in this container Player
	 * 
	 * @return ArrayList of player objects
	 */
	public ArrayList<Player> getComponents() {
		return this.components;
	}

	/**
	 * Get the duration of the container player either from a cache or generate
	 * teh duraction and cache the value so that future calls to this method
	 * return the cached value.
	 */
	public double getDuration() {
		if (!this.usingDurationCache) {
			double sum = 0;
			for (Player p : this.components) {
				sum += p.getDuration();
			}
			this.duration = sum;
			this.usingDurationCache = true;
			return sum;
		} else {
			return this.duration;
		}
	}

	/**
	 * Get the time that we have played in this container player so far.
	 */
	public double getTimePlayed() {
		return this.timePlayed;
	}

	/**
	 * Prepare all the components of the container player
	 */
	public void prepare() {
		for (Player p : this.components) {
			p.prepare();
		}
	}

	/**
	 * Unrender all of the components in the contaier player
	 */
	public void unRenderAll() {
		for (Player p : this.components) {
			if (p instanceof ContainerPlayer) {
				((ContainerPlayer) p).unRenderAll();
			} else if (p instanceof SingleInstancePlayer) {
				if (p.isPlaying) {
					p.pause();
				}
				((SingleInstancePlayer) p).unRender();
			}
		}
	}

	/**
	 * Reset each object in the container player object
	 */
	@Override
	public void reset() {
		for (Player p : this.components) {
			p.reset();
		}
		super.reset();
	}

}
