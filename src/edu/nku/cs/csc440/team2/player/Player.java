package edu.nku.cs.csc440.team2.player;

public abstract class Player {
	
	private Arbiter subject;
	private boolean paused;
	private int timePlayed = 0;
	

	public abstract void play();
	
	public abstract void stop();
	
	public abstract void pause();
	
	public abstract void seekForward();
	
	public abstract void seekBackward();
	
	public abstract int getDuration();
	
	public abstract boolean isPaused();
	
}
