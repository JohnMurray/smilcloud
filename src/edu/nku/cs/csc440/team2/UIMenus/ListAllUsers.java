package edu.nku.cs.csc440.team2.UIMenus;

import java.util.ArrayList;

import edu.nku.cs.csc440.team2.SMILCloud;
import edu.nku.cs.csc440.team2.provider.MessageProvider;
import edu.nku.cs.csc440.team2.provider.UserProvider;
import edu.nku.cs.csc460.team2.R;
import android.app.ListActivity;
import android.os.Bundle;
import edu.nku.cs.csc440.team2.mediaCloud.Pair;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

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
				
				Toast.makeText(ListAllUsers.this, "Shared Message With " + contactList.get(position).one, 
						Toast.LENGTH_LONG).show();
				ListAllUsers.this.finish();
			}
        });
    }

}