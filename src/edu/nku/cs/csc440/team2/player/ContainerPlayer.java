package edu.nku.cs.csc440.team2.player;

import java.util.ArrayList;

public abstract class ContainerPlayer extends Player {

	protected ArrayList<Player> components = new ArrayList<Player>();
	private boolean usingDurationCache = false;
	
	@Override
	public void pause() 
	{
		for( Player p : this.components )
		{
			p.pause();
		}
	}

	
	public void addComponent(Player p)
	{
		this.components.add(p);
	}
	
	public ArrayList<Player> getComponents()
	{
		return this.components;
	}

	public double getDuration()
	{
		if( ! this.usingDurationCache )
		{
			double sum = 0;
			for( Player p : this.components )
			{
				sum += p.getDuration();
			}
			this.duration = sum;
			this.usingDurationCache = true;
			return sum;
		}
		else
		{
			return this.duration;
		}
	}
	
	public double getTimePlayed()
	{
		return this.timePlayed;
	}
	
	
	public void prepare()
	{
		for( Player p : this.components )
		{
			p.prepare();
		}
	}
	
	public void unRenderAll()
	{
		for( Player p : this.components )
		{
			if( p instanceof ContainerPlayer )
			{
				((ContainerPlayer) p).unRenderAll();
			}
			else if( p instanceof SingleInstancePlayer )
			{
				if( p.isPlaying ) { p.pause(); }
				((SingleInstancePlayer) p).unRender();
			}
		}
	}

}
