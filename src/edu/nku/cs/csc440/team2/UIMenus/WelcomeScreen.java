package edu.nku.cs.csc440.team2.UIMenus;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import edu.nku.cs.csc440.team2.SMILCloud;
import edu.nku.cs.csc460.team2.R;

/**
 * The welcome screen. The first thing you see when you start the application.
 * 
 * @author John Murray
 * @version 1.0 4/24/11
 * 
 */
public class WelcomeScreen extends Activity {

	/**
	 * Display the welcome screen and set the event handlers for your two
	 * actions, login or register.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		SharedPreferences settings = getSharedPreferences(SMILCloud.PREFS_NAME, 
				Context.MODE_PRIVATE);
		int userId = settings.getInt("userId", SMILCloud.NO_USER);
		if (userId != SMILCloud.NO_USER) {
			SMILCloud smilCloud = (SMILCloud) getApplication();
			smilCloud.setUserId(userId);
			Intent i = new Intent(this, MainMenu.class);
			startActivity(i);
			this.finish();
		}
		

		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome_screen);

		Button login = (Button) findViewById(R.id.welcome_login);
		Button register = (Button) findViewById(R.id.welcome_register);

		login.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// launch intent
				WelcomeScreen.this.startActivity(new Intent(WelcomeScreen.this,
						AuthenticateScreen.class));
			}
		});

		register.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// launch intent
				startActivity(new Intent(WelcomeScreen.this,
						RegisterScreen.class));
			}
		});
	}
}
