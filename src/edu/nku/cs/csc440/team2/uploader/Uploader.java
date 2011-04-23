package edu.nku.cs.csc440.team2.uploader;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import edu.nku.cs.csc460.team2.R;

public class Uploader extends Activity 
{

	private Uri uri;
	private EditText fileLocationText;
	
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
    	setContentView(R.layout.uploader_main);
    	
    	fileLocationText = (EditText)findViewById(R.id.uploader_edit_text);
    	Button findMedia = (Button)findViewById(R.id.uploader_select_file_button);
    	Button upload = (Button)findViewById(R.id.uploader_upload_file_button);
    	
    	
    	findMedia.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent("org.openintents.action.PICK_FILE");
		    	startActivityForResult(intent, 1);
			}
		});
    	
    	upload.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
			}
		});
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
    	super.onActivityResult(requestCode, resultCode, data);
    	
    	if( resultCode == RESULT_OK && data != null )
    	{
    		this.uri = data.getData();
    		if( this.uri != null)
    		{
    			String filePath = this.uri.getPath();
    			this.fileLocationText.setText(filePath);
    		}
    	}
    }
	
}
