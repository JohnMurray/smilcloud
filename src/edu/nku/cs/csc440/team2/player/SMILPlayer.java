package edu.nku.cs.csc440.team2.player;

import java.util.ArrayList;
import java.util.concurrent.Callable;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import edu.nku.cs.csc440.team2.SMILCloud;
import edu.nku.cs.csc460.team2.R;

/**
 * 
 * @author john
 *
 */
public class SMILPlayer extends Activity {
    
	private final int CONTROL_ID = 99999;
	private ArrayList<RelativeLayout> zIndexes = new ArrayList<RelativeLayout>(); 
	private RelativeLayout root;
	private boolean hasBeenTouched = false;
	//private ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(2);
	//private ScheduledFuture<Void> scheduledFuture = null;
	
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
        //TODO: write new testing code using the PriceLine (negotiator) class. 
        /*
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
        */
        
        this.addControls();
        
        
        /*
         * test code here
         */
        SMILCloud smilCloud = ((SMILCloud)getApplicationContext());
        smilCloud.queueDocumentToPlay("testing id string");
        Log.i("Main Application", smilCloud.getQueuedDocumentForPlayback());
        
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
    	//Inflate the overlay from XML specification and set the initial view to be invisible.
    	LayoutInflater inflater = getLayoutInflater();
    	RelativeLayout ctrl_overlay = (RelativeLayout)inflater.inflate(R.layout.play_controls_yt, null);
    	Animation animation = new AlphaAnimation(1.0f, 0.0f);
    	animation.setFillAfter(true);
    	ctrl_overlay.startAnimation(animation);
    	ctrl_overlay.setVisibility(View.INVISIBLE);
    	
    	//Set the onTouch event listener such that it will hide and 
    	ctrl_overlay.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// write code to add other controls and what not (the fade in / fade out)
				if( ! hasBeenTouched )
				{
					(new ShowControls(v)).call();
					Log.i("Main Activity", "control overlay has been touched");
					// TODO: fix executer so that it actually works... idk?
					//scheduledFuture = scheduledThreadPoolExecutor.schedule(
					//		new HideControls(v), 5L, TimeUnit.SECONDS);
					hasBeenTouched = true;
				}
				else
					
				{
					(new HideControls(v)).call();
					hasBeenTouched = false;
					Log.i("Main Activity", "controlOverlay has been pressed but has already been touched");
				}
				return false;
			}
		});
    	/*
    	 * Note: might be able to do this better by specifying the onTouch handlers in XML
    	 * 
    	 * Get the references to all the controls we will be using in the Player
    	 */
    	ImageButton playPause = (ImageButton)(((ViewGroup)ctrl_overlay.getChildAt(0)).getChildAt(0));
    	ImageButton rewind = (ImageButton)(((ViewGroup)ctrl_overlay.getChildAt(0)).getChildAt(1));
    	ImageButton fastForward = (ImageButton)(((ViewGroup)ctrl_overlay.getChildAt(0)).getChildAt(2));
    	SeekBar seekBar = (SeekBar)(((ViewGroup)ctrl_overlay.getChildAt(0)).getChildAt(3));
    	
    	/*
    	 * For each control that we will be using for the Player we will define
    	 * the on touch events. 
    	 */
    	playPause.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if( !hasBeenTouched )
				{
					(new ShowControls((View)v.getParent().getParent())).call();
					Log.i("Main Activity", "playPause has been pressed");
					hasBeenTouched = true;
				}
				else	
				{
					/*
					 * TODO: add code to implement onTouch listener for
					 * current control
					 */
					Log.i("Main Activity", "playPause has been pressed but has already been touched");
				}
			}
		});
    	
    	rewind.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if( !hasBeenTouched )
				{
					(new ShowControls((View)v.getParent().getParent())).call();
					Log.i("Main Activity", "rewind has been pressed");
					hasBeenTouched = true;
				}
				else	
				{
					/*
					 * TODO: add code to implement onTouch listener for
					 * current control
					 */
					Log.i("Main Activity", "rewind has been pressed but has already been touched");
				}
			}
		});
    	
    	fastForward.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if( !hasBeenTouched )
				{
					(new ShowControls((View)v.getParent().getParent())).call();
					Log.i("Main Activity", "fastForward has been pressed");
					hasBeenTouched = true;
				}
				else	
				{
					/*
					 * TODO: add code to implement onTouch listener for
					 * current control
					 */
					Log.i("Main Activity", "fastforward has been pressed but has already been touched");
				}
			}
		});
    	
    	seekBar.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if( !hasBeenTouched )
				{
					(new ShowControls((View)v.getParent().getParent())).call();
					Log.i("Main Activity", "seekBar has been pressed");
					hasBeenTouched = true;
				}
				else	
				{
					/*
					 * TODO: add code to implement onTouch listener for
					 * current control
					 */
					Log.i("Main Activity", "seekBar has been pressed but has already been touched");
				}
			}
		});
    	
    	/*
    	 * Apply the (totally pimp'd out) controls to the page
    	 */
    	ctrl_overlay.setId(CONTROL_ID);
    	this.root.addView(ctrl_overlay);
    }
    
    /**
     * 
     * @author john
     *
     * Define a Callable class to hide controls so that this can be threaded for
     * delayed and timed events.
     */
    private class HideControls implements Callable<Void>
    {
    	private View v;
    	public HideControls(View v)
    	{
    		this.v = v;
    	}
    	public Void call()
    	{
    		Animation myFadeOutAnimation = AnimationUtils.loadAnimation(v.getContext(), R.anim.player_fade_out);
    		myFadeOutAnimation.setFillAfter(true);
    		this.v.startAnimation(myFadeOutAnimation);
    		return null;
    	}
    }
    
    /**
     * 
     * @author john
     * 
     * Define a Callable class to show controls so that this can be threaded for
     * delayed and timed events
     */
    private class ShowControls implements Callable<Void>
    {
    	private View v;
    	public ShowControls(View v)
    	{
    		this.v = v;
    	}
    	public Void call()
    	{
    		Animation myFadeInAnimation = AnimationUtils.loadAnimation(v.getContext(), R.anim.player_fade_in);
    		myFadeInAnimation.setFillAfter(true);
    		v.startAnimation(myFadeInAnimation);
    		return null;
    	}
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
    /*private static class LayerBuilder
    {
    	/**
    	 * @author john 
    	 * @param indexes
    	 * @param context
    	 * @return ArrayList<ArrayList<Object>>
    	 * 
    	 * Factory function for building the RelativeLayouts for placing objects
    	 * into and what not. 
    	 *
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
    }*/
}