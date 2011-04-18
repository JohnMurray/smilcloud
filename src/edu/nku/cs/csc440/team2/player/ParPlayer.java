package edu.nku.cs.csc440.team2.player;


public class ParPlayer extends ContainerPlayer
{
	
	public ParPlayer(double start, double end)
	{
		this.start = start;
		this.duration = end - start;
	}
	
	public void play()
	{
		for( Player p : this.components )
		{
			if( p.getTimePlayed() >= p.getDuration() )
			{
				if( p.isPlaying )
				{
					if( p instanceof SingleInstancePlayer )
					{
						((SingleInstancePlayer) p).unRender();
					}
					p.isPlaying = false;
				}
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
		if( this.duration == 0 )
		{
			double lowestStartTime = Integer.MAX_VALUE;
			double highestEndTime = 0;
			for( Player p : this.components )
			{
				//get lowest start time
				if( p.start < lowestStartTime )
					lowestStartTime = p.start;
				//get highest end time (not duration... END TIME!)
				if( p.start + p.duration > highestEndTime )
					highestEndTime = p.start + p.duration;
			}
			this.duration = highestEndTime - lowestStartTime;
		}
		return this.duration;
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