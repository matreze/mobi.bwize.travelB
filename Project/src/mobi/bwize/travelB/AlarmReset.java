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
 * File Name: AlarmReset.java
 * Description:
 * 
 * 
 * 
 */

package mobi.bwize.travelB;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.TimeZone;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

public class AlarmReset extends BroadcastReceiver {

	private int depHour;
	private int depMinute;
	private int retHour;
	private int retMinute;
	private int depYear;
	private int depMonthOfYear;
	private int depDayOfMonth;
	private int retYear;
	private int retMonthOfYear;
	private int retDayOfMonth;
	Context context2;

	@Override
	public void onReceive(Context context, Intent intent) {

		context2=context;
		Trip_Database db = new Trip_Database(context);
		Cursor cursor;

		try {
			db.open();

			cursor = db.getAllTrips();

			if (cursor != null) {
				cursor.moveToFirst();
			}

			while (cursor.isAfterLast() == false) {
				if (cursor.getString(cursor.getColumnIndex("notification"))
						.equals("Yes")) {

					String depDateParsed = cursor.getString(
							cursor.getColumnIndex("departure_date"))
							.replaceAll("\\s", "");
					String destDateParsed = cursor.getString(
							cursor.getColumnIndex("return_date")).replaceAll(
							"\\s", "");

					String depTimeParsed = cursor.getString(
							cursor.getColumnIndex("departure_time"))
							.replaceAll("\\s", "");
					String destTimeParsed = cursor.getString(
							cursor.getColumnIndex("return_time")).replaceAll(
							"\\s", "");

					String[] depTempDate;
					String[] destTempDate;
					String[] depTempTime;
					String[] destTempTime;

					/* delimiter */
					String delimiter = "-";
					/*
					 * given string will be split by the argument delimiter
					 * provided.
					 */
					depTempDate = depDateParsed.split(delimiter);
					destTempDate = destDateParsed.split(delimiter);

					/* delimiter */
					delimiter = ":";
					/*
					 * given string will be split by the argument delimiter
					 * provided.
					 */
					depTempTime = depTimeParsed.split(delimiter);
					destTempTime = destTimeParsed.split(delimiter);

					depYear = Integer.parseInt(depTempDate[2]);
					depMonthOfYear = Integer.parseInt(depTempDate[0]);
					depDayOfMonth = Integer.parseInt(depTempDate[1]);
					depHour = Integer.parseInt(depTempTime[0]);
					depMinute = Integer.parseInt(depTempTime[1]);

					retYear = Integer.parseInt(destTempDate[2]);
					retMonthOfYear = Integer.parseInt(destTempDate[0]);
					retDayOfMonth = Integer.parseInt(destTempDate[1]);
					retHour = Integer.parseInt(destTempTime[0]);
					retMinute = Integer.parseInt(destTempTime[1]);
					
					setAlarm(cursor.getInt(cursor.getColumnIndex("_id")));

				}
				cursor.moveToNext();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		db.close();
	}

	private void setAlarm(int id) {
		// making the Intent id unique
		int inte_id = id * 100;
		// this variables hold the values that will be used to calculate the
		// time
		long hour = AlarmManager.INTERVAL_HOUR;
		long day = AlarmManager.INTERVAL_DAY;
		long week = AlarmManager.INTERVAL_DAY * 7;
		// long DepartureTime=depTimeInMillis-(hour*4); - i don't need this
		// because i will set the -(hour*4) in the notification times
		// long ReturnTime=retTimeInMillis-(hour*4);

		// getting the date and time of departure and return to pass to the
		// AlarmManager
		Calendar depcal = Calendar.getInstance();

		depcal.setTimeZone(TimeZone.getTimeZone("GMT"));
		depcal.set((Calendar.YEAR), depYear);
		depcal.set((Calendar.MONTH), depMonthOfYear - 1);
		depcal.set((Calendar.DAY_OF_MONTH), depDayOfMonth);
		depcal.set((Calendar.HOUR_OF_DAY), depHour);
		depcal.set((Calendar.MINUTE), depMinute);
		long depTimeInMillis = depcal.getTimeInMillis();

		Calendar retcal = Calendar.getInstance();
		retcal.setTimeZone(TimeZone.getTimeZone("GMT"));
		retcal.set((Calendar.YEAR), retYear);
		retcal.set((Calendar.MONTH), retMonthOfYear - 1);
		retcal.set((Calendar.DAY_OF_MONTH), retDayOfMonth);
		retcal.set((Calendar.HOUR_OF_DAY), retHour);
		retcal.set((Calendar.MINUTE), retMinute);
		long retTimeInMillis = retcal.getTimeInMillis();

		/* calculating the time to display the notifications */

		/* notification 0 */
		// creates a intent for pack bags notification
		Intent note_pack = new Intent(context2,
				AlarmReceiver.class);
		// puts on the intent the information about the notification
		note_pack.putExtra("type", "note");
		note_pack.putExtra("title",
				context2.getResources().getString(R.string.tip1_title));
		note_pack.putExtra("note", context2.getResources().getString(R.string.tip1));
		// FLAG_UPDATE_CURRENT > this will send correct extra's informations to
		// AlarmReceiver Class
		PendingIntent sender = PendingIntent.getBroadcast(
				context2, inte_id++, note_pack,
				PendingIntent.FLAG_UPDATE_CURRENT|  Intent.FILL_IN_DATA);

		long tip1Time;
		AlarmManager alarmMgr = (AlarmManager) context2.getSystemService(Context.ALARM_SERVICE);

		if (depHour > 7 && depHour < 22) {
			// this tip is going to be displayed 48 hours before the flight
			tip1Time = depTimeInMillis - (hour * 48);

		} else {
			// offset by extra 10 hours so that alarm is during day.
			tip1Time = depTimeInMillis - (hour * 10);
		}
		if (tip1Time > System.currentTimeMillis()) {
			// set alarm as it is in the future
			alarmMgr.set(AlarmManager.RTC_WAKEUP, tip1Time, sender);
		}
		/* end of time for notification 0 */

		/* notification 1 */
		Intent alarmTime = new Intent(context2,
				AlarmReceiver.class);
		alarmTime.putExtra("type", "alarm");
		alarmTime.putExtra("title",
				context2.getResources().getString(R.string.alarm_notice_title));
		alarmTime.putExtra("note",
				context2.getResources().getString(R.string.alarm_notice));
		PendingIntent sender1 = PendingIntent.getBroadcast(
				context2, inte_id++, alarmTime,
				PendingIntent.FLAG_UPDATE_CURRENT|  Intent.FILL_IN_DATA);

		long tip1;
		AlarmManager alarmMgr1 = (AlarmManager) context2.getSystemService(Context.ALARM_SERVICE);

		// this tip is going to be displayed 4 hours before the flight
		tip1 = depTimeInMillis - (hour * 4);
		if (tip1 > System.currentTimeMillis()) {
			// set alarm as it is in the future
			alarmMgr1.set(AlarmManager.RTC_WAKEUP, tip1, sender1);
		}
		/* end of time for notification 1 */

		/* alarm2 */
		Intent alarm2Time = new Intent(context2,
				AlarmReceiver.class);
		alarm2Time.putExtra("type", "alarm");
		alarm2Time.putExtra("title",
				context2.getResources().getString(R.string.alarm_notice_title));
		alarm2Time.putExtra("note",
				context2.getResources().getString(R.string.alarm_notice));
		PendingIntent sender21 = PendingIntent.getBroadcast(
				context2, inte_id++, alarm2Time,
				PendingIntent.FLAG_UPDATE_CURRENT|  Intent.FILL_IN_DATA);

		long tip21;
		AlarmManager alarm2Mgr1 = (AlarmManager) context2.getSystemService(Context.ALARM_SERVICE);

		// this tip is going to be displayed 4 hours before the flight
		tip21 = retTimeInMillis - (hour * 4);
		if (tip21 > System.currentTimeMillis()) {
			// set alarm as it is in the future
			alarm2Mgr1.set(AlarmManager.RTC_WAKEUP, tip21, sender21);
		}
		/* end of time for alarm2 */

		/* notification 2 */
		Intent note_con = new Intent(context2,
				AlarmReceiver.class);
		note_con.putExtra("type", "note");
		note_con.putExtra("title", context2.getResources()
				.getString(R.string.tip2_title));
		note_con.putExtra("note", context2.getResources().getString(R.string.tip2));
		PendingIntent sender2 = PendingIntent.getBroadcast(
				context2, inte_id++, note_con,
				PendingIntent.FLAG_UPDATE_CURRENT|  Intent.FILL_IN_DATA);

		long tip2;
		AlarmManager alarmMgr2 = (AlarmManager) context2.getSystemService(Context.ALARM_SERVICE);

		if (depHour > 7 && depHour < 22) {
			// this tip is going to be displayed 30 days before the flight
			tip2 = depTimeInMillis - (day * 30);

		} else {
			// offset by extra 10 hours so that alarm is during day.
			tip2 = depTimeInMillis - (hour * 10);

		}

		if (tip2 > System.currentTimeMillis()) {
			// set alarm as it is in the future
			alarmMgr2.set(AlarmManager.RTC_WAKEUP, tip2, sender2);
		}
		/* end of time for notification 2 */

		/* notification 3 */
		Intent note_check = new Intent(context2,
				AlarmReceiver.class);
		note_check.putExtra("type", "note");
		note_check.putExtra("title",
				context2.getResources().getString(R.string.tip3_title));
		note_check.putExtra("note", context2.getResources().getString(R.string.tip3));
		PendingIntent sender3 = PendingIntent.getBroadcast(
				context2, inte_id++, note_check,
				PendingIntent.FLAG_UPDATE_CURRENT|  Intent.FILL_IN_DATA);

		long tip3;
		AlarmManager alarmMgr3 = (AlarmManager) context2.getSystemService(Context.ALARM_SERVICE);

		if (depHour > 7 && depHour < 22) {
			// this tip is going to be displayed 24 hours before the flight
			tip3 = depTimeInMillis - (hour * 24);

		} else {
			// offset by extra 10 hours so that alarm is during day.
			tip3 = depTimeInMillis - (hour * 10);

		}

		if (tip3 > System.currentTimeMillis()) {
			// set alarm as it is in the future
			alarmMgr3.set(AlarmManager.RTC_WAKEUP, tip3, sender3);
		}
		/* end of time for notification 3 */

		/* notification 4 */
		Intent bag = new Intent(context2, AlarmReceiver.class);
		bag.putExtra("type", "note");
		bag.putExtra("title", context2.getResources().getString(R.string.tip4_title));
		bag.putExtra("note", context2.getResources().getString(R.string.tip4));
		PendingIntent sender4 = PendingIntent.getBroadcast(
				context2, inte_id++, bag,
				PendingIntent.FLAG_UPDATE_CURRENT|  Intent.FILL_IN_DATA);

		long tip4;
		AlarmManager alarmMgr4 = (AlarmManager) context2.getSystemService(Context.ALARM_SERVICE);

		if (depHour > 7 && depHour < 22) {
			// this tip is going to be displayed 8 hours before the flight
			tip4 = depTimeInMillis - (hour * 8);

		} else {
			// offset by extra 10 hours so that alarm is during day.
			tip4 = depTimeInMillis - (hour * 10);

		}

		if (tip4 > System.currentTimeMillis()) {
			// set alarm as it is in the future
			alarmMgr4.set(AlarmManager.RTC_WAKEUP, tip4, sender4);
		}
		/* end of time for notification 4 */

		/* notification 5 */
		Intent hand = new Intent(context2, AlarmReceiver.class);
		hand.putExtra("type", "note");
		hand.putExtra("title", context2.getResources().getString(R.string.tip5_title));
		hand.putExtra("note", context2.getResources().getString(R.string.tip5));
		PendingIntent sender5 = PendingIntent.getBroadcast(
				context2, inte_id++, hand,
				PendingIntent.FLAG_UPDATE_CURRENT|  Intent.FILL_IN_DATA);

		long tip5;
		AlarmManager alarmMgr5 = (AlarmManager) context2.getSystemService(Context.ALARM_SERVICE);

		if (depHour > 7 && depHour < 22) {
			// this tip is going to be displayed 6 days before the flight
			tip5 = depTimeInMillis - (hour * 6);

		} else {
			// offset by extra 10 hours so that alarm is during day.
			tip5 = depTimeInMillis - (hour * 10);

		}

		if (tip5 > System.currentTimeMillis()) {
			// set alarm as it is in the future
			alarmMgr5.set(AlarmManager.RTC_WAKEUP, tip5, sender5);
		}
		/* end of time for notification 5 */

		/* notification 6 */
		Intent accomodation = new Intent(context2,
				AlarmReceiver.class);
		accomodation.putExtra("type", "note");
		accomodation.putExtra("title",
				context2.getResources().getString(R.string.tip6_title));
		accomodation.putExtra("note", context2.getResources().getString(R.string.tip6));
		PendingIntent sender6 = PendingIntent.getBroadcast(
				context2, inte_id++, accomodation,
				PendingIntent.FLAG_UPDATE_CURRENT|  Intent.FILL_IN_DATA);

		long tip6;
		AlarmManager alarmMgr6 = (AlarmManager) context2.getSystemService(Context.ALARM_SERVICE);

		if (depHour > 7 && depHour < 22) {
			// this tip is going to be displayed 6 days before the flight
			tip6 = depTimeInMillis - (week * 1);

		} else {
			// offset by extra 10 hours so that alarm is during day.
			tip6 = depTimeInMillis - (hour * 10);

		}

		if (tip6 > System.currentTimeMillis()) {
			// set alarm as it is in the future
			alarmMgr6.set(AlarmManager.RTC_WAKEUP, tip6, sender6);
		}

	}
}