package edu.nku.cs.csc440.team2.UIMenus;

import edu.nku.cs.csc440.team2.provider.UserProvider;
import edu.nku.cs.csc460.team2.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class WelcomeScreen extends Activity {
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.welcome_screen);
    	
    	/////// TEST
    	//UserProvider up = new UserProvider();
    	//int p = up.addUser("user2", "password", "f1", "l1");
    	//int u = up.login("user2", "password");
    	
    	Button login = (Button)findViewById(R.id.welcome_login);
    	Button register = (Button)findViewById(R.id.welcome_register);
    	
    	login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				//launch intent
				
			}
    		
    	});
    	
    	register.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//launch intent
				
			}
    		
    	});
    }
}
