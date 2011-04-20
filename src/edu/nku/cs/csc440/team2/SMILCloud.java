package edu.nku.cs.csc440.team2;

import java.util.ArrayList;

import edu.nku.cs.csc440.team2.composer.Box;
import edu.nku.cs.csc440.team2.composer.TrackManager;
import edu.nku.cs.csc440.team2.mediaCloud.MessageLite;

import android.app.Application;

public class SMILCloud extends Application 
{
  private ArrayList<MessageLite> messages;
  
  private TrackManager trackManager;
  private Box selectedBox;
  
  private String queuedDocumentToPlayID;
  private String queueDocumentToEditID;
	
  	public void setTrackManager(TrackManager t) {
  		this.trackManager = t;
	}
  	public TrackManager getTrackManager() {
  		TrackManager t = this.trackManager;
  		this.trackManager = null; // make sure we crash if I screwed up IPC
  		return t;
  	}
  	
  	public void setSelectedBox(Box b) {
  		this.selectedBox = b;
  	}
  	public Box getSelectedBox() {
  		Box b = this.selectedBox;
  		this.selectedBox = null; // make sure we crash if I screwed up IPC
  		return b;
  	}
  
	public void queueDocumentToPlay(String id) { this.queuedDocumentToPlayID = id; }
	public String getQueuedDocumentForPlayback() { return this.queuedDocumentToPlayID; }
	
	public void queueDocumentToEdit(String id) { this.queueDocumentToEditID = id; }
	public String getQueuedDocumentForEditing() { return this.queueDocumentToEditID; }

  public synchronized void addMessage(MessageLite message)
  {
    this.messages.add(message);
  }

  public synchronized ArrayList<MessageLite> getMessages()
  {
    return this.messages;
  }
}
