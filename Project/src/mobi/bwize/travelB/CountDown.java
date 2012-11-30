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
 * File Name: CountDown.java
 * Description:
 * Fragment that provides the countdown clock functionality.
 * The time is set by the fragment manager passing the departure
 * time.
 * 
 * The timer will keep running until the clock reaches zero.
 */

package mobi.bwize.travelB;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import mobi.bwize.travelB.R;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

// This is a fragment that is displayed in the Dashboard
public class CountDown extends Fragment {
	// Define the countdown timer object
	CountDownTimer mCountDownTimer;
	// Milliseconds variable to hold the number of ms until departure time
	long mMilliseconds = 0; // 60 seconds * 1000 milliseconds
	// Format of the time string
	SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(
			"'Only'  D 'Days, ' HH' hours, 'mm' min, ' ss' sec To Go'");
	TextView mTextView;
	ViewGroup root;

	// Set the XML layout when the fragment is called
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Set XML layout for this fragment
		root = (ViewGroup) inflater.inflate(R.layout.main, null);
		// return root;
		return root;
	}

	// Function that is called by the fragmentManager to set and
	// start the countdown timer
	void countDown(String date, String time) {
		// Parse the date and time values to remove any spaces
		String dateParsed = date.replaceAll("\\s", "");
		String timeParsed = time.replaceAll("\\s", "");

		String[] tempDate;
		String[] tempTime;

		/* delimiter */
		String delimiter = "-";
		/* given string will be split by the argument delimiter provided. */
		tempDate = dateParsed.split(delimiter);

		/* delimiter */
		delimiter = ":";
		/* given string will be split by the argument delimiter provided. */
		tempTime = timeParsed.split(delimiter);

		// Set the date and time values with parsed data
		int dayValue = Integer.parseInt(tempDate[1]);
		int monthValue = Integer.parseInt(tempDate[0]);
		int yearValue = Integer.parseInt(tempDate[2]);
		int hourValue = Integer.parseInt(tempTime[0]);
		int minuteValue = Integer.parseInt(tempTime[1]);

		// Set the calendar object with retrieved time and date
		Calendar countdownTimeEnd = Calendar.getInstance();
		countdownTimeEnd.setTimeZone(TimeZone.getTimeZone("GMT"));
		countdownTimeEnd.set(Calendar.DAY_OF_MONTH, dayValue);
		countdownTimeEnd.set(Calendar.MONTH, monthValue - 1);
		countdownTimeEnd.set(Calendar.YEAR, yearValue);
		countdownTimeEnd.set(Calendar.HOUR_OF_DAY, hourValue);
		countdownTimeEnd.set(Calendar.MINUTE, minuteValue);

		// Get the current time in milliseconds
		long currentTime = System.currentTimeMillis();
		// Get the remaining ms until departure byt subtracting 
		// the calendar ms value from the current time
		mMilliseconds = countdownTimeEnd.getTimeInMillis() - currentTime;

		// Set the timezone
		mSimpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		// Define the textview to use for the countdown
		mTextView = (TextView) root.findViewById(R.id.textView1);

		// Start and set the timer
		mCountDownTimer = new CountDownTimer(mMilliseconds, 1000) {
			
			// When finished display "Enjoy your Trip!.
			@Override
			public void onFinish() {
				mTextView.setText(mSimpleDateFormat.format(0));
				mTextView.setText(getResources().getString(R.string.text_countdown));
			}

			@Override
			public void onTick(long millisUntilFinished) {
				mTextView
						.setText(mSimpleDateFormat.format(millisUntilFinished));
			}
		}.start();
	}
}