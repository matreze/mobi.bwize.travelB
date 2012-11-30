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
 * File Name: AddTrip.java
 * Description:
 * 
 * AddTrip provides the capability of entering the trip
 * information into the form so that it can be saved into
 * the main database for later retrieval.
 * 
 * Information gathered includes dates, times, location info
 * and a variety of preferences.
 * 
 */

package mobi.bwize.travelB;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.TimeZone;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

// AddTrip class, extending fragment activity.
// Currently the app is restricted to a smartphone layout.
// By using fragments, the possibility exists to easily create a 
// tablet specific layout.
public class AddTrip extends FragmentActivity {

	// Define variables for various labels
	EditText myEdit, myEdit2, myEditDepFlightNo, myEditReturnFlightNo,
			myEditExtra;
	Button myButton, myButton2, cancelButton, mPickTime, myInsertTrip;
	RadioGroup myEditDepFlightCon, myEditReturnFlightCon, myEditNotification;
	Calendar depDate, retDate;

	// Various variables. They are class variables to allow the easy
	// access of data from the different functions
	static final int DATE_DIALOG_ID = 0;
	static final int TIME_DEP = 999;
	static final int TIME_RET = 888;
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
	private int start = 0;

	private long id;

	private EditText activeDateDisplay;
	private Calendar activeDate;

	private Cursor cursor_dest;
	private Cursor cursor_city_dest;
	private Cursor cursor_dep;
	private Cursor cursor_city_dep;
	private SQLiteDatabase countries;
	private Spinner countryDest;
	private Spinner cityDest;
	private Spinner countryDep;
	private Spinner cityDep;
	CountriesDB helper;

	private String countryDestValue;
	private String cityDestValue;
	private String countryDepValue;
	private String cityDepValue;
	private String departure_connection;
	private String return_connection;
	private String notification_status;

	// The onCreate method sets up the form.
	// References to form elements are saved here.
	// The dynamic spinners for the countries and cities are set.
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Grab and display the relevant XML layout
		setContentView(R.layout.add_trip);

		// set the current time values for the time pickers
		setCurrentTimeOnView();

		/* get the current date */
		depDate = Calendar.getInstance();

		/* get the current date */
		retDate = Calendar.getInstance();

		/* capture our View elements for the start date function */
		myEdit = (EditText) findViewById(R.id.departure_date_xml);
		myButton = (Button) findViewById(R.id.pick_departure_date);
		myEdit2 = (EditText) findViewById(R.id.return_date_xml);
		myButton2 = (Button) findViewById(R.id.pick_return_date);

		/* for the other capture fields */
		myEditDepFlightNo = (EditText) findViewById(R.id.departure_flight_xml);
		myEditDepFlightCon = (RadioGroup) findViewById(R.id.dep_flight_connection_xml);
		myEditReturnFlightNo = (EditText) findViewById(R.id.return_flight_xml);
		myEditReturnFlightCon = (RadioGroup) findViewById(R.id.return_flight_connection_xml);
		myEditExtra = (EditText) findViewById(R.id.comments_xml);
		myEditNotification = (RadioGroup) findViewById(R.id.notifications_xml);
		myInsertTrip = (Button) findViewById(R.id.button_addtrip_xml);

		/* Start of Spinner code */
		// Open new helper for the countries DB
		helper = new CountriesDB(this);
		countries = helper.getWritableDatabase();
		// create an array to specify which fields we want to display
		String[] from = new String[] { "country_name" };
		// create an array of the display item we want to bind our data to
		int[] to = new int[] { android.R.id.text1 };

		// Get the reference to out spinner
		countryDep = (Spinner) findViewById(R.id.departure_country_xml);
		// Retrieve a cursor with all Country values
		cursor_dep = countries.query("tblCountries", new String[] {
				"country_name", "currency_code", "country_name_safe",
				BaseColumns._ID }, null, null, null, null, null);
		startManagingCursor(cursor_dep);

		// create simple cursor adapter
		SimpleCursorAdapter adapterDep = new SimpleCursorAdapter(this,
				android.R.layout.simple_spinner_item, cursor_dep, from, to);
		adapterDep
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Set the data in our Spinner
		countryDep.setAdapter(adapterDep);

