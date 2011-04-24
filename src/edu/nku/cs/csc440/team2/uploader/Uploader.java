package edu.nku.cs.csc440.team2.uploader;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import edu.nku.cs.csc440.team2.SMILCloud;
import edu.nku.cs.csc440.team2.provider.MediaProvider;
import edu.nku.cs.csc460.team2.R;

public class Uploader extends Activity 
{
	/*
	 * Static strings for output.
	 */
	public static final String BAD_MEDIA = "File is not a valid/accepted media file.";
	public static final String FAILED_UPLOAD = "Upload could not be completed at this time.\nUnable to Connect";
	public static final String GOOD_UPLOAD = "File successfully uploaded.";

	private Uri uri;
	private EditText fileLocation;
	private String fileLocationText;
	private ProgressDialog mProgressDialog;
	/*
	 * The background activity for uploading media
	 */
	private Runnable mUploadMedia = new Runnable() {
		@Override
		public void run() {
			String fileLocation = Uploader.this.fileLocationText;
			String [] temp = fileLocation.split("\\.");
			String ext = temp[temp.length - 1];
			String type = Uploader.this.determineType(ext);
			
			if( type == null )
			{
				Uploader.this.dismissAndNotify(Uploader.BAD_MEDIA);
			}
			else
			{
				String mediaUrl = (new MediaProvider()).saveMedia(fileLocation, type, 
						((SMILCloud)Uploader.this.getApplication()).getUserId());
				if( mediaUrl == null )
				{
					Uploader.this.dismissAndNotify(Uploader.FAILED_UPLOAD);
				}
				else
				{
					Uploader.this.dismissAndNotify(Uploader.GOOD_UPLOAD);
					runOnUiThread(Uploader.this.mClearEditText);
				}
			}
		}
	};
	
	/*
	 * The Runnable for clearing the EditText box
	 */
	private Runnable mClearEditText = new Runnable() {
		
		@Override
		public void run() {
			Uploader.this.fileLocation.setText("");
		}
	};
	
	private String mDismissMessage;
	
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
    	
    	fileLocation = (EditText)findViewById(R.id.uploader_edit_text);
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
				Uploader.this.mProgressDialog = ProgressDialog.show(Uploader.this,
						"Please Wait...", "Uploading Media...", true);
				Uploader.this.fileLocationText = Uploader.this.fileLocation.getText().toString();
				(new Thread(null, Uploader.this.mUploadMedia, "Upload Media")).start();
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
    			this.fileLocation.setText(filePath);
    		}
    	}
    }
    
    protected String determineType(String ext) {
		String type = null;
		if( ext.equals("mp3") || ext.equals("wav") || ext.equals("ogg") )
		{
			type = "audio";
		}
		else if( ext.equals("jpg") || ext.equals("gif") || ext.equals("png") || ext.equals("bmp") )
		{
			type = "image";
		}
		else if( ext.equals("3gp") || ext.equals("mp4") )
		{
			type = "video";
		}
		
		return type;
	}
    
    public void dismissAndNotify(String message)
    {
    	this.mDismissMessage = message;
    	runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Uploader.this.mProgressDialog.dismiss();
				Toast.makeText(Uploader.this, Uploader.this.mDismissMessage, 
						Toast.LENGTH_LONG).show();
			}
		});
    }
	
}
