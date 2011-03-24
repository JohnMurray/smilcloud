package edu.nku.cs.csc440.team2.player;

import java.util.ArrayList;

public class ParPlayer extends ContainerPlayer implements PlayerContainer 
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
	
	public void seekForward()
	{
		
	}
	
	public void seekBackward()
	{
		
	}
	
}
