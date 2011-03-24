package edu.nku.cs.csc440.team2.player;

import java.util.ArrayList;

public abstract class ContainerPlayer extends Player {

	protected ArrayList<Player> components = new ArrayList<Player>();
	
	@Override
	public void pause() 
	{
		for( Player p : this.components )
		{
			p.pause();
		}
	}


	public double getDuration()
	{
		double sum = 0;
		for( Player p : this.components )
		{
			sum += p.getDuration();
		}
		return sum;
	}
	
	public double getTimePlayed()
	{
		double sum = 0;
		for( Player p : this.components )
		{
			sum += p.getTimePlayed();
		}
		return sum;
	}
	
	/**
	 * Do not need to define a render for ContainerPlayer classes
	 */
	protected void render() {}
	/**
	 * Do not need to define a unRender for ContainerPlayer classes
	 */
	protected void unRender() {}

}
