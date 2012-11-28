package mobi.bwize.project;

import java.sql.SQLException;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class DashBoardActivity extends FragmentActivity {

	ImageButton backButton, viewButton;
	TextView txtDest;
	String details[];
	String destination[];
	String departure[];
	String cityParsed;
	String countryParsed;
	String trip;
	int id;
	String message;
	CountDown countDownFragment; 

	/*
		 * String[] columns = new String[] { 
		 * 0 KEY_TRIP_ID, 
		 * 1 KEY_DESTINATION_COUNTRY,
		 * 2 KEY_DESTINATION_CITY, 
		 * 3 KEY_DEPARTURE_FLIGHT_CODE, 
		 * 4 KEY_DEPARTURE_DATE,
		 * 5 KEY_DEPARTUE_TIME, 
		 * 6 KEY_DEPARTURE_FLIGHT_CONNECTION, 
		 * 7 KEY_VACCINATION,
		 * 8 KEY_VISA, 
		 * 9 KEY_RETURN_COUNTRY, 
		 * 10 KEY_RETURN_CITY, 
		 * 11 KEY_RETURN_FLIGHT_CODE,
		 * 12 KEY_RETURN_DATE, 
		 * 13 KEY_RETURN_TIME, 
		 * 14 KEY_RETURN_FLIGHT_CONNECTION,
		 * 15 NOTIFICATION_STATUS, 
		 * 16 KEY_COMMENTS };
		 */

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dashboard);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		FragmentManager fragmentManager = getSupportFragmentManager();

		Intent intent = getIntent();

		// check the string from the previous activity
		message = intent.getStringExtra("ID");
		id = Integer.valueOf(message);
		trip = message;

		Trip_Database db = new Trip_Database(this);

		try {
			db.open();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Get the destination name from the last activity to populate the

		// Get the destination name from the last activity to populate the
		details = db.getTripDetails(Integer.valueOf(message));
		destination = db.get_Destination(id);
		departure = db.get_ReturnInfo(id);

		cityParsed = destination[1].replaceAll("\\s", "%20");
		countryParsed = destination[0].replaceAll("\\s", "%20");

		CurrentWeather currentWeatherDownFragment = (CurrentWeather) fragmentManager
				.findFragmentById(R.id.frag_2);
		currentWeatherDownFragment.setCurrentWeather(countryParsed, cityParsed);

		ToDO toDoFragment = (ToDO) fragmentManager
				.findFragmentById(R.id.frag_3);
		toDoFragment.setID(message);

		countDownFragment = (CountDown) fragmentManager
				.findFragmentById(R.id.frag_1);
		countDownFragment.countDown(db.get_DepartureDate(id),
				db.get_DepartureTime(id));

		db.close();

		ScrollView sview = (ScrollView) this.findViewById(R.id.scrollview);
		sview.smoothScrollTo(0, 0);
		/*
		 * String destData;
		 * 
		 * Intent myIntent = getIntent(); destData =
		 * myIntent.getStringExtra("DESTINATION");
		 */

		// txtDest.setText(details[2]);

		backButton = (ImageButton) this.findViewById(R.id.imageButton_back);
		backButton.setOnClickListener(new OnClickListener() {
			// @Override
			public void onClick(View v) {
				finish();
			}
		});

	}

	@Override
	public void onResume() {
		super.onResume();
		
		Trip_Database db = new Trip_Database(this);

		try {
			db.open();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Cursor test = db.checkIfExists(id);

		if (test.getCount() != 0) {
			
			db.close();
		} else {
			db.close();
			finish();
		}
	}

	public void viewDetails(View view) {

		Intent ViewDetails = new Intent(this, ViewDetails.class);

		Log.d("cityDep", departure[1]);
		Log.d("citydest", destination[1]);

		// To do
		
		
		ViewDetails.putExtra("id", message);
		ViewDetails.putExtra("City", destination[1]);
		ViewDetails.putExtra("DepDate", details[4]);
		ViewDetails.putExtra("DepFlightNo", details[3]);
		ViewDetails.putExtra("DepTime", details[5]);
		ViewDetails.putExtra("DepConnection", details[6]);
		ViewDetails.putExtra("CountryDep", departure[0]);
		ViewDetails.putExtra("CityDep", departure[1]);
		ViewDetails.putExtra("CountryDest", destination[0]);
		ViewDetails.putExtra("CityDest", destination[1]);
		ViewDetails.putExtra("ReturnDate", details[12]);
		ViewDetails.putExtra("ReturnFlightNo", details[11]);
		ViewDetails.putExtra("ReturnTime", details[13]);
		ViewDetails.putExtra("ReturnConnection", details[14]);
		ViewDetails.putExtra("Notification", details[15]);
		ViewDetails.putExtra("Comments", details[16]);

		startActivity(ViewDetails);

	}

	public void weatherTenDay(View view) {

		Intent weatherTenDay = new Intent(this, WeatherTenDay.class);

		weatherTenDay.putExtra("Country", countryParsed);
		weatherTenDay.putExtra("City", cityParsed);

		startActivity(weatherTenDay);

	}

	public void currency(View view) {

		Intent currency = new Intent(this, CurrencyConv.class);

		currency.putExtra("countryDest", destination[0]);
		currency.putExtra("countryDep", departure[0]);

		startActivity(currency);

	}

	@SuppressLint("SetJavaScriptEnabled")
	public void wiki(View view) {

		if (InternetStatus.getInstance(this).isOnline(this)) {

			WebView webView = (WebView) findViewById(R.id.wikiView_xml);
			webView.getSettings().setJavaScriptEnabled(true);
			webView.loadUrl("http://en.wikipedia.org/wiki/" + cityParsed);
		} else {

			Toast.makeText(this,
					"No Intenet Connection Detected. Please Try Again Later",
					Toast.LENGTH_SHORT).show();

		}
	}

	public void add(View view) {

		View entryView;
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		entryView = getLayoutInflater().inflate(R.layout.new_task, null);
		alertDialogBuilder.setView(entryView);
		final EditText taskEditText = (EditText) entryView
				.findViewById(R.id.task_text_xml);

		// set the database helper
		final ToDoDBHelper helper2 = new ToDoDBHelper(this);
		// set title
		alertDialogBuilder.setTitle("New Task");

		// set dialog message
		alertDialogBuilder
				.setCancelable(true)
				.setPositiveButton("Add",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								SQLiteDatabase database;
								database = helper2.getWritableDatabase();
								database.execSQL("INSERT INTO tblToDo (trip,task,date) VALUES ('"
										+ trip
										+ "', '"
										+ taskEditText.getText().toString()
										+ "',"
										+ (int) System.currentTimeMillis()
										+ ")");
								Log.d("set", taskEditText.getText().toString());
								helper2.close();
								ToDO.refresh();
							}
						})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// if this button is clicked, just close
								// the dialog box and do nothing
								dialog.cancel();
							}
						});
		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();
		// show it
		alertDialog.show();

	}

}