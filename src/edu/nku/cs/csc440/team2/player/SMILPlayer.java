package edu.nku.cs.csc440.team2.player;

import java.util.ArrayList;

import edu.nku.cs.csc460.team2.R;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 
 * @author john
 *
 */
public class SMILPlayer extends Activity {
    
	private final int CONTROL_ID = 99999;
	private ArrayList<RelativeLayout> zIndexes = new ArrayList<RelativeLayout>(); 
	private RelativeLayout root;
	private boolean hasBeenTouched = true;
	
	/**
     * @param savedInstanceState
     * @return void
     * @note Activity should run in landscape mode.
     * 
     * Call when Activity is first created
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	//create the main instance and get the root View
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_main);
        this.root = (RelativeLayout)findViewById(R.id.player_root_layout);
        
        
        //TESTING STUFF BELOW HERE (FOR THIS METHOD)
        //add some stuff to the root layout
        RelativeLayout text = new RelativeLayout(this);
        TextView tv1 = new TextView(this);
        tv1.setText("ahhHH!!!!ksdjf");
        text.addView(tv1);
        this.root.addView(text);
        
        /**
         * This isn't really testing information, we will be using this code only
         * slightly modified when using Shane's code.
         */
        int [] indexes = {1, 2, 3};
        ArrayList<ArrayList<Object>> zees = LayerBuilder.buildZIndexes(indexes, this);
        for( ArrayList<Object> tuple : zees)
        {
        	if( tuple.size() == 2)
        	{
        		this.root.addView((RelativeLayout)tuple.get(0), (RelativeLayout.LayoutParams)tuple.get(1));
        	}
        	else
        	{
        		this.root.addView((RelativeLayout)tuple.get(0));
        	}
        	this.zIndexes.add((RelativeLayout)tuple.get(0));
        }
        
        //TODO:add overlaying View w/ action to show player controls on click
        this.addControls();
        
    }
    
    /**
     * 
     * @param v
     * 
     * Add player to a given view (most likely the root view). This should also
     * be applied last, since it will insert an overlaying view. 
     */
    private void addControls()
    {
    	//create control overlay and add it to the root view
    	LayoutInflater inflater = getLayoutInflater();
    	RelativeLayout ctrl_overlay = (RelativeLayout)inflater.inflate(R.layout.player_controls, null);
    	ctrl_overlay.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO: write code to add other controls and what not (the fade in / fade out)
				if( ! hasBeenTouched )
				{
					hasBeenTouched = true;
				}
				else
				{
					hasBeenTouched = false;
				}
				return false;
			}
		});
    	ctrl_overlay.setId(CONTROL_ID);
    	this.root.addView(ctrl_overlay);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.player_menu, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) 
    {
    	switch(item.getItemId())
    	{
    	case R.id.go_to_library:
    		this.goToLibrary();
    		return true;
    	case R.id.exit_player:
    		finish();
    		return true;
    	default:
    		return super.onOptionsItemSelected(item);
    	}
    }
    
    
    /**
     * @author john
     * @return void
     * 
     * Launch the Library Sub-Activity
     */
    private void goToLibrary()
    {
    	//TODO: write code to launch Library Sub-Activity
    }
    
    
    
    
    /**
     * @author john
     * 
     * Used to build the z-index ViewGroup layers
     */
    //TODO: alter code to use Shane's Region objects to generate more accurate Views with correct
    //		origins (not just 0,0 all the time) 
    private static class LayerBuilder
    {
    	/**
    	 * @author john 
    	 * @param indexes
    	 * @param context
    	 * @return ArrayList<ArrayList<Object>>
    	 * 
    	 * Factory function for building the RelativeLayouts for placing objects
    	 * into and what not. 
    	 */
    	public static ArrayList<ArrayList<Object>> buildZIndexes(int [] indexes, SMILPlayer context)
    	{
    		ArrayList<ArrayList<Object>> list = new ArrayList<ArrayList<Object>>();
    		//build layers (z-indicies)
    		for(int i : indexes)
    		{
    			RelativeLayout tempView = new RelativeLayout(context);
    			//TODO: replace width and height with actual values from shane's code objects
    			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
    					RelativeLayout.LayoutParams.FILL_PARENT,
    					RelativeLayout.LayoutParams.WRAP_CONTENT);
    			tempView.setId(i);
    			
    			//TODO: remove the following code chunk, testing only
    			TextView tv = new TextView(context);
    			tv.setText("testing code testing code");
    			tempView.addView(tv);
    			
    			ArrayList<Object> tempArrayList = new ArrayList<Object>();
    			tempArrayList.add(tempView);
    			tempArrayList.add(lp);
    			list.add(tempArrayList);
    		}
    		return list;
    	}
    }
    
    
    
    
    /**
     * @author john
     * 
     * Build the SMIL player object's from Shane's parsed objects and what not		
     */
    private class SMILPlayerBuilder
    {
    	/**
    	 * @author john
    	 * @return SeqPlayer
    	 * @param Shane's parsed object (?)
    	 */
    	public SMILPlayerBuilder() //Shane's parsed object
    	{
    		
    	}
    	
    	/**
    	 * @author john
    	 * @return SeqPlayer
    	 * 
    	 * Generate a static SMIL object for testing purposes
    	 */
    	public Player genTestStub()
    	{
    		return null;
    	}
    }
}