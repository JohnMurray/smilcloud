package edu.nku.cs.csc440.team2.player;

public class SeqPlayer extends ContainerPlayer
{
	
	public void play()
	{
		for( Player p : this.components )
		{
			if( p.getTimePlayed() < p.getDuration() )
			{
				p.play();
				break;
			}
			else if( p.isPlaying && p.getTimePlayed() == p.getDuration() )
			{
				if( p instanceof SingleInstancePlayer )
				{
					((SingleInstancePlayer) p).unRender();
				}
				p.isPlaying = false;
			}
		}
		this.timePlayed += Player.PLAYBACK_INTERVAL;
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
