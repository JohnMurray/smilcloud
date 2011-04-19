package edu.nku.cs.csc440.team2.composer;

import java.util.LinkedList;
import java.util.List;

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

/**
 * An ImageBrowser gets a list of available image Media from the MediaProvider
 * and displays it in a scrollable list. When an item is clicked, information
 * about the selected Media is returned to the calling Activity in an Intent.
 * 
 * Much of this code was adapted from
 * http://softwarepassion.com/android-series-custom-listview-items-and-adapters
 * 
 * @author William Knauer <knauerw1@nku.edu>
 * @version 2011.0418
 */
public class ImageBrowser extends ListActivity {
	/**
	 * Custom ArrayAdapter to load an array of image type Media objects into a
	 * ListView.
	 */
	public class ImageListAdapter extends ArrayAdapter<Media> {
		private List<Media> mItems;

		/**
		 * Class constructor.
		 * 
		 * @param context
		 *            The context used to construct Views.
		 * @param textViewResourceId
		 *            The layout resource used by this adapter.
		 * @param objects
		 *            The List of image type Media objects to be shown.
		 */
		public ImageListAdapter(Context context, int textViewResourceId,
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
				view = inflater.inflate(R.layout.image_browser_row, null);
			}
			Media m = mItems.get(position);
			if (m != null) {
				TextView name = (TextView) view
						.findViewById(R.id.image_browser_row_name);
				if (name != null) {
					name.setText(m.getName());
				}
				ImageView thumb = (ImageView) view
						.findViewById(R.id.image_browser_row_thumbnail);
				if (thumb != null) {
					//thumb.setImageBitmap(); TODO hook into Media
				}
			}
			return view;
		}

	}

	/** Dialog instructing the user to wait upon retrieving data for list */
	private ProgressDialog mProgressDialog;

	/** The Media to be displayed in this ListActivity */
	private List<Media> mMedia;

	/** The adapter for displaying image Media in a ListActivity */
	private ImageListAdapter mImageListAdapter;

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
				mImageListAdapter.notifyDataSetChanged();
			}
			mProgressDialog.dismiss();
		}

	};

	/**
	 * Retrieves the Media from the cloud and stores it in mMedia.
	 */
	public void getMedia() {
		//Media[] media = mProvider.getAllMedia(0);
		//for (int i = 0; i < media.length; i ++) {
		//	if (media[i].getType().equalsIgnoreCase("image")) {
		//		mMedia.add(media[i]);
		//	}
		//}
		
		Media m1 = new Media("", "", "Cloud crapping a rainbow");
		m1.setType("image");
		mMedia.add(m1);

		Media m2 = new Media("", "", "longcat");
		m2.setType("image");
		mMedia.add(m2);

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
		setContentView(R.layout.image_browser);
		mProvider = new MediaProvider();
		mMedia = new LinkedList<Media>();
		mImageListAdapter = new ImageListAdapter(this,
				R.layout.image_browser_row, mMedia);
		setListAdapter(mImageListAdapter);

		/*
		 * Load Media from cloud in a separate thread while displaying a
		 * "Please wait" message in this UI thread
		 */
		Thread thread = new Thread(null, mViewMedia, "MagentoBackground");
		thread.start();
		mProgressDialog = ProgressDialog.show(ImageBrowser.this,
				"Please wait...", "Retrieving list...", true);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Media m = (Media) l.getItemAtPosition(position);

		/* Dump data into intent */
		Intent i = new Intent();
		i.putExtra("name", m.getName());
		i.putExtra("id", m.getMediaId());
		i.putExtra("source", m.getMediaUrl());
		i.putExtra("thumb", m.getThumbUrl());

		/* Return result and finish */
		setResult(RESULT_OK, i);
		finish();
	}
}
