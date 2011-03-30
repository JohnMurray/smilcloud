package edu.nku.cs.csc440.team2.mediaCloud;

import java.sql.*;
import java.util.ArrayList;

public class Media {
	
	private String mediaUrl;
	private String thumbUrl;
	private String name;
	private String type;
	private String mediaId;

	public Media() {}
	
	public Media(String mediaUrl, String thumbUrl, String name) {
		this.mediaUrl = mediaUrl;
		this.thumbUrl = thumbUrl;
		this.name = name;	
		this.type = type;
	}
	
	public void store(Connection conn, int userId) {
		this.addMedia(conn,mediaUrl,thumbUrl,name,type,userId);
	}
	
	public String getMediaId() {
		return mediaId;
	}
	
	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}
	
	public String getMediaUrl() {
		return mediaUrl;
	}
	
	public String getThumbUrl() {
		return thumbUrl;
	}
	
	public String getName() {
		return name;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	private void addMedia(Connection conn, String mediaUrl, String thumbUrl, String name, String type, int userId) {		
		try{			
			String sql = "INSERT INTO media(thumbUrl, name, mediaUrl, type, userId) VALUES (?, ?,?, ?, ?)";
			PreparedStatement preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setString(1, thumbUrl);
			preparedStatement.setString(2, name);
			preparedStatement.setString(3, mediaUrl);
			preparedStatement.setString(4, type);
			preparedStatement.setInt(5, userId);
			preparedStatement.executeUpdate();
			preparedStatement.close();
			preparedStatement = null;

			
		} catch (SQLException ex) {
			// handle any errors
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
	}
	
	public static Media[] getLibrary(Connection conn, int userId) {
		ArrayList<Media> allMedia = new ArrayList<Media>();
		try{
			Statement stmt = null;	
			ResultSet rs = null;
			stmt = conn.createStatement();
			String query = "SELECT * FROM media WHERE userId = " + userId;
			rs = stmt.executeQuery(query);
			while (rs.next()) {
				Media temp = new Media(rs.getString("mediaUrl"),rs.getString("thumbUrl"),rs.getString("name"));
				temp.setType(rs.getString("type"));
				temp.setMediaId(rs.getString("mediaId"));
				allMedia.add(temp);
			}			
			
			stmt.close();
			stmt = null;
		} catch (SQLException ex) {
			// handle any errors
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
		
		Media[] mediaArray = new Media[allMedia.size()];
		return allMedia.toArray(mediaArray);
	}

}
