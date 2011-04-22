package edu.nku.cs.csc440.team2.UIMenus;

import edu.nku.cs.csc440.team2.SMILCloud;
import edu.nku.cs.csc440.team2.composer.Composer;
import edu.nku.cs.csc440.team2.inbox.CreatedMessages;
import edu.nku.cs.csc440.team2.inbox.Inbox;
import edu.nku.cs.csc460.team2.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainMenu extends Activity {
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.main_menu_screen);
    	
    	Button inbox = (Button)findViewById(R.id.main_menu_inbox);
    	Button createdMessage = (Button)findViewById(R.id.main_menu_created_messages);
    	Button composeNew = (Button)findViewById(R.id.main_menu_compose_messages);
    	Button exit = (Button)findViewById(R.id.main_menu_exit);
    	
    	inbox.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				MainMenu.this.startActivity(new Intent(
						MainMenu.this, Inbox.class));
			}
    	});
    	
    	createdMessage.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				MainMenu.this.startActivity(new Intent(
						MainMenu.this, CreatedMessages.class));
			}
    	});
    	
    	composeNew.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				((SMILCloud)MainMenu.this.getApplication()).queueDocumentToEdit(null);
				MainMenu.this.startActivity(new Intent(
						MainMenu.this, Composer.class));
			}
    	});
    	
    	exit.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				MainMenu.this.finish();
			}
    	});
    	
	}

}
