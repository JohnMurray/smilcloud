package edu.nku.cs.csc440.team2.service;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.R;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import edu.nku.cs.csc440.team2.SMILCloud;
import edu.nku.cs.csc440.team2.inbox.Inbox;
import edu.nku.cs.csc440.team2.mediaCloud.MessageLite;
import edu.nku.cs.csc440.team2.provider.MessageProvider;

public class SmilService extends Service {

	/** For showing and hiding our notification. */
	private NotificationManager mNM;

	private Timer timer = new Timer();

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

		timer.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				showNotification();
			}
		}, 0, 15000);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	private void showNotification() {

		/*
		 * Pull the current user's ID from the application data. If the user
		 */
		SMILCloud app = (SMILCloud) getApplication();
		int userId = app.getUserId();
		if (userId != SMILCloud.NO_USER) {

			MessageProvider mp = new MessageProvider();

			ArrayList<MessageLite> serverList = mp.getAllMessage(userId);
			ArrayList<MessageLite> appList = app.getMessages();
			
			for( MessageLite m : serverList )
			{
				if( appList != null && ! appList.contains(m) )
				{
					CharSequence text = "You've got SMIL: " + m.getName();

					// Set the icon, scrolling text and timestamp
					Notification notification = new Notification(
							R.drawable.sym_action_email, "You've got SMIL!",
							System.currentTimeMillis());

					// The PendingIntent to launch our activity if the user selects this
					// notification
					PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
							new Intent(this, Inbox.class), 0);

					// Set the info for the views that show in the notification panel.
					notification.setLatestEventInfo(this, getText(R.string.no), text,
							contentIntent);

					// Send the notification.
					mNM.notify(R.string.yes, notification);
				}
			}
			
			app.setMessages(serverList);

			
		}
	}
}
