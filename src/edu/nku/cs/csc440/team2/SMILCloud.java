package edu.nku.cs.csc440.team2;

import java.util.ArrayList;

import android.app.Application;
import edu.nku.cs.csc440.team2.composer.Box;
import edu.nku.cs.csc440.team2.composer.TrackManager;
import edu.nku.cs.csc440.team2.mediaCloud.MessageLite;

/**
 * A global shared object between all of the activities. This is used when
 * passing data through Intents requires too much overhead or when the data
 * must persist throughout the life of the application.
 * 
 * @author John Murray, William Knauer
 * @version 1.0	4/24/11
 */
public class SMILCloud extends Application {
	
	/**
	 * User ID that is indicative of no user.
	 */
	public final static int NO_USER = -1;
	
	/**
	 * The interval at which the backround service and Inbox update themselves
	 * to check for new messages. Time is in milliseconds. 
	 */
	public final static int UPDATE_INTERVAL_MILLISEC = 15000;

	/**
	 * List of messages that is accessed by the Inbox and the background
	 * service for displaying a user's messages.
	 */
	private ArrayList<MessageLite> messages;
	
	/**
	 * The id of a message that is about to be previewed.
	 */
	private String previewMessageId;
	
	/**
	 * The id of a message that is about to be played. 
	 */
	private String queuedDocumentToPlayID = null;
	
	/**
	 * The id of a message that is about to be edited.
	 */
	private String queueDocumentToEditID = null;
	
	/**
	 * The currenlty selected box in teh composer.  
	 */
	private Box selectedBox;
	
	/**
	 * The id of a message that is about to be shared.
	 */
	private String shareMessageID = null;
	
	/**
	 * The current trackManager in use.
	 */
	private TrackManager trackManager;
	
	/**
	 * The current user id, initially set to the NO_USER constant until login.
	 */
	private int userID = NO_USER;

	/**
	 * Store the current TrackManager
	 * 
	 * @param t
	 * 			TrackManager to be stored
	 */
	public synchronized void setTrackManager(TrackManager t) {
		this.trackManager = t;
	}

	/**
	 * Retrieves the stored TrackManager
	 * 
	 * @return the last set TrackManager
	 */
	public synchronized TrackManager getTrackManager() {
		TrackManager t = this.trackManager;
		this.trackManager = null;
		return t;
	}

	/**
	 * Stores a Box object.
	 * 
	 * @param b
	 * 			Box to store
	 */
	public synchronized void setSelectedBox(Box b) {
		this.selectedBox = b;
	}

	/**
	 * Retreives a box object
	 * 
	 * @return a Box object
	 */
	public synchronized Box getSelectedBox() {
		Box b = this.selectedBox;
		this.selectedBox = null;
		return b;
	}

	/**
	 * Stores the id of a SMIL document. Usually used for playback.
	 * 
	 * @param id
	 * 			id of the SMIL document
	 */
	public synchronized void queueDocumentToPlay(String id) {
		this.queuedDocumentToPlayID = id;
	}

	/**
	 * Retreives the id of a SMIL document. Usually used by SMILPlayer class
	 * for playback.
	 * @return id of SMIL Document
	 */
	public synchronized String getQueuedDocumentForPlayback() {
		return this.queuedDocumentToPlayID;
	}

	/**
	 * Stores the id of a SMIL doucment. Usually used for editing.
	 * @param id
	 * 			id of the SMIL document
	 */
	public synchronized void queueDocumentToEdit(String id) {
		this.queueDocumentToEditID = id;
	}

	/**
	 * Retrieves the id of a SMILD document. Usually used by the Composer
	 * class for editing. 
	 * @return
	 * 			id of a SMIL document
	 */
	public synchronized String getQueuedDocumentForEditing() {
		return this.queueDocumentToEditID;
	}

	/**
	 * Store a list of MessageLite objects. Usually used for populating the
	 * Inbox with new messages.
	 * @param a
	 * 			the list of messages for displaying in the Inbox
	 */
	public synchronized void setMessages(ArrayList<MessageLite> a) {
		this.messages = a;
	}

	/**
	 * Retreive a list of messages for displaying in the Inbox.
	 * @return
	 * 			list of MessageLite objects
	 */
	public synchronized ArrayList<MessageLite> getMessages() {
		return this.messages;
	}

	/**
	 * Store the user id of the currently authenticate user. This can
	 * also be set to the NO_USER value to indicate that no user is
	 * currently logged in. 
	 * @param userID
	 * 				The user id of the currently authenticated user.
	 */
	public synchronized void setUserId(int userID) {
		this.userID = userID;
	}
	
	/**
	 * Retrieve the currently authenticated user's id.
	 * @return
	 * 			the user id
	 */
	public synchronized int getUserId() {
		return this.userID;
	}
	
	/**
	 * Retreive the id of a SMIL document. This is usually used for
	 * sharing messages with other users. 
	 * @return
	 * 			id of a SMIL document
	 */
	public synchronized String getSharedMessageId()
	{
		return this.shareMessageID;
	}
	
	/**
	 * Store an id of a SMIL document. Usually used for sharing 
	 * messages between users of the applicatin.
	 * @param s
	 * 			id of the SMIL document that you would like to share
	 */
	public synchronized void setSharedMessageId(String s)
	{
		this.shareMessageID = s;
	}
	
	/**
	 * Store the id of a SMIL document. This is usually used to keep
	 * an account of draft documents, so they can be cleaned up when
	 * the document is saved. 
	 * @param s
	 * 			The id of a SMIL document.
	 */
	public synchronized void setPreviewDocumentId(String s)
	{
		this.previewMessageId = s;
	}
	
	/**
	 * Retrieve the id of a SMIL document. This is usually used by the
	 * composer set of classes for maintaining a clean work-space by
	 * automatically deleting drafts of documents when they are saved.
	 * @return
	 * 			the id of a previously previewd (draft) document. It is
	 * 			the id of a SMIL document
	 */
	public synchronized String getPreviewDocumentId()
	{
		return this.previewMessageId;
	}
}
