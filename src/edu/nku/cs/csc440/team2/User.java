package edu.nku.cs.csc440.team2;

public class User {

	private int userId;
	private String username;
	private String password;
	
	public User(){}
	
	public User(int id, String user, String pass){
		
		userId = id;
		username = user;
		password = pass;
	}
	
	public int getUserId(){
		return this.userId;
	}
	public String getUsername(){
		return this.username;
	}
	public String getPassword(){
		return this.password;
	}
}
