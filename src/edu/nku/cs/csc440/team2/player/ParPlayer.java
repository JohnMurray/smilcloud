package edu.nku.cs.csc440.team2.player;


public class ParPlayer extends ContainerPlayer
{
	
	public void play()
	{
		for( Player p : this.components )
		{
			if( p.getTimePlayed() == p.getDuration() && p.isPlaying )
			{
				if( p instanceof SingleInstancePlayer )
				{
					((SingleInstancePlayer) p).unRender();
				}
				p.isPlaying = false;
			}
			else
			{
				p.play();
			}
		}
		this.timePlayed += Player.PLAYBACK_INTERVAL;
	}
	
	
	@Override
	public double getDuration()
	{
		double lowestStartTime = Integer.MAX_VALUE;
		double highestEndTime = 0;
		for( Player p : this.components )
		{
			//get lowest start time
			
			//get highest end time (not duration... END TIME!)
		}
		return lowestStartTime + highestEndTime;
	}
	
	//TODO: EXTRA -- implement seek forward sometime
	public void seekForward()
	{
		
	}
	
	//TODO: EXTRA -- implement seek backward sometime
	public void seekBackward()
	{
		
	}
	
}