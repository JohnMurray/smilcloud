package edu.nku.cs.csc440.team2.player;

import java.util.ArrayList;
import java.util.Stack;

public class Arbiter 
{

	private ArrayList<Player> subscribers;
	private Player rootSeq;
	private Stack<Boolean> waitForBuffer;
	
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
	
	private void pauseAllForBuffer()
	{
		this.rootSeq.pause();
	}
	
	public void notifyBuffering()
	{
		if( this.waitForBuffer.empty() )
		{
			this.pauseAllForBuffer();
		}
		this.waitForBuffer.push(true);
	}
	
	public void notifyDoneBuffering()
	{
		this.waitForBuffer.pop();
		if( this.waitForBuffer.empty() )
		{
			this.playAll();
		}
	}
	
}
