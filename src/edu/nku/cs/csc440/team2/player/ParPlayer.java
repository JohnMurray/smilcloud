package edu.nku.cs.csc440.team2.player;

/**
 * Is a ContainerPlayer that allows the simultaneous playback of
 * multiple Player objects.
 * @author John Murray
 * @version 1.0 4/24/11
 */
public class ParPlayer extends ContainerPlayer
{
	
	/**
	 * Generate a new ParPlayer object. 
	 * @param start
	 * 			The start time for the par player
	 * @param end
	 * 			The end time for the par player
	 */
	public ParPlayer(double start, double end)
	{
		this.start = start;
		this.duration = end - start;
	}
	
	/**
	 * Play each component that should be playing at this particular
	 * instance. Also unrender any objects that are done playing.
	 */
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
				if( this.subject.getTotalPlaybackTime() >= p.start )
				{
					p.play();
				}
			}
		}
		this.timePlayed += Player.PLAYBACK_INTERVAL;
	}
	
	/**
	 * Get the duraction of all of the components and cache them. Return
	 * the cache if one has already been generated.
	 */
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
	
	/**
	 * not implemented at the moment (perhaps in version 2)
	 */
	public void seekForward(){}
	/**
	 * not implemented at the moment (perhaps in version 2)
	 */
	public void seekBackward(){}
	
}