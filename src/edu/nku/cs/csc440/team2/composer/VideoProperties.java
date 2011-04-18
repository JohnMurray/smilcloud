package edu.nku.cs.csc440.team2.composer;

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
 * A VideoProperties Activity is responsible for creating and editing a
 * VideoBox. The begin and duration for playback cannot be changed from here.
 * 
 * @author William Knauer <knauerw1@nku.edu>
 * @version 2011.0416
 */
public class VideoProperties extends Activity {
	/** Handle for the button that sets the media source. */
	private Button mSetSourceButton;
	
	/** Handle for the SeekBar that sets the media's clipBegin time. */
	private SeekBar mClipOffsetBar;
	
	/** Handle for the label that shows the clipBegin time. */
	private TextView mClipBeginLabel;
	
	/** Handle for the label that shows the clipEnd time. */
	private TextView mClipEndLabel;
	
	/** Handle for the button that opens the RegionEditor */
	private Button mRegionEditButton;
	
	/** Handle for the button that deletes the media from the composer. */
	private Button mDeleteButton;
	
	/** The data structure that contains the message. */
	private TrackManager mTrackManager;
	
	/** The media being edited/created. */
	private VideoBox mBox;
	
	/** The id of the media being edited */
	private String mBoxId;
	
	/**
	 * Launches the RegionEditor activity.
	 */
	private void launchRegionEditor() {
		Intent i = new Intent(this, RegionEditor.class);
		i.putExtra("track_manager", mTrackManager);
		i.putExtra("box_id", mBox.getId());
		startActivityForResult(i, 0);
	}
	
	/**
	 * Assigns local handles and callbacks for widgets in the UI.
	 */
	private void loadWidgetsFromView() {
		mSetSourceButton = (Button) findViewById(R.id.source_button);
		mSetSourceButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO open source editor
				mBox.setSource(mBox.getId());
				mSetSourceButton.setEnabled(false);
			}

		});

		mClipOffsetBar = (SeekBar) findViewById(R.id.clip_offset_bar);
		mClipOffsetBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

					@Override
					public void onProgressChanged(SeekBar seekBar,
							int progress, boolean fromUser) {
						/* Assign new clipBegin time from SeekBar position */
						double maxClipBegin = mBox.getClipDuration()
								- mBox.getDuration();
						mBox.setClipBegin(Composer.snapTo(progress * 0.01
								* maxClipBegin));
						
						/* Update UI labels */
						mClipBeginLabel.setText("Clip Begin: "
								+ mBox.getClipBegin());
						mClipEndLabel.setText("Clip End: " + mBox.getClipEnd());
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
		mRegionEditButton = (Button) findViewById(R.id.region_edit_button);
		mRegionEditButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				launchRegionEditor();
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
		/* Update local data from RegionEditor */
		mTrackManager = data.getParcelableExtra("track_manager");
		mBox = (VideoBox) mTrackManager.getBox(mBoxId);
	}

	@Override
	public void onBackPressed() {
		/* If all required fields are filled in */
		if (mBox.getSource() != null && mBox.getRegion() != null) {
			/* Return the data structure and OK status */
			Intent i = new Intent();
			i.putExtra("track_manager", mTrackManager);
			setResult(RESULT_OK, i);
			finish();
		} else {
			/* Return canceled status */
			setResult(RESULT_CANCELED);
			finish();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.video_properties);
		loadWidgetsFromView();
		
		/* If we are being reconstructed from a previous state */
		if (savedInstanceState != null) {
			mTrackManager = savedInstanceState.getParcelable("track_manager");
			mBoxId = savedInstanceState.getString("box_id");
			mBox = (VideoBox) mTrackManager.getBox(mBoxId);
		} else {
			mTrackManager = getIntent().getParcelableExtra("track_manager");
			/* If we are editing existing media */
			if (getIntent().hasExtra("box_id")) {
				mBoxId = getIntent().getStringExtra("box_id");
				mBox = (VideoBox) mTrackManager.getBox(mBoxId);
			} else {
				/* Media must be created */
				mBox = new VideoBox(null, 0.0, 1.0, 1.0, null);
				mBoxId = mBox.getId();
				mTrackManager.addBox(mBox, mBox.getBegin());
			}
		}
		
		/* Disallow editing of the media's source if it's already set */
		if (mBox.getSource() != null) {
			mSetSourceButton.setEnabled(false);
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putParcelable("track_manager", mTrackManager);
		outState.putString("box_id", mBoxId);
	}

}
