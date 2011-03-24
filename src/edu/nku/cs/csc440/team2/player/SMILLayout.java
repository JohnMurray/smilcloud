package edu.nku.cs.csc440.team2.player;

import java.util.ArrayList;

import android.view.View;

public class SMILLayout 
{
	private ArrayList<View> regions;

	/**
	 * 
	 * @param regions
	 * 
	 * Given a list of Views, store them and act as a place to 
	 * retrieve View information out of. Just a shared object
	 * for reading View (region) information.
	 */
	public SMILLayout(ArrayList<View> regions)
	{
		this.regions = regions;
	}
	
	public View getViewById(int id)
	{
		return this.regions.get(id);
	}
	
	
}
