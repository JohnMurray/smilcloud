package edu.nku.cs.csc440.team2.composer;

import java.util.LinkedList;
import java.util.List;

import edu.nku.cs.csc440.team2.SMILCloud;
import edu.nku.cs.csc440.team2.mediaCloud.Media;
import edu.nku.cs.csc440.team2.provider.MediaProvider;
import edu.nku.cs.csc460.team2.R;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * An AudioBrowser gets a list of available audio Media from the MediaProvider
 * and displays it in a scrollable list. When an item is clicked, information
 * about the selected Media is returned to the calling Activity in an Intent.
 * 
 * Much of this code was adapted from
 * http://softwarepassion.com/android-series-custom-listview-items-and-adapters
 * 
 * @author William Knauer <knauerw1@nku.edu>
 * @version 2011.0420
 */
public class AudioBrowser extends ListActivity {
	/**
	 * Custom ArrayAdapter to load an array of audio type Media objects into a
	 * ListView.
	 */
	public class AudioListAdapter extends ArrayAdapter<Media> {
		private List<Media> mItems;

		/**
		 * Class constructor.
		 * 
		 * @param context
		 *            The context used to construct Views.
		 * @param textViewResourceId
		 *            The layout resource used by this adapter.
		 * @param objects
		 *            The List of audio type Media objects to be shown.
		 */
		public AudioListAdapter(Context context, int textViewResourceId,
				List<Media> objects) {
			super(context, textViewResourceId, objects);
			mItems = objects;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			if (view == null) {
				LayoutInflater inflater = (LayoutInflater)
						getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = inflater.inflate(R.layout.audio_browser_row, null);
			}
			Media m = mItems.get(position);
			if (m != null) {
				TextView name = (TextView) view
						.findViewById(R.id.audio_browser_row_name);
				if (name != null) {
					name.setText(m.getName());
				}
				TextView length = (TextView) view
						.findViewById(R.id.audio_browser_row_time);
				if (length != null) {
					length.setText(m.getDuration());
				}
				ImageView thumb = (ImageView) view.findViewById(R.id.audio_browser_row_thumb);
				if (thumb != null) {
					//thumb.setImageBitmap(mProvider.getImage(m.getThumbUrl()));
				}
			}
			return view;
		}

	}

	/** Dialog instructing the user to wait upon retrieving data for list */
	private ProgressDialog mProgressDialog;

	/** The Media to be displayed in this ListActivity */
	private List<Media> mMedia;

	/** The adapter for displaying audio Media in a ListActivity */
	private AudioListAdapter mAudioListAdapter;
	
	/** Flag for whether or not media was successfully retrieved */
	private boolean mServerDown;

	/** Loads media into mMedia */
	private Runnable mViewMedia = new Runnable() {

		@Override
		public void run() {
			getMedia();
		}

	};

	/** The MediaProvider used to retrieve Media from the cloud */
	private MediaProvider mProvider;

	/**
	 * Tells the ListAdapter that its data has changed and dismisses the
	 * "Please wait" dialog.
	 */
	private Runnable finishMediaRetrieval = new Runnable() {

		@Override
		public void run() {
			if (mMedia != null && mMedia.size() > 0) {
				mAudioListAdapter.notifyDataSetChanged();
			}
			mProgressDialog.dismiss();
			if (mServerDown) {
				Toast.makeText(getBaseContext(),
						"Unable to connect to server.",
						Toast.LENGTH_LONG).show();
			}
		}

	};

	/**
	 * Retrieves the Media from the cloud and stores it in mMedia.
	 */
	public void getMedia() {
		Media[] media = mProvider.getAllMedia(
				((SMILCloud) getApplication()).getUserId());
		
		/* Keep program from crashing if cloud is not accessible */
		if (media != null) {
			for (int i = 0; i < media.length; i ++) {
				if (media[i].getType().equalsIgnoreCase("audio")) {
					mMedia.add(media[i]);
				}
			}
			mServerDown = false;
		} else {
			mServerDown = true;
		}
		
		runOnUiThread(finishMediaRetrieval);
	}

	@Override
	public void onBackPressed() {
		/* Return that we canceled */
		setResult(RESULT_CANCELED);
		finish();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.audio_browser);
		mProvider = new MediaProvider();
		mMedia = new LinkedList<Media>();
		mServerDown = false;
		mAudioListAdapter = new AudioListAdapter(this,
				R.layout.audio_browser_row, mMedia);
		setListAdapter(mAudioListAdapter);

		/*
		 * Load Media from cloud in a separate thread while displaying a
		 * "Please wait" message in this UI thread
		 */
		Thread thread = new Thread(null, mViewMedia, "MagentoBackground");
		thread.start();
		mProgressDialog = ProgressDialog.show(AudioBrowser.this,
				"Please wait...", "Retrieving list...", true);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Media m = (Media) l.getItemAtPosition(position);

		/* Calculate duration in seconds */
		String[] dur = m.getDuration().split(":");
		double seconds = Double.parseDouble(dur[2]);
		seconds += Double.parseDouble(dur[1]) * 60.0;
		seconds += Double.parseDouble(dur[0]) * 60.0 * 60.0;
		
		/* Dump data into intent */
		Intent i = new Intent();
		i.putExtra("name", m.getName());
		i.putExtra("id", m.getMediaId());
		i.putExtra("length", seconds);
		i.putExtra("source", m.getMediaUrl());
		i.putExtra("thumb", m.getThumbUrl());

		/* Return result and finish */
		setResult(RESULT_OK, i);
		finish();
	}
}
