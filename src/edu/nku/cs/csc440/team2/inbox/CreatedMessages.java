package edu.nku.cs.csc440.team2.inbox;

import java.util.ArrayList;

import net.londatiga.android.ActionItem;
import net.londatiga.android.NewQAAdapter;
import net.londatiga.android.QuickAction;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.Toast;
import edu.nku.cs.csc440.team2.SMILCloud;
import edu.nku.cs.csc440.team2.UIMenus.ListAllUsers;
import edu.nku.cs.csc440.team2.composer.Composer;
import edu.nku.cs.csc440.team2.mediaCloud.MessageLite;
import edu.nku.cs.csc440.team2.player.SMILPlayer;
import edu.nku.cs.csc440.team2.provider.MessageProvider;
import edu.nku.cs.csc460.team2.R;

/**
 * Activity that shows the user's created messages. Utilizes the QuickAction
 * notfications. Offers options to create, edit, share, delete, and edit
 * messages.
 * 
 * @author John Murray
 * @version 1.0 4/24/11
 */
public class CreatedMessages extends Activity {

	/**
	 * The list of messages shown by the activity
	 */
	private ArrayList<MessageLite> messages;

	/**
	 * The adapter for hte menu items
	 */
	private NewQAAdapter adapter;

	/**
	 * the list view that will be populated with data
	 */
	private ListView mList;

	/**
	 * The titles of the items in the list
	 */
	private String[] data;

	/**
	 * Call when Activity is first created
	 * 
	 * @param savedInstanceState
	 * @return void
	 * @note Activity should run in landscape mode.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.created_messages_main);

		/*
		 * Get the list of messages and put the names into a string
		 */
		this.messages = this.getMessages();
		data = new String[this.messages.size()];
		if (this.messages != null) {
			for (int i = 0; i < this.messages.size(); i++) {
				data[i] = this.messages.get(i).getName();
			}
		}

		/*
		 * Define the list view and adapter and set the data that we collected
		 * in the section above
		 */
		mList = (ListView) findViewById(R.id.l_list);
		adapter = new NewQAAdapter(this);

		adapter.setData(data);
		mList.setAdapter(adapter);

		/*
		 * Define the actions to be used in the quick action menu
		 */
		final ActionItem shrAction = new ActionItem();

		shrAction.setTitle("Share");
		shrAction.setIcon(getResources().getDrawable(R.drawable.inbox_share));

		final ActionItem plyAction = new ActionItem();

		plyAction.setTitle("Play");
		plyAction.setIcon(getResources().getDrawable(R.drawable.inbox_play));

		final ActionItem delAction = new ActionItem();

		delAction.setTitle("Delete");
		delAction.setIcon(getResources().getDrawable(R.drawable.inbox_delete));

		final ActionItem edtAction = new ActionItem();

		edtAction.setTitle("Edit");
		edtAction.setIcon(getResources().getDrawable(R.drawable.inbox_edit));

		/*
		 * Set an onClick listener for the quick action. For each action, define
		 * a separate action
		 */
		mList.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				final QuickAction mQuickAction = new QuickAction(view);

				final ImageView mMoreImage = (ImageView) view
						.findViewById(R.id.i_more);

				final String text = data[position];

				mMoreImage.setImageResource(R.drawable.ic_list_more_selected);

				String tempId = null;
				for (MessageLite ml : CreatedMessages.this.messages) {
					if (ml.getName() == text) {
						tempId = ml.getUniqueId();
					}
				}
				final String messageId = tempId;

				/*
				 * Define the click listener for sharing
				 */
				shrAction.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						((SMILCloud) CreatedMessages.this.getApplication())
								.setSharedMessageId(messageId);
						Intent i = new Intent(CreatedMessages.this,
								ListAllUsers.class);
						CreatedMessages.this.startActivity(i);
						mQuickAction.dismiss();
					}
				});

				/*
				 * Define a click listener for playing
				 */
				plyAction.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						mQuickAction.dismiss();

						((SMILCloud) CreatedMessages.this.getApplication())
								.queueDocumentToPlay(messageId);
						Intent i = new Intent(CreatedMessages.this,
								SMILPlayer.class);
						CreatedMessages.this.startActivity(i);
					}
				});

				/*
				 * Define a click listener for deletion
				 */
				delAction.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Toast.makeText(CreatedMessages.this, "Deleted " + text,
								Toast.LENGTH_SHORT).show();
						mQuickAction.dismiss();
						// call provider to delete this message asynchronously
						new Thread(new Runnable() {
							@Override
							public void run() {
								(new MessageProvider())
										.deleteMessage(messageId);
								runOnUiThread(new Runnable() {
									@Override
									public void run() {
										CreatedMessages.this.resetData();
									}
								});
							}
						}).start();
						;
					}
				});

				/*
				 * Define a click listener for deletion
				 */
				edtAction.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Toast.makeText(CreatedMessages.this, "Edit " + text,
								Toast.LENGTH_SHORT).show();
						((SMILCloud) CreatedMessages.this.getApplication())
								.queueDocumentToEdit(messageId);
						Intent i = new Intent(CreatedMessages.this,
								Composer.class);
						CreatedMessages.this.startActivity(i);
						mQuickAction.dismiss();
					}
				});

				mQuickAction.addActionItem(plyAction);
				mQuickAction.addActionItem(shrAction);
				mQuickAction.addActionItem(edtAction);
				mQuickAction.addActionItem(delAction);

				mQuickAction.setAnimStyle(QuickAction.ANIM_AUTO);

				mQuickAction.setOnDismissListener(new OnDismissListener() {
					@Override
					public void onDismiss() {
						mMoreImage.setImageResource(R.drawable.ic_list_more);
					}
				});

				mQuickAction.show();
			}
		});

	}

	/**
	 * Reload the data in the list
	 */
	private void resetData() {
		this.messages = this.getMessages();
		this.data = new String[this.messages.size()];
		if (this.messages != null) {
			for (int i = 0; i < this.messages.size(); i++) {
				data[i] = this.messages.get(i).getName();
			}
		}
		this.adapter.setData(data);
		mList.setAdapter(adapter);
	}

	/**
	 * Load the menu into the Activity
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.inbox_menu, menu);
		return true;
	}

	/**
	 * Define what happens when itmes are selected
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.create_message:
			((SMILCloud) CreatedMessages.this.getApplication())
					.queueDocumentToEdit(null);
			CreatedMessages.this.startActivity(new Intent(CreatedMessages.this,
					Composer.class));
			return true;
		case R.id.exit_player:
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * Get a list of messages to show.
	 * 
	 * @return The list of messages to show
	 */
	private ArrayList<MessageLite> getMessages() {
		ArrayList<MessageLite> m = (new MessageProvider())
				.getSavedMessages(((SMILCloud) getApplication()).getUserId());
		if (m == null) {
			return new ArrayList<MessageLite>();
		}
		return m;
	}

}
