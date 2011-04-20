package edu.nku.cs.csc440.team2.composer;

import edu.nku.cs.csc440.team2.SMILCloud;
import edu.nku.cs.csc460.team2.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

/**
 * An AudioProperties Activity is responsible for creating and editing an
 * AudioBox. The begin and duration for playback cannot be changed from here.
 * 
 * @author William Knauer <knauerw1@nku.edu>
 * @version 2011.0420
 */
public class AudioProperties extends Activity {
	/** Handle for the button that sets the media source. */
	private Button mSetSourceButton;
	
	/** Handle for the SeekBar that sets the media's clipBegin time. */
	private SeekBar mClipOffsetBar;
	
	/** Handle for the label that shows the clipBegin time. */
	private TextView mClipBeginLabel;
	
	/** Handle for the label that shows the clipEnd time. */
	private TextView mClipEndLabel;
	
	/** Handle for the button that deletes the media from the composer. */
	private Button mDeleteButton;
	
	/** The data structure that contains the message. */
	private TrackManager mTrackManager;
	
	/** The media being edited/created. */
	private AudioBox mBox;
	
	/**
	 * Assigns local handles and callbacks for widgets in the UI.
	 */
	private void loadWidgetsFromView() {
		mSetSourceButton = (Button) findViewById(R.id.source_button);
		mSetSourceButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(getBaseContext(), AudioBrowser.class);
				startActivityForResult(i, 0);
			}

		});

		mClipOffsetBar = (SeekBar) findViewById(R.id.clip_offset_bar);
		mClipOffsetBar
				.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

					@Override
					public void onProgressChanged(SeekBar seekBar,
							int progress, boolean fromUser) {
						/* Assign new clipBegin time from SeekBar position */
						double maxClipBegin = mBox.getClipDuration()
								- mBox.getDuration();
						mBox.setClipBegin(Composer.snapTo(progress
								* (1.0 / mClipOffsetBar.getMax())
								* maxClipBegin));
						
						/* Update UI labels */
						mClipBeginLabel.setText("Clip Begin: "
								+ mBox.getClipBegin());
						mClipEndLabel.setText("Clip End: "
								+ mBox.getClipEnd());
					}

					@Override
					public void onStartTrackingTouch(SeekBar seekBar) {
						// do nothing
					}

					@Override
					public void onStopTrackingTouch(SeekBar seekBar) {
						// do nothing
					}

				});

		mClipBeginLabel = (TextView) findViewById(R.id.clip_begin_label);
		mClipEndLabel = (TextView) findViewById(R.id.clip_end_label);
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
	protected void onActivityResult (int requestCode,
			int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			/* Media was successfully chosen */
			mBox.setName(data.getStringExtra("name"));
			mBox.setId(data.getStringExtra("id"));
			mBox.setClipDuration(data.getDoubleExtra("length", 1.0));
			mBox.setSource(data.getStringExtra("source"));
		}
	}
	
	@Override
	public void onBackPressed() {
		/* If all required fields are filled in */
		if (mBox.getSource() != null) {
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
		setContentView(R.layout.audio_properties);
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
		mBox = (AudioBox) ((SMILCloud) getApplication()).getSelectedBox();
		
		if (mBox == null) {
			/* Media must be created */
			mBox = new AudioBox(null, 0.0, 1.0, 1.0);
			mTrackManager.addBox(mBox, mBox.getBegin());
		}
		
		/* Initialize the SeekBar */
		double range = mBox.getClipDuration() - mBox.getDuration();
		double val = mBox.getClipBegin() / range;
		mClipOffsetBar.setProgress((int) (val * mClipOffsetBar.getMax()));
		
		/* Disallow editing of the media's source if it's already set */
		if (mBox.getSource() != null) {
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
