package edu.nku.cs.csc440.team2.composer;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

/**
 * @author William Knauer <knauerw1@nku.edu>
 * @version 2011.0321
 */
public abstract class Box extends Drawable
		implements Comparable<Box>
{
	public static final int HEIGHT = 40;
	public static final int SPACING = 7;
	public static final int TEXT_OFFSET = 5;
	
	private double mBegin;
	private double mDuration;
	private final String mSource;
	
	public Box(String source, double begin, double duration)
	{
		super();
		super.setBounds(0, 0, ComposerView.secToPx(duration), HEIGHT);
		mSource = source;
		mBegin = begin;
		mDuration = duration;
	}
	
	public int compareTo(Box another)
	{
		int result;
		if (getBegin() < another.getBegin())
		{
			result = -1;
		}
		else if (getBegin() == another.getBegin())
		{
			result = 0;
		}
		else
		{
			result = 1;
		}
		return result;
	}
	
	public boolean containsTime(double time)
	{
		boolean contains = false;
		if (getBegin() >= time && time < getEnd())
		{
			contains = true;
		}
		return contains;
	}
	
	public abstract void drawOutline(Canvas canvas);
	
	public double getBegin()
	{
		return mBegin;
	}
	
	public double getDuration()
	{
		return mDuration;
	}
	
	public double getEnd()
	{
		return mBegin + mDuration;
	}
	
	public String getSource()
	{
		return mSource;
	}
	
	public void setBegin(double begin)
	{
		mBegin = begin;
	}
	
	public void setDuration(double duration)
	{
		mDuration = duration;
	}
}
