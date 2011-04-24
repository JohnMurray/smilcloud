package edu.nku.cs.csc440.team2.player;

import java.util.ArrayList;
import java.util.Stack;

public class Arbiter 
{

	private ArrayList<Player> subscribers = new ArrayList<Player>();
	private SeqPlayer rootSeq;
	private Stack<Boolean> waitForBuffer = new Stack<Boolean>();
	private double totalPlaybackTime = 0.0;
	public boolean paused;
	
	public void register(Player p)
	{
		this.subscribers.add(p);
	}
	
	public void deRegister(Player p)
	{
		this.subscribers.remove(p);
	}
	
	
	public void playAll()
	{
		this.rootSeq.play();
	}
	
	public void notifyBuffering() 
	{
		this.waitForBuffer.push(true);
	}
	
	public void notifyDoneBuffering()
	{
		if( ! this.waitForBuffer.empty() )
		{
			this.waitForBuffer.pop();
		}
	}
	
	public boolean isBufferQueueEmpty()
	{
		return this.waitForBuffer.empty();
	}

	public void setRootSeq(SeqPlayer seq)
	{
		this.rootSeq = seq;
	}
	
	public void incrementTotalPlaybackTime()
	{
		this.totalPlaybackTime  += Player.PLAYBACK_INTERVAL;
	}
	
	public double getTotalPlaybackTime()
	{
		return this.totalPlaybackTime;
	}
	
	public void reset()
	{
		this.totalPlaybackTime = 0.0;
	}
}
