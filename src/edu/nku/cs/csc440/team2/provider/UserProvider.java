package edu.nku.cs.csc440.team2.provider;

import java.util.ArrayList;

import com.thoughtworks.xstream.XStream;

import edu.nku.cs.csc440.team2.User;
import edu.nku.cs.csc440.team2.mediaCloud.Media;
import edu.nku.cs.csc440.team2.mediaCloud.Pair;

public class UserProvider {

	public UserProvider(){}
	
	public User getUserById(int id){
		
		// Return user
		User user = null;
		
		
		return user;
	}
	
	public User getUserByUsernamePassword(String username, String password){
		
		// Return user
		User user = null;
		
		
		return user;
	}
	
	public ArrayList<Pair<String, String>> getContactList(){
		
		try {
			String xml = RequestHelper
					.makeHttpGetRequest("http://nkucloud.dyndns.org:8080/mediacloud/getUsers.jsp");
			XStream xstream = new XStream();
			ArrayList<Pair<String, String>> allUsers = (ArrayList<Pair<String, String>>) xstream.fromXML(xml);

			return allUsers;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
		
	}
}
