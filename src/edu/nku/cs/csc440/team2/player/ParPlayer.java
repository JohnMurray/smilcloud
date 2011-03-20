package edu.nku.cs.csc440.team2.player;

import java.util.ArrayList;

public class ParPlayer extends Player implements PlayerContainer 
{
	private ArrayList<Player> components = new ArrayList<Player>();
	
	public void addComponent(Player p)
	{
		this.components.add(p);
	}
	
	public void play()
	{
		
	}
	
	public void pause()
	{
		
	}
	
	public void stop()
	{
		
	}
	
	public void seekForward()
	{
		
	}
	
	public void seekBackward()
	{
		
	}
	
	public int getDuration()
	{
		return 0;
	}
	
	public boolean isPaused()
	{
		return false;
	}
	
}
