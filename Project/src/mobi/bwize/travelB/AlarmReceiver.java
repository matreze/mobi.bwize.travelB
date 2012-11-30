/* Group 2 
 * travelB - A user friendly travel planner
 * 
 * By:
 * Amabille Leal
 * Dominika Nowak
 * Alison Price
 * Michael Reid
 * 
 * Date: 30th November 2012
 * 
 * v 1.0
 * 
 * File Name: AlarmReceiver.java
 * Description:
 * 
 * 
 * 
 */

package mobi.bwize.travelB;

import mobi.bwize.travelB.R;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class AlarmReceiver extends BroadcastReceiver {

	private static int NOTIFICATION_ID = 1;
	private NotificationManager manager = null;

	@Override
	public void onReceive(Context context, Intent intent) {

		if (intent.getStringExtra("type").equals("note")) {

			//manager = (NotificationManager) .getSystemService(Context.NOTIFICATION_SERVICE);
			manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
			Bundle extras = intent.getExtras();
			
			Intent intent_tips=new Intent(context, TipDisplay.class);
			intent_tips.putExtra("title",  extras.getString("title"));
			intent_tips.putExtra("tip",  extras.getString("note"));
			
			Notification notf = new Notification(R.drawable.bee, "Tip!",
					System.currentTimeMillis());
			PendingIntent contentIntent = PendingIntent.getActivity(context,
					NOTIFICATION_ID, intent_tips,
					0);

			// here we get the title and description of our Notification
			
			String title = extras.getString("title");
			String note = "Press to view tip";
			notf.setLatestEventInfo(context, title, note,  contentIntent);
			notf.flags = Notification.FLAG_INSISTENT;
			// here we set the default sound for our notification
			notf.defaults |= Notification.DEFAULT_VIBRATE;

			// The PendingIntent to launch our activity if the user selects this
			// notification
			manager.notify(NOTIFICATION_ID++, notf);

		}

		else {
			//(NotificationManager)
				//	.getSystemService(Context.NOTIFICATION_SERVICE);
			manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

			
			Notification notf = new Notification(R.drawable.warningbee,
					"WARNING:", System.currentTimeMillis());
			PendingIntent contentIntent = PendingIntent.getActivity(context,
					NOTIFICATION_ID, new Intent(context, TripList.class),
					0);

			// here we get the title and description of our Notification
			Bundle extras = intent.getExtras();
			String title = extras.getString("title");
			String note = extras.getString("note");
			notf.setLatestEventInfo(context, title, note, contentIntent);
			notf.flags = Notification.FLAG_INSISTENT;
			notf.defaults |= Notification.DEFAULT_SOUND;
			notf.defaults |= Notification.DEFAULT_VIBRATE;
			notf.defaults |= Notification.DEFAULT_LIGHTS;
			// here we set the default sound for our notification

			// The PendingIntent to launch our activity if the
			manager.notify(NOTIFICATION_ID++, notf);
		}
		
		
	}
	// cancel notification
    public void clearNotification(View v) {
		manager.cancel(NOTIFICATION_ID);
	}
}