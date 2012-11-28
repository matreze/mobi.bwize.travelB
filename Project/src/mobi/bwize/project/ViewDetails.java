package mobi.bwize.project;

import java.sql.SQLException;
import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;

public class ViewDetails extends FragmentActivity {
	EditText myEdit, myEdit2, myEditDepFlightNo, myEditReturnFlightNo,
			myEditExtra;
	Button myButton, myButton2, cancelButton, mPickTime, myInsertTrip;
	RadioGroup myEditDepFlightCon, myEditReturnFlightCon, myEditNotification;
	Calendar depDate, retDate;
	TextView txtDest, txtDepTime, txtReturnTime;
	TextView dataToSet;
	TextView dataToSet2;
	ImageButton backButton;

	static final int DATE_DIALOG_ID = 0;
	static final int TIME_DEP = 999;
	static final int TIME_RET = 888;
	private int depHour;
	private int depMinute;
	private int retHour;
	private int retMinute;
	private int start = 0;
	private int id;

	private EditText activeDateDisplay;
	private Calendar activeDate;


	private TextView countryDest;
	private TextView cityDest;
	private TextView countryDep;
	private TextView cityDep;


	private String destCity;
	private String depCity;
	private String destCountry;
	private String depCountry;

	
	String destData, destDepDate, depFlight, depTime,  returnDate, returnFlight, returnTime, comments;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_trip);
		Intent myIntent = getIntent();

		backButton = (ImageButton) this.findViewById(R.id.imageButton_back);
		myButton = (Button) this.findViewById(R.id.button_updatetrip_xml);
		myButton2 = (Button) this.findViewById(R.id.button_delete_xml);

		backButton.setOnClickListener(new OnClickListener() {
			// @Override
			public void onClick(View v) {
				finish();
			}
		});

		setCurrentTimeOnView();

		/* capture our View elements for the start date function */
		txtDest=(TextView) findViewById(R.id.text_trip_name_xml);
		dataToSet = (TextView) findViewById(R.id.dep_time_xml);
		dataToSet2 = (TextView) findViewById(R.id.ret_time_xml);
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

		

		countryDep = (TextView) findViewById(R.id.departure_country_xml);
		countryDest = (TextView) findViewById(R.id.destination_country_xml);
		cityDep = (TextView) findViewById(R.id.departure_city_xml);
		cityDest = (TextView) findViewById(R.id.destination_city_xml);
		

		/* get the current date */
		depDate = Calendar.getInstance();

		// cancel button
		cancelButton = (Button) this.findViewById(R.id.button_delete_xml);
		cancelButton.setOnClickListener(new OnClickListener() {
			// @Override
			public void onClick(View v) {
				
				Trip_Database db = new Trip_Database(ViewDetails.this);
				Log.d("trying1","");
				try {
					db.open();
					Log.d("trying","");
					db.deleteEntry(id);
					db.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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

		id=Integer.valueOf(myIntent.getStringExtra("id"));
		destData = myIntent.getStringExtra("City");
		destDepDate = myIntent.getStringExtra("DepDate");
		depFlight = myIntent.getStringExtra("DepFlightNo");
		depTime = myIntent.getStringExtra("DepTime");
		destCountry = myIntent.getStringExtra("CountryDest");
		destCity = myIntent.getStringExtra("CityDest");
		depCountry = myIntent.getStringExtra("CountryDep");
		depCity = myIntent.getStringExtra("CityDep");
		returnDate = myIntent.getStringExtra("ReturnDate");
		returnFlight = myIntent.getStringExtra("ReturnFlightNo");
		returnTime = myIntent.getStringExtra("ReturnTime");
		comments = myIntent.getStringExtra("Comments");
		
    	String depTimeParsed=depTime.replaceAll("\\s","");
    	String[] depTempTime;  	  
    	  /* delimiter */
    	String delimiter = ":";
    	  /* given string will be split by the argument delimiter provided. */
    	depTempTime = depTimeParsed.split(delimiter);
       depHour = Integer.parseInt(depTempTime[0]);
         depMinute = Integer.parseInt(depTempTime[1]);
		
         String destTimeParsed=returnTime.replaceAll("\\s","");
     	String[] destTempTime;  	  
     	  /* given string will be split by the argument delimiter provided. */
     	destTempTime = destTimeParsed.split(delimiter);
        retHour = Integer.parseInt(destTempTime[0]);
          retMinute = Integer.parseInt(destTempTime[1]);
		
          
		txtDest.setText(destData);
		myEdit.setText(destDepDate);
		myEditDepFlightNo.setText(depFlight);
		dataToSet.setText(depTime);
		countryDest.setText(destCountry);
		cityDest.setText(destCity);
		countryDep.setText(depCountry);
		cityDep.setText(depCity);
		myEdit2.setText(returnDate);
		myEditReturnFlightNo.setText(returnFlight);
		dataToSet2.setText(returnTime);
		myEditExtra.setText(comments);

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
			

			db.updateTrip_byid(id, 
					destCountry, 
					destCity,
					myEditDepFlightNo.getText().toString(), 
					myEdit.getText().toString(), 
					departure_time, 
					departure_connection,
					null, 
					null, 
					depCountry, 
					depCity,
					myEditReturnFlightNo.getText().toString(), 
					myEdit2.getText().toString(), 
					return_time,
					return_connection,
					myEditExtra.getText().toString(),
					notification_status);
			db.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		finish();

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
				dataToSet.setText(time);

				break;
			case 0:
				retHour = selectedHour;
				retMinute = selectedMinute;

				String endTime = retHour + ":" + pad(retMinute);
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