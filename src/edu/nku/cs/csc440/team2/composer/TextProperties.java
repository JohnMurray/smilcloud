package edu.nku.cs.csc440.team2.composer;

import edu.nku.cs.csc460.team2.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

/**
 * A TextProperties Activity is responsible for creating and editing a TextBox.
 * The begin and duration for playback cannot be changed from here.
 * 
 * @author William Knauer <knauerw1@nku.edu>
 * @version 2011.0420
 */
public class TextProperties extends Activity {
	/** Handle to the EditText that sets the text */
	private EditText mEditText;
	
	/** Handle for the button that opens the RegionEditor */
	private Button mRegionEditButton;
	
	/** Handle for the button that deletes the media from the composer. */
	private Button mDeleteButton;
	
	/** The data structure that contains the message. */
	private TrackManager mTrackManager;
	
	/** The media being edited/created. */
	private TextBox mBox;
	
	/**
	 * Assigns local handles and callbacks for widgets in the UI.
	 */
	private void loadWidgetsFromView() {
		mEditText = (EditText) findViewById(R.id.textbox_text);
		mRegionEditButton = (Button) findViewById(R.id.region_edit_button);
		mRegionEditButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				/* Commit the EditText contents to the TextBox */
				mBox.setName(mEditText.getText().toString());
				
				Intent i = new Intent(getBaseContext(), RegionEditor.class);
				i.putExtra("track_manager", mTrackManager);
				i.putExtra("box_id", mBox.getId());
				startActivityForResult(i, 0);
			}

		});

		mDeleteButton = (Button) findViewById(R.id.delete_button);
		mDeleteButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				/* Delete the media from the data structure */
				mTrackManager.removeBox(mBox);

				/* Return the data structure and deletion status */
				Intent i = new Intent();
				i.putExtra("track_manager", mTrackManager);
				setResult(RESULT_OK, i);
				finish();
			}

		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		mTrackManager = data.getParcelableExtra("track_manager");
		String boxId = data.getStringExtra("box_id");
		mBox = (TextBox) mTrackManager.getBox(boxId);
	}

	@Override
	public void onBackPressed() {
		/* Commit the EditText contents to the TextBox */
		mBox.setName(mEditText.getText().toString());
		
		/* If all required fields are filled in */
		if (mBox.getRegion() != null) {
			/* Return OK status */
			Intent i = new Intent();
			i.putExtra("track_manager", mTrackManager);
			setResult(RESULT_OK, i);
		} else {
			setResult(RESULT_CANCELED);
		}
		
		finish();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.text_properties);
		loadWidgetsFromView();
		
		/* Load from Intent */
		mTrackManager = getIntent().getParcelableExtra("track_manager");
		String boxId = getIntent().getStringExtra("box_id");
		mBox = (TextBox) mTrackManager.getBox(boxId);
		
		if (mBox == null) {
			/* Media must be created */
			mBox = new TextBox(null, 0, 10, null);
			mTrackManager.addBox(mBox, mBox.getBegin());
		}
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		
		/* Set the EditText's text if possible */
		if (mBox.getName() != null) {
			mEditText.setText(mBox.getName());
			mEditText.setSelection(mEditText.getText().length());
		}
	}

}