		// Code to handle what happens when a country is selected
		// When the country is selected, the second spinner must be populated
		// with the corresponding cities for that country.
		countryDep.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View view,
					int position, long id) {
				// Move the cursor to the position that was selected
				cursor_dep.moveToPosition(position);
				// Grab the country name that was selected - this acts as the
				// key
				// for retrieving the cities
				countryDepValue = cursor_dep.getString(cursor_dep
						.getColumnIndex("country_name"));
				// Populate the city cursor with the list of cities in the
				// selected country
				cursor_city_dep = countries.query("tblCities", new String[] {
						BaseColumns._ID, "city_name" }, "country_name=" + "'"
						+ countryDepValue + "'", null, null, null, null);
				// Call function that actually populates the City spinner
				setCityDep();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

		// Repeat the previous code for the destination country
		// Get the reference to out spinner
		countryDest = (Spinner) findViewById(R.id.destination_country_xml);
		// Retrieve a cursor with all Country values
		cursor_dest = countries.query("tblCountries", new String[] {
				"country_name", "currency_code", "country_name_safe",
				BaseColumns._ID }, null, null, null, null, null);
		startManagingCursor(cursor_dest);
		// create simple cursor adapter
		SimpleCursorAdapter adapterDest = new SimpleCursorAdapter(this,
				android.R.layout.simple_spinner_item, cursor_dest, from, to);
		adapterDest
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// get reference to our spinner
		countryDest.setAdapter(adapterDest);

		// Code to handle what happens when a country is selected
		// When the country is selected, the second spinner must be populated
		// with the corresponding cities for that country.
		countryDest.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View view,
					int position, long id) {
				// Move the cursor to the position that was selected
				cursor_dest.moveToPosition(position);
				// Grab the country name that was selected - this acts as the
				// key
				// for retrieving the cities
				countryDestValue = cursor_dest.getString(cursor_dest
						.getColumnIndex("country_name"));
				// Populate the city cursor with the list of cities in the
				// selected country
				cursor_city_dest = countries.query("tblCities", new String[] {
						BaseColumns._ID, "city_name" }, "country_name=" + "'"
						+ countryDestValue + "'", null, null, null, null);
				// Call function that actually populates the City spinner
				setCityDest();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

		// cancel button
		cancelButton = (Button) this.findViewById(R.id.button_cancel_xml);
		cancelButton.setOnClickListener(new OnClickListener() {
			// @Override
			public void onClick(View v) {
				finish();
			}
		});

		/* add a click listener to the button - for departure date */
		myButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showDateDialog(myEdit, depDate);
			}
		});

		/* add a click listener to the button - for return date */
		myButton2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showDateDialog(myEdit2, retDate);
			}
		});

		/* display the current date in the edittext fields */
		updateDisplay(myEdit, depDate);
		updateDisplay(myEdit2, retDate);
	}

	public void saveTrip(View view) {

		// Format the time values from the Time pickers into a String
		// This can then be parsed when required again.
		String departure_time = depHour + ":" + pad(depMinute);
		String return_time = retHour + ":" + pad(retMinute);

		// get selected radio button from radioGroup
		int selectedId = myEditDepFlightCon.getCheckedRadioButtonId();
		// find the radiobutton by returned id
		RadioButton radioButton = (RadioButton) findViewById(selectedId);
		// Store the value of the redio button into the appropriate string value
		departure_connection = (String) radioButton.getText();

		// get selected radio button from radioGroup
		selectedId = myEditReturnFlightCon.getCheckedRadioButtonId();
		// find the radiobutton by returned id
		radioButton = (RadioButton) findViewById(selectedId);
		// Store the value of the redio button into the appropriate string value
		return_connection = (String) radioButton.getText();

		// get selected radio button from radioGroup
		selectedId = myEditNotification.getCheckedRadioButtonId();
		// find the radiobutton by returned id
		radioButton = (RadioButton) findViewById(selectedId);
		// Store the value of the redio button into the appropriate string value
		notification_status = (String) radioButton.getText();

		// Open the database to submit the trip
		Trip_Database db = new Trip_Database(this);
		try {
			db.open();

			// Enter the info into the db and get the returned id
			id = db.create_Entry(countryDestValue, cityDestValue,
					myEditDepFlightNo.getText().toString(), myEdit.getText()
							.toString(), departure_time, departure_connection,
					null, null, countryDepValue, cityDepValue,
					myEditReturnFlightNo.getText().toString(), myEdit2
							.getText().toString(), return_time,
					return_connection, myEditExtra.getText().toString(),
					notification_status);
			db.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// Parsing the dates to be used in the alarm setting function
		// Parse to remove any potential spaces
		String depDateParsed = myEdit.getText().toString()
				.replaceAll("\\s", "");
		String destDateParsed = myEdit2.getText().toString()
				.replaceAll("\\s", "");

		String[] depTempDate;
		String[] destTempDate;

		/* delimiter */
		String delimiter = "-";
		/* given string will be split by the argument delimiter provided. */
		depTempDate = depDateParsed.split(delimiter);
		destTempDate = destDateParsed.split(delimiter);

		// Place the parsed values into int values for alarms
		depYear = Integer.parseInt(depTempDate[2]);
		depMonthOfYear = Integer.parseInt(depTempDate[0]);
		depDayOfMonth = Integer.parseInt(depTempDate[1]);

		retYear = Integer.parseInt(destTempDate[2]);
		retMonthOfYear = Integer.parseInt(destTempDate[0]);
		retDayOfMonth = Integer.parseInt(destTempDate[1]);

		// Check to see if notifications are wanted
		if (notification_status.equals("Yes")) {
			// Call function if they are desired
			setAlarm();
		}
		// End activity and return to calling activity
		countries.close();
		finish();

	}

	// set the alarms
	private void setAlarm() {
		// making the Intent id unique using the id as a seed
		int inte_id = (int) id * 100;
		// this variables hold the values that will be used to calculate the time
		long hour = AlarmManager.INTERVAL_HOUR;
		long day = AlarmManager.INTERVAL_DAY;
		long week = AlarmManager.INTERVAL_DAY * 7;


		// getting the date and time of departure and return to pass to the
		// AlarmManager
		Calendar depcal = Calendar.getInstance();

		depcal.setTimeZone(TimeZone.getTimeZone("GMT"));
		depcal.set((Calendar.YEAR), depYear);
		depcal.set((Calendar.MONTH), depMonthOfYear - 1);
		depcal.set((Calendar.DAY_OF_MONTH), depDayOfMonth);
		depcal.set((Calendar.HOUR_OF_DAY), depHour);
		depcal.set((Calendar.MINUTE), depMinute);
		//getting the departure time in Miliseconds
		long depTimeInMillis = depcal.getTimeInMillis();

		//setting the values grabbed from user input to the variables
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
		Intent note_pack = new Intent(getApplicationContext(),
				AlarmReceiver.class);
		// puts on the intent the information about the notification
		note_pack.putExtra("type", "note");
		note_pack.putExtra("title", getResources().getString(R.string.tip1_title));
		note_pack.putExtra("note", getResources().getString(R.string.tip1));
		//creates a pending intent that will hand the values to the Alarm Receiver
		PendingIntent sender = PendingIntent.getBroadcast(getApplicationContext(), 
		inte_id++, note_pack, PendingIntent.FLAG_ONE_SHOT);

		long tip1Time;
		AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		//this statment says that the notification only will be displayed before 22 and after 7 hours.
		if (depHour > 7 && depHour < 22) {
			// this tip is going to be displayed 48 hours before the flight
			tip1Time = depTimeInMillis - (hour * 48);

		} else {
			// if the time to display the notification is not before 22 and after 7 hours
			// it will be displayed 10 hours before.
			tip1Time = depTimeInMillis - (hour * 10);
		}
		if (tip1Time > System.currentTimeMillis()) {
			// set alarm as it is in the future
			alarmMgr.set(AlarmManager.RTC_WAKEUP, tip1Time, sender);
		}
		/* end of time for notification 0 */

		/* notification 1 */
		//create a new intent to this notification
		Intent alarmTime = new Intent(getApplicationContext(), AlarmReceiver.class);
		//adds extra information to the intent
		alarmTime.putExtra("type", "alarm");
		alarmTime.putExtra("title",	getResources().getString(R.string.alarm_notice_title));
		alarmTime.putExtra("note", getResources().getString(R.string.alarm_notice));
		//creates the Pending intent to handle the information about this notification
		PendingIntent sender1 = PendingIntent.getBroadcast(getApplicationContext(), 
		inte_id++, alarmTime, PendingIntent.FLAG_ONE_SHOT);

		long tip1;
		AlarmManager alarmMgr1 = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

		// this notification is going to be displayed 4 hours before the flight
		tip1 = depTimeInMillis - (hour * 4);
		if (tip1 > System.currentTimeMillis()) {
			// set alarm as it is in the future
			alarmMgr1.set(AlarmManager.RTC_WAKEUP, tip1, sender1);
		}
		/* end of time for notification 1 */

		/* alarm2 */
		Intent alarm2Time = new Intent(getApplicationContext(),
				AlarmReceiver.class);
		alarm2Time.putExtra("type", "alarm");
		alarm2Time.putExtra("title", getResources().getString(R.string.alarm_notice_title));
		alarm2Time.putExtra("note", getResources().getString(R.string.alarm_notice));
		PendingIntent sender21 = PendingIntent.getBroadcast(getApplicationContext(), 
			inte_id++, alarm2Time, PendingIntent.FLAG_ONE_SHOT);

		long tip21;
		AlarmManager alarm2Mgr1 = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

		// this tip is going to be displayed 4 hours before the flight
		tip21 = retTimeInMillis - (hour * 4);
		if (tip21 > System.currentTimeMillis()) {
			// set alarm as it is in the future
			alarm2Mgr1.set(AlarmManager.RTC_WAKEUP, tip21, sender21);
		}
		/* end of time for alarm2 */

		/* notification 2 */
		Intent note_con = new Intent(getApplicationContext(), AlarmReceiver.class);
		note_con.putExtra("type", "note");
		note_con.putExtra("title", getResources().getString(R.string.tip2_title));
		note_con.putExtra("note", getResources().getString(R.string.tip2));
		PendingIntent sender2 = PendingIntent.getBroadcast(getApplicationContext(), 
			inte_id++, note_con, PendingIntent.FLAG_ONE_SHOT);

		long tip2;
		AlarmManager alarmMgr2 = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

		if (depHour > 7 && depHour < 22) {
			// this tip is going to be displayed 30 days before the flight
			tip2 = depTimeInMillis - (day * 30);

		} else {
			// offset by extra 10 hours.
			tip2 = depTimeInMillis - (hour * 10);

		}

		if (tip2 > System.currentTimeMillis()) {
			// set alarm as it is in the future
			alarmMgr2.set(AlarmManager.RTC_WAKEUP, tip2, sender2);
		}
		/* end of time for notification 2 */

		/* notification 3 */
		Intent note_check = new Intent(getApplicationContext(),
				AlarmReceiver.class);
		note_check.putExtra("type", "note");
		note_check.putExtra("title", getResources().getString(R.string.tip3_title));
		note_check.putExtra("note", getResources().getString(R.string.tip3));
		PendingIntent sender3 = PendingIntent.getBroadcast(getApplicationContext(), 
			inte_id++, note_check, PendingIntent.FLAG_ONE_SHOT);

		long tip3;
		AlarmManager alarmMgr3 = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

		if (depHour > 7 && depHour < 22) {
			// this tip is going to be displayed 24 hours before the flight
			tip3 = depTimeInMillis - (hour * 24);

		} else {
			// offset by extra 10 hours.
			tip3 = depTimeInMillis - (hour * 10);

		}

		if (tip3 > System.currentTimeMillis()) {
			// set alarm as it is in the future
			alarmMgr3.set(AlarmManager.RTC_WAKEUP, tip3, sender3);
		}
		/* end of time for notification 3 */

		/* notification 4 */
		Intent bag = new Intent(getApplicationContext(), AlarmReceiver.class);
		bag.putExtra("type", "note");
		bag.putExtra("title", getResources().getString(R.string.tip4_title));
		bag.putExtra("note", getResources().getString(R.string.tip4));
		PendingIntent sender4 = PendingIntent.getBroadcast(getApplicationContext(),
				 inte_id++, bag, PendingIntent.FLAG_ONE_SHOT);

		long tip4;
		AlarmManager alarmMgr4 = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

		if (depHour > 7 && depHour < 22) {
			// this tip is going to be displayed 8 hours before the flight
			tip4 = depTimeInMillis - (hour * 8);

		} else {
			// offset by extra 10 hours.
			tip4 = depTimeInMillis - (hour * 10);

		}

		if (tip4 > System.currentTimeMillis()) {
			// set alarm as it is in the future
			alarmMgr4.set(AlarmManager.RTC_WAKEUP, tip4, sender4);
		}
		/* end of time for notification 4 */

		/* notification 5 */
		Intent hand = new Intent(getApplicationContext(), AlarmReceiver.class);
		hand.putExtra("type", "note");
		hand.putExtra("title", getResources().getString(R.string.tip5_title));
		hand.putExtra("note", getResources().getString(R.string.tip5));
		PendingIntent sender5 = PendingIntent.getBroadcast(getApplicationContext(), 
			inte_id++, hand, PendingIntent.FLAG_ONE_SHOT);

		long tip5;
		AlarmManager alarmMgr5 = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

		if (depHour > 7 && depHour < 22) {
			// this tip is going to be displayed 6 hours before the flight
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
		Intent accomodation = new Intent(getApplicationContext(), AlarmReceiver.class);
		accomodation.putExtra("type", "note");
		accomodation.putExtra("title", getResources().getString(R.string.tip6_title));
		accomodation.putExtra("note", getResources().getString(R.string.tip6));
		PendingIntent sender6 = PendingIntent.getBroadcast(getApplicationContext(), inte_id++, 
			accomodation, PendingIntent.FLAG_ONE_SHOT);

		long tip6;
		AlarmManager alarmMgr6 = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

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

	// Functions to fill the city spinners once the country spinner has
	// a selected country
	public void setCityDep() {
		// Get the database helper
		countries = helper.getWritableDatabase();
		// Find the city spinner
		cityDep = (Spinner) findViewById(R.id.departure_city_xml);
		String[] from2 = new String[] { "city_name" };
		// create an array of the display item we want to bind our data to
		int[] to2 = new int[] { android.R.id.text1 };
		// create simple cursor adapter
		SimpleCursorAdapter adapter_city = new SimpleCursorAdapter(this,
				android.R.layout.simple_spinner_item, cursor_city_dep, from2,
				to2);
		adapter_city
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		// Set the spinner with the city values
		cityDep.setAdapter(adapter_city);

		// When a city is selected, the value is saved in the class variable for
		// the city
		cityDep.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View view,
					int position, long id) {
				// Get the string value for the city - default value
				cityDepValue = cityDep.getSelectedItem().toString();
				// If item is selected, use the position id to save city value
				if (position != -1) {
					cursor_city_dep.moveToPosition(position);
					cityDepValue = cursor_city_dep.getString(cursor_city_dep
							.getColumnIndex("city_name"));
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

	}

	// Functions to fill the city spinners once the country spinner has
	// a selected country
	public void setCityDest() {
		// Get the database helper
		countries = helper.getWritableDatabase();
		// Find the city spinner
		cityDest = (Spinner) findViewById(R.id.destination_city_xml);
		String[] from2 = new String[] { "city_name" };
		// create an array of the display item we want to bind our data to
		int[] to2 = new int[] { android.R.id.text1 };
		// create simple cursor adapter
		SimpleCursorAdapter adapter_city = new SimpleCursorAdapter(this,
				android.R.layout.simple_spinner_item, cursor_city_dest, from2,
				to2);
		adapter_city
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// set the spinner values
		cityDest.setAdapter(adapter_city);

		// When a city is selected, the value is saved in the class variable for
		// the city
		cityDest.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View view,
					int position, long id) {
				// Get the string value for the city - default value
				cityDestValue = cityDest.getSelectedItem().toString();
				// If item is selected, use the position id to save city value
				if (position != -1) {
					cursor_city_dest.moveToPosition(position);
					cityDestValue = cursor_city_dest.getString(cursor_city_dest
							.getColumnIndex("city_name"));
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

	}

	// Update the date value in the appropriate EditText field
	private void updateDisplay(EditText dateDisplay, Calendar date) {
		dateDisplay.setText(new StringBuilder()
				// Month is 0 based so add 1
				.append(date.get(Calendar.MONTH) + 1).append("-")
				.append(date.get(Calendar.DAY_OF_MONTH)).append("-")
				.append(date.get(Calendar.YEAR)).append(" "));

	}

	// Date dialog functions
	public void showDateDialog(EditText dateDisplay, Calendar date) {
		activeDateDisplay = dateDisplay;
		activeDate = date;
		showDialog(DATE_DIALOG_ID);
	}
	private OnDateSetListener dateSetListener = new OnDateSetListener() {
		// @Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			activeDate.set(Calendar.YEAR, year);
			activeDate.set(Calendar.MONTH, monthOfYear);
			activeDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
			updateDisplay(activeDateDisplay, activeDate);
			unregisterDateDisplay();
		}
	};
	private void unregisterDateDisplay() {
		activeDateDisplay = null;
		activeDate = null;
	}

	// Create the appropriate dialog based on the id passed
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
			return new DatePickerDialog(this, dateSetListener,
					activeDate.get(Calendar.YEAR),
					activeDate.get(Calendar.MONTH),
					activeDate.get(Calendar.DAY_OF_MONTH));
		case TIME_DEP:
			// set time picker as current time
			return new TimePickerDialog(this, timePickerListener, depHour,
					depMinute, false);
		case TIME_RET:
			// set time picker as current time
			return new TimePickerDialog(this, timePickerListener, depHour,
					depMinute, false);
		}
		return null;
	}

	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		super.onPrepareDialog(id, dialog);
		switch (id) {
		case DATE_DIALOG_ID:
			((DatePickerDialog) dialog).updateDate(
					activeDate.get(Calendar.YEAR),
					activeDate.get(Calendar.MONTH),
					activeDate.get(Calendar.DAY_OF_MONTH));
			break;
		}
	}

	// sets the time to the current for the timepickers
	public void setCurrentTimeOnView() {
		final Calendar c = Calendar.getInstance();
		depHour = c.get(Calendar.HOUR_OF_DAY);
		depMinute = c.get(Calendar.MINUTE);
	}

	// based on the selected dialog, save the time values to the relevant fields
	private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int selectedHour,
				int selectedMinute) {
			switch (start) {
			case 1:
				depHour = selectedHour;
				depMinute = selectedMinute;
				// Set the time on the form
				String time = depHour + ":" + pad(depMinute);
				TextView dataToSet = (TextView) findViewById(R.id.dep_time_xml);
				dataToSet.setText(time);

				break;
			case 0:
				retHour = selectedHour;
				retMinute = selectedMinute;
				// Set the time on the form
				String endTime = retHour + ":" + pad(retMinute);
				TextView dataToSet2 = (TextView) findViewById(R.id.ret_time_xml);
				dataToSet2.setText(endTime);

				break;
			}
		}
	};

	// Set the variable to allow the onPickerDialog to enter time in the correct variables
	public void depSet(View v) {
		start = 1;
		showDialog(TIME_DEP);
	}
	public void retSet(View v) {
		start = 0;
		showDialog(TIME_RET);
	}

	// properly formats the minutes number if less than 10 to display a zero eg
	// 7 -> 07
	private static String pad(int c) {
		if (c >= 10)
			return String.valueOf(c);
		else
			return "0" + String.valueOf(c);
	}

}