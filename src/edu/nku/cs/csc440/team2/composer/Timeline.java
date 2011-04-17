package edu.nku.cs.csc440.team2.composer;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;

/**
 * A Timeline is a Drawable that contains tick marks and numerals that measure
 * time across the x-axis.
 * 
 * @author William Knauer <knauerw1@nku.edu>
 * @version 2011.0321
 */
public class Timeline {
	public static final int HEIGHT = 20;
	private Rect mBounds;
	private int mBgColor;
	private int mFgColor;

	/**
	 * Class constructor.
	 * 
	 * @param length
	 *            The total length of the message.
	 */
	public Timeline(int bgColor, int fgColor) {
		mBounds = new Rect();
		mBgColor = bgColor;
		mFgColor = fgColor;
	}

	public void draw(Canvas canvas) {
		Paint p = new Paint();
		
		/* Draw background */
		p.setAntiAlias(true);
		p.setColor(mBgColor);
		canvas.drawRect(getBounds(), p);

		/* Draw markings */
		p.setColor(mFgColor);
		p.setTextSize(16.0f);
		p.setTextAlign(Align.CENTER);
		double length = Composer.pxToSec(mBounds.width());
		for (int i = 0; i < length * 10; i++) {
			switch (i % 10) {
			case 0:
				drawSecond(i, canvas, p);
				break;
			case 1:
				drawTenth(i, canvas, p);
				break;
			case 2:
				drawTenth(i, canvas, p);
				break;
			case 3:
				drawTenth(i, canvas, p);
				break;
			case 4:
				drawTenth(i, canvas, p);
				break;
			case 5:
				drawHalf(i, canvas, p);
				break;
			case 6:
				drawTenth(i, canvas, p);
				break;
			case 7:
				drawTenth(i, canvas, p);
				break;
			case 8:
				drawTenth(i, canvas, p);
				break;
			case 9:
				drawTenth(i, canvas, p);
			}
		}
	}

	/**
	 * Draws an indicator at a half-second position.
	 * 
	 * @param i
	 *            The number of the half we are drawing at.
	 * @param canvas
	 *            The canvas to draw on.
	 */
	private void drawHalf(int i, Canvas canvas, Paint p) {
		canvas.drawLine(Composer.secToPx(i / 10.0), getBounds().bottom,
				Composer.secToPx(i / 10.0), getBounds().top
						+ getBounds().height() * 0.500f, p);
	}

	/**
	 * Draws an indicator at a second position.
	 * 
	 * @param i
	 *            The number of the seconds we are drawing at.
	 * @param canvas
	 *            The canvas to draw on.
	 */
	private void drawSecond(int i, Canvas canvas, Paint p) {
		canvas.drawText(String.valueOf(i / 10), Composer.secToPx(i / 10.0),
				getBounds().bottom - p.getFontMetrics().bottom, p);
		canvas.drawLine(Composer.secToPx(i / 10.0), getBounds().bottom,
				Composer.secToPx(i / 10.0), getBounds().top
						+ getBounds().height() * 0.850f, p);
	}

	/**
	 * Draws an indicator at a tenth-second position.
	 * 
	 * @param i
	 *            The number of the tenth we are drawing at.
	 * @param canvas
	 *            The canvas to draw on.
	 */
	private void drawTenth(int i, Canvas canvas, Paint p) {
		canvas.drawLine(Composer.secToPx(i / 10.0), getBounds().bottom,
				Composer.secToPx(i / 10.0), getBounds().top
						+ getBounds().height() * 0.667f, p);
	}

	public Rect getBounds() {
		return mBounds;
	}

	public void setBounds(int left, int top, int right, int bottom) {
		mBounds.set(left, top, right, bottom);
	}

}
