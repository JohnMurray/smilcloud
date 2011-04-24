package edu.nku.cs.csc440.team2.player;

import android.util.Log;

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
			else if( p.isPlaying && p.getTimePlayed() >= p.getDuration() )
			{
				Log.w("SeqPlayer", "player should be done playing");
				if( p instanceof SingleInstancePlayer )
				{
					((SingleInstancePlayer) p).unRender();
				}
				p.isPlaying = false;
			}
		}
		this.timePlayed += Player.PLAYBACK_INTERVAL;
	}
	
	//not implemented at the moment (perhaps in version 2)
	public void seekForward(){}
	public void seekBackward(){}
	
}
