package edu.nku.cs.csc440.team2.UIMenus;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import edu.nku.cs.csc440.team2.SMILCloud;
import edu.nku.cs.csc440.team2.provider.UserProvider;
import edu.nku.cs.csc460.team2.R;

/**
 * Show the registreation screen for the application.
 * 
 * @author John Murray
 * @version 1.0 4/24/11
 * 
 */
public class RegisterScreen extends Activity {

	/**
	 * Display the registration screen and setup all the event handlers for the
	 * specified actions (register). Also define code that will handle the
	 * registration process and error/success messages as well as launching the
	 * authenticated part of the application.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_main);

		Button register = (Button) findViewById(R.id.register_register);

		register.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// get value from the content fields and make a call
				// to the server to create and authenticate the user.

				String firstName;
				String lastName;
				String userName;
				String password;

				EditText fn = (EditText) findViewById(R.id.register_first_name);
				firstName = fn.getText().toString();

				EditText ln = (EditText) findViewById(R.id.register_last_name);
				lastName = ln.getText().toString();

				EditText un = (EditText) findViewById(R.id.register_username);
				userName = un.getText().toString();

				EditText pd = (EditText) findViewById(R.id.register_password);
				password = pd.getText().toString();

				int newUser = (new UserProvider()).addUser(userName, password,
						firstName, lastName);
				if (newUser == -1) {
					// the user was not created.. do something about this
					Context context = getApplicationContext();
					CharSequence text = "Unable to register. Try again.";
					Toast toast = Toast.makeText(context, text,
							Toast.LENGTH_LONG);
					toast.show();
				} else if (newUser == -2) {
					// the user was not created(non-unique).. do something about
					// this
					Context context = getApplicationContext();
					CharSequence text = "User already exists, please log in.";
					Toast toast = Toast.makeText(context, text,
							Toast.LENGTH_LONG);
					toast.show();
				} else {
					//store their session in the login
					SharedPreferences settings = getSharedPreferences(SMILCloud.PREFS_NAME, 
							Context.MODE_PRIVATE);
					SharedPreferences.Editor setEditor = settings.edit();
					setEditor.putInt("userId", newUser);
					setEditor.commit();
					((SMILCloud) getApplication()).setUserId(newUser);
					
					// launch the "Main Menu" activity and let them know they're registered
					Toast.makeText(RegisterScreen.this, "Successfully Registered!", Toast.LENGTH_LONG);
					Intent i = new Intent(RegisterScreen.this, MainMenu.class);
					i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(i);
					RegisterScreen.this.finish();
				}
			}

		});
	}
}