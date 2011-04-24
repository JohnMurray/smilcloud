package edu.nku.cs.csc440.team2.UIMenus;

import java.util.ArrayList;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import edu.nku.cs.csc440.team2.SMILCloud;
import edu.nku.cs.csc440.team2.mediaCloud.Pair;
import edu.nku.cs.csc440.team2.provider.MessageProvider;
import edu.nku.cs.csc440.team2.provider.UserProvider;
import edu.nku.cs.csc460.team2.R;

public class ListAllUsers extends ListActivity {
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_list_main);
        
        final ArrayList<Pair<String, Integer>> contactList = (new UserProvider()).getContactList();
        final String [] listItems = new String[contactList.size()];
        for( int i = 0; i < listItems.length; i++ )
        {
        	listItems[i] = contactList.get(i).one;
        }
        
        setListAdapter(new ArrayAdapter<String> (this, 
        		android.R.layout.simple_list_item_1, listItems));
        
        ListView lv = getListView();
        lv.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long id) {
				final int p = position;
				new Thread(new Runnable() {
					@Override
					public void run() {
						(new MessageProvider()).sendMessageById(
								((SMILCloud)ListAllUsers.this.getApplication()).getUserId(), 
								contactList.get(p).two.intValue(), 
								((SMILCloud)ListAllUsers.this.getApplication()).getSharedMessageId());
						((SMILCloud)ListAllUsers.this.getApplication()).setSharedMessageId(null);
					}
				}).run();
				Toast.makeText(ListAllUsers.this, "Shared Message With " + contactList.get(position).one, 
						Toast.LENGTH_LONG).show();
				ListAllUsers.this.finish();
			}
        });
    }

}
