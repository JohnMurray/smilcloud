package edu.nku.cs.csc440.team2.composer;

import edu.nku.cs.csc440.team2.SMILCloud;
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
				Intent i = new Intent(getBaseContext(), RegionEditor.class);
				startActivityForResult(i, 0);
			}

		});

		mDeleteButton = (Button) findViewById(R.id.delete_button);
		mDeleteButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				/* Delete the media from the data structure */
				mTrackManager.removeBox(mBox);
				mBox = null;

				/* Return the data structure and deletion status */
				setResult(RESULT_OK);
				save();
				finish();
			}

		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// do nothing
	}

	@Override
	public void onBackPressed() {
		/* If all required fields are filled in */
		if (mBox.getName() != null && mBox.getRegion() != null) {
			/* Return OK status */
			setResult(RESULT_OK);
		} else {
			/* Return canceled status */
			mTrackManager.removeBox(mBox);
			mBox = null;
			setResult(RESULT_CANCELED);
		}
		
		save();
		finish();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.text_properties);
		loadWidgetsFromView();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		save();
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		
		/* Load from Application */
		mTrackManager = ((SMILCloud) getApplication()).getTrackManager();
		mBox = (TextBox) ((SMILCloud) getApplication()).getSelectedBox();
		
		if (mBox == null) {
			/* Media must be created */
			mBox = new TextBox(null, 0.0, 1.0, null);
			mTrackManager.addBox(mBox, mBox.getBegin());
		}
		
		/* Set the EditText's text if possible */
		if (mBox.getName() != null) {
			mEditText.setText(mBox.getName());
			mEditText.setSelection(mEditText.getText().length());
		}
	}
	
	/**
	 * Saves the TrackManager and Box to the Application.
	 */
	private void save() {
		/* Save text */
		if (mBox != null) {
			mBox.setName(mEditText.getText().toString());
		}
		
		/* Save to Application */
		((SMILCloud) getApplication()).setTrackManager(mTrackManager);
		((SMILCloud) getApplication()).setSelectedBox(mBox);
	}

}
