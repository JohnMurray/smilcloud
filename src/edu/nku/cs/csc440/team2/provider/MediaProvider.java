package edu.nku.cs.csc440.team2.provider;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;

import com.thoughtworks.xstream.XStream;
import edu.nku.cs.csc440.team2.mediaCloud.Media;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;

public class MediaProvider {

	public MediaProvider() {

	}

	public Media[] getAllMedia(int userId) {

		try {
			String xml = RequestHelper
					.makeHttpGetRequest("http://nkucloud.dyndns.org:8080/mediacloud/getLibrary.jsp?user="
							+ userId);
			XStream xstream = new XStream();
			Media[] allMedia = (Media[]) xstream.fromXML(xml);

			return allMedia;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Get Media (Video or Audio)
	 * 
	 * @param url
	 * @return Prepared MediaPlayer
	 */
	public MediaPlayer getMedia(String url) {

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
	 * 
	 * @param url
	 * @return Bitmap
	 */
	public Bitmap getImage(String url) {

		// Return Bitmap object
		Bitmap image = null;

		try {
			URL filePath = new URL(url);

			HttpURLConnection conn = (HttpURLConnection) filePath
					.openConnection();

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

	/**
	 * Get text
	 * 
	 * @param url
	 */
	public String getText(String url) {

		String text = "";

		try {
			text = RequestHelper.makeHttpGetRequest(url);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return text;
	}
	
	/**
	 * Delete Media by ID
	 * @param mediaId
	 */
	public void deleteMedia(int mediaId){
		
		try {
			RequestHelper.makeHttpGetRequest("http://nkucloud.dyndns.org:8080/mediacloud/deleteMedia.jsp?mediaId=" + mediaId);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Save Image/Audio/Video
	 * 
	 * @param localPath
	 */
	public void saveMedia(String localPath, String type, int userId) {

		File file = new File(localPath);

		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(
				"http://nkucloud.dyndns.org:8080/mediacloud/storeMedia.jsp");

		try {
			MultipartEntity entity = new MultipartEntity();

			entity.addPart("user", new StringBody(userId + ""));
			entity.addPart("name", new StringBody(file.getName()));
			entity.addPart("type", new StringBody(type));
			entity.addPart("file", new FileBody(file));
			httppost.setEntity(entity);
			
			HttpResponse response = httpclient.execute(httppost);
			
			int i = 0;
		} catch (ClientProtocolException e) {
		} catch (IOException e) {
		}
	}
}
