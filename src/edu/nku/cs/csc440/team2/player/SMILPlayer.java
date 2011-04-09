package edu.nku.cs.csc440.team2.player;

import java.io.File;

import java.util.ArrayList;

import java.util.concurrent.Callable;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
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
import edu.nku.cs.csc440.team2.SMILCloud;
import edu.nku.cs.csc440.team2.provider.MediaProvider;
import edu.nku.cs.csc440.team2.message.Message;
import edu.nku.cs.csc460.team2.R;

/**
 * 
 * @author john
 *
 */
public class SMILPlayer extends Activity {
    
	private final int CONTROL_ID = 99999; 
	private RelativeLayout rootView;
	private SeqPlayer root;
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
    	
    	
		//MediaProvider mp = new MediaProvider();
    	
    	//File f = new File(Environment.getExternalStorageDirectory() + "/test2.png");
    	
    	//mp.saveMedia(f.getPath(), "image", 1);
    	
    	
    	//create the main instance and get the root View
    	/*
    	 * create the main instance and get the root View
    	 */
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_main);
        this.rootView = (RelativeLayout)findViewById(R.id.player_root_layout);
        
        RelativeLayout videoContainer = this.loadVideoContainer();
        
        /*
         * Check to see if there is  SMIL message that is queued to play
         * and if there is, then request to get the message and media
         * from the provider.
         * 
         * If there is nothing to play, then don't do anything... we
         * are done for now I suppose... just leave the screen black
         * and hope that they can figure out what they are doing.
         */
        SMILCloud smilCloud = ((SMILCloud)getApplicationContext());
        String playbackID = smilCloud.getQueuedDocumentForPlayback();
        if( !(playbackID == null) && playbackID.length() != 0 )
        {
        	//TODO: write code to get message and initialize structures
        }     
        
        
        /*
         * Well, I lied. We are not entirely done yet... We need
         * to add some controls to the player so that the use has
         * at least a little control over things. 
         */
        this.addControls();
        
        
        
        
        /*
         * TEST CODE:
         * 		to load a smil message locally and play it. This only uses
         * 		the TestMedia instance now, no actual text, video, etc. 
         */
		File f = new File(Environment.getExternalStorageDirectory()
				+ "/image_only_message.smil");
		
		Message message = null;
		try 
		{
			message = new Message(f);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		PriceLine pl = new PriceLine(message, this, videoContainer);
		pl.negotiateBigDeal();
		this.root = (SeqPlayer)pl.getDocumentAndNameYourOwnPrice();
		this.preparePlayer();
		Log.w("hello", "there");
		
		this.startPlayback();
		
    }
    
    
    
    private void startPlayback()
    {
    	this.root.play();
    	try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    
    /**
     * Prepare the media, and sit in a busy wait until the
     * preparation of the data-structure is done... we might
     * want to display some type of wait-thing on the UI thread,
     * but I guess that will come later.
     * 
     * TODO: add waiting bar/loader thing to the UI while preparing
     */
    private void preparePlayer()
    {
    	this.root.prepare();
    	Arbiter subject = this.root.getSubject();
    	subject.setRootSeq(this.root);
    	while( ! subject.isBufferQueueEmpty() );
    }
    
    
    
    /**
     * Define the layout that will act as the container to hold
     * the video. This will be passed in, along with the context
     * and the SMIL message during translation. 
     */
    private RelativeLayout loadVideoContainer()
    {
    	RelativeLayout videoContainer = new RelativeLayout(this);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
        		RelativeLayout.LayoutParams.MATCH_PARENT,
        		RelativeLayout.LayoutParams.MATCH_PARENT);
        lp.setMargins(0, 0, 0, 0);
        videoContainer.setLayoutParams(lp);
        this.rootView.addView(videoContainer);
        return videoContainer;
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
    	this.rootView.addView(ctrl_overlay);
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
    
    /**
     * Inflate the options menu from XML
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.player_menu, menu);
        return true;
    }
    
    /**
     * Define an action to perform on menu-item select
     */
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
}