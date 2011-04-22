package edu.nku.cs.csc440.team2.UIMenus;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import edu.nku.cs.csc440.team2.SMILCloud;
import edu.nku.cs.csc460.team2.R;

public class WelcomeScreen extends Activity {
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		
		SMILCloud smilCloud = (SMILCloud) getApplication();
    	if( smilCloud.getUserId() != SMILCloud.NO_USER )
    	{
    		this.finish();
    	}
    	
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.welcome_screen);
    	
    	Button login = (Button)findViewById(R.id.welcome_login);
    	Button register = (Button)findViewById(R.id.welcome_register);
    	
    	login.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				//launch intent
				WelcomeScreen.this.startActivity(new Intent(
						WelcomeScreen.this, AuthenticateScreen.class));
			}	
    	});
    	
    	register.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//launch intent
				startActivity(new Intent(
						WelcomeScreen.this, RegisterScreen.class));
			}
    	});
    }
}
