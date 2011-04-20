package edu.nku.cs.csc440.team2.uploader;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;
import edu.nku.cs.csc460.team2.R;

public class Uploader extends Activity 
{

	/**
     * @param savedInstanceState
     * @return void
     * @note Activity should run in landscape mode.
     * 
     * Call when Activity is first created
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.inbox_main);
    	Intent intent = new Intent("org.openintents.action.PICK_FILE");
    	startActivityForResult(intent, 1);
    	
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
    	super.onActivityResult(requestCode, resultCode, data);
    	
    	if( resultCode == RESULT_OK && data != null )
    	{
    		Uri fileUri = data.getData();
    		if( fileUri != null)
    		{
    			String filePath = fileUri.getPath();
    			Toast.makeText(this, filePath, Toast.LENGTH_LONG);
    		}
    	}
    }
	
}
