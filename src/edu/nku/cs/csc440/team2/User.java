package edu.nku.cs.csc440.team2;

/**
 * Represents the current user. This is more of a struct-like
 * structure than a class. It only encapsulates the user's username,
 * password and user-id.
 * 
 * @author Jon Schrage, Pochara Prapatanan
 * @version 1.0 4/24/11
 */
public class User {

	/**
	 * The id of a user.
	 */
	private int userId;
	
	/**
	 * The username of a user.
	 */
	private String username;
	
	/**
	 * The passwor of a user.
	 */
	private String password;
	
	/**
	 * No-arg constructor.
	 */
	public User(){}
	
	/**
	 * Initialize the structure with all of its instance data.
	 * @param id
	 * 			The id of the user
	 * @param user
	 * 			The username of the user
	 * @param pass
	 * 			The password of the user
	 */
	public User(int id, String user, String pass){
		
		userId = id;
		username = user;
		password = pass;
	}
	
	/**
	 * Get the user id from the structure
	 * @return
	 * 			the id of the user
	 */
	public int getUserId(){
		return this.userId;
	}
	
	/**
	 * Get hte username from the structure
	 * @return
	 * 			the username of the user
	 */
	public String getUsername(){
		return this.username;
	}
	
	/**
	 * Get the password from the structure
	 * @return
	 * 			the password of the user
	 */
	public String getPassword(){
		return this.password;
	}
}
