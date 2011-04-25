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
 * An AudioProperties Activity is responsible for creating and editing an
 * AudioBox. The begin and duration for playback cannot be changed from here.
 * 
 * @author William Knauer <knauerw1@nku.edu>
 * @version 2011.0424
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
						int maxClipBegin = mBox.getClipDuration()
								- mBox.getDuration();
						mBox.setClipBegin((int) (progress
								* (1.0 / mClipOffsetBar.getMax()) * maxClipBegin));

						/* Update UI labels */
						mClipBeginLabel.setText("Clip Begin: "
								+ AudioVideoBox.formatTime(mBox.getClipBegin()));
						mClipEndLabel.setText("Clip End: "
								+ AudioVideoBox.formatTime(mBox.getClipEnd()));
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
		if (resultCode == RESULT_OK) {
			/* Media was successfully chosen */
			mBox.setName(data.getStringExtra("name"));
			mBox.setClipDuration(data.getIntExtra("length", 10));
			mBox.setSource(data.getStringExtra("source"));

			/* Disallow editing of the media's source if it's already set */
			if (mBox.getSource() != null) {
				mSetSourceButton.setEnabled(false);
				mSetSourceButton.setText(mBox.getName());
			}
		}
	}

	@Override
	public void onBackPressed() {
		/* If all required fields are filled in */
		if (mBox.getSource() != null) {
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
		setContentView(R.layout.audio_properties);
		loadWidgetsFromView();

		/* Load from Intent */
		mTrackManager = getIntent().getParcelableExtra("track_manager");
		String boxId = getIntent().getStringExtra("box_id");
		mBox = (AudioBox) mTrackManager.getBox(boxId);

		if (mBox == null) {
			/* Media must be created */
			mBox = new AudioBox(null, 0, 10, 10);
			mTrackManager.addBox(mBox, mBox.getBegin());
		}
	}

	@Override
	protected void onStart() {
		super.onStart();

		/* Initialize the SeekBar */
		int range = mBox.getClipDuration() - mBox.getDuration();
		double val = ((double) mBox.getClipBegin()) / range;
		mClipOffsetBar.setProgress((int) (val * mClipOffsetBar.getMax()));

		/* Disallow editing of the media's source if it's already set */
		if (mBox.getSource() != null) {
			mSetSourceButton.setEnabled(false);
			mSetSourceButton.setText(mBox.getName());
		}
	}

}
