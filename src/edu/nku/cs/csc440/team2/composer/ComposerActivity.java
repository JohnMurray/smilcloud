package edu.nku.cs.csc440.team2.composer;

import java.util.LinkedList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;

/**
 * @author William Knauer <knauerw1@nku.edu>
 * @version 2011.0321
 */
public class ComposerActivity extends Activity
{
	public static final int REQ_CODE_AUDIO_PROP = 23;
	public static final int REQ_CODE_IMAGE_PROP = 24;
	public static final int REQ_CODE_TEXT_PROP = 25;
	public static final int REQ_CODE_VIDEO_PROP = 26;
	
	private ComposerView mTemporalView;
	
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mTemporalView = new ComposerView(this);
        this.setContentView(mTemporalView, new LayoutParams(
        		LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
    	// TODO modify the mediabox based on intent
    }
    
    public void launchPropertiesActivity(Box m, LinkedList<Box> elts)
    {
    	if (m instanceof AudioBox)
    	{
    		// no region
    	}
    	else
    	{
    		// has a region
    	}
    }
}
