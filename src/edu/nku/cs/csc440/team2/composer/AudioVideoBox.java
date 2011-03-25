package edu.nku.cs.csc440.team2.composer;

/**
 * @author William Knauer <knauerw1@nku.edu>
 * @version 2011.0321
 */
public abstract class AudioVideoBox extends Box
{
	private double mClipBegin;
	private double mClipDuration;
	
	public AudioVideoBox(String source, double begin,
			double duration, double clipDuration)
	{
		super(source, begin, duration);
		mClipBegin = 0.0;
		mClipDuration = clipDuration;
	}
	
	public double getClipBegin()
	{
		return mClipBegin;
	}
	
	public double getClipDuration()
	{
		return mClipDuration;
	}
	
	public double getClipEnd()
	{
		return mClipBegin + mClipDuration;
	}
	
	public void setClipBegin(double clipBegin)
	{
		mClipBegin = clipBegin;
	}
}
