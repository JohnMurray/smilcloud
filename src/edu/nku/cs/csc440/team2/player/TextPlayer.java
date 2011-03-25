package edu.nku.cs.csc440.team2.player;

public class TextPlayer extends SingleInstancePlayer 
{
	private String text;
	private String resourceURL;
	
	public TextPlayer(String text, String resource)
	{
		this.text = text;
		this.resourceURL = resource;
	}

	public void play()
	{
		
	}
	
	public void pause()
	{
		
	}
	
	public void seekForward()
	{
		
	}
	
	public void seekBackward()
	{
		
	}
	
	public void render()
	{
		
	}
	
	public void unRender()
	{
		
	}
	
	public void prepare()
	{
		if( this.text == null )
		{
			//retrieve the text from the Provider
		}
	}
	
}
