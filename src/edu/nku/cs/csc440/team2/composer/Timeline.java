package edu.nku.cs.csc440.team2.composer;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.drawable.Drawable;

/**
 * A Timeline is a Drawable that contains tick marks and numerals that measure
 * time across the x-axis.
 * 
 * @author William Knauer <knauerw1@nku.edu>
 * @version 2011.0321
 */
public class Timeline extends Drawable
{
	public static final int HEIGHT = 20;
	private final double mLength;
	private Paint bgPaint;
	private Paint tickPaint;
	private Paint numPaint;
	
	/**
	 * Class constructor.
	 * 
	 * @param length The total length of the message.
	 */
	public Timeline(double length)
	{
		mLength = length;
		
		/* Initialize Paints */
		bgPaint = new Paint();
		bgPaint.setAntiAlias(true);
		bgPaint.setColor(Color.argb(255, 247, 233, 142)); // flavescent
		
		tickPaint = new Paint();
		tickPaint.setAntiAlias(true);
		tickPaint.setColor(Color.BLACK);
		tickPaint.setStrokeWidth(1.0f);
		
		numPaint = new Paint();
		numPaint.setAntiAlias(true);
		numPaint.setTextSize(16.0f);
		numPaint.setTextAlign(Align.CENTER);
		numPaint.setColor(Color.BLACK);
	}
	
	@Override
	public void draw(Canvas canvas)
	{
		/* Draw background */
		canvas.drawRect(getBounds(), bgPaint);
		
		/* Draw markings */
		for (int i = 0; i < mLength * 10; i ++)
		{
			switch (i % 10)
			{
			case 0:
				drawSecond(i, canvas);
				break;
			case 1:
				drawTenth(i, canvas);
				break;
			case 2:
				drawTenth(i, canvas);
				break;
			case 3:
				drawTenth(i, canvas);
				break;
			case 4:
				drawTenth(i, canvas);
				break;
			case 5:
				drawHalf(i, canvas);
				break;
			case 6:
				drawTenth(i, canvas);
				break;
			case 7:
				drawTenth(i, canvas);
				break;
			case 8:
				drawTenth(i, canvas);
				break;
			case 9:
				drawTenth(i, canvas);
			}
		}
	}
	
	/**
	 * Draws an indicator at a tenth-second position.
	 * 
	 * @param i The number of the tenth we are drawing at.
	 * @param canvas The canvas to draw on.
	 */
	private void drawTenth(int i, Canvas canvas)
	{
		canvas.drawLine(
				ComposerView.secToPx(i / 10.0),
				getBounds().bottom,
				ComposerView.secToPx(i / 10.0),
				getBounds().top + getBounds().height() * 0.667f,
				tickPaint);
	}
	
	/**
	 * Draws an indicator at a half-second position.
	 * 
	 * @param i The number of the half we are drawing at.
	 * @param canvas The canvas to draw on.
	 */
	private void drawHalf(int i, Canvas canvas)
	{
		canvas.drawLine(
				ComposerView.secToPx(i / 10.0),
				getBounds().bottom,
				ComposerView.secToPx(i / 10.0),
				getBounds().top + getBounds().height() * 0.500f,
				tickPaint);
	}
	
	/**
	 * Draws an indicator at a second position.
	 * 
	 * @param i The number of the seconds we are drawing at.
	 * @param canvas The canvas to draw on.
	 */
	private void drawSecond(int i, Canvas canvas)
	{
		canvas.drawText(
				String.valueOf(i / 10),
				ComposerView.secToPx(i / 10.0),
				getBounds().bottom - numPaint.getFontMetrics().bottom,
				numPaint);
		canvas.drawLine(
				ComposerView.secToPx(i / 10.0),
				getBounds().bottom,
				ComposerView.secToPx(i / 10.0),
				getBounds().top + getBounds().height() * 0.850f,
				tickPaint);
	}

	@Override
	public int getOpacity()
	{
		// Unimplemented for now.
		return 0;
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
