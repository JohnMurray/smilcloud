package edu.nku.cs.csc440.team2.player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import android.view.View;
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
	
	private Message message;
	private ArrayList<View> regions = new ArrayList<View>();
	private SeqPlayer root;

	/**
	 * instantiate the translator with the messenger classes
	 */
	public PriceLine(Message m)
	{
		this.message = m;
	}
	
	/**
	 * Internally translate the message into the SMIL representation
	 * that is going to be used by the Player classes
	 */
	public void negotiateBigDeal()
	{
		HashMap regions = new HashMap(20, .8f);
		LinkedList<Body> body = this.message.getBody();
		
		/*
		 * Assuming that the Body contains every element and we
		 * need to explicitly find everything that is not a par
		 * or sequence object
		 */
		for( Body b : body )
		{
			if( b instanceof Sequence || b instanceof Parallel )
			{
				
			}
			else
			{
				
			}
		}
	}
	
	
	public ArrayList<View> getRegionsAndAGoodDeal()
	{
		return this.regions;
	}
	
	
	public Player getDocumentAndNameYourOwnPrice()
	{
		return this.root;
	}
	
	
}
