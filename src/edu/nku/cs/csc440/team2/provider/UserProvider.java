package edu.nku.cs.csc440.team2.provider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;

import com.thoughtworks.xstream.XStream;

import edu.nku.cs.csc440.team2.User;
import edu.nku.cs.csc440.team2.mediaCloud.Pair;

public class UserProvider {

	public UserProvider(){}
	
	
	
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
	
	public int addUser(String userName, String password, String firstName, String lastName){
		
		int userId = -1;
		
		List<NameValuePair> data = new ArrayList<NameValuePair>();
		
		
		data.add(new BasicNameValuePair("userName", userName));
		data.add(new BasicNameValuePair("password", password));
		data.add(new BasicNameValuePair("firstName", firstName));
		data.add(new BasicNameValuePair("lastName", firstName));
		
		String url = "http://nkucloud.dyndns.org:8080/mediacloud/addUser.jsp";
		//url = "http://localhost:55052/User/Test/1";

		try {

			String result = RequestHelper.makeHttpPostRequest(url, data);
			
			userId = Integer.parseInt(result);
			
			
		} catch (ClientProtocolException e) {
		} catch (IOException e) {
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return userId;
	}
}
