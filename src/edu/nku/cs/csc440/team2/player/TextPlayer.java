package edu.nku.cs.csc440.team2.player;

public class TextPlayer extends SingleInstancePlayer 
{
	private String text;
	
	public TextPlayer(String text, String resource, double start, double duration)
	{
		this.text = text;
		this.resourceURL = resource;
		this.start = start;
		this.duration = duration;
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
