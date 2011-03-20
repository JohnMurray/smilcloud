package edu.nku.cs.csc440.team2.player;

import android.view.ViewGroup;



public class SMILRegion 
{

	private String id;
	private ViewGroup view;
	
	//I don't think we need any of the private instance data below this comment
	//as it should be defined in the view
	private int height;
	private int width;
	private String backgroundColor;
	private int left;
	private int top;
	private int zIndex;
	private String fit; //not really sure what this one is for (don't remember)
	
	
	public SMILRegion()
	{
		
	}
	
	public ViewGroup getViewGroup()
	{
		return view;
	}
	
}
