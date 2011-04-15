package edu.nku.cs.csc440.team2.service;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import edu.nku.cs.csc440.team2.inbox.Inbox;
import edu.nku.cs.csc440.team2.mediaCloud.MessageLite;
import edu.nku.cs.csc440.team2.provider.MessageProvider;
import android.R;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

public class SmilService extends Service {
	
	
	/** For showing and hiding our notification. */
    NotificationManager mNM;
    
    private Timer timer = new Timer();
	
    @Override
    public IBinder onBind(Intent arg0) {
          return null;
    }
    @Override
    public void onCreate() {
          super.onCreate();
          mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
          
          timer.scheduleAtFixedRate( new TimerTask() {
			
			@Override
			public void run() {
		          showNotification();
			}
        }, 0, 5000);
    }
    
    @Override
    public void onDestroy() {
          super.onDestroy();
    }
    
    private void showNotification() {
    	
    	
    	/// HERE
    	int userId = 1;
    	
    	String i = Calendar.getInstance().getTimeInMillis() + "";
    	
    	MessageProvider mp = new MessageProvider();
    	
    	MessageLite[] list = mp.getAllMessage(userId);
    	
    	///..................
    	
    	
    	// In this sample, we'll use the same text for the ticker and the expanded notification
        CharSequence text = "You've got SMIL!" + i;//getText(R.string.ok);

        // Set the icon, scrolling text and timestamp
        Notification notification = new Notification(R.drawable.sym_action_email, "You've got SMIL!" + i,
                System.currentTimeMillis());

        // The PendingIntent to launch our activity if the user selects this notification
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, Inbox.class), 0);

        // Set the info for the views that show in the notification panel.
        notification.setLatestEventInfo(this, getText(R.string.no),
                       text, contentIntent);

        // Send the notification.
        mNM.notify(R.string.yes, notification);
    }
}

