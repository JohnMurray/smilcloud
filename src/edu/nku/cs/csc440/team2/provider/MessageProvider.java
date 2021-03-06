package edu.nku.cs.csc440.team2.provider;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.os.Environment;

import com.thoughtworks.xstream.XStream;

import edu.nku.cs.csc440.team2.mediaCloud.MessageLite;
import edu.nku.cs.csc440.team2.message.Message;


public class MessageProvider {
	
	private String tempFolder = Environment.getExternalStorageDirectory() + "/smiltemp";

	public MessageProvider() { 
		
		File folder = new File(tempFolder);
		if(!folder.exists())
			folder.mkdir();
	}
	
	/**
	 * Get list of saved messaged
	 * @param userId
	 * @return
	 */
	public ArrayList<MessageLite> getSavedMessages(int userId){
		
		String url = "http://nkucloud.dyndns.org:8080/mediacloud/getSavedMessageList.jsp?user=" + userId;
		
		return getRemoteMessageLites(url);
	}

	/**
	 * Get list of message sent from other users by userId
	 * @param userId
	 * @return ArrayList of MessageLites 
	 */
	public ArrayList<MessageLite> getAllMessage(int userId) {

		String url = "http://nkucloud.dyndns.org:8080/mediacloud/getMessageList.jsp?user=" + userId;
		
		return getRemoteMessageLites(url);
	}
	
	@SuppressWarnings("unchecked")
	private ArrayList<MessageLite> getRemoteMessageLites(String url){
		
		ArrayList<MessageLite> allMessageLite = null;
		try {
			String xml = RequestHelper
					.makeHttpGetRequest(url);
			
			XStream xstream = new XStream();
			
			allMessageLite = (ArrayList<MessageLite>) xstream.fromXML(xml);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return allMessageLite;
	}

	/**
	 * Get Message
	 * 
	 * @param Message
	 *            ID
	 * @return Message
	 */
	public Message getMessageById(String id) {

		// Return parsed message
		Message msg = null;

		// Make request to get message XML

		try {
			String xml = RequestHelper
					.makeHttpGetRequest("http://nkucloud.dyndns.org:8080/mediacloud/getMessage.jsp?uniqueId="
							+ id);

			File fi = new File(Environment.getExternalStorageDirectory()
					+ "/tempfile.xml");

			fi.createNewFile();

			FileOutputStream fop = new FileOutputStream(fi);
			fop.write(xml.getBytes());

			fop.flush();
			fop.close();

			msg = new Message(fi);


		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return msg;
	}
	
	/**
	 * Delete message on the cloud
	 * @param messageId
	 */
	public void deleteMessage(String messageId){
		
		try {
			RequestHelper.makeHttpGetRequest("http://nkucloud.dyndns.org:8080/mediacloud/deleteMessage.jsp?messageId=" + messageId);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Save SMIL message on the cloud
	 * @param userId
	 * @param messageTitle
	 * @param msg
	 * @return Message ID
	 */
	public String saveMessage(int userId, String messageTitle, Message msg) {
		
		String result = "";
		String xml = getSmil(msg);

		String url = "http://nkucloud.dyndns.org:8080/mediacloud/storeMessage.jsp";
		
		
		List<NameValuePair> data = new ArrayList<NameValuePair>();
		
		
		data.add(new BasicNameValuePair("userId", userId + ""));
		data.add(new BasicNameValuePair("name", messageTitle));
		data.add(new BasicNameValuePair("message", xml));

		try {

			// Make POST request
			result = RequestHelper.makeHttpPostRequest(url, data);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result.trim();
	}
	

	/**
	 * Send message
	 * 
	 * @param Message
	 * @return Message ID
	 */
	public void sendMessage(int senderId, int recipientId, String messageTitle ,Message msg) {

		String xml = getSmil(msg);

		String url = "http://nkucloud.dyndns.org:8080/mediacloud/sendMessage.jsp";
		
		List<NameValuePair> data = new ArrayList<NameValuePair>();
		
		data.add(new BasicNameValuePair("userId", senderId + ""));
		data.add(new BasicNameValuePair("recipient", recipientId + ""));
		data.add(new BasicNameValuePair("name", messageTitle));
		data.add(new BasicNameValuePair("message", xml));

		try {

			// Make POST request
			RequestHelper.makeHttpPostRequest(url, data);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//return "";

	}
	
	/**
	 * Share Message to other users by ID
	 * @param userId
	 * @param recipientId
	 * @param messageId
	 */
	public void sendMessageById(int senderId, int recipientId, String messageId){
		
		String url = "http://nkucloud.dyndns.org:8080/mediacloud/sendMessageById.jsp";
		
		List<NameValuePair> data = new ArrayList<NameValuePair>();
		
		data.add(new BasicNameValuePair("userId", senderId + ""));
		data.add(new BasicNameValuePair("recipient", recipientId + ""));
		data.add(new BasicNameValuePair("uniqueId", messageId));
		

		try {

			// Make POST request
			RequestHelper.makeHttpPostRequest(url, data);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	private String getSmil(Message msg){
		
		String xml = "";
		// Read SMIL
		try {
			File temp = new File(tempFolder + "/temp.smil");
			if(!temp.exists())
				temp.createNewFile();
			
			File smilFile = msg.toFile(tempFolder + "/temp.smil");
			
			// From cache
			StringBuilder sb = new StringBuilder();

		    BufferedReader br = new BufferedReader(new FileReader(smilFile));
		    String line;

		    while ((line = br.readLine()) != null) {
		        sb.append(line);
		        sb.append('\n');
		    }
		    
		    xml = sb.toString();
		    
			
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return xml;
	}

}
