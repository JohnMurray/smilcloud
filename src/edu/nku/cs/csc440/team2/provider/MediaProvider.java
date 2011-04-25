package edu.nku.cs.csc440.team2.provider;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
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
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Environment;

public class MediaProvider {

	private String cacheFolder = Environment.getExternalStorageDirectory()
			+ "/smilcache";

	public MediaProvider() {

		File folder = new File(cacheFolder);
		if (!folder.exists())
			folder.mkdir();
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

			// Try to find cache
			File cache = getCacheFile(url);
			if (cache != null) {

				// From cache
				image = BitmapFactory.decodeFile(cache.getPath());

			} else {

				URL filePath = new URL(url);

				HttpURLConnection conn = (HttpURLConnection) filePath
						.openConnection();

				conn.setDoInput(true);
				conn.connect();

				InputStream is = conn.getInputStream();

				// Bitmap
				image = BitmapFactory.decodeStream(is);

				// Cache
				cacheBitmap(image, getFileName(url));
			}

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

		File cache = getCacheFile(url);

		try {
			// Check cache
			if (cache != null) {

				// From cache
				StringBuilder sb = new StringBuilder();

				BufferedReader br = new BufferedReader(new FileReader(cache));
				String line;

				while ((line = br.readLine()) != null) {
					sb.append(line);
					sb.append('\n');
				}

				text = sb.toString();

			} else {

				// From Cloud
				text = RequestHelper.makeHttpGetRequest(url);

				// Cache new file
				cacheText(text, getFileName(url));
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return text;
	}

	/**
	 * Delete Media by ID
	 * 
	 * @param mediaId
	 */
	public void deleteMedia(int mediaId) {

		try {
			RequestHelper
					.makeHttpGetRequest("http://nkucloud.dyndns.org:8080/mediacloud/deleteMedia.jsp?mediaId="
							+ mediaId);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Save Image/Audio/Video
	 * 
	 * @param localPath
	 * @return
	 */
	public String saveMedia(String localPath, String type, int userId,
			String mediaName, boolean useMediaName) {

		String returnedUrl = null;
		File file = new File(localPath);

		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(
				"http://nkucloud.dyndns.org:8080/mediacloud/storeMedia.jsp");

		try {
			MultipartEntity entity = new MultipartEntity();

			entity.addPart("user", new StringBody(userId + ""));
			if (useMediaName) {
				entity.addPart("name", new StringBody(mediaName));
			} else {
				entity.addPart("name", new StringBody(file.getName()));
			}
			entity.addPart("type", new StringBody(type));
			entity.addPart("file", new FileBody(file));
			httppost.setEntity(entity);

			HttpResponse response = httpclient.execute(httppost);

			// Get url
			BufferedReader in = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));
			StringBuffer sb = new StringBuffer("");
			String line = "";
			String NL = System.getProperty("line.separator");
			while ((line = in.readLine()) != null) {
				sb.append(line + NL);
			}
			in.close();
			returnedUrl = sb.toString().trim();

			int i = 0;
		} catch (ClientProtocolException e) {
		} catch (IOException e) {
		}

		return returnedUrl;
	}

	private void removeCache() {

		// TODO: Check size and remove stale cache
	}

	private File getCacheFile(String url) {

		String fileName = getFileName(url);

		File file = new File(cacheFolder + "/" + fileName);

		if (file.exists())
			return file;

		return null;
	}

	//
	// Cache Bitmap
	private void cacheBitmap(Bitmap bitmap, String fileName) {

		File file = new File(cacheFolder, fileName);

		OutputStream outStream = null;

		Bitmap.CompressFormat format = CompressFormat.JPEG;

		String ext = fileName.substring(fileName.lastIndexOf(".") + 1);

		if (ext.compareToIgnoreCase("png") == 0)
			format = CompressFormat.PNG;

		try {
			if (!file.exists())
				file.createNewFile();
			outStream = new FileOutputStream(file);
			bitmap.compress(format, 100, outStream);
			outStream.flush();
			outStream.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// Cache Text File
	private void cacheText(String str, String fileName) {

		File file = new File(cacheFolder + "/" + fileName);

		try {
			FileWriter writer = new FileWriter(file);

			writer.append(str);
			writer.flush();
			writer.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private String getFileName(String fullpath) {

		return fullpath.substring(fullpath.lastIndexOf("/") + 1);
	}
}
