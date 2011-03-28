package edu.nku.cs.csc440.team2.provider;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import com.thoughtworks.xstream.XStream;
import edu.nku.cs.csc440.team2.mediaCloud.Media;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;

public class MediaProvider {
	
	public MediaProvider(){
		
	}
	
	public Media[] getAllMedia(int userId){
		
		try {
			String xml = RequestHelper.makeHttpGetRequest("http://nkucloud.dyndns.org:8080/mediacloud/getLibrary.jsp?user=" + userId);
			XStream xstream = new XStream();
			Media[] allMedia = (Media[]) xstream.fromXML(xml);
			
		
			return  allMedia;
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	/**
	 * Get Media (Video or Audio)
	 * @param url
	 * @return Prepared MediaPlayer
	 */
	public MediaPlayer getMedia(String url){
		
		// Return Media Player
		MediaPlayer media = new MediaPlayer();
		
		
		try {
			
			// Set Source
			media.setDataSource(url);
			
			// Prepare
			media.prepare();
			
			
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return media;
	}
	
	/**
	 * Get individual image
	 * @param url
	 * @return Bitmap
	 */
	public Bitmap getImage(String url){

		// Return Bitmap object
		Bitmap image = null;
		
		try {
			URL filePath = new URL(url);
			
			HttpURLConnection conn = (HttpURLConnection)filePath.openConnection();
			
			conn.setDoInput(true);
			conn.connect();
			
			InputStream is = conn.getInputStream();
			
			image = BitmapFactory.decodeStream(is);
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return image;
	}
	
	public void saveMedia(String localPath){
		
		
	}
}
