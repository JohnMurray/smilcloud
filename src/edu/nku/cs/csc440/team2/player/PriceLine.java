package edu.nku.cs.csc440.team2.player;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedList;

import android.content.Context;
import android.widget.RelativeLayout;
import edu.nku.cs.csc440.team2.message.*;

/**
 * 
 * @author john
 * 
 * Negotiate between the classes of the messenger package and the 
 * classes of the player package. (of the edu.nku.cs.csc440.team2 
 * namespace)
 */
public class PriceLine 
{
	private Context context;
	private Message message;
	private Hashtable<String, RegionData> regions = new Hashtable<String, RegionData>(20, .75f);
	private SeqPlayer root;
	private ArrayList<RelativeLayout> layouts;

	/**
	 * instantiate the translator with the messenger classes
	 */
	public PriceLine(Context context, Message m)
	{
		this.context = context;
		this.message = m;
	}
	
	/**
	 * Internally translate the message into the SMIL representation
	 * that is going to be used by the Player classes
	 */
	public void negotiateBigDeal()
	{
		this.makePrivateDeal(this.message.getBody(), this.root);
		this.generateViewGroupsAndDemandACheaperRate();
		this.sortRootSequenceAndEatRawDealForBreakfast();
	}
	
	/**
	 * 
	 * @param body
	 * 
	 * Helper method for negotiateBigDeal method.
	 * 
	 * TODO: Refactor method to create decorated Player structure as well.
	 */
	private void makePrivateDeal(LinkedList<Body> body, PlayerContainer parent)
	{
		/*
		 * Assuming that the Body contains every element and we
		 * need to explicitly find everything that is not a par
		 * or sequence object
		 */
		for( Body b : body )
		{			
			/*
			 * If we are dealing with a Parallel or Sequence object,
			 * then we need to recursively call this function.
			 */
			if( b instanceof Parallel )
			{
				ParPlayer par = new ParPlayer();
				parent.addComponent(par);
				makePrivateDeal(((Parallel) b).getBody(), par);
			}
			else if( b instanceof Sequence )
			{
				SeqPlayer seq = new SeqPlayer();
				parent.addComponent(seq);
				makePrivateDeal(((Sequence) b).getBody(), seq);
			}
			else if( b instanceof Media )
			{
				/*
				 * Add region information from the SMIL Document. This will
				 * be used later to generate ViewGroup (Relative Layout)
				 * informaiton
				 */
				this.regions.put( b.getId(),
						(new RegionData( ((Media) b).getRegion().getZindex(), 
								((Media) b).getRegion().getDimensions(),
								((Media) b).getRegion().getOrigin())
						)
				);
				
				if( b instanceof Text )
				{
					/*
					 * Get everything we need from this object
					 */
					TextPlayer text = new TextPlayer(null, ((Text) b).getSrc());
					parent.addComponent(text);
				}
				else if( b instanceof Image )
				{
					/*
					 * Get everything we need from this object
					 */
					
				}
				else if( b instanceof Audio )
				{
					/*
					 * Get everything we need from this object
					 */
				}
				else if( b instanceof Video )
				{
					/*
					 * Get everything we need from this object
					 */
				}
			}
		}
	}
	
	/**
	 * Method to generate ViewGroups (RelativeLayouts) based on the data collected
	 * in MakePrivateDeal. Works with the Hashtable<String, RegionData> object.
	 */
	private void generateViewGroupsAndDemandACheaperRate()
	{
		this.layouts = new ArrayList<RelativeLayout>(this.regions.size());
		Enumeration<String> e = this.regions.keys();
		while( e.hasMoreElements() )
		{
			String id = e.nextElement();
			/*
			 * Obtain the layout information from the regionData and use it
			 * to build a RelativeLayout ViewGroup
			 */
			RegionData regionData = this.regions.get(id);
			SmilDimension smilDimension = regionData.dimension;
			SmilDimension origin = regionData.origin;
			
			RelativeLayout temp = new RelativeLayout(this.context);
			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
					smilDimension.getWidth(),
					smilDimension.getHeight());			
			lp.setMargins(origin.getWidth(), origin.getHeight(), 0, 0);
			temp.setLayoutParams(lp);
			
			
			/*
			 * Add the newly build ViewGroup to the Hashtable with the zIndex
			 * as the ID
			 * 
			 * TODO:determine if the z-index will work as a unique ID or not...
			 * 
			 * If it is not, then we will need a way to link the of the SMIL
			 * Region to the RelativeLayout (hashtable perhaps), otherwise we
			 * can just keep what we have. 
			 */
			layouts.add(regionData.zIndex, temp);
		}
	}
	
	/**
	 * The current structure for the root (implicit Sequence object) is sorted
	 * by the ID (String value) and I need to sort them by the playback start-
	 * time... so we need to do some sorting here. (and eat raw deals of course)
	 */
	public void sortRootSequenceAndEatRawDealForBreakfast()
	{
		//TODO: write code to sort root Sequence by playback time
	}
	
	
	public Hashtable<String, RegionData> getRegionsAndAGoodDeal()
	{
		return this.regions;
	}
	
	
	public Player getDocumentAndNameYourOwnPrice()
	{
		return this.root;
	}
	
	
	/**
	 * 
	 * @author john
	 *
	 * Define a struct-like class just to hold some data for
	 * a brief period of time.
	 */
	private class RegionData {
		public SmilDimension origin;
		public SmilDimension dimension;
		public int zIndex;
		public RegionData(int zIndex, SmilDimension dim, SmilDimension origin)
		{
			this.origin = origin;
			this.dimension = dim;
			this.zIndex = zIndex;
		}
	}
	
	
}
