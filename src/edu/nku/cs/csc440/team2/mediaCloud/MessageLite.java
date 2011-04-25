package mediaCloud;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class MessageLite {
	
	private String name;
	private String sender;
	private String date;
	private String uniqueId;
	
	MessageLite(String name, String sender, String date, String uniqueId) {
		this.name = name;
		this.sender = sender;
		this.date = date;
		this.uniqueId = uniqueId;
	}
	
	public String getName() {
		return name;
	}
	
	public String getSender() {
		return sender;
	}
	
	public String getDate() {
		return date;
	}
	
	public String getUniqueId() {
		return uniqueId;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setSender(String sender) {
		this.sender = sender;
	}
	
	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}
	
	public static MessageLite[] getMessageList(Connection conn, int userId) {
		ArrayList<MessageLite> inbox = new ArrayList<MessageLite>();
		try{
			Statement stmt = null;	
			ResultSet rs = null;
			stmt = conn.createStatement();
			String query = "SELECT * FROM messages WHERE userId = " + userId;
			rs = stmt.executeQuery(query);
			while (rs.next()) {
				MessageLite temp = new MessageLite(rs.getString("name"),"",rs.getString("sentDate"),rs.getString("uniqueId"));
				
				Statement stmtUser = null;	
				ResultSet rsUser = null;
				stmtUser = conn.createStatement();
				String queryUser = "SELECT * FROM users WHERE user_id = " + rs.getString("senderId");
				rsUser = stmtUser.executeQuery(queryUser);
				rsUser.next();
				temp.setSender(rsUser.getString("first_name") + " " + rsUser.getString("last_name"));
				stmtUser.close();
				stmtUser = null;

						
				
				inbox.add(temp);
			}			
			
			stmt.close();
			stmt = null;
		} catch (SQLException ex) {
			// handle any errors
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
		
		MessageLite[] inboxArray = new MessageLite[inbox.size()];
		return inbox.toArray(inboxArray);
	}

}
