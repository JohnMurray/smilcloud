package edu.nku.cs.csc440.team2.composer;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import edu.nku.cs.csc440.team2.mediaCloud.Media;
import edu.nku.cs.csc440.team2.provider.MediaProvider;
import edu.nku.cs.csc460.team2.R;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * By tutorial from http://www.softwarepassion.com/android-series-custom-listview-items-and-adapters/
 * 
 * @author William Knauer <knauerw1@nku.edu>
 * @version 2011.0417
 */
public class AudioBrowser extends ListActivity {
	private ProgressDialog mProgressDialog;
	private List<Media> mMedia;
	private AudioListAdapter mAudioListAdapter;
	private Runnable viewMedia;
	private MediaProvider mProvider;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.audio_browser);
		mProvider = new MediaProvider();
		mMedia = new LinkedList();
	}
	
	public class AudioListAdapter extends ArrayAdapter<Media> {
		private List<Media> mItems;

		public AudioListAdapter(Context context, int resource,
				int textViewResourceId, List<Media> objects) {
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
				TextView name =
					(TextView) view.findViewById(R.id.audio_browser_row_name);
				if (name != null) {
					name.setText(m.getName());
				}
				TextView time =
					(TextView) view.findViewById(R.id.audio_browser_row_time);
				if (time != null) {
					time.setText("9:99"); // TODO replace with m.getTime() ?
				}
			}
			return view;
		}
		
	}
}
