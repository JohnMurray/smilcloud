package edu.nku.cs.csc440.team2.composer;

import java.util.Collections;
import java.util.LinkedList;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;

/**
 * @author William Knauer <knauerw1@nku.edu>
 * @version 2011.0321
 */
public class Track extends Drawable
{
	public static final int HEIGHT = Box.HEIGHT + (2 * Box.SPACING);
	private final double mLength;
	private LinkedList<Box> mBoxes;
	
	public Track(double length)
	{
		mBoxes = new LinkedList<Box>();
		mLength = length;
	}
	
	public boolean addBox(Box elt, double begin)
	{
		boolean added = false;
		if (fits(elt, begin))
		{
			elt.setBegin(begin);
			mBoxes.add(elt);
			Collections.sort(mBoxes);
			added = true;
		}
		return added;
	}
	
	public boolean addBox(Box elt, int targetX, int targetY)
	{
		boolean result = false;
		if (getBounds().contains(targetX, targetY))
		{
			double begin = ComposerView.pxToSec(targetX);
			begin = ComposerView.snapTo(begin);
			result = addBox(elt, begin);
		}
		return result;
	}
	
	public boolean contains(Box elt)
	{
		return mBoxes.contains(elt);
	}
	
	@Override
	public void draw(Canvas canvas)
	{
		/* Draw background */
		Paint bgFillPaint = new Paint();
		bgFillPaint.setColor(Color.DKGRAY);
		bgFillPaint.setAntiAlias(true);
		bgFillPaint.setStyle(Style.FILL);
		canvas.drawRect(getBounds(), bgFillPaint);
		
		/* Draw outline */
		Paint outlinePaint = new Paint();
		outlinePaint.setColor(Color.WHITE);
		outlinePaint.setAntiAlias(true);
		outlinePaint.setStyle(Style.STROKE);
		outlinePaint.setStrokeWidth(1.0f);
		canvas.drawRect(getBounds(), outlinePaint);
		
		/* Draw MediaBoxes */
		for (Box mb : mBoxes)
		{
			/* Place each MediaBox */
			mb.setBounds(
					ComposerView.secToPx(mb.getBegin()),
					getBounds().top + Box.SPACING,
					ComposerView.secToPx(mb.getEnd()),
					getBounds().top + Box.SPACING + Box.HEIGHT);
			/* Draw each MediaBox */
			mb.draw(canvas);
		}
	}

	public boolean fits(Box elt, double begin)
	{
		boolean fits = true;
		double end = begin + elt.getDuration();
		
		/* If the MediaBox fits within the bounds of the Track */
		if (begin < 0 || end > mLength)
		{
			fits = false;
		}
		
		/* Check each MediaBox in this Track */
		for (Box mb : mBoxes)
		{
			/* If elt's begin overlaps with that MediaBox */
			if (mb.getBegin() <= begin && begin < mb.getEnd()) 
			{
				fits = false;
			}
			
			/* If elt's end overlaps with that MediaBox */
			if (mb.getBegin() < end && end <= mb.getEnd())
			{
				fits = false;
			}
		}
		return fits;
	}
	
	public Box getBox(double time)
	{
		Box result = null;
		for (Box m : mBoxes)
		{
			if (m.containsTime(time))
			{
				result = m;
			}
		}
		return result;
	}
	
	public Box getBox(int targetX, int targetY)
	{
		Box result = null;
		if (getBounds().contains(targetX, targetY))
		{
			double time = ComposerView.pxToSec(targetX);
			result = getBox(time);
		}
		return result;
	}
	
	public LinkedList<Box> getBoxes()
	{
		return new LinkedList<Box>(mBoxes);
	}

	@Override
	public int getOpacity()
	{
		// Unimplemented for now.
		return 0;
	}
	
	public boolean isEmpty()
	{
		return mBoxes.isEmpty();
	}
	
	public Box removeBox(int targetX, int targetY)
	{
		Box result = null;
		if (getBounds().contains(targetX, targetY))
		{
			double time = ComposerView.pxToSec(targetX);
			result = removeBox(time);
		}
		return result;
	}
	
	public Box removeBox(double time)
	{
		Box result = getBox(time);
		if (result != null)
		{
			mBoxes.remove(result);
		}
		return result;
	}
	
	@Override
	public void setAlpha(int alpha)
	{
		// Unimplemented for now.
	}

	@Override
	public void setColorFilter(ColorFilter cf)
	{
		// Unimplemented for now.
	}

}
