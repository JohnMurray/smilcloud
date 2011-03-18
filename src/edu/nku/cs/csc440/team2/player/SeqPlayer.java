package edu.nku.cs.csc440.team2.player;

import java.util.ArrayList;

public class SeqPlayer extends Player implements PlayerContainer {
	private ArrayList<Player> components = new ArrayList<Player>();
	
	public void addComponent(Player p)
	{
		this.components.add(p);
	}
}
