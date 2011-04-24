package edu.nku.cs.csc440.team2;

import java.util.ArrayList;

import android.app.Application;
import edu.nku.cs.csc440.team2.composer.Box;
import edu.nku.cs.csc440.team2.composer.TrackManager;
import edu.nku.cs.csc440.team2.mediaCloud.MessageLite;

public class SMILCloud extends Application {
	
	public final static int NO_USER = -1;

	private ArrayList<MessageLite> messages;
	private String previewMessageId;
	private String queuedDocumentToPlayID = null;
	private String queueDocumentToEditID = null;
	private Box selectedBox;
	private String shareMessageID = null;
	private TrackManager trackManager;
	private int userID = NO_USER;

	public synchronized void setTrackManager(TrackManager t) {
		this.trackManager = t;
	}

	public synchronized TrackManager getTrackManager() {
		TrackManager t = this.trackManager;
		this.trackManager = null;
		return t;
	}

	public synchronized void setSelectedBox(Box b) {
		this.selectedBox = b;
	}

	public synchronized Box getSelectedBox() {
		Box b = this.selectedBox;
		this.selectedBox = null;
		return b;
	}

	public synchronized void queueDocumentToPlay(String id) {
		this.queuedDocumentToPlayID = id;
	}

	public synchronized String getQueuedDocumentForPlayback() {
		return this.queuedDocumentToPlayID;
	}

	public synchronized void queueDocumentToEdit(String id) {
		this.queueDocumentToEditID = id;
	}

	public synchronized String getQueuedDocumentForEditing() {
		return this.queueDocumentToEditID;
	}

	public synchronized void setMessages(ArrayList<MessageLite> a) {
		this.messages = a;
	}

	public synchronized ArrayList<MessageLite> getMessages() {
		return this.messages;
	}

	public synchronized void setUserId(int userID) {
		this.userID = userID;
	}
	
	public synchronized int getUserId() {
		return this.userID;
	}
	
	public synchronized String getSharedMessageId()
	{
		return this.shareMessageID;
	}
	
	public synchronized void setSharedMessageId(String s)
	{
		this.shareMessageID = s;
	}
	
	public synchronized void setPreviewDocumentId(String s)
	{
		this.previewMessageId = s;
	}
	
	public synchronized String getPreviewDocumentId()
	{
		return this.previewMessageId;
	}
}
