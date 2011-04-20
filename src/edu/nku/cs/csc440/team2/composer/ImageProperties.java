package edu.nku.cs.csc440.team2.composer;

import edu.nku.cs.csc440.team2.SMILCloud;
import edu.nku.cs.csc460.team2.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * An ImageProperties Activity is responsible for creating and editing an
 * ImageBox. The begin and duration for playback cannot be changed from here.
 * 
 * @author William Knauer <knauerw1@nku.edu>
 * @version 2011.0420
 */
public class ImageProperties extends Activity {
	/** Request code for launching ImageBrowser */
	private static final int REQ_SOURCE = 1;

	/** Request code for launching RegionEditor */
	private static final int REQ_REGION = 2;

	/** Handle for the button that sets the media source. */
	private Button mSetSourceButton;

	/** Handle for the button that opens the RegionEditor */
	private Button mRegionEditButton;

	/** Handle for the button that deletes the media from the composer. */
	private Button mDeleteButton;

	/** The data structure that contains the message. */
	private TrackManager mTrackManager;

	/** The media being edited/created. */
	private ImageBox mBox;

	/**
	 * Assigns local handles and callbacks for widgets in the UI.
	 */
	private void loadWidgetsFromView() {
		mSetSourceButton = (Button) findViewById(R.id.source_button);
		mSetSourceButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(getBaseContext(), ImageBrowser.class);
				startActivityForResult(i, 0);
			}

		});

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
	protected void onActivityResult(int requestCode,
			int resultCode, Intent data) {
		if (requestCode == REQ_SOURCE) {
			/* Set Box info from intent */
			mBox.setName(data.getStringExtra("name"));
			mBox.setId(data.getStringExtra("id"));
			mBox.setSource(data.getStringExtra("source"));
			// TODO mBox.setThumbUrl(data.getStringExtra("thumb"));

		} else if (requestCode == REQ_REGION) {
			// nothing to do
		}
	}

	@Override
	public void onBackPressed() {
		/* If all required fields are filled in */
		if (mBox.getSource() != null && mBox.getRegion() != null) {
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
		setContentView(R.layout.image_properties);
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
		mBox = (ImageBox) ((SMILCloud) getApplication()).getSelectedBox();

		if (mBox == null) {
			/* Media must be created */
			mBox = new ImageBox(null, 0.0, 1.0, null);
			mTrackManager.addBox(mBox, mBox.getBegin());
		}

		if (mBox.getSource() != null) {
			/* Disallow editing of the media's source */
			mSetSourceButton.setEnabled(false);
			mSetSourceButton.setText(mBox.getName());
		}
	}
	
	/**
	 * Saves the TrackManager and Box to the Application.
	 */
	private void save() {
		/* Save to Application */
		((SMILCloud) getApplication()).setTrackManager(mTrackManager);
		((SMILCloud) getApplication()).setSelectedBox(mBox);
	}

}
