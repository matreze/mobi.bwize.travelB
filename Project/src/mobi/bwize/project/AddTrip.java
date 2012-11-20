package mobi.bwize.project;

import java.sql.SQLException;
import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
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

public class AddTrip extends FragmentActivity {
	EditText myEdit, myEdit2, myEditDepFlightNo, myEditReturnFlightNo,
			myEditExtra;
	Button myButton, myButton2, cancelButton, mPickTime, myInsertTrip;
	RadioGroup myEditDepFlightCon, myEditReturnFlightCon, myEditNotification;
	Calendar depDate, retDate;

	static final int DATE_DIALOG_ID = 0;
	static final int TIME_DEP = 999;
	static final int TIME_RET = 888;
	private int depHour;
	private int depMinute;
	private int retHour;
	private int retMinute;
	private int start = 0;

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

	private String countryDestValue;
	private String cityDestValue;
	private String countryDepValue;
	private String cityDepValue;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_trip);

		setCurrentTimeOnView();

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

		CountiresDB helper = new CountiresDB(this);
		countries = helper.getWritableDatabase();
		// create an array to specify which fields we want to display
		String[] from = new String[] { "country_name" };
		// create an array of the display item we want to bind our data to
		int[] to = new int[] { android.R.id.text1 };

		countryDep = (Spinner) findViewById(R.id.departure_country_xml);
		cursor_dep = countries.query("tblCountries", new String[] {
				"country_name", "currency_code", "country_name_safe",
				BaseColumns._ID }, null, null, null, null, null);
		startManagingCursor(cursor_dep);

		// create simple cursor adapter
		SimpleCursorAdapter adapterDep = new SimpleCursorAdapter(this,
				android.R.layout.simple_spinner_item, cursor_dep, from, to);
		adapterDep
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// get reference to our spinner
		countryDep.setAdapter(adapterDep);

		countryDep.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View view,
					int position, long id) {
				cursor_dep.moveToPosition(position);
				// Log.d("position",Integer.toString(position));
				countryDepValue = cursor_dep.getString(cursor_dep
						.getColumnIndex("country_name"));
				cursor_city_dep = countries.query("tblCities", new String[] {
						BaseColumns._ID, "city_name" }, "country_name=" + "'"
						+ countryDepValue + "'", null, null, null, null);
				// Log.d("here",countryName);
				setCityDep();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

		countryDest = (Spinner) findViewById(R.id.destination_country_xml);
		cursor_dest = countries.query("tblCountries", new String[] {
				"country_name", "currency_code", "country_name_safe",
				BaseColumns._ID }, null, null, null, null, null);
		startManagingCursor(cursor_dest);
		SimpleCursorAdapter adapterDest = new SimpleCursorAdapter(this,
				android.R.layout.simple_spinner_item, cursor_dest, from, to);
		adapterDest
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// get reference to our spinner
		countryDest.setAdapter(adapterDest);

		countryDest.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View view,
					int position, long id) {
				cursor_dest.moveToPosition(position);
				// Log.d("position",Integer.toString(position));
				countryDestValue = cursor_dest.getString(cursor_dest
						.getColumnIndex("country_name"));
				cursor_city_dest = countries.query("tblCities", new String[] {
						BaseColumns._ID, "city_name" }, "country_name=" + "'"
						+ countryDestValue + "'", null, null, null, null);
				// Log.d("here",countryName);
				setCityDest();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

		/* get the current date */
		depDate = Calendar.getInstance();

		// cancel button
		cancelButton = (Button) this.findViewById(R.id.button_cancel_xml);
		cancelButton.setOnClickListener(new OnClickListener() {
			// @Override
			public void onClick(View v) {
				finish();
			}
		});

		/* add a click listener to the button */
		myButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showDateDialog(myEdit, depDate);
			}
		});

		/* capture our View elements for the end date function */

		/* get the current date */
		retDate = Calendar.getInstance();

		/* add a click listener to the button */
		myButton2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showDateDialog(myEdit2, retDate);
			}
		});

		/* display the current date (this method is below) */
		updateDisplay(myEdit, depDate);
		updateDisplay(myEdit2, retDate);
	}

	public void saveTrip(View view) {

		/*
		 * create_Entry(String destination_country, String destination_city,
		 * String departure_flight_code, String departure_date, String
		 * departure_time, String departure_connection, String vaccination,
		 * String visa, String return_country, String return_city, String
		 * return_flight_code, String return_date, String return_time, String
		 * return_connection, String comments, String notification_status)
		 */

		String departure_time = depHour + ":" + pad(depMinute);
		String return_time = retHour + ":" + pad(retMinute);

		// get selected radio button from radioGroup
		int selectedId = myEditDepFlightCon.getCheckedRadioButtonId();

		// find the radiobutton by returned id
		RadioButton radioButton = (RadioButton) findViewById(selectedId);
		String departure_connection = (String) radioButton.getText();

		selectedId = myEditReturnFlightCon.getCheckedRadioButtonId();
		radioButton = (RadioButton) findViewById(selectedId);
		String return_connection = (String) radioButton.getText();

		selectedId = myEditNotification.getCheckedRadioButtonId();
		radioButton = (RadioButton) findViewById(selectedId);
		String notification_status = (String) radioButton.getText();

		Trip_Database db = new Trip_Database(this);
		try {
			db.open();

			Log.d("depctry", countryDepValue);
			Log.d("depcty", cityDepValue);
			Log.d("destctry", countryDestValue);
			Log.d("depcty", cityDestValue);

			long id = db.create_Entry(countryDestValue, cityDestValue,
					myEditDepFlightNo.getText().toString(), myEdit.getText()
							.toString(), departure_time, departure_connection,
					null, null, countryDepValue, cityDepValue,
					myEditReturnFlightNo.getText().toString(), myEdit2
							.getText().toString(), return_time,
					return_connection, myEditExtra.getText().toString(),
					notification_status);
			db.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		finish();

	}

	public void setCityDep() {
		CountiresDB helper = new CountiresDB(this);
		countries = helper.getWritableDatabase();
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

		// get reference to our spinner
		cityDep.setAdapter(adapter_city);

		cityDep.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View view,
					int position, long id) {
				cityDepValue = cityDep.getSelectedItem().toString();
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

	public void setCityDest() {
		CountiresDB helper = new CountiresDB(this);
		countries = helper.getWritableDatabase();
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
		// get reference to our spinner
		cityDest.setAdapter(adapter_city);

		cityDest.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View view,
					int position, long id) {

				cityDestValue = cityDest.getSelectedItem().toString();
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

	private void updateDisplay(EditText dateDisplay, Calendar date) {
		dateDisplay.setText(new StringBuilder()
				// Month is 0 based so add 1
				.append(date.get(Calendar.MONTH) + 1).append("-")
				.append(date.get(Calendar.DAY_OF_MONTH)).append("-")
				.append(date.get(Calendar.YEAR)).append(" "));

	}

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

		/*
		 * // Insert into Database myInsertTrip.setOnClickListener(new
		 * OnClickListener() {
		 * 
		 * // @Override public void onClick(View arg0) {
		 * 
		 * Toast.makeText(AddTrip.this, "Trip inserted",
		 * Toast.LENGTH_SHORT).show();
		 * 
		 * }
		 * 
		 * });
		 */

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

				String endTime = retHour + ":" + pad(retMinute);
				TextView dataToSet2 = (TextView) findViewById(R.id.ret_time_xml);
				dataToSet2.setText(endTime);

				break;
			}
		}
	};

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