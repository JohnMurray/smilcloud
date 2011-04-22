package edu.nku.cs.csc440.team2.UIMenus;

import edu.nku.cs.csc440.team2.SMILCloud;
import edu.nku.cs.csc440.team2.provider.UserProvider;
import edu.nku.cs.csc460.team2.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class AuthenticateScreen extends Activity {
	@Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.authenticate_screen);
    	
    	Button login = (Button)findViewById(R.id.authenticate_authenticate);
    	
    	login.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// get the auth credentials and try to log in.
				String username;
				String password;
				
				EditText un = (EditText)findViewById(R.id.authenticate_username);
				username = un.getText().toString();
				
				EditText ps = (EditText)findViewById(R.id.authenticate_password);
				password = ps.getText().toString();
				
				int authResult = (new UserProvider()).login(username, password);
				if( authResult == -1 )
				{
					//the user was not created.. do something about this
					Context context = getApplicationContext();
					CharSequence text = "FAIL! Try again.";
					Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
					toast.show();
				}
				else
				{
					((SMILCloud) getApplication()).setUserId(authResult);
					//launch the "Main Menu" activity
					Intent i = new Intent(AuthenticateScreen.this, MainMenu.class);
					startActivity(i);
					AuthenticateScreen.this.finish();
				}
			}
    		
    	});
	}
}
