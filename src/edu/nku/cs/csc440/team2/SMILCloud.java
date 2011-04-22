package edu.nku.cs.csc440.team2;

import java.util.ArrayList;

import android.app.Application;
import edu.nku.cs.csc440.team2.composer.Box;
import edu.nku.cs.csc440.team2.composer.TrackManager;
import edu.nku.cs.csc440.team2.mediaCloud.MessageLite;

public class SMILCloud extends Application {
	
	public final static int NO_USER = -1;

	private ArrayList<MessageLite> messages;
	private TrackManager trackManager;
	private Box selectedBox;
	private String queuedDocumentToPlayID = null;
	private String queueDocumentToEditID = null;
	private int userID = -1;

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

	public void queueDocumentToPlay(String id) {
		this.queuedDocumentToPlayID = id;
	}

	public String getQueuedDocumentForPlayback() {
		return this.queuedDocumentToPlayID;
	}

	public void queueDocumentToEdit(String id) {
		this.queueDocumentToEditID = id;
	}

	public String getQueuedDocumentForEditing() {
		return this.queueDocumentToEditID;
	}

	public synchronized void addMessage(MessageLite message) {
		this.messages.add(message);
	}

	public synchronized ArrayList<MessageLite> getMessages() {
		return this.messages;
	}

	public synchronized void setUserId(int userID) {
		this.userID = userID;
	}
<<<<<<< HEAD
	
	public synchronized int getUserId()
	{
=======

	public synchronized int getUserId() {
>>>>>>> 97d71cd24de78c0d86b371503ce89b448382cea1
		return this.userID;
	}
}
