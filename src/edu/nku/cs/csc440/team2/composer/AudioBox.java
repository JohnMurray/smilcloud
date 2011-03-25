package edu.nku.cs.csc440.team2.composer;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;

/**
 * @author William Knauer <knauerw1@nku.edu>
 * @version 2011.0321
 */
public class AudioBox extends AudioVideoBox {
	private static int sCount = 1;
	private final String mLabel;
	
	public AudioBox(String source, double begin, double duration,
			double clipDuration)
	{
		super(source, begin, duration, clipDuration);
		mLabel = "Audio " + sCount;
		sCount ++;
	}
	
	@Override
	public void draw(Canvas canvas)
	{
		/* Draw background */
		Paint bgFillPaint = new Paint();
		bgFillPaint.setColor(Color.argb(255, 139, 0, 204)); // deep purple
		bgFillPaint.setAntiAlias(true);
		bgFillPaint.setStyle(Style.FILL);
		canvas.drawRect(getBounds(), bgFillPaint);
		
		/* Draw label */
		Paint labelPaint = new Paint();
		labelPaint.setColor(Color.WHITE);
		labelPaint.setAntiAlias(true);
		labelPaint.setTextAlign(Align.LEFT);
		labelPaint.setTextSize(16.0f);
		canvas.drawText(mLabel, getBounds().left + TEXT_OFFSET,
				getBounds().top + (getBounds().height() / 2)
				+ labelPaint.getFontMetrics().descent, labelPaint);
		
		/* TODO Draw RHS grip */
		
		/* Draw border */
		drawOutline(canvas);
	}
	
	public void drawOutline(Canvas canvas)
	{
		/* Draw outline */
		Paint outlinePaint = new Paint();
		outlinePaint.setColor(Color.WHITE);
		outlinePaint.setAntiAlias(true);
		outlinePaint.setStyle(Style.STROKE);
		outlinePaint.setStrokeWidth(2.0f);
		canvas.drawRect(getBounds(), outlinePaint);
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
