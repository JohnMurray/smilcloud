package edu.nku.cs.csc440.team2.UIMenus;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
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
 * Activity to handle authentication against the cloud.
 * 
 * 
 * @author John Murray
 * @version 1.0 4/24/11
 * 
 */
public class AuthenticateScreen extends Activity {

	/**
	 * Constant message to display if the username or password are left blank"
	 */
	public static final String INVALID_INPUT_MSG = "Username and password required.";
	/**
	 * Constant message to display if we are unable to connect to the cloud
	 * (network error)
	 */
	public static final String NETWORK_ERROR_MSG = "Unable to connect to the cloud.";
	/**
	 * Constant message to display if we are unable to login with given creds.
	 */
	public static final String INVALID_CREDS = "Authentication failed with given username and password";

	/**
	 * Instance data to hold the username
	 */
	private String username = null;
	/**
	 * Instance data to hold password
	 */
	private String password = null;
	/**
	 * Instance data to hold the user id
	 */
	private Integer userId = null;

	/**
	 * Progress dialog to show when connecting to the cloud
	 */
	private ProgressDialog mProgressDialog;

	/**
	 * Thread to run authentication in the background so that the UI does not
	 * "appear" to be slow or sluggish
	 */
	private Runnable mAuthenticate = new Runnable() {
		@Override
		public void run() {
			if (AuthenticateScreen.this.username.equals("")
					|| AuthenticateScreen.this.password.equals("")) {
				// invalid input
				runOnUiThread(AuthenticateScreen.this.mInvalidInput);
			} else {
				AuthenticateScreen.this.userId = (new UserProvider()).login(
						AuthenticateScreen.this.username,
						AuthenticateScreen.this.password);
				if (AuthenticateScreen.this.userId == null) {
					// network, or unplanned, error occurred
					runOnUiThread(AuthenticateScreen.this.mNetworkError);
				} else if (AuthenticateScreen.this.userId == SMILCloud.NO_USER) {
					// auth-credentials incorrrect
					runOnUiThread(AuthenticateScreen.this.mInvalidCreds);
				} else {
					// We have successfully logged in!
					runOnUiThread(AuthenticateScreen.this.mSuccessfulLogin);
					runOnUiThread(AuthenticateScreen.this.mSuccessfulLogin);
				}
			}
		}
	};

	/**
	 * Message to show the user when they didn't provide both username and
	 * password
	 */
	private Runnable mInvalidInput = new Runnable() {
		@Override
		public void run() {
			AuthenticateScreen.this.mProgressDialog.dismiss();
			Toast toast = Toast.makeText(getApplicationContext(),
					AuthenticateScreen.INVALID_INPUT_MSG, Toast.LENGTH_LONG);
			toast.show();
		}
	};

	/**
	 * Message to show the user when they provide invalid credentials
	 */
	private Runnable mInvalidCreds = new Runnable() {
		@Override
		public void run() {
			AuthenticateScreen.this.mProgressDialog.dismiss();
			Toast toast = Toast.makeText(getApplicationContext(),
					AuthenticateScreen.INVALID_CREDS, Toast.LENGTH_LONG);
			toast.show();
		}
	};

	/**
	 * Message to show the user when there has been a network error
	 */
	private Runnable mNetworkError = new Runnable() {
		@Override
		public void run() {
			AuthenticateScreen.this.mProgressDialog.dismiss();
			Toast toast = Toast.makeText(getApplicationContext(),
					AuthenticateScreen.NETWORK_ERROR_MSG, Toast.LENGTH_LONG);
			toast.show();
		}
	};

	/**
	 * Message to show the user when they authenticate successfully
	 */
	private Runnable mSuccessfulLogin = new Runnable() {
		@Override
		public void run() {
			AuthenticateScreen.this.mProgressDialog.dismiss();
			((SMILCloud) getApplication())
					.setUserId(AuthenticateScreen.this.userId);
			// launch the "Main Menu" activity
			Intent i = new Intent(AuthenticateScreen.this, MainMenu.class);
			i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(i);
		}
	};

	/**
	 * Start the activity and display the login form and handle the actions when
	 * the user presses submit.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.authenticate_screen);

		Button login = (Button) findViewById(R.id.authenticate_authenticate);

		login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// get the auth credentials and try to log in.
				EditText un = (EditText) findViewById(R.id.authenticate_username);
				AuthenticateScreen.this.username = un.getText().toString();

				EditText ps = (EditText) findViewById(R.id.authenticate_password);
				AuthenticateScreen.this.password = ps.getText().toString();

				AuthenticateScreen.this.mProgressDialog = ProgressDialog.show(
						AuthenticateScreen.this, "Please Wait...",
						"Logging You In...", true);
				(new Thread(AuthenticateScreen.this.mAuthenticate)).start();
			}

		});
	}
}
