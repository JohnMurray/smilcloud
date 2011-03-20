package edu.nku.cs.csc440.team2;

import android.app.Application;

public class SMILCloud extends Application 
{

	private String queuedDocumentToPlayID;
	
	public void queueDocumentToPlay(String id) { this.queuedDocumentToPlayID = id; }
	public String getQueuedDocumentForPlayback() { return this.queuedDocumentToPlayID; }
}
